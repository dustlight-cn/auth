import Menu from './menu'

export class MainMenu {
  home: Menu
  account: Menu

  constructor(home: Menu,
              account: Menu) {
    this.home = home
    this.account = account
  }
}

export default new MainMenu(
  new Menu("home", "/", "home", "首页 Home", []),
  new Menu("account", "/personal-info", "person", "个人信息 Account personal", [])
)


