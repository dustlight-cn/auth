<template>
  <q-card>
    <!-- 应用名 -->
    <q-card-section>
      <div class="row no-wrap items-center">
        <div class="col text-h6">
          {{client.clientName}}
          <q-btn flat round dense icon="edit">
            <q-popup-edit
              @save="updateClientName"
              buttons
              v-model="client.clientName"
              label-set="保存"
              label-cancel="取消"
            >
              <q-input label="应用名" hint="应用名不可重复" v-model="client.clientName" counter/>
            </q-popup-edit>
          </q-btn>
        </div>
      </div>
    </q-card-section>

    <!-- 应用描述 -->
    <q-card-section class="q-pt-none">
      <div class="text-subtitle1">
        描述
        <q-btn flat round dense icon="edit">
          <q-popup-edit
            @save="updateClientDescription"
            buttons
            v-model="client.description"
            label-set="保存"
            label-cancel="取消"
          >
            <q-input label="应用描述" hint="应用名不可重复" v-model="client.description" counter/>
          </q-popup-edit>
        </q-btn>
      </div>
      <div class="text-caption text-grey">
        {{client.description}}
      </div>
    </q-card-section>

    <!-- AppKey & AppSecret -->
    <q-card-section class="q-pt-none">
      <div class="text-subtitle1">
        AppKey
      </div>
      <div class="text-caption text-grey">
        {{client.clientId}}
      </div>
    </q-card-section>
    <q-card-section class="q-pt-none">
      <div class="text-subtitle1">
        AppSecret
      </div>
      <div v-if="client.clientSecret" class="text-caption text-grey">
        {{client.clientSecret}}
      </div>
      <q-btn dense @click="resetSecret" flat color="negative" v-else label="重置"/>
    </q-card-section>

    <!-- 回调地址 -->
    <q-card-section class="q-pt-none">
      <div class="text-subtitle1">
        回调地址
        <q-btn flat round dense icon="edit">
          <q-popup-edit
            @save="updateRedirectUri"
            buttons
            v-model="client.registeredRedirectUri"
            label-set="保存"
            label-cancel="取消"
          >
            <q-input
              v-for="(uri,i) in client.registeredRedirectUri"
              :key="i"
              rounded bottom-slots dense
              v-model="client.registeredRedirectUri[i]" :label="'URL-' + (i + 1)" hint="" counter>
              <template v-slot:append>
                <q-btn round dense
                       @click="()=>client.registeredRedirectUri.splice(i,1)"
                       icon="delete"
                       color="negative"
                       flat/>
              </template>
            </q-input>

            <q-btn @click="()=>client.registeredRedirectUri.push('')" round dense icon="add"
                   color="primary"
                   flat/>
          </q-popup-edit>
        </q-btn>
      </div>
      <div v-for="(uri,i) in client.registeredRedirectUri" :key="i" class="text-caption text-grey">
        {{uri}}
      </div>
    </q-card-section>

    <!-- 授权作用域（Scopes） -->
    <q-card-section class="q-pt-none">
      <div class="text-subtitle1">
        授权作用域
        <q-btn flat round dense icon="edit">
          <q-popup-edit
            title="授权作用域"
            v-model="client.scope"
            auto-save
          >
            <q-list>
              <q-item>
                <q-item-section style="min-width: 100px">
                  <q-item-label overline>
                    作用域列表 ({{client.scope.length}})
                  </q-item-label>
                </q-item-section>
                <q-item-section class="text-right">
                  <q-item-label>
                    <q-btn @click="showScopeDialog" round dense icon="add"
                           flat/>
                  </q-item-label>
                </q-item-section>
              </q-item>
              <q-item dense v-for="(scope,i) in client.scope" :key="scope.id">
                <q-item-section>
                  <q-item-label caption>
                    <q-chip
                      @remove="()=>removeClientScope(client.scopeDetails[client.scope[i]])"
                      removable>
                      {{client.scope[i] + '(' +
                      client.scopeDetails[client.scope[i]].description +
                      ")"}}
                    </q-chip>
                  </q-item-label>
                </q-item-section>
              </q-item>
              <q-item class="text-center"
                      v-if="client.scope == null || client.scope.length == 0">
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
                          选择授权作用域 {{client.scope.length}}/{{scopes.length}}
                        </q-item-label>
                      </q-item-section>
                    </q-item>
                    <q-separator/>
                    <q-item v-for="scope in scopes" :key="scope.id">
                      <q-item-section>
                        <q-item-label>{{scope.name}}</q-item-label>
                        <q-item-label caption>{{scope.description}}</q-item-label>
                      </q-item-section>
                      <q-item-section class="text-right">
                        <q-item-label>
                          <q-icon v-if="scope.flag" name="check"/>
                          <q-btn @click="()=>addScope(scope)" v-else flat round dense
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
      <div v-for="(value,key) in client.scopeDetails" :key="key" class="text-caption text-grey">
        <q-chip>{{key}}({{value.description}})</q-chip>
      </div>
      <q-item class="text-center"
              v-if="client.scope == null || client.scope.length == 0">
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
            v-model="client.authorizedGrantTypes"
            auto-save
          >
            <q-list>
              <q-item>
                <q-item-section style="min-width: 100px">
                  <q-item-label overline>
                    模式列表 ({{client.authorizedGrantTypes.length}})
                  </q-item-label>
                </q-item-section>
                <q-item-section class="text-right">
                  <q-item-label>
                    <q-btn @click="showGrantTypeDialog" round dense icon="add"
                           flat/>
                  </q-item-label>
                </q-item-section>
              </q-item>
              <q-item dense v-for="(type,i) in client.authorizedGrantTypes" :key="i">
                <q-item-section>
                  <q-item-label caption>
                    <q-chip
                      @remove="()=>removeClientGrantType(client.grantTypeDetails[type])"
                      removable>
                      {{type}}
                    </q-chip>
                  </q-item-label>
                </q-item-section>
              </q-item>
              <q-item class="text-center"
                      v-if="client.authorizedGrantTypes == null || client.authorizedGrantTypes.length == 0">
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
                          选择授权作用域 {{client.authorizedGrantTypes.length}}/{{grantTypes.length}}
                        </q-item-label>
                      </q-item-section>
                    </q-item>
                    <q-separator/>
                    <q-item v-for="type in grantTypes" :key="type.id">
                      <q-item-section>
                        <q-item-label>{{type.name}}</q-item-label>
                      </q-item-section>
                      <q-item-section class="text-right">
                        <q-item-label>
                          <q-icon v-if="type.flag" name="check"/>
                          <q-btn @click="()=>addGrantType(type)" v-else flat round dense
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
      <div v-for="(type,i) in client.authorizedGrantTypes" :key="i" class="text-caption text-grey">
        <q-chip>{{type}}</q-chip>
      </div>
      <q-item class="text-center"
              v-if="client.authorizedGrantTypes == null || client.authorizedGrantTypes.length == 0">
        <q-item-section>
          <q-item-label caption>
            无授权模式
          </q-item-label>
        </q-item-section>
      </q-item>
    </q-card-section>
    <q-separator/>
    <q-card-actions align="right">
      <q-btn icon="delete" @click="deleteClient" v-close-popup flat color="negative"
             label="删除"/>
      <q-btn v-close-popup flat color="primary" label="返回"/>
    </q-card-actions>
  </q-card>
