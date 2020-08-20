<template>
  <div class="q-pa-md">
    <q-list title="asd" separator>
      <q-item>
        <q-item-section>
          <q-item-label overline>应用列表 ({{clients.length}})</q-item-label>
        </q-item-section>
      </q-item>
      <q-item @click="select(client)" v-for="client in clients" clickable v-ripple>
        <q-item-section>
          <q-item-label>{{client.clientName}}</q-item-label>
          <q-item-label caption>{{client.description}}</q-item-label>
        </q-item-section>
      </q-item>
      <q-item class="text-center" v-if="clients == null || clients.length == 0">
        <q-item-section>
          <q-item-label caption>空空如也</q-item-label>
        </q-item-section>
      </q-item>
      <q-page-sticky position="bottom-right" :offset="[18, 18]">
        <q-btn to="./clients/create" v-if="hasAuthority('CREATE_CLIENT')" color="primary" icon="add" round/>
        <q-btn @click="applyForDeveloper" v-else icon="security" flat color="primary" label="申请成为开发者"/>
      </q-page-sticky>
    </q-list>

    <!-- Card -->
    <q-dialog v-model="selected">
      <q-card class="my-card">
        <q-card-section>
          <div class="row no-wrap items-center">
            <!--            ellipsis-->
            <div class="col text-h6">
              {{selectedClient.clientName}}
              <q-btn flat round dense icon="edit">
                <q-popup-edit
                  @save="(val,initVal)=>updateClientName(val,initVal,selectedClient)"
                  buttons
                  v-model="selectedClient.clientName"
                  label-set="保存"
                  label-cancel="取消"
                >
                  <q-input label="应用名" hint="应用名不可重复" v-model="selectedClient.clientName" counter/>
                </q-popup-edit>
              </q-btn>

            </div>
          </div>
        </q-card-section>
        <q-card-section class="q-pt-none">
          <div class="text-subtitle1">
            描述
            <q-btn flat round dense icon="edit">
              <q-popup-edit
                @save="(val,initVal)=>updateClientDescription(val,initVal,selectedClient)"
                buttons
                v-model="selectedClient.description"
                label-set="保存"
                label-cancel="取消"
              >
                <q-input label="应用描述" hint="应用名不可重复" v-model="selectedClient.description" counter/>
              </q-popup-edit>
            </q-btn>
          </div>
          <div class="text-caption text-grey">
            {{selectedClient.description}}
          </div>
        </q-card-section>

        <q-card-section class="q-pt-none">
          <div class="text-subtitle1">
            AppKey
          </div>
          <div class="text-caption text-grey">
            {{selectedClient.clientId}}
          </div>
        </q-card-section>
        <q-card-section class="q-pt-none">
          <div class="text-subtitle1">
            AppSecret
          </div>
          <div v-if="selectedClient.clientSecret" class="text-caption text-grey">
            {{selectedClient.clientSecret}}
          </div>
          <q-btn dense @click="()=>{resetSecret(selectedClient)}" flat color="negative" v-else label="重置"/>
        </q-card-section>
        <q-card-section class="q-pt-none">
          <div class="text-subtitle1">
            回调地址
            <q-btn flat round dense icon="edit">
              <q-popup-edit
                @save="(val,initVal)=>updateRedirectUri(val,initVal,selectedClient)"
                buttons
                v-model="selectedClient.registeredRedirectUri"
                label-set="保存"
                label-cancel="取消"
              >
                <q-input
                  v-for="(uri,i) in selectedClient.registeredRedirectUri"
                  rounded bottom-slots dense
                  v-model="selectedClient.registeredRedirectUri[i]" :label="'URL-' + (i + 1)" hint="" counter>
                  <template v-slot:append>
                    <q-btn round dense
                           @click="()=>selectedClient.registeredRedirectUri.splice(i,1)"
                           icon="delete"
                           color="negative"
                           flat/>
                  </template>
                </q-input>

                <q-btn @click="()=>selectedClient.registeredRedirectUri.push('')" round dense icon="add" color="primary"
                       flat/>
              </q-popup-edit>
            </q-btn>
          </div>
          <div v-for="(uri,i) in selectedClient.registeredRedirectUri" class="text-caption text-grey">
            {{uri}}
          </div>
        </q-card-section>
        <q-card-section class="q-pt-none">
          <div class="text-subtitle1">
            授权作用域
            <q-btn flat round dense icon="edit">
              <q-popup-edit
                title="授权作用域"
                v-model="selectedClient.scope"
                auto-save
              >
                <q-list>
                  <q-item>
                    <q-item-section style="min-width: 100px">
                      <q-item-label overline>
                        作用域列表 ({{selectedClient.scope.length}})
                      </q-item-label>
                    </q-item-section>
                    <q-item-section class="text-right">
                      <q-item-label>
                        <q-btn @click="()=>showScopeDialog()" round dense icon="add"
                               flat/>
                      </q-item-label>
                    </q-item-section>
                  </q-item>
                  <q-item dense v-for="(scope,i) in selectedClient.scope">
                    <q-item-section>
                      <q-item-label caption>
                        <q-chip
                          @remove="()=>removeClientScope(selectedClient, selectedClient.scopeDetails[selectedClient.scope[i]])"
                          removable>
                          {{selectedClient.scope[i] + '(' +
                          selectedClient.scopeDetails[selectedClient.scope[i]].description +
                          ")"}}
                        </q-chip>
                      </q-item-label>
                    </q-item-section>
                  </q-item>
                  <q-item class="text-center"
                          v-if="selectedClient.scope == null || selectedClient.scope.length == 0">
                    <q-item-section>
                      <q-item-label caption>
                        无授权作用域
                      </q-item-label>
                    </q-item-section>
                  </q-item>
                </q-list>
                <q-dialog v-model="selectingScope">
                  <q-card class="my-card">
                    <q-card-section>
                      <q-list separator>
                        <q-item>
                          <q-item-section>
                            <q-item-label overline>
                              选择授权作用域 {{selectedClient.scope.length}}/{{scopes.length}}
                            </q-item-label>
                          </q-item-section>
                        </q-item>
                        <q-separator/>
                        <q-item v-for="scope in scopes">
                          <q-item-section>
                            <q-item-label>{{scope.name}}</q-item-label>
                            <q-item-label caption>{{scope.description}}</q-item-label>
                          </q-item-section>
                          <q-item-section class="text-right">
                            <q-item-label>
                              <q-icon v-if="scope.flag" name="check"/>
                              <q-btn @click="()=>addScope(scope,selectedClient)" v-else flat round dense
                                     icon="add"/>
                            </q-item-label>
                          </q-item-section>
                        </q-item>
                      </q-list>
                    </q-card-section>
                    <q-inner-loading :showing="loadingScopes">
                      <q-spinner-gears size="50px" color="primary"/>
                    </q-inner-loading>

                    <q-card-actions align="right">
                      <q-btn v-close-popup flat color="primary" label="返回"/>
                    </q-card-actions>

                  </q-card>
                </q-dialog>
              </q-popup-edit>
            </q-btn>
          </div>
          <div v-for="(value,key) in selectedClient.scopeDetails" class="text-caption text-grey">
            <q-chip>{{key}}({{value.description}})</q-chip>
          </div>
          <q-item class="text-center"
                  v-if="selectedClient.scope == null || selectedClient.scope.length == 0">
            <q-item-section>
              <q-item-label caption>
                无授权作用域
              </q-item-label>
            </q-item-section>
          </q-item>
        </q-card-section>

        <q-card-section class="q-pt-none">
          <div class="text-subtitle1">
            授权模式
            <q-btn flat round dense icon="edit">
              <q-popup-edit
                title="授权作用域"
                v-model="selectedClient.authorizedGrantTypes"
                auto-save
              >
                <q-list>
                  <q-item>
                    <q-item-section style="min-width: 100px">
                      <q-item-label overline>
                        模式列表 ({{selectedClient.authorizedGrantTypes.length}})
                      </q-item-label>
                    </q-item-section>
                    <q-item-section class="text-right">
                      <q-item-label>
                        <q-btn @click="()=>showGrantTypeDialog()" round dense icon="add"
                               flat/>
                      </q-item-label>
                    </q-item-section>
                  </q-item>
                  <q-item dense v-for="(type,i) in selectedClient.authorizedGrantTypes">
                    <q-item-section>
                      <q-item-label caption>
                        <q-chip
                          @remove="()=>removeClientGrantType(selectedClient, selectedClient.grantTypeDetails[type])"
                          removable>
                          {{type}}
                        </q-chip>
                      </q-item-label>
                    </q-item-section>
                  </q-item>
                  <q-item class="text-center"
                          v-if="selectedClient.authorizedGrantTypes == null || selectedClient.authorizedGrantTypes.length == 0">
                    <q-item-section>
                      <q-item-label caption>
                        无授权模式
                      </q-item-label>
                    </q-item-section>
                  </q-item>
                </q-list>
                <q-dialog v-model="selectingGrantTypes">
                  <q-card class="my-card">
                    <q-card-section>
                      <q-list separator>
                        <q-item>
                          <q-item-section>
                            <q-item-label overline>
                              选择授权作用域 {{selectedClient.authorizedGrantTypes.length}}/{{grantTypes.length}}
                            </q-item-label>
                          </q-item-section>
                        </q-item>
                        <q-separator/>
                        <q-item v-for="type in grantTypes">
                          <q-item-section>
                            <q-item-label>{{type.name}}</q-item-label>
                          </q-item-section>
                          <q-item-section class="text-right">
                            <q-item-label>
                              <q-icon v-if="type.flag" name="check"/>
                              <q-btn @click="()=>addGrantType(type,selectedClient)" v-else flat round dense
                                     icon="add"/>
                            </q-item-label>
                          </q-item-section>
                        </q-item>
                      </q-list>
                    </q-card-section>
                    <q-inner-loading :showing="loadingGrantTypes">
                      <q-spinner-gears size="50px" color="primary"/>
                    </q-inner-loading>

                    <q-card-actions align="right">
                      <q-btn v-close-popup flat color="primary" label="返回"/>
                    </q-card-actions>

                  </q-card>
                </q-dialog>
              </q-popup-edit>
            </q-btn>
          </div>
          <div v-for="type in selectedClient.authorizedGrantTypes" class="text-caption text-grey">
            <q-chip>{{type}}</q-chip>
          </div>
          <q-item class="text-center"
                  v-if="selectedClient.authorizedGrantTypes == null || selectedClient.authorizedGrantTypes.length == 0">
            <q-item-section>
              <q-item-label caption>
                无授权模式
              </q-item-label>
            </q-item-section>
          </q-item>
        </q-card-section>
        <q-separator/>
        <q-card-actions align="right">
          <q-btn icon="delete" @click="()=>{deleteClient(selectedClient)}" v-close-popup flat color="negative"
                 label="删除"/>
          <q-btn v-close-popup flat color="primary" label="返回"/>
        </q-card-actions>
      </q-card>


    </q-dialog>

    <q-inner-loading :showing="loading">
      <q-spinner-gears size="50px" color="primary"/>
    </q-inner-loading>
  </div>
