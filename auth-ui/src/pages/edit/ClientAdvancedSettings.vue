<template>
  <div>
    <require-authorization>
      <template v-slot="{user}">
        {{ "", user_ == null || user != null && user_.uid != user.uid ? user_ = user : "" }}
        <!-- 错误页面 -->
        <q-page v-if="error" class="text-center">
          <div class="q-pa-md">
            <div style="font-size: 10vh">
              {{ error.details ? error.message : error.name }}
            </div>
            <div class="text-h4" style="opacity:.4">
              {{ error.details || error.message }}
            </div>
          </div>
        </q-page>

        <edit-page v-else>
          <template v-slot="{wide}">
            <!-- 骨架 -->
            <div v-if="loading || !client">
              <div class="q-mb-sm">
                <div class="text-h4 q-mb-sm">
                  <q-skeleton type="text" width="40%"/>
                </div>
                <q-separator class="q-ma-none"/>
                <q-item class="q-pa-none q-mt-sm">
                  <q-item-section avatar class="q-pa-none q-mr-md" style="min-width: 40px;">
                    <q-skeleton type="QAvatar"/>
                  </q-item-section>
                  <q-item-section>
                    <q-skeleton type="text" width="60%"/>
                  </q-item-section>
                </q-item>
              </div>
              <q-separator class="q-ma-none"/>
              <q-list class="q-mb-md">
                <q-item class="q-pa-none q-mt-md" v-for="index in 8" :key="index">
                  <q-item-section>
                    <q-item-label header class="q-pl-none">
                      <q-skeleton type="text" width="32%"/>
                    </q-item-label>
                    <q-item-label>
                      <q-skeleton type="text" width="52%"/>
                    </q-item-label>
                  </q-item-section>
                </q-item>
              </q-list>
            </div>
            <!-- 页面 -->
            <div v-else>
              <div class="q-mb-sm">
                <div class="text-h4 q-mb-sm">{{ client.name }}</div>
                <q-separator/>
                <q-item class="q-pa-none q-mt-sm">
                  <q-item-section avatar class="q-pa-none" style="min-width: 40px;">
                    <q-btn v-if="owner && owner.uid"
                           color="accent"
                           rounded dense flat no-caps :to="{name:'user',params:{id:owner.uid}}">
                      <avatar :size="36" :user="owner"/>
                      <span class="q-pl-sm q-pr-xs">{{ ownerName }}</span>
                    </q-btn>
                  </q-item-section>
                  <q-item-section v-if="owner">
                    <q-item-label>
                      {{ $tt("Client", "createdAt") }}
                      <span class="text-caption">{{ $util.dateFormat(client.createdAt) }}</span>
                    </q-item-label>
                  </q-item-section>
                  <q-item-section v-else>
                    <q-skeleton type="text" width="60%"/>
                  </q-item-section>

                </q-item>
              </div>
              <q-separator/>
              <q-list class="q-mb-md">
                <!-- 应用自定义角色, Client Custom Roles -->
                <q-item class="q-pa-none q-mt-md q-pr-sm">
                  <q-item-section>
                    <q-item-label header class="q-pl-none">
                      <span>{{ $tt($options, "roles") }}</span>
                      <q-icon dense size="1.25em" round flat name="help">
                        <q-tooltip>
                          {{ $tt($options, "rolesTips") }}
                        </q-tooltip>
                      </q-icon>
                    </q-item-label>
                  </q-item-section>
                  <q-item-section side>
                    <q-btn dense flat round icon="add" @click.stop="()=>$refs['roles'].add()"
                    :loading="$refs['roles'] && $refs['roles'].loading"/>
                  </q-item-section>
                </q-item>
                <roles ref="roles" :removable="hasWriteClientPermissionOrOwnClientOrMemberOfClient"
                       :editable="hasWriteClientPermissionOrOwnClientOrMemberOfClient"
                       :client="client"/>

                <!-- 应用自定义权限, Client Custom Authorities -->
                <q-item class="q-pa-none q-mt-md q-pr-sm">
                  <q-item-section>
                    <q-item-label header class="q-pl-none">
                      <span>{{ $tt($options, "authorities") }}</span>
                      <q-icon dense size="1.25em" round flat name="help">
                        <q-tooltip>
                          {{ $tt($options, "authoritiesTips") }}
                        </q-tooltip>
                      </q-icon>
                    </q-item-label>
                  </q-item-section>
                  <q-item-section side>
                    <q-btn dense flat round icon="add" @click.stop="()=>$refs['authorities'].add()"
                           :loading="$refs['authorities'] && $refs['roles'].loading"/>
                  </q-item-section>
                </q-item>
                <authorities ref="authorities" :removable="hasWriteClientPermissionOrOwnClientOrMemberOfClient"
                             :editable="hasWriteClientPermissionOrOwnClientOrMemberOfClient"
                             :client="client"/>


                <!-- 操作按钮 -->
                <q-separator class="q-mt-md"/>
                <div
                  class="text-center q-gutter-sm q-pt-lg">
                  <q-btn
                    color="grey-7"
                    flat icon="keyboard_arrow_left"
                    :label="$t('goBack')"
                    @click="()=>$router.back()"
                    no-caps/>
                </div>
              </q-list>
            </div>
          </template>
        </edit-page>
      </template>
    </require-authorization>

  </div>
