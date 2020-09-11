<template>
  <q-page class="q-pa-md">
    <q-list>
      <q-item clickable v-ripple v-for="client in clients"
              :key="client" @click="preAuthorize(client)">
        <q-item-section avatar>
          <q-avatar v-if="clientInfo[client] && clientInfo[client].icon" :icon="clientInfo[client].icon"/>
          <q-avatar v-else icon="home"/>
        </q-item-section>
        <q-item-section>
          <q-item-label>
            {{clientInfo[client] && clientInfo[client].name ? clientInfo[client].name : client}}
          </q-item-label>
        </q-item-section>
      </q-item>
    </q-list>
    <div>
      {{clientInfo}}
    </div>
  </q-page>
</template>

<script>
  import qs from 'qs'
  import ClientInfo from "components/ClientInfo";

  export default {
    name: "Login",
    data() {
      return {
        clients: [],
        loading: false,
        clientInfo: ClientInfo
      }
    },
    computed: {
      baseUrl() {
        let url = window.location.origin + window.location.pathname
        if (!url.endsWith("/"))
          url += "/";
        return url
      }
    },
    methods: {
      loadClients() {
        this.loading = true
        this.$uclient.getClients()
          .then(r => this.clients = r)
          .finally(() => this.loading = false)
      },
      preAuthorize(client) {
        let redirect_uri = this.baseUrl + "code/" + client
        this.$uclient.preAuthorization(client, redirect_uri)
          .then(r => {
            this.$q.sessionStorage.remove("login_redirect_uri")
            if (this.$route.query.redirect_uri)
              this.$q.sessionStorage.set("login_redirect_uri", this.$route.query.redirect_uri) // 存储跳转地址
            let scopeString = ""
            if (r.scopes)
              r.scopes.forEach(scope => {
                if (scopeString.length > 0)
                  scopeString += " "
                scopeString += scope;
              })
            let params = {
              client_id: r.client_id,
              response_type: r.response_type,
              scope: scopeString,
              state: r.state,
              redirect_uri: redirect_uri
            }
            let authorization_uri = r.authorization_uri
            let str = qs.stringify(params)
            window.location.href = authorization_uri + "?" + str
          })
      }
    },
    mounted() {
      this.loadClients()
    }
  }
</script>

<style scoped>

</style>
