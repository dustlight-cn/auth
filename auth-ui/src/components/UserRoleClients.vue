<template>
  <div>
    <q-list v-if="loading">
      <transition
        v-for="i in 3" :key="i"
        appear
        enter-active-class="animated fadeIn"
        leave-active-class="animated fadeOut">
        <q-item class="q-pl-xs " style="min-width: 0px">
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
      </transition>
    </q-list>
    <div v-else>
      <q-list v-if="roleClients && roleClients.length > 0">
        <transition
          v-for="(client,index) in roleClients"
          :key="client.cid"
          appear
          enter-active-class="animated fadeIn"
          leave-active-class="animated fadeOut">
          <q-expansion-item
            headerClass="q-pa-none"
            @before-show="()=>$refs['user-client-role-'+index][0].loadOnce()"
            :group="user.uid + '-' + 'roleClients' + (managed?'-managed':'')">
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
              <user-client-roles :ref="'user-client-role-'+index" :user="user" :client="client"
                                 :updating="updating.roles"
                                 :onUpdated="onRoleGrantOrRevoke"
                                 :managed="managed"
                                 :current-user="currentUser" dense/>
            </template>
          </q-expansion-item>
        </transition>
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
    currentUser: Object,
    managed: Boolean,
    onRoleGrantOrRevoke: Function,
    onRoleUpdating: Function
  },
  data() {
    return {
      roleClients: [],
      loading: false,
      updating: {
        roles: []
      }
    }
  },
  methods: {
    getClientIndex(clientId) {
      for (let i in this.roleClients)
        if (this.roleClients[i].cid == clientId)
          return i;
      return -1;
    },
    updateRole(client, role, type, count) {
      let index = this.getClientIndex(client.cid)
      if (index == -1) {
        this.roleClients.push(client)
        this.$nextTick(() => this.updateRole(client, role, type, count))
        return
      }
      this.roleClients[index].count = count;
      if (count == 0)
        this.roleClients.splice(index, 1);
      let component = this.$refs['user-client-role-' + index][0]
      if (type == 1)
        component.roleGranted(role)
      else component.roleRevoked(role)
    },
    load() {
      if (this.loading || !this.user || !this.user.uid)
        return
      this.loading = true;
      this.$rolesApi.getUserRoleClients(this.managed ? this.currentUser.uid : this.user.uid
        , this.managed)
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
    },
    "updating.roles"() {
      if (!this.onRoleUpdating)
        return
      this.onRoleUpdating(this.updating.roles)
    }
  }
}
</script>

<style scoped>

</style>
