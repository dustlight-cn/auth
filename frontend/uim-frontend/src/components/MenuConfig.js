class MenuItem {

  icon = null
  label = null
  link = null
  keyword = null
  authorities = null

  constructor(label, link, icon, authorities, keyword) {
    this.label = label
    this.link = link
    this.icon = icon
    this.authorities = authorities
    this.keyword = keyword
  }

}

const menus = {
  main: [
    new MenuItem("首页", "/", "home"),
    new MenuItem("个人信息", "/info", "person", null, "personal 个人 information 信息 email 邮箱 nickname 昵称 username 用户名 avatar 头像 gender 性别"),
    new MenuItem("应用", "/clients", "apps", null, "app application apps client clients"),
    new MenuItem("模板", "/templates", "mail", ['READ_TEMPLATE'], "template templates"),
    new MenuItem("权限", "/authorities", "security", ['MANAGE_AUTHORITY']),
    new MenuItem("角色", "/roles", "admin_panel_settings", ['MANAGE_ROLE']),
    new MenuItem("授权作用域", "/scopes", "app_settings_alt", ['MANAGE_SCOPE'])
  ],
  other: [
    new MenuItem("帮助", "/help", "help"),
    new MenuItem("反馈", "/feedback", "announcement")
  ],
  avatar: [
    new MenuItem("管理账号", "/info")
  ],
  pages: [
    new MenuItem("重置密码", "/password", "ion-key", null, "reset password 重置密码"),
    new MenuItem("更换邮箱", "/info/change_email", "email", null, "reset email 更换邮箱")
  ]
}

export default menus
