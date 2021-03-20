# 前端配置
构建或调试前端项目时，配置后端接口地址、谷歌 reCAPTCHA 密钥等。

## 配置
文件位于 [auth-ui/src/config.ts](../auth-ui/src/config.ts)

```typescript
import Vue from 'vue';
import {LocalStorage, SessionStorage} from "quasar";
import pkg from '../package.json';

export interface Config {
    /**
     * API路径
     */
    host: string,
    /**
     * 谷歌 reCAPTCHA 客户端密钥
     */
    recaptchaKey: string,
    /**
     * 表单正则
     */
    pattern: {
        username: RegExp,
        email: RegExp,
        password: RegExp,
        account: RegExp
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

const config: Config = {
    host: "http://localhost:8080",
    // host: "https://api.dustlight.cn",
    recaptchaKey: "6Lcp1xAaAAAAAEp6YI3vE4rLG5Ehgj4EeMip04er",
    pattern: {
        username: /^[a-zA-Z]([-_a-zA-Z0-9]{5,19})$/,
        email: /^\S+@\S+$/,
        password: /^.{6,20}$/,
        account: /^\S{1,20}$/
    },
    storage: Vue.prototype.$q.localStorage,
    getUserFrequency: 60000, // 60秒
    pkg: pkg
}
export default config
```

