import Menu from './menu'

export default {
  home: new Menu("home", {name: "Index"}, "home", "首页 Home", []),
  account: new Menu("account", {name: "personal-info"}, "person", "个人信息 Account personal information", []),
  clients: new Menu("clients", {name: "clients"}, "apps", "应用 Clients OAuth2 Apps Application", []),
  users: new Menu("users", {name: "users"}, "person_search", "用户管理 Users Manage Search", []),
  systemSetting: new Menu("systemSettings", {name: "system-settings"}, "settings", "系统设置 System Settings",
    ["WRITE_AUTHORITY", "WRITE_TYPE", "WRITE_SCOPE", "WRITE_ROLE", "GRANT_ROLE"])
}


