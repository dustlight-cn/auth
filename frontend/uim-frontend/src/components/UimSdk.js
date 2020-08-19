import config from "./Config"
import axios from 'axios'
import qs from "qs";

class UimSdk {
  avatar_updatedAt = new Date().getTime()
  user = {
    register: {
      sendEmailCode() {

      }
    },
    /**
     * 获取当前登录用户信息
     * @returns {Promise<AxiosResponse<any>>}
     */
    getCurrentUserDetails() {
      return axios
        .get(config.apiRoot + config.apiUrl.user.currentUserDetails)
        .then(res => {
          if (res == null)
            return
          // 处理用户数据
          res.avatar = (size, params) => config.apiRoot + config.apiUrl.user.userAvatar + res.uid + "?" + qs.stringify({
            size: size,
            params: params,
            t: this.avatar_updatedAt
          })
          res.nickname = (res.nickname == null || res.nickname.length == 0) ? res.username : res.nickname
          res.logout = () => axios.get(config.apiRoot + config.apiUrl.user.logout).finally(() => location.reload())
          return res
        })
    },
    /**
     * 更新头像时间戳
     */
    notifyAvatarUpdate(timestamp) {
      if (timestamp == null)
        timestamp = new Date().getTime()
      this.avatar_updatedAt = timestamp
    }
  }
}

export default new UimSdk()
