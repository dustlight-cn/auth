import config from "./config"
import axios, {AxiosInstance} from 'axios'
import qs from "qs";
import de from "quasar/lang/de";
import da from "quasar/lang/da";

/**
 * Uim客户端
 */
class UimClient {
  constructor(baseURL, exceptionHandler) {
    this.onException = (e) => exceptionHandler == null || exceptionHandler == null ? console.log(e) : exceptionHandler(e)
    this.baseURL = baseURL
    this.ax = axios.create({
      baseURL: this.baseURL
    })
    this.ax.interceptors.response.use(response => {
      let data = response.data;
      return data
    }, error => {
      this.onException(error)
      return Promise.reject(error.response.statusText + " " + error.response.status)
    })
  }

  getClients() {
    return this.ax.get(config.api.clients)
  }

  getUserInfo() {
    return this.ax.get(config.api.userInfo)
  }

  preAuthorization(client, redirect_uri) {
    let data = {redirect_uri: redirect_uri}
    return this.ax.get(config.api.oauth.authorization + client + "?" + qs.stringify(data))
  }

  authorization(client, code, state) {
    let data = {
      code: code,
      state: state
    }
    return this.ax.get(config.api.oauth.code + client + "?" + qs.stringify(data))
  }
}

export default UimClient
