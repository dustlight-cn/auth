import Vue from 'vue'
import UimClient from "app/src/sdk/uim-client";

const client = new UimClient("/", Vue.prototype.$throw)
Vue.prototype.$uclient = client
