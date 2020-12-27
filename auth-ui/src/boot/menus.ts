import menus from '../menus'

import {boot} from "quasar/wrappers";

declare module 'vue/types/vue' {
  interface Vue {
    $menus: typeof menus;
  }
}

export default boot(({Vue}) => {
  Vue.prototype.$menus = menus;
});
