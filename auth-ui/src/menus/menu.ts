export default class Menu {
  icon: string
  title: string
  link: string
  keywords: string | string[]
  authorities: string | string[]

  constructor(title: string, link: string, icon: string, keywords: string | string[], authorities: string | string[]) {
    this.title = title
    this.link = link
    this.icon = icon
    this.keywords = keywords
    this.authorities = authorities
  }
}
