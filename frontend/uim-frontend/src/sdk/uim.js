import config from "./config"
import axios from 'axios'
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
  }

  /**
   * 获取当前登录用户信息
   * @returns {Promise<AxiosResponse<any>>}
   */
  getCurrentUserDetails() {
    return this.ax.get(config.api.user.currentUserDetails)
      .then(res => {
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

  /**
   * 生成上传头像URL
   * @returns {AxiosPromise<any>}
   */
  generateUploadAvatarUrl() {
    return this.ax.post(config.api.user.resetAvatar)
  }

  /**
   * 更新性别
   * @param gender 性别
   * @returns {AxiosPromise<any>}
   */
  updateGender(gender) {
    return this.ax.post(config.api.user.resetGender, qs.stringify({gender: gender}))
  }

  /**
   * 更新昵称
   * @param nickname 昵称
   * @returns {AxiosPromise<any>}
   */
  updateNickname(nickname) {
    return this.ax.post(config.api.user.resetNickname, qs.stringify({nickname: nickname}))
  }

  /**
   * 更新头像时间戳
   */
  notifyAvatarUpdate(timestamp) {
    if (timestamp == null)
      timestamp = new Date().getTime()
    this.avatar_updatedAt = timestamp
  }
}

/**
 * Uim客户端
 */
class Uim {
  constructor(baseURL, exceptionHandler) {
    this.onException = (e) => exceptionHandler == null || exceptionHandler == null ? console.log(e) : exceptionHandler(e)
    this.baseURL = baseURL
    this.avatar_updatedAt = new Date().getTime()
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
  }
}

export default Uim
