<template>
  <div>
    <q-list v-if="loading">
      <transition
          v-for="i in 3"
          :key="i"
          appear
          enter-active-class="animated fadeIn"
          leave-active-class="animated fadeOut">
        <q-item
            :dense="dense"
            :class="itemClass"
        >
          <q-item-section avatar style="min-width: 0px;">
            <q-icon name="person"></q-icon>
          </q-item-section>
          <q-item-section>
            <q-item-label>
              <q-skeleton type="text" width="2em"/>
            </q-item-label>
            <q-item-label caption>
              <q-skeleton type="text" width="5em"/>
            </q-item-label>
          </q-item-section>
          <q-item-section side>
            <q-skeleton v-if="managed" type="QAvatar" size="1.5em"/>
            <q-skeleton v-else type="text" width="2em"/>
          </q-item-section>
        </q-item>
      </transition>
    </q-list>
    <div v-else>
      <q-list v-if="targetRoles && targetRoles.length>0">
        <transition
            v-for="(role,index) in targetRoles"
            :key="role.rid"
            appear
            enter-active-class="animated fadeIn"
            leave-active-class="animated fadeOut">
          <q-item
              :dense="dense"
              :class="itemClass"
              clickable v-ripple
          >
            <q-item-section avatar style="min-width: 0px;">
              <q-icon name="person" :color="managed && hasRole(role) ? 'accent' : ''"></q-icon>
            </q-item-section>
            <q-item-section>
              <q-item-label v-if="managed && hasRole(role)" class="text-accent">{{ role.roleName }}</q-item-label>
              <q-item-label v-else>{{ role.roleName }}</q-item-label>
              <q-item-label caption>{{ role.roleDescription }}</q-item-label>
            </q-item-section>

            <!-- 添加或删除角色 -->
            <q-item-section side v-if="managed">
              <q-btn flat round
                     :icon="hasRole(role) ? 'remove' : 'add'"
                     @click.stop="()=>grantOrRevokeRole(role)"
                     :loading="isRoleUpdating(role)"
              />
            </q-item-section>
            <!-- 对角色时限进行编辑 -->
            <q-item-section side v-else>
              <q-item-label v-if="role.expiredAt">
                <div class="text-caption text-grey" v-if="!(hasGrantUserPermission || isClientOwner || isClientMember)">
                  <span>{{ $tt($options, "expiredAt") }}</span>
                  <span class="q-ml-xs">
                                            {{ $util.dateFormat(role.expiredAt, "YYYY/mm/dd HH:MM:SS") }}
                                          </span>
                </div>
                <q-btn @click.stop="()=>editUserRole(role)" v-else no-caps flat dense
                       class="text-caption text-grey">
                  <span>{{ $tt($options, "expiredAt") }}</span>
                  <span class="q-ml-xs">
                                            {{ $util.dateFormat(role.expiredAt, "YYYY/mm/dd HH:MM:SS") }}
                                          </span>
                </q-btn>
              </q-item-label>
              <q-item-label v-else-if="hasGrantUserPermission || isClientOwner || isClientMember">
                <q-btn @click.stop="()=>editUserRole(role)" class="text-grey" round flat icon="timer"/>
              </q-item-label>
            </q-item-section>
          </q-item>
        </transition>

        <transition
            v-if="managed"
            appear
            enter-active-class="animated fadeIn"
            leave-active-class="animated fadeOut">
          <div>
            <q-separator class="q-ma-sm"/>
            <div class="text-center text-caption text-grey ">
              {{ usersRoles.length + " / " + roles.length }}
            </div>
          </div>
        </transition>
      </q-list>
      <no-results v-else/>

    </div>
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
    dense: Boolean,
    loadOnMounted: Boolean,
    managed: Boolean,
    itemClass: String,
    onUpdated: Function,
    updating: Array
  },
  data() {
    return {
      usersRoles: [],
      roles: [],
      loading: false,
      loaded: false
    }
  },
  methods: {
    roleGranted(role) {
      this.usersRoles.push(role)
    },
    roleRevoked(role) {
      let index = this.getRoleIndex(role);
      if (index < 0)
        return
      this.usersRoles.splice(index, 1)
    },
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
    grantOrRevokeRole(role) {
      if (this.updating.indexOf(role.rid) >= 0)
        return 0;
      if (this.hasRole(role)) {
        this.updating.push(role.rid);
        this.$rolesApi.deleteUserClientRoles(this.user.uid, this.client.cid, [role.rid])
            .then(res => {
              this.roleRevoked(role)
              if (this.onUpdated)
                this.onUpdated(this.client, role, 0, this.usersRoles.length)
            })
            .finally(() => this.updating.splice(this.updating.indexOf(role.rid), 1))
      } else {
        this.updating.push(role.rid);
        this.$rolesApi.setUserClientRoles(this.user.uid, this.client.cid, [{rid: role.rid}])
            .then(res => {
              this.roleGranted(role)
              if (this.onUpdated)
                this.onUpdated(this.client, role, 1, this.usersRoles.length)
            })
            .finally(() => this.updating.splice(this.updating.indexOf(role.rid), 1))
      }
    },
    hasPermission(authority) {
      return this.currentUser && this.currentUser.authorities && this.currentUser.authorities.indexOf(authority) >= 0;
    },
    hasRole(role) {
      let roleName = role;

      if (roleName instanceof Object || roleName instanceof Array) {
        roleName = roleName['roleName']
      }
      if (!roleName) {
        console.error("Illegal role name '" + roleName + "'");
        return false;
      }
      for (let i in this.usersRoles) {
        if (this.usersRoles[i].roleName == roleName)
          return true;
      }
      return false;
    },
    getRoleIndex(role) {
      let roleName = role;

      if (roleName instanceof Object || roleName instanceof Array) {
        roleName = roleName['roleName']
      }
      if (!roleName) {
        console.error("Illegal role name '" + roleName + "'");
        return false;
      }
      for (let i in this.usersRoles) {
        if (this.usersRoles[i].roleName == roleName)
          return i;
      }
      return -1;
    },
    isRoleUpdating(role) {
      return this.updating.indexOf(role.rid) >= 0;
    },
    load() {
      if (this.loading || !this.user || !this.user.uid || !this.client || !this.client.cid)
        return
      this.loading = true
      this.$rolesApi.getUserClientRoles(this.user.uid, this.client.cid)
          .then(res => {
            this.usersRoles = res.data
            if (this.managed)
              return this.$rolesApi.getClientRoles(this.client.cid).then(res => this.roles = res.data);
          })
          .catch(e => {
            this.usersRoles = []
            if (this.managed)
              this.roles = []
            return Promise.reject(e)
          })
          .finally(() => {
            this.loading = false
          })
    },
    loadOnce() {
      if (this.loaded)
        return
      this.loaded = true
      this.load()
    }
  },
  computed: {
    hasGrantUserPermission() {
      return this.hasPermission("GRANT_USER");
    },
    isClientOwner() {
      return this.currentUser && this.currentUser.uid && this.client && this.client.owner && this.currentUser.uid == this.client.owner.uid
    },
    isClientMember() {
      return this.currentUser && this.currentUser.uid && this.client && this.client.members && this.client.members.indexOf(this.currentUser.uid) >= 0
    },
    targetRoles() {
      return this.managed ? this.roles : this.usersRoles;
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
    if (this.loadOnMounted)
      this.load()
  }
}
</script>

<style scoped>

</style>
