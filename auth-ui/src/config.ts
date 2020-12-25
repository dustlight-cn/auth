import Vue from 'vue';
import {LocalStorage, SessionStorage} from "quasar";

export interface Config {
  host: string,
  recaptchaKey: string,
  pattern: {
    username: RegExp,
    email: RegExp,
    password: RegExp,
    account: RegExp
  },
  storage: SessionStorage | LocalStorage
}

const config: Config = {
  host: "http://localhost:8080",
  recaptchaKey: "6Lcp1xAaAAAAAEp6YI3vE4rLG5Ehgj4EeMip04er",
  pattern: {
    username: /^[a-zA-Z]([-_a-zA-Z0-9]{5,19})$/,
    email: /^\S+@\S+$/,
    password: /^.{6,20}$/,
    account: /^\S{1,20}$/
  },
  storage: Vue.prototype.$q.localStorage
}
export default config
