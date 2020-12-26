import config from "src/config";
import {boot} from "quasar/wrappers";

declare module 'vue/types/vue' {
  interface Vue {
    $cfg: typeof config;
  }
}

export default boot(({Vue}) => {
  Vue.prototype.$cfg = config;
})
