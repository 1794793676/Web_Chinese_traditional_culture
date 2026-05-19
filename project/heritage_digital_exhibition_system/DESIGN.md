---
name: Heritage Digital Exhibition System
colors:
  surface: '#fff9ee'
  surface-dim: '#dfd9cf'
  surface-bright: '#fff9ee'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f9f3e8'
  surface-container: '#f3ede2'
  surface-container-high: '#ede8dd'
  surface-container-highest: '#e8e2d7'
  on-surface: '#1d1c15'
  on-surface-variant: '#58413f'
  inverse-surface: '#333029'
  inverse-on-surface: '#f6f0e5'
  outline: '#8b716e'
  outline-variant: '#dfbfbc'
  surface-tint: '#a93630'
  primary: '#690409'
  on-primary: '#ffffff'
  primary-container: '#8a1f1d'
  on-primary-container: '#ff9c93'
  inverse-primary: '#ffb4ac'
  secondary: '#775a16'
  on-secondary: '#ffffff'
  secondary-container: '#fdd485'
  on-secondary-container: '#785a17'
  tertiary: '#00392d'
  on-tertiary: '#ffffff'
  tertiary-container: '#075242'
  on-tertiary-container: '#84c4af'
  error: '#B42318'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#ffdad6'
  primary-fixed-dim: '#ffb4ac'
  on-primary-fixed: '#410003'
  on-primary-fixed-variant: '#881e1c'
  secondary-fixed: '#ffdea3'
  secondary-fixed-dim: '#e9c174'
  on-secondary-fixed: '#261900'
  on-secondary-fixed-variant: '#5c4200'
  tertiary-fixed: '#aef0da'
  tertiary-fixed-dim: '#93d3bf'
  on-tertiary-fixed: '#002019'
  on-tertiary-fixed-variant: '#055141'
  background: '#fff9ee'
  on-background: '#1d1c15'
  surface-variant: '#e8e2d7'
  ink-text: '#1F2522'
  mist-border: '#E9E1D1'
  white-card: '#FFFCF6'
  success: '#2E7D32'
typography:
  display-lg:
    fontFamily: Noto Serif SC
    fontSize: 48px
    fontWeight: '700'
    lineHeight: '1.2'
  display-lg-mobile:
    fontFamily: Noto Serif SC
    fontSize: 32px
    fontWeight: '700'
    lineHeight: '1.2'
  headline-md:
    fontFamily: Noto Serif SC
    fontSize: 32px
    fontWeight: '600'
    lineHeight: '1.3'
  headline-sm:
    fontFamily: Noto Serif SC
    fontSize: 24px
    fontWeight: '600'
    lineHeight: '1.4'
  body-lg:
    fontFamily: Noto Sans SC
    fontSize: 18px
    fontWeight: '400'
    lineHeight: '1.6'
  body-md:
    fontFamily: Noto Sans SC
    fontSize: 16px
    fontWeight: '400'
    lineHeight: '1.6'
  label-md:
    fontFamily: Noto Sans SC
    fontSize: 14px
    fontWeight: '500'
    lineHeight: '1.2'
    letterSpacing: 0.05em
  admin-table:
    fontFamily: Noto Sans SC
    fontSize: 14px
    fontWeight: '400'
    lineHeight: '1.5'
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  container-max: 1200px
  gutter: 24px
  margin-desktop: 80px
  margin-tablet: 40px
  margin-mobile: 20px
  section-gap: 120px
---

# Design.md — Stitch 前端页面设计指令

> 用途：将本文件作为 Stitch 的页面生成参考。目标是生成一个符合《Web开发技术》课程设计要求的“中华优秀传统文化”网站前端视觉方案。生成结果应偏高保真网页 UI，而不是简单线框图。

---

## 1. Project Brief

Design a responsive Chinese traditional culture website named:

```text
华夏文脉 · 中华优秀传统文化数字展馆
```

The website is a digital exhibition hall for Chinese excellent traditional culture. It should combine a modern web layout with traditional Chinese visual elements such as paper texture, ink painting, cinnabar seals, gold accents, scroll-like cards, and subtle landscape patterns.

