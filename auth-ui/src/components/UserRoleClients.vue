<template>
  <div>
    <q-list v-if="loading">
      <q-item class="q-pl-xs " style="min-width: 0px" v-for="i in 3" :key="i">
        <q-item-section avatar>
          <q-skeleton size="30px" square type="QAvatar"/>
        </q-item-section>
        <q-item-section>
          <q-skeleton type="text" width="4em"/>
        </q-item-section>
        <q-item-section side>
          <q-skeleton type="text" width="2em"/>
        </q-item-section>
      </q-item>
    </q-list>
    <div v-else>
      <q-list v-if="roleClients && roleClients.length > 0">
        <q-expansion-item
          headerClass="q-pa-none"
          @before-show="()=>$refs['user-client-role-'+index][0].loadOnce()"
          :group="user.uid + '-' + 'roleClients'"
          v-for="(client,index) in roleClients"
          :key="client.cid">
          <template v-slot:header>
            <q-item-section class="q-pl-xs" avatar style="min-width: 0px">
              <client-logo :client="client"/>
            </q-item-section>
            <q-item-section>
              <q-item-label>
                {{ client.name }}
              </q-item-label>
            </q-item-section>
            <q-item-section side>
              <q-item-label caption>
                <q-icon name="person"/>
                <b>{{ client.count }}</b>
              </q-item-label>
            </q-item-section>
          </template>
          <template v-slot:default>
            <user-client-roles :ref="'user-client-role-'+index" class="q-pl-md" :user="user" :client="client"
                               :current-user="currentUser" dense/>
          </template>
        </q-expansion-item>
      </q-list>
      <no-results v-else/>
    </div>
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
      loading: false,
      console: console
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
