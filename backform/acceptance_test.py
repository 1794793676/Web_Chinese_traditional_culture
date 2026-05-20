#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Web 课程设计后端验收接口测试脚本

测试流程：
1. GET    /api/auth/captcha?purpose=login
2. POST   /api/auth/login
3. GET    /api/categories
4. GET    /api/articles/featured
5. 未登录 GET /api/articles/{slug}，应 401
6. 登录后 GET /api/articles/{slug}
7. POST   /api/articles/{id}/view
8. POST   /api/articles/{id}/like
9. 重复 POST /api/articles/{id}/like，不应 500
10. DELETE /api/articles/{id}/like
11. POST   /api/articles/{id}/comments
12. GET    /api/articles/{id}/comments
13. POST   /api/articles/{id}/share
14. demo 访问 /api/admin/dashboard，应 403
15. admin 访问 /api/admin/dashboard
16. admin PATCH /api/admin/comments/{id}/status

运行前：
  pip install requests

运行：
  python scripts/acceptance_test.py

可选环境变量：
  BASE_URL=http://localhost:8080

  DEMO_USERNAME=demo
  DEMO_PASSWORD=demo123456

  ADMIN_USERNAME=admin
  ADMIN_PASSWORD=admin123456

  CAPTCHA_CODE=1234
  DEMO_CAPTCHA_CODE=1234
  ADMIN_CAPTCHA_CODE=1234