The website must include public homepage, login, registration, protected cultural content pages, article detail page with interaction area, and admin dashboard pages for viewing likes, comments, and shares.

---

## 2. Design Goals

1. Create a visually unified, elegant, and modern frontend design.
2. Reflect the theme of Chinese excellent traditional culture.
3. Make the hierarchy clear: homepage → category page → detail page.
4. Show clear login/register/captcha UI.
5. Show protected content behavior for guests.
6. Show user interaction UI: like, comment, share.
7. Show admin dashboard UI: total likes, comments, shares, popular pages.
8. Ensure responsive layouts for desktop, tablet, and mobile.

---

## 3. Visual Style

### 3.1 Keywords

```text
Chinese heritage, digital museum, elegant, calm, scholarly, paper texture, ink, cinnabar, gold, jade green, modern cards, clean navigation, responsive layout
```

### 3.2 Color Palette

Use the following color system:

```text
Primary Cinnabar Red: #8A1F1D
Gold Accent: #C9A45A
Paper Background: #F6F0E5
Ink Text: #1F2522
Jade Green: #2F6F5E
Mist Border: #E9E1D1
White Card: #FFFCF6
Error Red: #B42318
Success Green: #2E7D32
```

### 3.3 Typography

Use Chinese serif style for titles and clean sans-serif for body text.

Recommended visual direction:

```text
Headings: Noto Serif SC / Source Han Serif SC / Songti SC
Body: Noto Sans SC / Microsoft YaHei
Admin data tables: system sans-serif
```

### 3.4 Decorative Elements

Use subtle traditional decorations, not overly complex:

- light paper texture background
- ink landscape line art
- cinnabar seal icon
- gold divider lines
- cloud pattern or window lattice pattern
- scroll-inspired cards
- simple line icons

Avoid cartoon style, heavy gradients, and cluttered decorations.

---

## 4. Site Map to Design

Generate designs for these pages and states:

```text
index.html                         Public homepage
pages/register.html                Registration page
pages/login.html                   Login page with captcha
pages/ideas.html                   Protected category page: 思想理念
pages/artifacts.html               Protected category page: 器物与非遗
pages/festivals.html               Protected category page: 节日民俗
pages/detail.html                  Protected article detail page
pages/no-access.html               Guest no-permission state or modal
admin/dashboard.html               Admin dashboard
admin/comments.html                Admin comment management
admin/interactions.html            Admin interaction statistics
```

For category pages, use one shared visual template and vary the title, subtitle, and card content.

---

## 5. Page Requirements

## 5.1 Homepage — `index.html`

Access: public, visible to guests and logged-in users.

Design a full homepage with the following sections:

### Header

- Left: logo seal icon + text `华夏文脉`
- Center navigation:
  - 首页
  - 文化专题
  - 数字展馆
  - 关于本站
- Right:
  - Guest state: `登录` button and `注册` button
  - Logged-in state: avatar + username dropdown
- Mobile: hamburger menu

### Hero Section

Use a large elegant hero area.

Text:

```text
华夏文脉 · 中华优秀传统文化数字展馆
在现代网页中重读传统文化的思想、器物与精神
```

Buttons:

```text
进入展馆
注册体验
```

Visual:

- light paper background
- faint Chinese landscape illustration
- cinnabar seal ornament
- gold accent lines

### Culture Category Cards

Display six cards:

```text
思想理念
精神品格
器物与非遗
节日民俗
科学技艺
生态智慧
```

Each card should include:

- icon
- title
- one-line description
- arrow or `查看专题`

When displayed as guest, show a small lock icon or note: `登录后查看完整内容`.

### Featured Articles

Show 3 or 6 article cards.

Each article card includes:

- image placeholder
- category tag
- title
- summary
- source label
- like/comment/share counts preview

Example titles:

```text
民为邦本：传统政治智慧中的人民观
榫卯结构：不用一钉一铆的东方技艺
二十四节气：顺时而作的生态智慧
```

### Timeline Section

Design a horizontal or vertical cultural timeline:

