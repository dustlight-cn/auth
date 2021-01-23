export default class Menu {
  icon: string | null
  title: string
  link: string
  keywords: string | string[]
  authorities: string | string[] | null

  public constructor(title: string,
                     link: string,
                     icon: string,
                     keywords: string | string[],
                     authorities: string | string[] | null) {
    this.title = title
    this.link = link
    this.icon = icon
    this.keywords = keywords
    this.authorities = authorities
  }

  public getAuthorities(): string[] {
    if (this.authorities == null)
      return [];
    if (this.authorities instanceof Array)
      return this.authorities;
    return [this.authorities];
  }
}