"""

from __future__ import annotations

import base64
import os
import sys
import time
from pathlib import Path
from typing import Any, Callable, Optional

import requests


BASE_URL = os.getenv("BASE_URL", "http://localhost:8080").rstrip("/")

DEMO_USERNAME = os.getenv("DEMO_USERNAME", "demo")
DEMO_PASSWORD = os.getenv("DEMO_PASSWORD", "demo123456")

ADMIN_USERNAME = os.getenv("ADMIN_USERNAME", "admin")
ADMIN_PASSWORD = os.getenv("ADMIN_PASSWORD", "admin123456")

TIMEOUT = 15


class ApiTester:
    def __init__(self) -> None:
        self.failures: list[str] = []

        self.article_id: Optional[str] = None
        self.article_slug: Optional[str] = None

        self.comment_id: Optional[str] = None
        self.comment_content = f"验收测试评论-{int(time.time())}"

    def url(self, path: str) -> str:
        if not path.startswith("/"):
            path = "/" + path
        return BASE_URL + path

    def parse_json(self, resp: requests.Response) -> Any:
        try:
            return resp.json()
        except Exception:
            return None

    def payload(self, obj: Any) -> Any:
        """
        兼容常见统一响应结构：
          { "code": 200, "data": ... }
          { "success": true, "data": ... }
          直接返回 list / dict
        """
        if isinstance(obj, dict) and "data" in obj:
            return obj["data"]
        return obj

    def deep_find_any(self, obj: Any, keys: list[str]) -> Any:
        """
        在嵌套 JSON 中查找指定字段。
        """
        if obj is None:
            return None

        target_keys = {k.lower() for k in keys}

        if isinstance(obj, dict):
            for k, v in obj.items():
                if k.lower() in target_keys and v is not None:
                    return v

            for v in obj.values():
                found = self.deep_find_any(v, keys)
                if found is not None:
                    return found

        if isinstance(obj, list):
            for item in obj:
                found = self.deep_find_any(item, keys)
                if found is not None:
                    return found

        return None

    def find_first_article(self, obj: Any) -> Optional[dict]:
        """
        从 /api/articles/featured 的返回结果中找到第一篇文章。
        兼容：
          data: []
          data.records
          data.list
          data.items
          data.rows
          data.content
        """
        obj = self.payload(obj)

        if isinstance(obj, list):
            for item in obj:
                if isinstance(item, dict):
                    if any(k in item for k in ["id", "articleId", "article_id", "slug", "articleSlug"]):
                        return item

                    nested = self.find_first_article(item)
                    if nested:
                        return nested

        if isinstance(obj, dict):
            if any(k in obj for k in ["id", "articleId", "article_id", "slug", "articleSlug"]):
                return obj

            for key in ["records", "list", "items", "rows", "content"]:
                if key in obj:
                    nested = self.find_first_article(obj[key])
                    if nested:
                        return nested

            for value in obj.values():
                nested = self.find_first_article(value)
                if nested:
                    return nested

        return None

    def find_comment_id_by_content(self, obj: Any, content: str) -> Optional[str]:
        """
        在评论列表中通过评论内容查找评论 id。
        """
        obj = self.payload(obj)

        if isinstance(obj, dict):
            text = str(
                obj.get("content")
                or obj.get("commentContent")
                or obj.get("text")
                or ""
            )

            if content in text:
                cid = (
                    obj.get("id")
                    or obj.get("commentId")
                    or obj.get("comment_id")
                )
                if cid is not None:
                    return str(cid)

            for value in obj.values():
                found = self.find_comment_id_by_content(value, content)
                if found:
                    return found

        if isinstance(obj, list):
            for item in obj:
                found = self.find_comment_id_by_content(item, content)
                if found:
                    return found

        return None

    def status_ok(
        self,
        label: str,
        resp: requests.Response,
        expected: tuple[int, ...] | Callable[[int], bool],
    ) -> bool:
        if callable(expected):
            ok = expected(resp.status_code)
            expected_text = getattr(expected, "__name__", "custom condition")
        else:
            ok = resp.status_code in expected
            expected_text = str(expected)

        if ok:
            print(f"[PASS] {label} -> HTTP {resp.status_code}")
            return True

        body = resp.text[:500].replace("\n", " ")
        msg = f"[FAIL] {label} -> HTTP {resp.status_code}, expected {expected_text}, body={body}"
        print(msg)
        self.failures.append(msg)
        return False

    def request(
        self,
        session: requests.Session,
        method: str,
        path: str,
        label: str,
        expected: tuple[int, ...] | Callable[[int], bool] = (200,),
        **kwargs: Any,
    ) -> requests.Response:
        resp = session.request(
            method=method,
            url=self.url(path),
            timeout=TIMEOUT,
            **kwargs,
        )
        self.status_ok(label, resp, expected)
        return resp

    def save_captcha_image(self, image_value: str, filename_prefix: str) -> Optional[Path]:
        """
        兼容：
          1. data:image/png;base64,...
          2. 纯 base64
          3. svg 文本
        """
        if not image_value:
            return None

        raw = image_value.strip()

        if raw.startswith("<svg"):
            out = Path(f"{filename_prefix}.svg")
            out.write_text(raw, encoding="utf-8")
            print(f"验证码 SVG 已保存：{out.resolve()}")
            return out

        if raw.startswith("data:image"):
            header, raw = raw.split(",", 1)

            if "svg" in header:
                suffix = "svg"
            elif "jpeg" in header or "jpg" in header:
                suffix = "jpg"
            elif "gif" in header:
                suffix = "gif"
            else:
                suffix = "png"

            try:
                data = base64.b64decode(raw)
                out = Path(f"{filename_prefix}.{suffix}")
                out.write_bytes(data)
                print(f"验证码图片已保存：{out.resolve()}")
                return out
            except Exception as e:
                print(f"[WARN] data URL 验证码解析失败：{e}")
                return None

        try:
            data = base64.b64decode(raw, validate=True)

            if data.startswith(b"\x89PNG"):
                suffix = "png"
            elif data.startswith(b"\xff\xd8"):
                suffix = "jpg"
            elif data.startswith(b"GIF"):
                suffix = "gif"
            elif data.lstrip().startswith(b"<svg"):
                suffix = "svg"
            else:
                suffix = "png"

            out = Path(f"{filename_prefix}.{suffix}")
            out.write_bytes(data)
            print(f"验证码图片已保存：{out.resolve()}")
            return out
        except Exception:
            pass

        print("[WARN] 未能识别验证码图片格式")
        return None

    def get_captcha(self, session: requests.Session, role: str) -> tuple[Optional[str], str]:
        """
        GET /api/auth/captcha?purpose=login

        兼容：
          1. application/json 返回 captchaId + imageBase64
          2. image/png 直接返回图片
          3. image/svg+xml 直接返回 SVG
          4. JSON 中返回 imageUrl / captchaUrl
        """
        resp = session.get(
            self.url("/api/auth/captcha"),
            timeout=TIMEOUT,
            params={"purpose": "login"},
        )

        self.status_ok(
            f"{role}: GET /api/auth/captcha?purpose=login",
            resp,
            expected=(200,),
        )

        content_type = resp.headers.get("Content-Type", "").lower()

        # 情况一：后端直接返回图片二进制
        if (
            content_type.startswith("image/png")
            or content_type.startswith("image/jpeg")
            or content_type.startswith("image/jpg")
            or content_type.startswith("image/gif")
        ):
            if "jpeg" in content_type or "jpg" in content_type:
                suffix = "jpg"
            elif "gif" in content_type:
                suffix = "gif"
            else:
                suffix = "png"

            out = Path(f"captcha_{role}.{suffix}")
            out.write_bytes(resp.content)
            print(f"验证码图片已保存：{out.resolve()}")

            captcha_code = self.read_captcha_code(role)
            return None, captcha_code

        # 情况二：后端直接返回 SVG
        if "svg" in content_type:
            out = Path(f"captcha_{role}.svg")
            out.write_bytes(resp.content)
            print(f"验证码 SVG 已保存：{out.resolve()}")

            captcha_code = self.read_captcha_code(role)
            return None, captcha_code

        # 情况三：JSON 返回
        raw_json = self.parse_json(resp)
        data = self.payload(raw_json)

        captcha_id = self.deep_find_any(
            data,
            [
                "captchaId",
                "captchaKey",
                "captchaToken",
                "uuid",
                "key",
                "id",
            ],
        )

        # 不使用 "code" 作为验证码字段，避免误读统一响应中的 code=200
        captcha_code = self.deep_find_any(
            data,
            [
                "captchaCode",
                "captcha",
                "verifyCode",
                "answer",
                "text",
            ],
        )

        image_value = self.deep_find_any(
            data,
            [
                "image",
                "imageBase64",
                "captchaImage",
                "img",
                "base64",
            ],
        )

        image_url = self.deep_find_any(
            data,
            [
                "imageUrl",
                "captchaUrl",
                "url",
            ],
        )

        if image_value:
            self.save_captcha_image(str(image_value), f"captcha_{role}")

        elif image_url:
            image_url = str(image_url)

            if image_url.startswith("/"):
                image_url = self.url(image_url)

            print(f"验证码图片 URL：{image_url}")

            try:
                img_resp = session.get(image_url, timeout=TIMEOUT)
                if img_resp.status_code == 200:
                    img_ct = img_resp.headers.get("Content-Type", "").lower()

                    if "svg" in img_ct:
                        out = Path(f"captcha_{role}.svg")
                    elif "jpeg" in img_ct or "jpg" in img_ct:
                        out = Path(f"captcha_{role}.jpg")
                    elif "gif" in img_ct:
                        out = Path(f"captcha_{role}.gif")
                    else:
                        out = Path(f"captcha_{role}.png")

                    out.write_bytes(img_resp.content)
                    print(f"验证码图片已保存：{out.resolve()}")
                else:
                    print(f"[WARN] 验证码图片 URL 下载失败：HTTP {img_resp.status_code}")
            except Exception as e:
                print(f"[WARN] 验证码图片 URL 下载异常：{e}")

        env_code = (
            os.getenv(f"{role.upper()}_CAPTCHA_CODE")
            or os.getenv("CAPTCHA_CODE")
        )

        if env_code:
            captcha_code = env_code

        if not captcha_code:
            captcha_code = self.read_captcha_code(role)

        return (
            str(captcha_id) if captcha_id is not None else None,
            str(captcha_code),
        )

    def read_captcha_code(self, role: str) -> str:
        env_code = (
            os.getenv(f"{role.upper()}_CAPTCHA_CODE")
            or os.getenv("CAPTCHA_CODE")
        )

        if env_code:
            return env_code

        try:
            return input(f"请输入 {role} 登录验证码：").strip()
        except EOFError:
            return ""

    def login(
        self,
        username: str,
        password: str,
        role: str,
        step_label: str,
    ) -> requests.Session:
        """
        POST /api/auth/login

        注意：
          获取验证码和登录必须使用同一个 requests.Session()，
          否则基于 Session/Cookie 的验证码会验证失败。
        """
        session = requests.Session()

        captcha_id, captcha_code = self.get_captcha(session, role)

        body = {
            "username": username,
            "password": password,
            "purpose": "login",

            # 兼容不同后端 DTO 字段名
            "captchaCode": captcha_code,
            "captcha": captcha_code,
            "verifyCode": captcha_code,
            "code": captcha_code,
        }

        if captcha_id:
            body.update(
                {
                    "captchaId": captcha_id,
                    "captchaKey": captcha_id,
                    "captchaToken": captcha_id,
                    "uuid": captcha_id,
                }
            )

        resp = self.request(
            session,
            "POST",
            "/api/auth/login",
            step_label,
            expected=(200, 201),
            json=body,
        )

        raw_json = self.parse_json(resp)
        data = self.payload(raw_json)

        token = self.deep_find_any(
            data,
            [
                "token",
                "accessToken",
                "jwt",
                "jwtToken",
            ],
        )

        if token:
            session.headers.update({"Authorization": f"Bearer {token}"})

        return session

    def run(self) -> int:
        print(f"BASE_URL = {BASE_URL}")

        demo = self.login(
            DEMO_USERNAME,
            DEMO_PASSWORD,
            "demo",
            "Step 2: demo POST /api/auth/login",
        )

        # Step 3
        self.request(
            demo,
            "GET",
            "/api/categories",
            "Step 3: GET /api/categories",
            expected=(200,),
        )

        # Step 4
        resp = self.request(
            demo,
            "GET",
            "/api/articles/featured",
            "Step 4: GET /api/articles/featured",
            expected=(200,),
        )

        featured_json = self.parse_json(resp)
        article = self.find_first_article(featured_json)

        if not article:
            msg = "[FAIL] 无法从 /api/articles/featured 返回中提取文章 id/slug"
            print(msg)
            self.failures.append(msg)
            return self.finish()

        self.article_id = str(
            article.get("id")
            or article.get("articleId")
            or article.get("article_id")
            or ""
        ).strip() or None

        self.article_slug = str(
            article.get("slug")
            or article.get("articleSlug")
            or article.get("urlSlug")
            or ""
        ).strip() or None

        if not self.article_slug:
            msg = "[FAIL] 文章缺少 slug，无法执行 GET /api/articles/{slug}"
            print(msg)
            self.failures.append(msg)
            return self.finish()

        print(f"测试文章：id={self.article_id}, slug={self.article_slug}")

        # Step 5：未登录访问文章详情，应 401
        anonymous = requests.Session()
        self.request(
            anonymous,
            "GET",
            f"/api/articles/{self.article_slug}",
            "Step 5: 未登录 GET /api/articles/{slug}，应 401",
            expected=(401,),
        )

        # Step 6：登录后访问文章详情
        resp = self.request(
            demo,
            "GET",
            f"/api/articles/{self.article_slug}",
            "Step 6: 登录后 GET /api/articles/{slug}",
            expected=(200,),
        )

        detail_json = self.parse_json(resp)
        detail_data = self.payload(detail_json)

        if not self.article_id:
            found_id = self.deep_find_any(
                detail_data,
                ["id", "articleId", "article_id"],
            )
            if found_id:
                self.article_id = str(found_id)

        if not self.article_id:
            msg = "[FAIL] 无法获得文章 id，后续 view/like/comment/share 无法执行"
            print(msg)
            self.failures.append(msg)
            return self.finish()

        # Step 7
        self.request(
            demo,
            "POST",
            f"/api/articles/{self.article_id}/view",
            "Step 7: POST /api/articles/{id}/view",
            expected=(200, 201, 204),
        )

        # Step 8
        self.request(
            demo,
            "POST",
            f"/api/articles/{self.article_id}/like",
            "Step 8: POST /api/articles/{id}/like",
            expected=(200, 201, 204, 409),
        )

        # Step 9：重复点赞不应 500
        def not_500(status: int) -> bool:
            return status != 500

        self.request(
            demo,
            "POST",
            f"/api/articles/{self.article_id}/like",
            "Step 9: 重复 POST /api/articles/{id}/like，不应 500",
            expected=not_500,
        )

        # Step 10
        self.request(
            demo,
            "DELETE",
            f"/api/articles/{self.article_id}/like",
            "Step 10: DELETE /api/articles/{id}/like",
            expected=(200, 204, 404),
        )

        # Step 11
        resp = self.request(
            demo,
            "POST",
            f"/api/articles/{self.article_id}/comments",
            "Step 11: POST /api/articles/{id}/comments",
            expected=(200, 201),
            json={"content": self.comment_content},
        )

        comment_create_json = self.parse_json(resp)

        self.comment_id = self.find_comment_id_by_content(
            comment_create_json,
            self.comment_content,
        )

        if not self.comment_id:
            cid = self.deep_find_any(
                self.payload(comment_create_json),
                ["id", "commentId", "comment_id"],
            )
            if cid:
                self.comment_id = str(cid)

        # Step 12
        resp = self.request(
            demo,
            "GET",
            f"/api/articles/{self.article_id}/comments",
            "Step 12: GET /api/articles/{id}/comments",
            expected=(200,),
        )

        comments_json = self.parse_json(resp)

        if not self.comment_id:
            self.comment_id = self.find_comment_id_by_content(
                comments_json,
                self.comment_content,
            )

        if self.comment_id:
            print(f"测试评论 id={self.comment_id}")
        else:
            msg = "[FAIL] 无法从评论创建或评论列表中找到本次评论 id，后续 admin PATCH 无法执行"
            print(msg)
            self.failures.append(msg)

        # Step 13
        self.request(
            demo,
            "POST",
            f"/api/articles/{self.article_id}/share",
            "Step 13: POST /api/articles/{id}/share",
            expected=(200, 201, 204),
            json={"channel": "link"},
        )

        # Step 14：demo 访问后台，应 403
        self.request(
            demo,
            "GET",
            "/api/admin/dashboard",
            "Step 14: demo GET /api/admin/dashboard，应 403",
            expected=(403,),
        )

        # admin 登录
        admin = self.login(
            ADMIN_USERNAME,
            ADMIN_PASSWORD,
            "admin",
            "Admin 准备: POST /api/auth/login",
        )

        # Step 15
        self.request(
            admin,
            "GET",
            "/api/admin/dashboard",
            "Step 15: admin GET /api/admin/dashboard",
            expected=(200,),
        )

        # Step 16
        if self.comment_id:
            self.patch_comment_status(admin, self.comment_id)

        return self.finish()

    def patch_comment_status(self, admin: requests.Session, comment_id: str) -> None:
        path = f"/api/admin/comments/{comment_id}/status"

        resp = admin.patch(
            self.url(path),
            timeout=TIMEOUT,
            json={
                "status": "hidden",
                "adminNote": "验收测试：隐藏评论"
            },
        )

        self.status_ok(
            "Step 16: admin PATCH /api/admin/comments/{id}/status",
            resp,
            expected=(200, 204),
        )

    def finish(self) -> int:
        print("\n========== 验收测试结果 ==========")

        if not self.failures:
            print("全部通过")
            return 0

        print(f"失败数量：{len(self.failures)}")
        for item in self.failures:
            print(item)

        return 1


if __name__ == "__main__":
    try:
        tester = ApiTester()
        sys.exit(tester.run())

    except requests.exceptions.ConnectionError:
        print(f"[ERROR] 无法连接后端：{BASE_URL}")
        print("请确认 Spring Boot 后端已经启动，并检查 BASE_URL 是否正确。")
        sys.exit(1)

    except requests.exceptions.Timeout:
        print("[ERROR] 请求超时")
        sys.exit(1)

    except KeyboardInterrupt:
        print("\n[ERROR] 用户中断测试")
        sys.exit(1)