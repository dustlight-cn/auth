import {
  UsersApi, TokenApi, AuthorizationApi,
  CAPTCHAApi
} from '@dustlight/auth-client-axios'
import {BASE_PATH} from '@dustlight/auth-client-axios/base'
import {boot} from "quasar/wrappers";

export * from '@dustlight/auth-client-axios'

declare module 'vue/types/vue' {
  interface Vue {
    $userApi: UsersApi;
    $tokenApi: TokenApi;
    $authorizationApi: AuthorizationApi
    $captchaApi: CAPTCHAApi
  }
}

export const basePath = BASE_PATH;

export const userApi = new UsersApi();
export const tokenApi = new TokenApi();
export const authorizationApi = new AuthorizationApi();
export const captchaApi = new CAPTCHAApi();

export default boot(({Vue}) => {
  // eslint-disable-next-line @typescript-eslint/no-unsafe-member-access
  Vue.prototype.$userApi = userApi;
  Vue.prototype.$tokenApi = tokenApi;
  Vue.prototype.$authorizationApi = authorizationApi;
  Vue.prototype.$captchaApi = captchaApi;
});

