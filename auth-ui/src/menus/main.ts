import Menu from './menu'

export default {
  home: new Menu("home", "/", "home", "首页 Home", []),
  account: new Menu("account", "/personal-info", "person", "个人信息 Account personal information", []),
  clients: new Menu("clients", "/clients", "apps", "应用 Clients OAuth2 Apps Application", []),
  users: new Menu("users", "/users", "person_search", "用户管理 Users Manage Search",
    ["READ_USER", "WRITE_USER", "WRITE_USER_EMAIL", "WRITE_USER_PASSWORD"]),
  systemSetting: new Menu("systemSettings", "/system-settings", "settings", "系统设置 System Settings",
    ["WRITE_AUTHORITY", "WRITE_TYPE", "WRITE_SCOPE", "WRITE_ROLE", "GRANT_ROLE"])
}


