import Vue from 'vue';
import {LocalStorage, SessionStorage} from "quasar";
import pkg from '../package.json';

export interface Config {
  /**
   * API路径
   */
  host: string | null | undefined,
  /**
   * 谷歌 reCAPTCHA 客户端密钥
   */
  recaptchaKey: string | undefined,
  /**
   * 表单正则
   */
  pattern: {
    username: RegExp,
    email: RegExp,
    phone: RegExp,
    password: RegExp,
    account: RegExp,
  },
  /**
   * 用户信息以及 Token 的存储方式
   */
  storage: SessionStorage | LocalStorage,
  /**
   * 获取用户信息的频率限制，单位毫秒
   */
  getUserFrequency: number,
  pkg: typeof pkg
}

const config = {
  dev: {
    host: ".",
    recaptchaKey: "6Lcp1xAaAAAAAEp6YI3vE4rLG5Ehgj4EeMip04er",
    pattern: {
      username: /^[a-zA-Z]([-_a-zA-Z0-9]{5,19})$/,
      email: /^\S+@\S+$/,
      phone: /^\+(9[976]\d|8[987530]\d|6[987]\d|5[90]\d|42\d|3[875]\d|2[98654321]\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|4[987654310]|3[9643210]|2[70]|7|1)\d{1,14}$/,
      password: /^.{6,20}$/,
      account: /^\S{1,20}$/
    },
    storage: Vue.prototype.$q.localStorage,
    getUserFrequency: 60000, // 60秒
    pkg: pkg
  },
  prod: {
    host: "https://api.worldgoodvoices.com",
    recaptchaKey: "6LczCyMcAAAAAAjPRqR0jyQy-PXV06tAe5D1h2Q7",
    pattern: {
      username: /^[a-zA-Z]([-_a-zA-Z0-9]{5,19})$/,
      email: /^\S+@\S+$/,
      phone: /^\+(9[976]\d|8[987530]\d|6[987]\d|5[90]\d|42\d|3[875]\d|2[98654321]\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|4[987654310]|3[9643210]|2[70]|7|1)\d{1,14}$/,
      password: /^.{6,20}$/,
      account: /^\S{1,20}$/
    },
    storage: Vue.prototype.$q.localStorage,
    getUserFrequency: 60000, // 60秒
    pkg: pkg
  }
}

// @ts-ignore
export default config[process.env.mode] || config['dev']
