<template>
  <div>
    <q-list v-if="roleClients && roleClients.length > 0">
      <q-expansion-item
        :group="user.uid + '-' + 'roleClients'"
        v-for="(client,index) in roleClients"
        :key="client.cid">
        <template v-slot:header>
          <q-item-section avatar>
            <client-logo :client="client"/>
          </q-item-section>
          <q-item-section>
            <q-item-label>
              {{ client.name }}
            </q-item-label>
          </q-item-section>
          <q-item-section side>
            <q-item-label>
              <q-icon name="person"/>
              <b>{{ client.count }}</b>
            </q-item-label>
          </q-item-section>
        </template>
        <template v-slot:default>
          <user-client-roles class="q-pl-md" :user="user" :client="client" :current-user="currentUser" dense/>
        </template>
      </q-expansion-item>
    </q-list>
    <no-results v-else/>
  </div>
</template>

<script>
import ClientLogo from "./ClientLogo";
import NoResults from "./NoResults";
import UserClientRoles from "./UserClientRoles";

export default {
  name: "UserRoleClients",
  components: {UserClientRoles, NoResults, ClientLogo},
  props: {
    user: Object,
    currentUser: Object
  },
  data() {
    return {
      roleClients: [],
      loading: false
    }
  },
  methods: {
    load() {
      if (this.loading || !this.user || !this.user.uid)
        return
      this.loading = true;
      this.$rolesApi.getUserRoleClients(this.user.uid)
        .then(res => {
          this.roleClients = res.data
        })
        .catch(e => {
          this.roleClients = []
          return Promise.reject(e)
        })
        .finally(() => this.loading = false)
    }
  },
  mounted() {
    this.load();
  },
  watch: {
    user() {
      this.load()
    }
  }
}
</script>

<style scoped>

</style>
