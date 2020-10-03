export default class Menu {
  icon
  title
  link
  keywords
  authorities

  constructor(title, link, icon, keywords, authorities) {
    this.title = title
    this.link = link
    this.icon = icon
    this.keywords = keywords
    this.authorities = authorities
  }
}
