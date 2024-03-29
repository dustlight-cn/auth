import {
  TokenApi,
  Configuration,
  User,
  UserApi,
  UsersApi,
  AuthorizationApi,
  CodeApi,
  GrantTypesApi,
  AuthoritiesApi,
  ScopesApi,
  RolesApi,
  ClientsApi
} from '@dustlight/auth-client-axios'
import {BASE_PATH} from '@dustlight/auth-client-axios/base'
import {boot} from "quasar/wrappers";
import config from "src/config";
import {LocalStorage, SessionStorage} from "quasar";
import globalAxios from 'axios';
import V from 'vue';

/**
 * Token
 */
export interface Token {
  access_token: string,
  scope: string,
  expires_in: number,
  token_type: string,
  expiredAt: number,

  isExpired(): boolean
}

/**
 * Storage
 */
export class S {
  storeInstance: SessionStorage | LocalStorage;
  bus: V;

  constructor(storeInstance: SessionStorage | LocalStorage) {
    this.storeInstance = storeInstance;
    this.bus = new V();
  }

  public storeToken(token: Token) {
    token.expiredAt = new Date().getTime() + token.expires_in * 1000;
    token.isExpired = () => new Date().getTime() > new Date(token.expiredAt).getTime();
    this.storeInstance.set("token", token);
    this.bus.$root.$emit("onTokenUpdate", token);
  }

  public loadToken(): Token | null {
    let token = <Token>this.storeInstance.getItem("token");
    if (token != null) {
      token.isExpired = () => new Date().getTime() > new Date(token.expiredAt).getTime();
    }
    if (token == null || token.isExpired()) {
      this.storeInstance.remove("user");
      this.storeInstance.remove("token");
      return null;
    }
    return token;
  }

  public onTokenUpdate(callback: (token: Token) => void) {
    this.bus.$root.$on("onTokenUpdate", callback);
  }

  public storeUser(user: User) {
    if (user != null) {
      user.nickname = user.nickname || user.username;
    }
    this.storeInstance.set("user", user);
    this.bus.$root.$emit("onUserUpdate", user);
  }

  public loadUser(): User | null {
    return this.storeInstance.getItem("user");
  }

  public onUserUpdate(callback: (user: User) => void) {
    this.bus.$root.$on("onUserUpdate", callback);
  }

  public clear() {
    this.storeInstance.remove("user")
    this.storeInstance.remove("token")
    this.bus.$root.$emit("onTokenUpdate", null);
    this.bus.$root.$emit("onUserUpdate", null);
  }
}

export class AuthException extends Error {
  public code: number;
  public message: string;
  public details: string;

  public constructor(code: number, message: string, details: string) {
    super(details);
    this.code = code;
    this.message = message;
    this.details = details;
  }
}

declare module 'vue/types/vue' {
  interface Vue {
    $tokenApi: TokenApi;
    $userApi: UserApi;
    $usersApi: UsersApi;
    $authorizationAi: AuthorizationApi
    $codeApi: CodeApi;
    $clientApi: ClientsApi;
    $grantTypesApi: GrantTypesApi;
    $authoritiesApi: AuthoritiesApi;
    $scopesApi: ScopesApi;
    $rolesApi: RolesApi;

    $apiCfg: Configuration;
    $s: S;
  }
}

let app_obj: any = null

const errorHandler = (error: any): any => {
  let res = error == null ? null : error.response;
  let e: AuthException;

  if (res != null && res.data != null && res.data.message) {
    const message = app_obj.i18n.messages[app_obj.i18n?.locale].errors[res.data.code] || res.data.message
    e = new AuthException(res.data.code, message, res.data.details);
  } else {
    e = new AuthException(-10, error.name || "Error", error.message);
  }
  if (e.code == 1) {
    V.prototype.$s.clear();
  }
  V.prototype.$throw(e);
  return Promise.reject(e);
};

globalAxios.interceptors.response.use(res => res, errorHandler)

const stateless = globalAxios.create()
const withCredentials = globalAxios.create({withCredentials: true})

withCredentials.interceptors.response.use(res => res, errorHandler);
stateless.interceptors.response.use(res => res, errorHandler);


export default boot(({Vue, app}) => {
  // eslint-disable-next-line @typescript-eslint/no-unsafe-member-access
  app_obj = app
  Vue.prototype.$s = new S(Vue.prototype.$q.localStorage);
  let path = config.host != null ? config.host : BASE_PATH;
  Vue.prototype.$apiCfg = new Configuration({
    basePath: path,
    accessToken: () => {
      let s = Vue.prototype.$s;
      s.loadToken()
      let token = s.loadToken();
      if (token == null)
        return null;
      return token.access_token;
    }
  })
  Vue.prototype.$tokenApi = new TokenApi(Vue.prototype.$apiCfg, path, stateless);
  Vue.prototype.$userApi = new UserApi(Vue.prototype.$apiCfg, path, withCredentials);
  Vue.prototype.$usersApi = new UsersApi(Vue.prototype.$apiCfg, path, stateless);
  Vue.prototype.$authorizationAi = new AuthorizationApi(Vue.prototype.$apiCfg, path, withCredentials);
  Vue.prototype.$codeApi = new CodeApi(Vue.prototype.$apiCfg, path, withCredentials);
  Vue.prototype.$clientApi = new ClientsApi(Vue.prototype.$apiCfg, path, stateless);
  Vue.prototype.$grantTypesApi = new GrantTypesApi(Vue.prototype.$apiCfg, path, stateless);
  Vue.prototype.$authoritiesApi = new AuthoritiesApi(Vue.prototype.$apiCfg, path, stateless);
  Vue.prototype.$scopesApi = new ScopesApi(Vue.prototype.$apiCfg, path, stateless);
  Vue.prototype.$rolesApi = new RolesApi(Vue.prototype.$apiCfg, path, stateless);
});

