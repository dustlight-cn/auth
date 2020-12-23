import {TokenApi, Configuration, User} from '@dustlight/auth-client-axios'
import {BASE_PATH} from '@dustlight/auth-client-axios/base'
import {boot} from "quasar/wrappers";
import config from "src/config";
import {SessionStorage} from "quasar";

export * from '@dustlight/auth-client-axios'

export interface Token {
  access_token: string,
  scope: string,
  expires_in: number,
  token_type: string,
  expiredAt: number,

  isExpired(): boolean
}

export class S {
  storeInstance: SessionStorage

  constructor(storeInstance: SessionStorage) {
    this.storeInstance = storeInstance;
  }

  public storeToken(token: Token) {
    token.expiredAt = new Date().getTime() + token.expires_in * 1000;
    this.storeInstance.set("token", token);
  }

  public loadToken(): Token | null {
    let token = <Token>this.storeInstance.getItem("token");
    if (token != null) {
      token.isExpired = () => new Date().getTime() > new Date(token.expiredAt).getTime();
    }
    if (token == null || token.isExpired()) {
      this.clear();
      return null;
    }
    return token;
  }

  public storeUser(user: User) {
    if (user != null) {
      user.nickname = user.nickname || user.username;
    }
    this.storeInstance.set("user", user);
  }

  public loadUser(): User | null {
    return this.storeInstance.getItem("user");
  }

  public clear() {
    this.storeInstance.remove("user")
    this.storeInstance.remove("token")
  }
}

declare module 'vue/types/vue' {
  interface Vue {
    $tokenApi: TokenApi;
    $apiCfg: Configuration;
    $s: S;
  }
}

export default boot(({Vue}) => {
  // eslint-disable-next-line @typescript-eslint/no-unsafe-member-access

  Vue.prototype.$s = new S(Vue.prototype.$q.sessionStorage);
  Vue.prototype.$apiCfg = new Configuration({
    basePath: config.host || BASE_PATH,
    apiKey: () => {
      let s = Vue.prototype.$s;
      s.loadToken()
      let token = s.loadToken();
      if (token == null)
        return null;
      return token.access_token;
    }
  })
  Vue.prototype.$tokenApi = new TokenApi(Vue.prototype.$apiCfg);
});

