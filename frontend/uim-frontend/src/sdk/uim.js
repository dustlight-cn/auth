import config from "./config"
import axios, {AxiosInstance} from 'axios'
import qs from "qs";
import de from "quasar/lang/de";
import da from "quasar/lang/da";

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

  sendEmailResetPasswordCode(email) {
    return this.ax.post(config.api.user.sendCode.email.resetPassword, qs.stringify({email: email}))
  }

  verifyEmailCode(email, code) {
    return this.ax.post(config.api.user.verify.email.register, qs.stringify({email: email, code: code}))
  }

  verifyResetPasswordEmailCode(email, code) {
    return this.ax.post(config.api.user.verify.email.resetPassword, qs.stringify({email: email, code: code}))
  }

  resetPasswordByEmail(email, password) {
    return this.ax.post(config.api.user.resetPasswordByEmail, qs.stringify({email: email, password: password}))
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

  updateEmail(email) {
    return this.ax.post(config.api.user.changeEmail, qs.stringify({email: email}))
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
    this.clientImageUpdateAt = {

    }
  }

  getCurrentUserClients() {
    return this.ax.get(config.api.client.currentUserClients)
  }

  getAllScopes() {
    return this.ax.get(config.api.client.allScopes)
  }

  getAllGrantTypes() {
    return this.ax.get(config.api.client.allGrantTypes)
  }

  getAllAuthorities() {
    return this.ax.get(config.api.client.allAuthorities)
  }

  getAllRoles() {
    return this.ax.get(config.api.client.allRoles)
  }

  resetClientSecret(clientId) {
    return this.ax.post(config.api.client.resetClientSecret + encodeURIComponent(clientId))
  }

  createClient(name, description, redirectUri, scopes, grantTypes) {
    return this.ax.post(config.api.client.createClient, qs.stringify({
      name: name, description: description, redirectUri: redirectUri, scopes: scopes, grantTypes: grantTypes
    }, {indices: false}))
  }

  deleteClient(clientId) {
    return this.ax.delete(config.api.client.deleteClient + encodeURIComponent(clientId))
  }

  updateClientRedirectUri(clientId, redirectUri) {
    return this.ax.post(config.api.client.updateClientRedirectUri + encodeURIComponent(clientId), qs.stringify({redirectUri: redirectUri}))
  }

  updateClientName(clientId, name) {
    return this.ax.post(config.api.client.updateClientName + encodeURIComponent(clientId), qs.stringify({name: name}))
  }

  updateClientDescription(clientId, description) {
    return this.ax.post(config.api.client.updateClientDescription + encodeURIComponent(clientId), qs.stringify({description: description}))
  }

  uploadClientImage(clientId) {
    return this.ax.post(config.api.client.uploadClientImage + encodeURIComponent(clientId))
  }

  notifyClientImageUpdate(clientId,timestamp) {
    if (timestamp == null)
      timestamp = new Date().getTime()
    this.clientImageUpdateAt[clientId] = timestamp
  }

  getClientImageUrl(clientId, size, params) {
    if (params == null)
      params = {}
    if (size != null)
      params.size = size
    return config.api.client.getClientImage + encodeURIComponent(clientId) + "?" + qs.stringify(params)
  }

  addClientScopes(clientId, scopes) {
    return this.ax.post(config.api.client.addClientScopes + encodeURIComponent(clientId) + "?" + qs.stringify({scopes: scopes}, {indices: false}))
  }

  deleteClientScopes(clientId, scopes) {
    return this.ax.delete(config.api.client.removeClientScopes + encodeURIComponent(clientId) + "?" + qs.stringify({scopes: scopes}, {indices: false}))
  }

  addClientGrantTypes(clientId, types) {
    return this.ax.post(config.api.client.addClientGrantTypes + encodeURIComponent(clientId) + "?" + qs.stringify({types: types}, {indices: false}))
  }

  deleteClientGrantTypes(clientId, types) {
    return this.ax.delete(config.api.client.removeClientGrantTypes + encodeURIComponent(clientId) + "?" + qs.stringify({types: types}, {indices: false}))
  }

  getAllTemplates() {
    return this.ax.get(config.api.client.allTemplates)
  }

  getTemplateText(name) {
    return this.ax.get(config.api.client.getTemplateText + encodeURIComponent(name))
  }

  setTemplateText(name, text) {
    return this.ax.post(config.api.client.updateTemplateText + encodeURIComponent(name), qs.stringify({text: text}))
  }

  setTemplateName(id, newName) {
    return this.ax.post(config.api.client.updateClientName + encodeURIComponent(id), qs.stringify({name: name}))
  }

  deleteTemplates(names) {
    return this.ax.delete(config.api.client.deleteTemplate, {data: names})
  }


  createAuthority(name, description) {
    return this.ax.post(config.api.client.createAuthority, qs.stringify({name: name, description: description}))
  }

  updateAuthority(id, name, description) {
    return this.ax.post(config.api.client.updateAuthority + encodeURIComponent(id), qs.stringify({
      name: name,
      description: description
    }))
  }

  deleteAuthority(id) {
    return this.ax.delete(config.api.client.deleteAuthority + encodeURIComponent(id))
  }

  createRole(name, description) {
    return this.ax.post(config.api.client.createRole, qs.stringify({name: name, description: description}))
  }

  updateRole(id, name, description) {
    return this.ax.post(config.api.client.updateRole + encodeURIComponent(id), qs.stringify({
      name: name,
      description: description
    }))
  }

  deleteRole(id) {
    return this.ax.delete(config.api.client.deleteRole + encodeURIComponent(id))
  }

  getRoleAuthorities(roleId) {
    return this.ax.get(config.api.client.getRoleAuthorities + encodeURIComponent(roleId))
  }

  addRoleAuthority(roleId, authorityId) {
    return this.ax.post(config.api.client.addRoleAuthority, qs.stringify({roleId: roleId, authorityId: authorityId}))
  }

  deleteRoleAuthority(roleId, authorityId) {
    return this.ax.delete(config.api.client.deleteRoleAuthority + "?" + qs.stringify({
      roleId: roleId,
      authorityId: authorityId
    }))
  }

  createScope(name, description) {
    return this.ax.post(config.api.client.createScope, qs.stringify({name: name, description: description}))
  }

  updateScope(id, name, description) {
    return this.ax.post(config.api.client.updateScope + encodeURIComponent(id), qs.stringify({
      name: name,
      description: description
    }))
  }

  deleteScope(id) {
    return this.ax.delete(config.api.client.deleteScope + encodeURIComponent(id))
  }

  getScopeAuthorities(scopeId) {
    return this.ax.get(config.api.client.getScopeAuthorities + encodeURIComponent(scopeId))
  }

  addScopeAuthority(scopeId, authorityId) {
    return this.ax.post(config.api.client.addScopeAuthority, qs.stringify({scopeId: scopeId, authorityId: authorityId}))
  }

  deleteScopeAuthority(scopeId, authorityId) {
    return this.ax.delete(config.api.client.deleteScopeAuthority + "?" + qs.stringify({
      scopeId: scopeId,
      authorityId: authorityId
    }))
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

  preAuthorize(response_type, client_id, redirect_uri, scope, state) {
    let data = {
      response_type: response_type,
      client_id: client_id,
      redirect_uri: redirect_uri,
      scope: scope,
      state: state
    }
    return this.ax.get(config.api.oauth.authorize + "?" + qs.stringify(data))
  }

  authorize(response_type, client_id, redirect_uri, scope, state, data) {
    if (data == null)
      data = {}
    data.response_type = response_type
    data.client_id = client_id
    data.redirect_uri = redirect_uri
    data.scope = scope
    data.state = state
    return this.ax.post(config.api.oauth.authorize, qs.stringify(data))
  }
}

export default Uim
