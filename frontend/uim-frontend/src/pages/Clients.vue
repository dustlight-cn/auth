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
        <q-btn v-else icon="security" flat color="primary" label="申请成为开发者"/>
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
          </div>
          <div v-for="(value,key) in selectedClient.scopeDescriptions" class="text-caption text-grey">
            <q-chip>{{key}}({{value}})</q-chip>
          </div>
        </q-card-section>
        <q-card-section class="q-pt-none">
          <div class="text-subtitle1">
            授权模式
          </div>
          <div v-for="type in selectedClient.authorizedGrantTypes" class="text-caption text-grey">
            <q-chip>{{type}}</q-chip>
          </div>
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
  import axios from 'axios'
  import qs from 'qs'

  export default {
    name: "Clients",
    inject: ["hasAuthority"],
    data() {
      return {
        loading: false,
        clients: [],
        selectedClient: {},
        selected: false
      }
    },
    methods: {
      load() {
        if (this.loading)
          return
        this.loading = true
        axios.get("/api/client/clients")
          .then(res => {
            this.clients = res
          }).finally(() => {
          this.loading = false
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
          axios.post("/api/client/app_secret/" + client.clientId).then(res => {
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
          axios.delete("/api/client/app/" + client.clientId).then(res => {
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
        axios
          .post("/api/client/app_redirect_uri/" + client.clientId, qs.stringify({redirectUri: data}))
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
        axios
          .post("/api/client/app_name/" + client.clientId, qs.stringify({name: val}))
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
        axios
          .post("/api/client/app_description/" + client.clientId, qs.stringify({description: val}))
          .catch(e => {
            client.description = initVal
          })
          .finally(() => {
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
