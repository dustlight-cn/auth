import menus, {Menus} from '../menus'
import {boot} from "quasar/wrappers";

declare module 'vue/types/vue' {
  interface Vue {
    $menus: Menus;
  }
}

export default boot(({Vue}) => {
  Vue.prototype.$menus = menus
});
