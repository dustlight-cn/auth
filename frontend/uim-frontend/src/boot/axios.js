import Vue from 'vue'
import axios from 'axios'
import createRouter from '../router/index'
import da from "quasar/lang/da";

const router = typeof createRouter === 'function'
  ? createRouter({Vue})
  : createRouter

class ApiError extends Error {
  constructor(message, code, data) {
    super(message)
    this.code = code
    this.data = data;
    if (Error.captureStackTrace)
      Error.captureStackTrace(this, this.constructor)
  }
}

class UnauthorizedError extends ApiError {
  constructor(message, code, data) {
    super(message, code, data);
  }
}

class AccessDenied extends ApiError {
  constructor(message, code, data) {
    super(message, code, data);
  }
}

axios.interceptors.response.use(response => {
  let data = response.data;
  if (data && data.code) {
    if (data.code != 200) {
      if (data.code == 501) {
        location.href = location.protocol + "//" + location.host + "/Login?redirect_uri=" + encodeURIComponent(location.href)
        let e = new ApiError(data.msg, data.code, data.data)
        Vue.prototype.$throw(e)
        throw e
      } else if (data.code == 503) {
        let e = new AccessDenied(data.msg, data.code, data.data);
        location.href = location.protocol + "//" + location.host + "/error/403?redirect_uri=" + encodeURIComponent(location.href)
        Vue.prototype.$throw(e)
        throw e
      }
      let e = new ApiError(data.msg, data.code, data.data)
      Vue.prototype.$throw(e)
      throw e
    } else {
      if (data.data && data.data.redirect_uri)
        location.href = data.data.redirect_uri;
      return data.data;
    }
  }
  return response
}, error => {
  console.log(error)
  Vue.prototype.$throw(error)
  return Promise.reject(error.response.status)
})

Vue.prototype.$axios = axios

//系统错误捕获
const errorHandler = (e, vm) => {
  console.error(e);
  Vue.prototype.$q.notify({
    message: (e.message || '异常') + (e.code ? ", 错误码：" + e.code : ""),
    caption: (e.data ? e.data : e),
    icon: 'warning',
    color: 'red-5'
  })
}
Vue.config.errorHandler = errorHandler;
Vue.prototype.$throw = (error) => errorHandler(error, this);
