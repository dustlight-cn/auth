import main, {MainMenu} from './main'

export class Menus {
  main: MainMenu

  constructor(main: MainMenu) {
    this.main = main
  }
}

export default new Menus(
  main
)
