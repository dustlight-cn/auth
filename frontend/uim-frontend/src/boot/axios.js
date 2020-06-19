import Vue from 'vue'
import axios from 'axios'
import createRouter from '../router/index'
import da from "quasar/lang/da";

const router = typeof createRouter === 'function'
  ? createRouter({Vue})
  : createRouter

class ApiError extends Error {
  constructor(message, code, data) {
    super(message + ": " + data)
    this.code = code
    this.data = data;
    Error.captureStackTrace(this, this.constructor)
  }
}

class UnauthorizedError extends ApiError {
  constructor(message, code, data) {
    super(message, code, data);
  }
}

axios.interceptors.response.use(response => {
  let data = response.data;
  if (data && data.code) {
    if (data.code != 200) {
      if (data.code == 501) {
        router.push({
          path: '/Login',
          query: {redirect_uri: location.href}
        })
        throw new UnauthorizedError(data.msg, data.code, data.data);
      }
      throw new ApiError(data.msg, data.code, data.data)
    } else {
      if (data.data && data.data.redirect_uri)
        location.href = data.data.redirect_uri;
      return data.data;
    }
  }
  return response
}, error => {
  console.log(error)
  return Promise.reject(error.response.status)
})

Vue.prototype.$axios = axios