</template>

<script>
  import qs from 'qs'

  export default {
    name: "Clients",
    inject: ["hasAuthority"],
    data() {
      return {
        loading: false,
        clients: [],
        selectedClient: {scope: [], authorizedGrantTypes: []},
        selected: false,
        selectingScope: false,
        scopes: [],
        loadingScopes: false,
        selectingGrantTypes: false,
        grantTypes: [],
        loadingGrantTypes: false
      }
    },
    methods: {
      load() {
        if (this.loading)
          return
        this.loading = true
        this.$uim.ax.get("api/client/clients")
          .then(res => {
            this.clients = res
          }).finally(() => {
          this.loading = false
        })
      },
      applyForDeveloper() {
        this.$q.loading.show()
        this.$uim.ax.post("api/user/applyForDeveloper")
          .then(r => {
            this.$uim.ax.get("api/user/logout")
              .finally(() => {
                location.reload()
              })
          }).catch(e => {
          this.$q.loading.hide()
        })
      },
      select(client) {
        this.selectedClient = client
        if (client.registeredRedirectUri == null)
          client.registeredRedirectUri = []
        this.selected = true
      },
      resetSecret(client) {
        this.$q.dialog({
          title: '是否重置',
          icon: 'message',
          message: '是否要重置应用"' + client.clientName + '"的AppSecret？这将导致旧的Secret失效。',
          cancel: true,
          ok: {
            label: "确定",
            color: "negative",
            flat: true
          },
          cancel: {label: "取消", color: "primary", flat: true}
        }).onOk(() => {
          this.$q.loading.show()
          this.$uim.ax.post("api/client/app_secret/" + client.clientId).then(res => {
            client.clientSecret = res
          }).finally(() => {
            this.$q.loading.hide()
          })
        })
      },
      deleteClient(client) {
        this.$q.dialog({
          title: '是否删除应用',
          icon: 'message',
          message: '删除操作不可撤销，是否要删除应用"' + client.clientName + '"？',
          cancel: true,
          ok: {
            label: "确定",
            color: "negative",
            flat: true
          },
          cancel: {label: "取消", color: "primary", flat: true}
        }).onOk(() => {
          this.$q.loading.show()
          this.$uim.ax.delete("api/client/app/" + client.clientId).then(res => {
            this.load()
          }).finally(() => {
            this.$q.loading.hide()
          })
        })
      },
      updateRedirectUri(val, initVal, client) {
        if (val == null)
          return
        this.$q.loading.show()
        let data = ""
        let i = 0
        val.forEach((uri) => {
          if (i > 0)
            data += ","
          data += encodeURI(uri)
          i++
        })
        this.$uim.ax
          .post("api/client/app_redirect_uri/" + client.clientId, qs.stringify({redirectUri: data}))
          .catch(e => {
            client.registeredRedirectUri = initVal
          })
          .finally(() => {
            this.$q.loading.hide()
          })
      },
      updateClientName(val, initVal, client) {
        if (val == null)
          return
        this.$q.loading.show()
        this.$uim.ax
          .post("api/client/app_name/" + client.clientId, qs.stringify({name: val}))
          .catch(e => {
            client.name = initVal
          })
          .finally(() => {
            this.$q.loading.hide()
          })
      },
      updateClientDescription(val, initVal, client) {
        if (val == null)
          return
        this.$q.loading.show()
        this.$uim.ax
          .post("api/client/app_description/" + client.clientId, qs.stringify({description: val}))
          .catch(e => {
            client.description = initVal
          })
          .finally(() => {
            this.$q.loading.hide()
          })
      },
      showScopeDialog() {
        this.selectingScope = true
        this.loadingScopes = true
        this.$uim.ax
          .get("api/client/scopes")
          .then(res => {
            this.scopes = []
            res.forEach(val => {
              val.flag = this.selectedClient.scope.indexOf(val.name) >= 0
              this.scopes.push(val)
            })
          }).finally(() => {
          this.loadingScopes = false
        })
      },
      removeClientScope(client, scope) {
        this.$q.loading.show()
        this.$uim.ax.delete("api/client/app_scopes/" + encodeURIComponent(client.clientId) + "?" + qs.stringify({scopes: [scope.id]}, {indices: false}))
          .then(r => {
            client.scope.splice(client.scope.indexOf(scope.name), 1)
            delete client.scopeDetails[scope.name]
          }).finally(() => {
          this.$q.loading.hide()
        })
      },
      addScope(scope, client) {
        this.$q.loading.show()
        this.$uim.ax.post("api/client/app_scopes/" + encodeURIComponent(client.clientId) + "?" + qs.stringify({scopes: [scope.id]}, {indices: false}))
          .then(r => {
            client.scope.push(scope.name)
            client.scopeDetails[scope.name] = scope
            scope.flag = true
          }).finally(() => {
          this.$q.loading.hide()
        })
      },
      showGrantTypeDialog() {
        this.selectingGrantTypes = true
        this.loadingGrantTypes = true
        this.$uim.ax
          .get("api/client/grant_types")
          .then(res => {
            this.grantTypes = []
            res.forEach(val => {
              val.flag = this.selectedClient.authorizedGrantTypes.indexOf(val.name) >= 0
              this.grantTypes.push(val)
            })
          }).finally(() => {
          this.loadingGrantTypes = false
        })
      },
      removeClientGrantType(client, type) {
        this.$q.loading.show()
        this.$uim.ax.delete("api/client/app_grant_types/" + encodeURIComponent(client.clientId) + "?" + qs.stringify({types: [type.id]}, {indices: false}))
          .then(r => {
            client.authorizedGrantTypes.splice(client.authorizedGrantTypes.indexOf(type.name), 1)
            delete client.grantTypeDetails[type.name]
          }).finally(() => {
          this.$q.loading.hide()
        })
      },
      addGrantType(type, client) {
        this.$q.loading.show()
        this.$uim.ax.post("api/client/app_grant_types/" + encodeURIComponent(client.clientId) + "?" + qs.stringify({types: [type.id]}, {indices: false}))
          .then(r => {
            client.authorizedGrantTypes.push(type.name)
            client.grantTypeDetails[type.name] = type
            type.flag = true
          }).finally(() => {
          this.$q.loading.hide()
        })
      }
    },
    mounted() {
      this.load()
    }
  }
</script>

<style scoped>

</style>
