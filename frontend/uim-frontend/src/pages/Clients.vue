<template>
  <q-page class="q-pa-md">
    <q-list separator>
      <q-item>
        <q-item-section>
          <q-item-label overline>应用列表 ({{clients.length}})</q-item-label>
        </q-item-section>
      </q-item>
      <q-item @click="select(client)" v-for="client in clients" :key="client.clientId" clickable v-ripple>
        <q-item-section avatar>
          <ClientImage :client="client" size="50"/>
        </q-item-section>
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

    <!-- ClientDetails -->
    <q-dialog v-model="selected">
      <ClientDetails :afterDelete="load" :client="selectedClient"/>
    </q-dialog>

    <q-inner-loading :showing="loading">
      <q-spinner-gears size="50px" color="primary"/>
    </q-inner-loading>
  </q-page>
</template>

<script>
  import ClientDetails from "components/ClientDetails";
  import ClientImage from "components/ClientImage";

  export default {
    name: "Clients",
    components: {ClientImage, ClientDetails},
    inject: ["hasAuthority", "user"],
    data() {
      return {
        loading: false,
        clients: [],
        selectedClient: {scope: [], authorizedGrantTypes: []},
        selected: false
      }
    },
    methods: {
      load() {
        if (this.loading)
          return
        this.loading = true
        this.$uim.client.getCurrentUserClients()
          .then(res => this.clients = res)
          .finally(() => this.loading = false)
      },
      applyForDeveloper() {
        this.$q.loading.show()
        this.$uim.user.applyForDeveloper()
          .then(() => this.user().logout())
          .catch((e) => this.$q.loading.hide())
      },
      select(client) {
        this.selectedClient = client
        if (client.registeredRedirectUri == null)
          client.registeredRedirectUri = []
        this.selected = true
      }
    },
    mounted() {
      this.load()
    }
  }
</script>

<style scoped>

</style>
