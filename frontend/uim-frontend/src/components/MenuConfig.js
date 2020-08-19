class MenuItem {

  icon = null
  label = null
  link = null
  authorities = null

  constructor(label, link, icon, authorities) {
    this.label = label
    this.link = link
    this.icon = icon
    this.authorities = authorities
  }

}

const menus = {
  main: [
    new MenuItem("首页", "/", "home"),
    new MenuItem("个人信息", "/info", "person"),
    new MenuItem("应用", "/clients", "apps"),
    new MenuItem("模板", "/templates", "mail", ['READ_TEMPLATE']),
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
  ]
}

export default menus
