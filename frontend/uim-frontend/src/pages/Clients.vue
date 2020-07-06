<template>
  <div class="q-pa-md">
    <q-list title="asd" separator>
      <q-item v-for="client in clients" clickable v-ripple>
        <q-item-section>
          <q-item-label>{{client.clientName}}</q-item-label>
          <q-item-label caption>{{client.description}}</q-item-label>
        </q-item-section>
      </q-item>
      <q-page-sticky position="bottom-right" :offset="[18, 18]">
        <q-btn v-if="hasAuthority('CREATE_CLIENT')" color="primary" icon="add" round dense/>
      </q-page-sticky>
    </q-list>
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
        clients: []
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
            console.log(res)
          }).finally(() => {
          this.$q.loading.hide()
          this.loading = false
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
