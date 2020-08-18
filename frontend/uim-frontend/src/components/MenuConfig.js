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

const menus = [
  new MenuItem("首页", "/", "home"),
  new MenuItem("账号", "/info", "person"),
  new MenuItem("应用", "/clients", "apps"),
  new MenuItem("模板", "/templates", "mail", ['READ_TEMPLATE']),
  new MenuItem("权限", "/authorities", "security", ['MANAGE_AUTHORITY']),
  new MenuItem("角色", "/roles", "admin_panel_settings", ['MANAGE_ROLE']),
  new MenuItem("授权作用域", "/scopes", "app_settings_alt", ['MANAGE_SCOPE'])
]

export default menus
