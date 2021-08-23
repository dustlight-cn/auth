<template>
  <div>
    <require-authorization v-if="!isEditing">
      <template v-slot="{user}">
        {{ "", user_ == null || user != null && user_.uid != user.uid ? user_ = user : "" }}
        <div v-if="!deleted">
          <q-banner v-if="targetUser && !targetUser.enabled"
                    inline-actions
                    class="bg-warning text-white">
            <q-icon size="32px" name="no_accounts"/>
            {{
              $tt($options, "userBanned")
            }}
          </q-banner>
          <edit-page v-if="error == null">
            <template v-slot="{wide}">
              <!-- 骨架 -->
              <div v-if="loading || !targetUser">
                <div class="text-center q-mb-sm">
                  <avatar :size="100"/>
                  <div class="text-h5 q-mb-sm q-mt-sm">
                    <q-skeleton style="margin: 0 auto;" type="text" width="15%"/>
                  </div>
                  <div class="text-caption">
                    <q-skeleton style="margin: 0 auto;" type="text" width="25%"/>
                  </div>
                </div>
                <q-separator/>
                <q-list class="q-mb-md">
                  <q-item class="q-pa-none q-mt-md" v-for="index in 6" :key="index">
                    <q-item-section>
                      <q-item-label header class="q-pl-none">
                        <q-skeleton type="text" width="15%"/>
                      </q-item-label>
                      <q-item-label class="code">
                        <q-skeleton type="text" width="25%"/>
                      </q-item-label>
                    </q-item-section>
                  </q-item>
                </q-list>
              </div>
              <!-- 页面 -->
              <div v-else>
                <div class="text-center q-mb-sm">
                  <q-btn @click="()=>edit.avatar=true" v-if="hasWriteUserPermission" round flat>
                    <avatar :size="100" :user="targetUser"/>
                  </q-btn>
                  <avatar v-else :size="100" :user="targetUser"/>
                  <div class="text-h5 q-mb-sm q-mt-sm" style="word-break: break-all;">
                    {{
                      targetUser.nickname && targetUser.nickname.trim() ? targetUser.nickname.trim() : targetUser.username
                    }}
                  </div>
                  <div v-if="targetUser.email" style="word-break: break-all;" class="text-subtitle1 text-grey">
                    {{ targetUser.email }}
                  </div>
                </div>

                <q-separator/>

                <q-list class="q-mb-md">
                  <!--  用户ID, UID-->
                  <q-item class="q-pa-none q-mt-md">
                    <q-item-section>
                      <q-item-label header class="q-pl-none">{{ $tt($options, "uid") }}</q-item-label>
                      <q-item-label class="code">{{ targetUser.uid }}</q-item-label>
                    </q-item-section>
                  </q-item>

                  <!--  用户名, Username -->
                  <q-item class="q-pa-none q-mt-md">
                    <q-item-section>
                      <q-item-label header class="q-pl-none">{{ $tt($options, "username") }}</q-item-label>
                      <q-item-label class="code">{{ targetUser.username }}</q-item-label>
                    </q-item-section>
                  </q-item>

                  <!--  用户昵称, Nickname -->
                  <q-item class="q-pa-none q-mt-md">
                    <q-item-section>
                      <q-item-label header class="q-pl-none">{{ $tt($options, "nickname") }}</q-item-label>
                      <q-item-label class="content">
                        {{
                          targetUser.nickname && targetUser.nickname.trim() ?
                            targetUser.nickname.trim() : "-"
                        }}
                      </q-item-label>
                    </q-item-section>
                    <q-item-section v-if="hasWriteUserPermission" side top>
                      <q-btn round flat icon="edit"
                             @click="()=>edit.nickname=true"/>
                    </q-item-section>
                  </q-item>

                  <!--  用户性别, Gender -->
                  <q-item class="q-pa-none q-mt-md">
                    <q-item-section>
                      <q-item-label header class="q-pl-none">{{ $tt($options, "gender") }}</q-item-label>
                      <q-item-label class="content">
                        {{ $t("gender." + targetUser.gender) }}
                      </q-item-label>
                    </q-item-section>
                    <q-item-section v-if="hasWriteUserPermission" side top>
                      <q-btn round flat icon="edit"
                             @click="()=>edit.gender=true"/>
                    </q-item-section>
                  </q-item>

                  <!--  用户电子邮箱, Email -->
                  <q-item class="q-pa-none q-mt-md" v-if="targetUser.email || hasWriteUserEmailPermission">
                    <q-item-section>
                      <q-item-label header class="q-pl-none">{{ $tt($options, "email") }}</q-item-label>
                      <q-item-label class="code">
                        {{ targetUser.email || "-" }}
                      </q-item-label>
                    </q-item-section>
                    <q-item-section v-if="hasWriteUserEmailPermission" side top>
                      <q-btn round flat icon="edit"
                             @click="()=>edit.email=true"/>
                    </q-item-section>
                  </q-item>

                  <!--  用户手机号码, Phone -->
                  <q-item class="q-pa-none q-mt-md" v-if="targetUser.phone || hasWriteUserPhonePermission">
                    <q-item-section>
                      <q-item-label header class="q-pl-none">{{ $tt($options, "phone") }}</q-item-label>
                      <q-item-label class="code">
                        {{ targetUser.phone || "-" }}
                      </q-item-label>
                    </q-item-section>
                    <q-item-section v-if="hasWriteUserPhonePermission" side top>
                      <q-btn round flat icon="edit"
                             @click="()=>edit.phone=true"/>
                    </q-item-section>
                  </q-item>

                  <!--  用户密码, Password -->
                  <q-item class="q-pa-none q-mt-md" v-if="hasWriteUserPasswordPermission">
                    <q-item-section>
                      <q-item-label header class="q-pl-none">{{ $tt($options, "password") }}</q-item-label>
                      <q-item-label class="content">******</q-item-label>
                    </q-item-section>
                    <q-item-section side top>
                      <q-btn round flat icon="edit"
                             @click="()=>edit.password=true"/>
                    </q-item-section>
                  </q-item>

                  <!-- 用户角色, Roles -->
                  <q-item :clickable="false" :v-ripple="false" class="q-pa-none q-mt-md">
                    <q-item-section>
                      <q-item-label header class="q-pl-none">
                        {{ $tt($options, "roles") }}
                      </q-item-label>
                    </q-item-section>
                    <q-item-section side top style="padding-left: 0px;">
                      <q-btn round flat icon="edit"
                             @click="editRoles"/>
                    </q-item-section>
                  </q-item>
                  <user-role-clients ref="user-role-clients" :user="targetUser" :current-user="user"/>

                  <!--  用户ID, UID-->
                  <q-item class="q-pa-none q-mt-md" v-if="targetUser && targetUser.createdAt">
                    <q-item-section>
                      <q-item-label header class="q-pl-none">{{ $tt("PersonalInfo", "regtime") }}</q-item-label>
                      <q-item-label class="code">{{ $util.dateFormat(targetUser.createdAt) }}</q-item-label>
                    </q-item-section>
                  </q-item>
                </q-list>

                <!-- 操作按钮 -->
                <q-separator v-if="hasDeleteUserPermission || hasLockUserPermission" class="q-mt-md"/>
                <div v-if="hasDeleteUserPermission || hasLockUserPermission">
                  <q-item v-if="hasLockUserPermission" class="q-pa-none q-mt-lg">
                    <q-space/>
                    <q-item-section>
                      <q-item-label caption>
                      </q-item-label>
                    </q-item-section>
                    <q-item-section side>
                      <q-btn :loading="updating.enabled" @click="()=>enableUser(!targetUser.enabled)" no-caps
                             color="grey"
                             :icon="'no_accounts'"
                             :label="$tt($options,targetUser.enabled?'banUser':'unbanUser')"/>
                    </q-item-section>
                  </q-item>

                  <q-item v-if="hasDeleteUserPermission" class="q-pa-none q-mt-sm">
                    <q-space/>
                    <q-item-section>
                      <q-item-label caption>
                      </q-item-label>
                    </q-item-section>
                    <q-item-section side>
                      <q-btn :loading="deleting" @click="deleteUser" no-caps color="negative" icon="delete"
                             :label="$tt($options,'deleteUser')"/>
                    </q-item-section>
                  </q-item>
                </div>
              </div>
            </template>
          </edit-page>
          <!-- 错误页面 -->
          <q-page v-else class="text-center">
            <div class="q-pa-md">
              <div style="font-size: 10vh">
                {{ error.details ? error.message : error.name }}
              </div>
              <div class="text-h4" style="opacity:.4">
                {{ error.details || error.message }}
              </div>
            </div>
          </q-page>
        </div>

        <!-- 删除后页面 -->
        <q-page v-else class="text-center">
          <div class="q-pa-md">
            <div style="font-size: 10vh">
              <q-icon name="delete"/>
            </div>
            <div class="text-h4" style="opacity:.4">
              {{ $tt($options, "deleted") }}
            </div>
          </div>
        </q-page>
      </template>
    </require-authorization>
    <transition
      v-else
      appear
      enter-active-class="animated fadeIn"
      leave-active-class="animated fadeOut">
      <div class="full-width">
        <nickname v-if="edit.nickname"
                  :user="targetUser"
                  :on-success="()=>edit.nickname=false"
                  :on-cancel="()=>edit.nickname=false"/>
        <gender v-if="edit.gender"
                :user="targetUser"
                :on-success="()=>edit.gender=false"
                :on-cancel="()=>edit.gender=false"/>
        <edit-avatar v-if="edit.avatar"
                     :user="targetUser"
                     :on-success="()=>edit.avatar=false"
                     :on-cancel="()=>edit.avatar=false"/>
        <email :without-password="true"
               v-if="edit.email"
               :user="targetUser"
               :on-success="()=>edit.email=false"
               :on-cancel="()=>edit.email=false"/>
        <phone :without-password="true"
               v-if="edit.phone"
               :user="targetUser"
               :on-success="()=>edit.phone=false"
               :on-cancel="()=>edit.phone=false"/>
        <password :without-old-password="true"
                  v-if="edit.password"
                  :user="targetUser"
                  :on-success="()=>edit.password=false"
                  :on-cancel="()=>edit.password=false"/>
      </div>
    </transition>
    <!-- 角色授权 -->
    <q-dialog :persistent="updating.roles.length>0" v-model="edit.roles" style="max-width: 400px;">
      <q-card class="full-width">
        <q-card-section>
          <div class="text-h6">{{ $tt($options, "roles") }}</div>
        </q-card-section>

        <q-card-section>
          <user-role-clients :current-user="user_" :user="targetUser" :managed="true"
                             :onRoleGrantOrRevoke="(...args)=>$refs['user-role-clients'].updateRole(...args)"/>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn :label="$t('done')"
                 color="accent"
                 :disable="updating.roles.length>0"
                 :loading="updating.roles.length>0"
                 v-close-popup/>
        </q-card-actions>
      </q-card>
    </q-dialog>
  </div>
