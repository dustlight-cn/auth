import Uim from "src/sdk/uim";
import Vue from 'vue'

const uim = new Uim("/", Vue.prototype.$throw)
Vue.prototype.$uim = uim
