export default class Menu {
  icon: string | null
  title: string
  to: any
  keywords: string | string[]
  authorities: string | string[] | null

  public constructor(title: string,
                     to: any,
                     icon: string,
                     keywords: string | string[],
                     authorities: string | string[] | null) {
    this.title = title
    this.to = to
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
