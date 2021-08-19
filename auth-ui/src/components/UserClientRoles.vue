<template>
  <div>
    <q-list v-if="roles && roles.length>0">
      <q-item
        :dense="dense"
        v-for="(role,index) in roles"
        :key="role.rid"
        class="q-pl-none"
      >
        <q-item-section avatar style="min-width: 0px;">
          <q-icon name="person"></q-icon>
        </q-item-section>
        <q-item-section>
          <q-item-label>{{ role.roleName }}</q-item-label>
          <q-item-label caption>{{ role.roleDescription }}</q-item-label>
        </q-item-section>
        <q-item-section class="text-right row q-pa-none">
          <q-item-label v-if="role.expiredAt">
            <div class="text-caption text-grey" v-if="!hasGrantUserPermission">
              <span>{{ $tt($options, "expiredAt") }}</span>
              <span class="q-ml-xs">
                                            {{ $util.dateFormat(role.expiredAt, "YYYY/mm/dd HH:MM:SS") }}
                                          </span>
            </div>
            <q-btn @click="()=>editUserRole(role)" v-else no-caps flat dense
                   class="text-caption text-grey">
              <span>{{ $tt($options, "expiredAt") }}</span>
              <span class="q-ml-xs">
                                            {{ $util.dateFormat(role.expiredAt, "YYYY/mm/dd HH:MM:SS") }}
                                          </span>
            </q-btn>
          </q-item-label>
          <q-item-label v-else-if="hasGrantUserPermission">
            <q-btn @click="()=>editUserRole(role)" class="text-grey" round flat icon="timer"/>
          </q-item-label>
        </q-item-section>
      </q-item>
    </q-list>
    <no-results v-else/>
  </div>
</template>

<script>
import RequireAuthorization from "./RequireAuthorization";
import NoResults from "./NoResults";
import UserRoleSettings from "./UserRoleSettings";

export default {
  name: "UserClientRoles",
  components: {NoResults, RequireAuthorization},
  props: {
    user: Object,
    client: Object,
    currentUser: Object,

    dense: Boolean
  },
  data() {
    return {
      roles: [],
      loading: false
    }
  },
  methods: {
    editUserRole(role) {
      this.$q.dialog({
        component: UserRoleSettings,
        parent: this,
        currentUser: this.currentUser,
        user: this.user,
        client: this.client,
        role: role
      })
    },
    hasPermission(authority) {
      return this.currentUser && this.currentUser.authorities && this.currentUser.authorities.indexOf(authority) >= 0;
    },
    load() {
      if (this.loading || !this.user || !this.user.uid || !this.client || !this.client.cid)
        return
      this.loading = true
      this.$rolesApi.getUserClientRoles(this.user.uid, this.client.cid)
        .then(res => {
          this.roles = res.data
        })
        .catch(e => {
          this.roles = []
          return Promise.reject(e)
        })
        .finally(() => {
          this.loading = false
        })

    }
  },
  computed: {
    hasWriteUserPermission() {
      return this.hasPermission("WRITE_USER");
    },
    hasWriteUserEmailPermission() {
      return this.hasPermission("WRITE_USER_EMAIL");
    },
    hasWriteUserPhonePermission() {
      return this.hasPermission("WRITE_USER_PHONE");
    },
    hasWriteUserPasswordPermission() {
      return this.hasPermission("WRITE_USER_PASSWORD");
    },
    hasGrantUserPermission() {
      return this.hasPermission("GRANT_USER");
    },
    hasDeleteUserPermission() {
      return this.hasPermission("DELETE_USER");
    },
    hasLockUserPermission() {
      return this.hasPermission("LOCK_USER");
    }
  },
  watch: {
    "user.uid"() {
      this.load()
    },
    "client.cid"() {
      this.load()
    }
  },
  mounted() {
    this.load()
  }
}
</script>

<style scoped>

</style>
