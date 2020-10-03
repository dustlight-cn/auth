import {DefaultApi} from '../sdk/index.ts'
import Vue from 'vue'

const api = new DefaultApi()
Vue.prototype.$api = api