```text
先秦思想 → 汉唐气象 → 宋元审美 → 明清技艺 → 当代表达
```

### Footer

Include:

```text
课程设计作品 | 素材来源说明 | 中华优秀传统文化数字展示
```

---

## 5.2 Register Page — `pages/register.html`

Design a split layout.

Left side:

- cultural illustration panel
- scroll or paper card visual
- text: `创建账号，进入华夏文脉数字展馆`

Right side:

Form card title:

```text
用户注册
```

Fields:

```text
用户名
邮箱
密码
确认密码
同意用户协议 checkbox
注册 button
已有账号？去登录
```

Include form states:

- normal input
- focused input
- error message
- loading button
- success message

Use warm paper background and gold/cinnabar accents.

---

## 5.3 Login Page — `pages/login.html`

Design a login page with captcha.

Form title:

```text
登录数字展馆
```

Fields:

```text
用户名 / 邮箱
密码
验证码
```

Captcha UI:

- input on left
- captcha image/canvas block on right
- refresh icon
- helper text: `点击图片刷新验证码`

Buttons and links:

```text
登录
没有账号？立即注册
```

Error state text examples:

```text
验证码错误，请重新输入
用户名或密码错误
```

After login, the UI should imply that users can access protected pages and interactions.

---

## 5.4 Protected Category Page Template

Design one category page template and use it for:

```text
思想理念
器物与非遗
节日民俗
```

Access: logged-in users only.

Page structure:

1. Header with logged-in user state.
2. Category hero banner.
3. Filter/search bar.
4. Article card grid.
5. Pagination or load more.

Category hero example for `思想理念`:

```text
思想理念
从民为邦本、革故鼎新、任人唯贤中理解传统中国的治理智慧。
```

Filter bar:

```text
搜索文化内容
全部标签
按热度排序
按时间排序
```

Card grid fields:

- cover image
- category tag
- title
- summary
- source
- like count
- comment count
- share count
- button: `阅读全文`

Use 3 columns on desktop, 2 on tablet, 1 on mobile.

---

## 5.5 Article Detail Page — `pages/detail.html`

Access: logged-in users only.

Design a detailed reading page.

Page structure:

1. Breadcrumb:

```text
首页 / 思想理念 / 民为邦本
```

2. Article header:

```text
民为邦本：传统政治智慧中的人民观
分类：思想理念
整理者：华夏文脉编辑组
发布时间：2026-05-18
素材来源：示例来源网站
```

3. Article content area:

- large cover image
- several paragraphs of readable Chinese placeholder content
- quote block
- knowledge highlight card

4. Interaction bar:

```text
点赞 128
评论 26
转发 15
```

Use icon buttons. Show active liked state.

5. Comment area:

- comment textarea placeholder: `写下你的理解与感受...`
- publish button: `发布评论`
- comment list with avatar, username, time, content

6. Related articles:

- 3 cards at the bottom

---

## 5.6 No Permission State — `pages/no-access.html` or Modal

Design a permission-blocking state for guests who try to access protected pages.

Text:

```text
你当前以游客身份访问
请登录后进入完整数字展馆，浏览文化专题并参与点赞、评论与转发。
```

Buttons:

```text
去登录
返回首页
```

Visual:

- lock icon
- paper card
- subtle seal decoration

---

## 5.7 Admin Dashboard — `admin/dashboard.html`

Access: admin only.

Design a clean admin dashboard that still uses the same color system but is more data-oriented.

Layout:

- left sidebar
- top bar
- main dashboard content

Sidebar menu:

```text
数据总览
内容统计
评论管理
互动数据
用户管理
返回前台
```

Top cards:

```text
文章总数
用户总数
点赞总数
评论总数
转发总数
```

Charts:

- category interaction ranking bar chart
- comment trend line chart
- popular content Top 5 table

Latest comments table:

```text
文章标题 | 用户 | 评论内容 | 时间 | 状态 | 操作
```

Make the dashboard look credible for a course project.

---

## 5.8 Admin Comments Page — `admin/comments.html`

Design a table management page.

Elements:

