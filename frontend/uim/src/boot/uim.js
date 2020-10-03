import {DefaultApi} from '../sdk/index.ts'
import {BASE_PATH} from "src/sdk/base";
import Vue from 'vue'
import axios from "axios"

const apiAxios = axios.create({
  withCredentials: true
})

const api = new DefaultApi(null, BASE_PATH, apiAxios)
Vue.prototype.$api = api
