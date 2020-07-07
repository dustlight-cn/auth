<template>
  <div class="q-pa-md">
    <q-list title="asd" separator>
      <q-item @click="select(client)" v-for="client in clients" clickable v-ripple>
        <q-item-section>
          <q-item-label>{{client.clientName}}</q-item-label>
          <q-item-label caption>{{client.description}}</q-item-label>
        </q-item-section>
      </q-item>
      <q-page-sticky position="bottom-right" :offset="[18, 18]">
        <q-btn to="./clients/create" v-if="hasAuthority('CREATE_CLIENT')" color="primary" icon="add" round/>
      </q-page-sticky>
    </q-list>

    <!-- Card -->
    <q-dialog v-model="selected">
      <q-card class="my-card">
        <q-card-section>
          <div class="row no-wrap items-center">
            <div class="col text-h6 ellipsis">
              {{selectedClient.clientName}}
            </div>
          </div>
        </q-card-section>
        <q-card-section class="q-pt-none">
          <div class="text-subtitle1">
            描述
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
          <q-btn v-close-popup flat color="primary" label="返回"/>
        </q-card-actions>
      </q-card>
    </q-dialog>
  </div>
</template>

<script>
  import axios from 'axios'

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
        this.$q.loading.show()
        axios.get("/api/client/clients")
          .then(res => {
            this.clients = res
          }).finally(() => {
          this.$q.loading.hide()
          this.loading = false
        })
      },
      select(client) {
        this.selectedClient = client
        this.selected = true
      }, resetSecret(client) {
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
            console.log(res)
            client.clientSecret = res
          }).finally(() => {
            this.$q.loading.hide()
          })
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