- search input
- status filter
- comments table
- actions: 查看, 隐藏, 删除

Table columns:

```text
评论ID
文章标题
用户
评论内容
评论时间
状态
操作
```

Include pagination.

---

## 5.9 Admin Interactions Page — `admin/interactions.html`

Design a statistics table for all subpages.

Table columns:

```text
页面ID
页面标题
所属专题
点赞数
评论数
转发数
最近更新
```

Include:

- data summary cards
- sortable table headers
- export button visual only: `导出数据`

---

## 6. Component System

Create reusable components with consistent styling:

```text
Header
Footer
HeroBanner
CultureCategoryCard
ArticleCard
InteractionBar
CommentBox
CommentList
AuthForm
CaptchaInput
PermissionModal
AdminSidebar
AdminStatCard
AdminTable
SearchFilterBar
TagBadge
```

### Button Styles

Primary button:

- background: cinnabar red
- text: white
- border radius: 999px or 12px
- hover: darker red

Secondary button:

- transparent or paper background
- border: gold
- text: ink or cinnabar

### Card Styles

- background: #FFFCF6
- border: 1px solid #E9E1D1
- border radius: 18px to 24px
- soft shadow
- hover lift effect

---

## 7. Responsive Rules

### Desktop

- Max content width: 1200px.
- Header horizontal navigation.
- Hero: two-column or centered large layout.
- Cards: 3 columns.
- Admin: fixed sidebar + main area.

### Tablet

- Cards: 2 columns.
- Header navigation can be compressed.
- Admin sidebar can become narrow icon sidebar.

### Mobile

- Header uses hamburger menu.
- Hero content centered.
- Buttons stacked vertically.
- Cards: 1 column.
- Article content full width.
- Admin tables become horizontally scrollable or card-style list.

---

## 8. Interaction States to Show

Please include UI states where possible:

```text
Guest state
Logged-in user state
Admin state
Form error state
Loading state
Empty comment state
Liked active state
No-permission state
Mobile menu open state
```

---

## 9. Sample Content for UI

Use the following sample cultural content to fill the UI.

### Categories

```text
思想理念：民为邦本、革故鼎新、任人唯贤、天人合一
精神品格：精忠报国、舍生取义、自强不息、厚德载物
器物与非遗：青铜器、陶瓷、书法、剪纸、榫卯结构
节日民俗：春节、清明、端午、中秋、重阳
科学技艺：二十四节气、中医药、农耕技术、古代水利
生态智慧：顺时而作、节用爱物、山水审美、天人合一
```

### Article Titles

```text
民为邦本：传统政治智慧中的人民观
革故鼎新：传统文化中的创新精神
榫卯结构：不用一钉一铆的东方技艺
二十四节气：顺时而作的生态智慧
端午节俗：家国情怀与民间记忆
书法之美：线条中的精神气韵
```

### Comment Examples

```text
这篇文章让我更直观地理解了传统文化中的人民观。
页面中的时间轴设计很清晰，适合文化主题展示。
非遗内容如果能加入更多图片会更有吸引力。
```

---

## 10. Design Constraints

1. Do not use Chinese folder names in the design annotations.
2. Keep page style consistent across all subpages.
3. Do not design the homepage as the only page; the project must visibly include at least three page levels.
4. Make login/register/captcha visually clear.
5. Make like/comment/share areas visible on detail pages.
6. Make admin statistics pages visible and complete.
7. The result should be suitable for conversion into HTML/CSS/JS or a frontend framework.
8. Use realistic spacing, readable typography, and clear hierarchy.
9. Avoid excessive decoration that reduces readability.
10. Make the website look like a complete course design project, not just a landing page.

---

## 11. Expected Stitch Output

Generate a complete responsive frontend UI design with these screen groups:

```text
1. Homepage desktop
2. Homepage mobile
3. Register page
4. Login page with captcha
5. Protected category page
6. Article detail page with like/comment/share
7. No-permission state
8. Admin dashboard
9. Admin comments table
10. Admin interaction statistics table
```

The final design should be elegant, structured, culturally themed, and implementation-ready.


# End DataStore Snapshot
