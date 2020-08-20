import config from "./config"
import axios, {AxiosInstance} from 'axios'
import qs from "qs";

/**
 * API异常
 */
class ApiError extends Error {
  constructor(message, code, data) {
    super(message)
    this.code = code
    this.data = data;
    if (Error.captureStackTrace)
      Error.captureStackTrace(this, this.constructor)
  }
}

/**
 * 未登录异常
 */
class UnauthorizedError extends ApiError {
  constructor(message, code, data) {
    super(message, code, data);
  }
}

/**
 * 无权限异常
 */
class AccessDenied extends ApiError {
  constructor(message, code, data) {
    super(message, code, data);
  }
}

/**
 * 用户相关API
 */
class UserApi {
  constructor(axiosInstance) {
    this.ax = axiosInstance
    this.avatar_updatedAt = new Date().getTime()
  }

  login(account, password) {
    return this.ax.post(config.api.user.login, qs.stringify({username: account, password: password}))
  }

  register(username, password) {
    return this.ax.post(config.api.user.register, qs.stringify({username: username, password: password}))
  }

  sendEmailRegisterCode(email) {
    return this.ax.post(config.api.user.sendCode.email.register, qs.stringify({email: email}))
  }

  verifyEmailCode(email, code) {
    return this.ax.post(config.api.user.verify.email.register, qs.stringify({email: email, code: code}))
  }

  getCurrentUserDetails() {
    return this.ax.get(config.api.user.currentUserDetails)
      .then((res) => {
        if (res == null)
          return
        // 处理用户数据
        res.avatar = (size, params) => this.ax.defaults.baseURL + config.api.user.userAvatar + res.uid + "?" + qs.stringify({
          size: size,
          params: params,
          t: this.avatar_updatedAt
        })
        res.nickname = (res.nickname == null || res.nickname.length == 0) ? res.username : res.nickname
        res.logout = () => this.ax.get(config.api.user.logout).finally(() => location.reload())
        return res
      })
  }

  generateUploadAvatarUrl() {
    return this.ax.post(config.api.user.resetAvatar)
  }

  updateGender(gender) {
    return this.ax.post(config.api.user.resetGender, qs.stringify({gender: gender}))
  }

  updateNickname(nickname) {
    return this.ax.post(config.api.user.resetNickname, qs.stringify({nickname: nickname}))
  }

  applyForDeveloper() {
    return this.ax.post(config.api.user.applyForDeveloper)
  }

  notifyAvatarUpdate(timestamp) {
    if (timestamp == null)
      timestamp = new Date().getTime()
    this.avatar_updatedAt = timestamp
  }
}

/**
 * 管理相关API
 */
class ClientApi {
  constructor(axiosInstance) {
    this.ax = axiosInstance
  }

  currentUserClients() {
    return this.ax.get(config.api.client.currentUserClients)
  }
}

/**
 * Uim客户端
 */
class Uim {
  constructor(baseURL, exceptionHandler) {
    this.onException = (e) => exceptionHandler == null || exceptionHandler == null ? console.log(e) : exceptionHandler(e)
    this.baseURL = baseURL
    this.ax = axios.create({
      baseURL: this.baseURL
    })
    this.ax.interceptors.response.use(response => {
      let data = response.data;
      if (data && data.code) {
        if (data.code != 200) {
          if (data.code == 501) {
            location.href = location.protocol + "//" + location.host + "/Login?redirect_uri=" + encodeURIComponent(location.href)
            let e = new ApiError(data.msg, data.code, data.data)
            this.onException(e)
            throw e
          } else if (data.code == 503) {
            let e = new AccessDenied(data.msg, data.code, data.data);
            location.href = location.protocol + "//" + location.host + "/error/403?redirect_uri=" + encodeURIComponent(location.href)
            this.onException(e)
            throw e
          }
          let e = new ApiError(data.msg, data.code, data.data)
          this.onException(e)
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
      this.onException(error)
      return Promise.reject(error.response.status)
    })
    this.user = new UserApi(this.ax)
    this.client = new ClientApi(this.ax)
  }
}

export default Uim