</template>

<script>
import EditPage from "../../components/EditPage";
import RequireAuthorization from "../../components/RequireAuthorization";
import Avatar from "../../components/Avatar";
import EditAvatar from "./Avatar";
import Nickname from "./Nickname";
import Gender from "./Gender";
import Email from "./Email";
import Password from "./Password";
import NoResults from "../../components/NoResults";
import Phone from "./Phone";
import UserRoleClients from "../../components/UserRoleClients";

export default {
  name: "User",
  components: {
    UserRoleClients,
    Phone, NoResults, Password, Email, Gender, Nickname, EditAvatar, Avatar, RequireAuthorization, EditPage
  },
  data() {
    return {
      user_: null,
      uid: this.$route.params.id,
      targetUser: null,
      loading: false,
      error: null,
      deleted: false,
      deleting: false,
      edit: {
        nickname: false,
        gender: false,
        avatar: false,
        email: false,
        phone: false,
        password: false,
        roles: false
      },
      updating: {
        roles: [],
        enabled: false
      },
      roles: null,
      rolesLoading: false
    }
  },
  computed: {
    isEditing() {
      return this.edit.nickname || this.edit.gender || this.edit.avatar || this.edit.email || this.edit.phone || this.edit.password;
    },
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
  methods: {
    hasRole(roleId) {
      return this.getUserRoleIndex(roleId) >= 0;
    },
    getUserRoleIndex(roleId) {
      if (this.targetUser == null || this.targetUser.roles == null)
        return -1;
      for (let index in this.targetUser.roles) {
        if (this.targetUser.roles[index].rid == roleId)
          return index;
      }
      return -1;
    },
    hasPermission(authority) {
      return this.user_ && this.user_.authorities && this.user_.authorities.indexOf(authority) >= 0;
    },
    loadUser() {
      if (this.loading)
        return;
      if (this.user_ && this.user_.uid && this.uid && this.user_.uid == this.uid) {
        this.targetUser = this.user_;
        return;
      }
      this.loading = true;
      this.$usersApi.getUser(this.uid)
        .then(res => {
          this.targetUser = res.data;
        })
        .catch(e => this.error = e)
        .finally(() => this.loading = false)
    },
    editRoles() {
      this.edit.roles = true;
      if (this.roles == null && !this.rolesLoading) {
        this.rolesLoading = true;
        this.$rolesApi.getRoles()
          .then(res => this.roles = res.data)
          .finally(() => this.rolesLoading = false)
      }
    },
    grantUser(role) {
      if (this.updating.roles.indexOf(role.rid) >= 0)
        return;
      this.updating.roles.push(role.rid);
      let contains = this.hasRole(role.rid);
      (contains ?
          this.$rolesApi.deleteUserRoles(this.uid, role.rid) :
          this.$rolesApi.setUserRoles(this.uid, [{
            rid: role.rid
          }])
      ).then(res => {
        if (contains)
          this.targetUser.roles.splice(this.getUserRoleIndex(role.rid), 1);
        else
          this.targetUser.roles.push({
            rid: role.rid,
            roleName: role.roleName,
            roleDescription: role.roleDescription,
            expiredAt: null
          })
      }).finally(() => {
        this.updating.roles.splice(this.updating.roles.indexOf(role.rid), 1);
      })
    },
    showDeleteSuccessMessage() {
      this.$q.notify({
        message: this.$t("deleteSuccess"),
        type: 'positive'
      })
    },
    showUpdateSuccessMessage() {
      this.$q.notify({
        message: this.$t("updateSuccess"),
        type: 'positive'
      })
    },
    enableUser(enabled) {
      if (this.updating.enabled)
        return;
      this.$q.dialog({
        type: "warning",
        color: "grey",
        message: this.$tt(this, enabled ? "unbanUserMsg" : "banUserMsg"),
        title: this.$tt(this, enabled ? "unbanUserTitle" : "banUserTitle"),
        ok: {},
        cancel: true
      }).onOk(() => {
        this.$usersApi.updateUserEnabled(this.uid, enabled)
          .then(() => {
            this.targetUser.enabled = enabled;
            this.showUpdateSuccessMessage();
          }).finally(() => this.updating.enabled = false)
      })
    },
    deleteUser() {
      if (this.deleting || this.deleted)
        return;
      this.$q.dialog({
        type: "warning",
        color: "negative",
        message: this.$tt(this, "deleteUserMsg"),
        title: this.$tt(this, "deleteUserTitle"),
        ok: {
          label: this.$t("delete")
        },
        cancel: true
      }).onOk(() => {
        this.deleting = true;
        this.$usersApi.deleteUser(this.uid)
          .then(() => {
            this.deleted = true;
            this.showDeleteSuccessMessage();
          })
          .finally(() => this.deleting = false);
      })
    }
  },
  watch: {
    user_() {
      if (this.user_ && this.user_.uid)
        this.loadUser();
    }
  }
}
</script>

<style scoped>
.code {
  font-family: Consolas;
  font-size: 16px;
  word-break: break-all;
}

.content {
  font-family: Consolas;
  font-size: 16px;
}
</style>
