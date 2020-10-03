import Menu from './menu'

const main = {
  home: new Menu("home", "/", "home", "首页 Home", []),
  account: new Menu("account", "/personal-info", "person", "个人信息 Account personal", [])
}

export default main