</template>

<script>
import EditPage from "../../components/EditPage";
import RequireAuthorization from "../../components/RequireAuthorization";
import Avatar from "../../components/Avatar";
import Authorities from "../../components/Authorities";
import Roles from "../../components/Roles";

export default {
  name: "ClientAdvancedSettings",
  components: {Roles, Authorities, Avatar, RequireAuthorization, EditPage},
  computed: {
    ownerName() {
      if (this.owner == null)
        return null;
      let name = this.owner.nickname;
      if (name == null || name.trim().length == 0)
        name = this.owner.username;
      return name;
    },
    isOwner() {
      return this.owner && this.user_ && this.owner.uid && this.user_.uid && this.owner.uid == this.user_.uid;
    },
    isMember() {
      return this.client && this.user_ && this.user_.uid && this.client.members && this.client.members.indexOf(this.user_.uid) >= 0
    },
    hasWriteClientPermission() {
      return this.hasPermission("WRITE_CLIENT");
    },
    hasWriteClientPermissionOrOwnClient() {
      return this.isOwner || this.hasWriteClientPermission;
    },
    hasWriteClientPermissionOrOwnClientOrMemberOfClient() {
      return this.isOwner || this.isMember || this.hasWriteClientPermission;
    },
    hasGrantClientPermission() {
      return this.hasPermission("GRANT_CLIENT");
    }
  },
  data() {
    return {
      clientId: this.$route.params.id,
      uid: this.$route.query.uid,
      client: this.$route.params.client,
      owner: this.$route.params.owner,
      loading: false,
      error: null,
      user_: null
    }
  },
  methods: {
    hasPermission(permission) {
      return this.user_ && this.user_.authorities && this.user_.authorities.indexOf(permission) >= 0;
    },
    loadClient() {
      if (this.loading)
        return;
      this.loading = true
      let p = this.uid ? this.$clientApi.getUserClient(this.uid, this.clientId) : this.$clientApi.getClient(this.clientId)
      try {
        p.then(res => {
          this.client = res.data
          let usr = this.user_;
          if (usr && usr.uid == this.client.uid)
            this.owner = usr;
          else
            this.$usersApi.getUser(this.client.uid).then((res) => this.owner = res.data);
        })
          .catch(e => {
            this.error = e;
            return Promise.reject(e)
          })
          .finally(() => this.loading = false)
      } catch (e) {
        this.error = e;
        this.loading = false
        throw e
      }
    }
  },
  mounted() {
    if (this.client == null || this.client.cid != this.clientId) {
      this.loadClient();
    }
  }
}
</script>

<style scoped>

</style>