</template>
<script>
  export default {
    name: "ClientDetails",
    props: {
      client: {
        type: Object,
        default: function () {
          return {
            clientId: "",
            clientName: "",
            clientSecret: "",
            clientDescription: "",
            redirectUri: [],
            scopes: [],
            grantTypes: []
          }
        }
      },
      afterDelete: {
        type: Function,
        default: (clientId) => {
        }
      }
    },
    data() {
      return {
        selectingScope: false,
        scopes: [],
        loadingScopes: false,
        selectingGrantTypes: false,
        grantTypes: [],
        loadingGrantTypes: false
      }
    },
    methods: {
      resetSecret() {
        this.$q.dialog({
          title: '是否重置',
          icon: 'message',
          message: '是否要重置应用"' + this.client.clientName + '"的AppSecret？这将导致旧的Secret失效。',
          cancel: true,
          ok: {
            label: "确定",
            color: "negative",
            flat: true
          },
          cancel: {label: "取消", color: "primary", flat: true}
        }).onOk(() => {
          this.$q.loading.show()
          this.$uim.client.resetClientSecret(this.client.clientId)
            .then(res => this.client.clientSecret = res)
            .finally(() => this.$q.loading.hide())
        })
      },
      deleteClient() {
        this.$q.dialog({
          title: '是否删除应用',
          icon: 'message',
          message: '删除操作不可撤销，是否要删除应用"' + this.client.clientName + '"？',
          cancel: true,
          ok: {
            label: "确定",
            color: "negative",
            flat: true
          },
          cancel: {label: "取消", color: "primary", flat: true}
        }).onOk(() => {
          this.$q.loading.show()
          this.$uim.client.deleteClient(this.client.clientId)
            .then(res => this.afterDelete(this.clientId))
            .finally(() => this.$q.loading.hide())
        })
      },
      updateRedirectUri(val, initVal) {
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
        this.$uim.client.updateClientRedirectUri(this.client.clientId, data)
          .catch(e => this.client.registeredRedirectUri = initVal)
          .finally(() => this.$q.loading.hide())
      },
      updateClientName(val, initVal) {
        if (val == null)
          return
        this.$q.loading.show()
        this.$uim.client.updateClientName(this.client.clientId, val)
          .catch(e => this.client.name = initVal)
          .finally(() => this.$q.loading.hide())
      },
      updateClientDescription(val, initVal) {
        if (val == null)
          return
        this.$q.loading.show()
        this.$uim.client.updateClientDescription(this.client.clientId, val)
          .catch(e => this.client.description = initVal)
          .finally(() => this.$q.loading.hide())
      },
      showScopeDialog() {
        this.selectingScope = true
        this.loadingScopes = true
        this.$uim.client.getAllScopes()
          .then(res => {
            this.scopes = []
            res.forEach(val => {
              val.flag = this.client.scope.indexOf(val.name) >= 0
              this.scopes.push(val)
            })
          })
          .finally(() => this.loadingScopes = false)
      },
      removeClientScope(scope) {
        this.$q.loading.show()
        this.$uim.client.deleteClientScopes(this.client.clientId, [scope.id])
          .then(r => {
            this.client.scope.splice(this.client.scope.indexOf(scope.name), 1)
            delete this.client.scopeDetails[scope.name]
          })
          .finally(() => this.$q.loading.hide())
      },
      addScope(scope) {
        this.$q.loading.show()
        this.$uim.client.addClientScopes(this.client.clientId, [scope.id])
          .then(r => {
            this.client.scope.push(scope.name)
            this.client.scopeDetails[scope.name] = scope
            scope.flag = true
          })
          .finally(() => this.$q.loading.hide())
      },
      showGrantTypeDialog() {
        this.selectingGrantTypes = true
        this.loadingGrantTypes = true
        this.$uim.client.getAllGrantTypes()
          .then(res => {
            this.grantTypes = []
            res.forEach(val => {
              val.flag = this.client.authorizedGrantTypes.indexOf(val.name) >= 0
              this.grantTypes.push(val)
            })
          })
          .finally(() => this.loadingGrantTypes = false)
      },
      removeClientGrantType(type) {
        this.$q.loading.show()
        this.$uim.client.deleteClientGrantTypes(this.client.clientId, [type.id])
          .then(r => {
            this.client.authorizedGrantTypes.splice(this.client.authorizedGrantTypes.indexOf(type.name), 1)
            delete this.client.grantTypeDetails[type.name]
          })
          .finally(() => this.$q.loading.hide())
      },
      addGrantType(type) {
        this.$q.loading.show()
        this.$uim.client.addClientGrantTypes(this.client.clientId, [type.id])
          .then(r => {
            this.client.authorizedGrantTypes.push(type.name)
            this.client.grantTypeDetails[type.name] = type
            type.flag = true
          })
          .finally(() => this.$q.loading.hide())
      }
    }
  }
</script>
