(function () {
  var user = null;

  try {
    user = window.localStorage.getItem("demoUser");
  } catch (error) {
    user = null;
  }

  document.querySelectorAll("[data-auth-state]").forEach(function (node) {
    node.textContent = user ? "当前用户：" + user : "游客状态";
  });
})();
