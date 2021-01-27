<template>
  <q-page padding>
    <require-authorization v-slot="{user,token}">
      {{ "", user_ != user && (user_ == null || user == null || user_.uid != user.uid) ? user_ = user : null }}

      <!-- 授权模式 Grant Types -->
      <q-card bordered flat class="q-pa-md q-mt-md">
        <q-item>
          <q-item-section class="text-h6 q-pb-md">
            {{ $tt($options, "grantTypes") }}
            <div class="text-caption">
              {{ $tt($options, "grantTypesDesc") }}
            </div>
          </q-item-section>
          <q-item-section side>
            <q-btn @click="createGrantType" :loading="loading.grantTypes"
                   :disable="loading.grantTypes || !hasWriteGrantTypePermission" dense
                   color="accent" rounded icon="add"/>
          </q-item-section>
        </q-item>
        <q-list separator>
          <transition
            v-for="(type,index) in data.grantTypes" :key="type.tid"
            appear
            enter-active-class="animated fadeIn"
            leave-active-class="animated fadeOut">
            <q-item>
              <q-item-section avatar>
                <q-icon name="electrical_services"/>
              </q-item-section>
              <q-item-section>
                <q-item-label>
                  {{ type.name || "-" }}
                  <q-popup-edit v-if="hasWriteGrantTypePermission"
                                @save="(val,initVal)=>updateGrantType(index,'name',initVal)"
                                :title="$t('name')"
                                color="accent" v-model="type.name"
                                buttons>
                    <q-input color="accent" type="text" v-model="type.name" dense autofocus/>
                  </q-popup-edit>
                </q-item-label>
                <q-item-label caption>
                  {{ type.description || "-" }}
                  <q-popup-edit v-if="hasWriteGrantTypePermission"
                                @save="(val,initVal)=>updateGrantType(index,'description',initVal)"
                                :title="$t('description')"
                                color="accent" v-model="type.description"
                                buttons>
                    <q-input color="accent" type="text" v-model="type.description" dense autofocus/>
                  </q-popup-edit>
                </q-item-label>
              </q-item-section>
              <q-item-section side>
                <q-btn :disable="!hasWriteGrantTypePermission" @click="()=>deleteGrantType(type)" rounded flat dense
                       icon="delete"/>
              </q-item-section>
            </q-item>
          </transition>
        </q-list>
        <q-card-actions
          align="center">
          <div class="text-caption text-grey" v-if="!data.grantTypes || data.grantTypes.length == 0">
            {{ $t("noResults") }}
          </div>
        </q-card-actions>
      </q-card>

      <!-- 授权作用域 Scopes -->
      <q-card bordered flat class="q-pa-md q-mt-md">
        <q-item>
          <q-item-section class="text-h6 q-pb-md">
            {{ $tt($options, "scopes") }}
            <div class="text-caption">
              {{ $tt($options, "scopesDesc") }}
            </div>
          </q-item-section>
          <q-item-section side>
            <q-btn @click="createScope" :loading="loading.scopes" :disable="loading.scopes || !hasWriteScopePermission"
                   dense color="accent" rounded
                   icon="add"/>
          </q-item-section>
        </q-item>
        <q-list separator>
          <transition
            v-for="(scope,index) in data.scopes" :key="scope.sid"
            appear
            enter-active-class="animated fadeIn"
            leave-active-class="animated fadeOut">
            <q-item>
              <q-item-section avatar>
                <q-icon name="policy"/>
              </q-item-section>
              <q-item-section>
                <q-item-label overline>
                  {{ scope.name || "-" }}
                  <q-popup-edit v-if="hasWriteScopePermission" @save="(val,initVal)=>updateScope(index,'name',initVal)"
                                :title="$t('name')"
                                color="accent" v-model="scope.name"
                                buttons>
                    <q-input color="accent" type="text" v-model="scope.name" dense autofocus/>
                  </q-popup-edit>
                </q-item-label>
                <q-item-label>
                  {{ scope.subtitle || "-" }}
                  <q-popup-edit v-if="hasWriteScopePermission"
                                @save="(val,initVal)=>updateScope(index,'subtitle',initVal)"
                                :title="$t('title')"
                                color="accent" v-model="scope.subtitle"
                                buttons>
                    <q-input color="accent" type="text" v-model="scope.subtitle" dense autofocus/>
                  </q-popup-edit>
                </q-item-label>
                <q-item-label caption>
                  {{ scope.description || "-" }}
                  <q-popup-edit v-if="hasWriteScopePermission"
                                @save="(val,initVal)=>updateScope(index,'description',initVal)"
                                :title="$t('description')"
                                color="accent" v-model="scope.description"
                                buttons>
                    <q-input color="accent" type="text" v-model="scope.description" dense autofocus/>
                  </q-popup-edit>
                </q-item-label>
              </q-item-section>
              <q-item-section side>
                <q-btn :disable="!hasWriteScopePermission" @click="()=>deleteScope(scope)" rounded flat dense
                       icon="delete"/>
              </q-item-section>
            </q-item>
          </transition>
        </q-list>
        <q-card-actions
          align="center">
          <div class="text-caption text-grey" v-if="!data.grantTypes || data.grantTypes.length == 0">
            {{ $t("noResults") }}
          </div>
        </q-card-actions>
      </q-card>

      <!-- 权限 Authorities -->
      <q-card bordered flat class="q-pa-md q-mt-md">
        <q-item>
          <q-item-section class="text-h6 q-pb-md">
            {{ $tt($options, "authorities") }}
            <div class="text-caption">
              {{ $tt($options, "authoritiesDesc") }}
            </div>
          </q-item-section>
          <q-item-section side>
            <q-btn @click="createAuthority" :loading="loading.authorities"
                   :disable="loading.authorities || !hasWriteAuthorityPermission" dense
                   color="accent" rounded
                   icon="add"/>
          </q-item-section>
        </q-item>
        <q-list separator>
          <transition
            v-for="(authority,index) in data.authorities" :key="authority.aid"
            appear
            enter-active-class="animated fadeIn"
            leave-active-class="animated fadeOut">
            <q-item>
              <q-item-section avatar>
                <q-icon name="security"/>
              </q-item-section>
              <q-item-section>
                <q-item-label>
                  {{ authority.authorityName || "-" }}
                  <q-popup-edit v-if="hasWriteAuthorityPermission"
                                @save="(val,initVal)=>updateAuthority(index,'authorityName',initVal)"
                                :title="$t('name')"
                                color="accent" v-model="authority.authorityName"
                                buttons>
                    <q-input color="accent" type="text" v-model="authority.authorityName" dense autofocus/>
                  </q-popup-edit>
                </q-item-label>
                <q-item-label caption>
                  {{ authority.authorityDescription || "-" }}
                  <q-popup-edit v-if="hasWriteAuthorityPermission"
                                @save="(val,initVal)=>updateAuthority(index,'authorityDescription',initVal)"
                                :title="$t('description')"
                                color="accent" v-model="authority.authorityDescription"
                                buttons>
                    <q-input color="accent" type="text" v-model="authority.authorityDescription" dense autofocus/>
                  </q-popup-edit>
                </q-item-label>
              </q-item-section>
              <q-item-section side>
                <q-btn :disable="!hasWriteAuthorityPermission" @click="()=>deleteAuthority(authority)" rounded flat
                       dense icon="delete"/>
              </q-item-section>
            </q-item>
          </transition>
        </q-list>
        <q-card-actions
          align="center">
          <div class="text-caption text-grey" v-if="!data.grantTypes || data.grantTypes.length == 0">
            {{ $t("noResults") }}
          </div>
        </q-card-actions>
      </q-card>

      <!-- 角色 Roles -->
      <q-card bordered flat class="q-pa-md q-mt-md">
        <q-item>
          <q-item-section class="text-h6 q-pb-md">
            {{ $tt($options, "roles") }}
            <div class="text-caption">
              {{ $tt($options, "rolesDesc") }}
            </div>
          </q-item-section>
          <q-item-section side>
            <q-btn @click="createRole" :loading="loading.roles" :disable="loading.roles || !hasWriteRolePermission"
                   dense color="accent" rounded
                   icon="add"/>
          </q-item-section>
        </q-item>
        <q-list separator>
          <transition
            v-for="(role,index) in data.roles" :key="role.rid"
            appear
            enter-active-class="animated fadeIn"
            leave-active-class="animated fadeOut">
            <q-item>
              <q-item-section avatar>
                <q-icon name="person"/>
              </q-item-section>
              <q-item-section>
                <q-item-label>
                  {{ role.roleName || "-" }}
                  <q-popup-edit v-if="hasWriteRolePermission"
                                @save="(val,initVal)=>updateRole(index,'roleName',initVal)"
                                :title="$t('name')"
                                color="accent" v-model="role.roleName"
                                buttons>
                    <q-input color="accent" type="text" v-model="role.roleName" dense autofocus/>
                  </q-popup-edit>
                </q-item-label>
                <q-item-label caption>
                  {{ role.roleDescription || "-" }}
                  <q-popup-edit v-if="hasWriteRolePermission"
                                @save="(val,initVal)=>updateRole(index,'roleDescription',initVal)"
                                :title="$t('description')"
                                color="accent" v-model="role.roleDescription"
                                buttons>
                    <q-input color="accent" type="text" v-model="role.roleDescription" dense autofocus/>
                  </q-popup-edit>
                </q-item-label>
              </q-item-section>
              <q-item-section side>
                <q-btn :disable="!hasGrantRolePermission" @click="()=>showGrantRoleDialog(role)" rounded flat dense
                       icon="security"/>
              </q-item-section>
              <q-item-section side>
                <q-btn :disable="!hasWriteRolePermission" @click="()=>deleteRole(role)" rounded flat dense
                       icon="delete"/>
              </q-item-section>
            </q-item>
          </transition>
        </q-list>
        <q-card-actions
          align="center">
          <div class="text-caption text-grey" v-if="!data.grantTypes || data.grantTypes.length == 0">
            {{ $t("noResults") }}
          </div>
        </q-card-actions>
      </q-card>

      <q-dialog :persistent="loading.grantingRole.length>0" style="max-width: 400px;" :value="selectedRole!=null"
                @input="(val)=>{if(!val)selectedRole=null}">
        <q-card v-if="selectedRole!=null" class="full-width">
          <q-card-section>
            <div class="text-h6">{{ $tt($options, 'grantRole') }}</div>
          </q-card-section>
          <q-card-section class="q-pt-none">
            <q-item>
              <q-item-section avatar>
                <q-icon name="person"/>
              </q-item-section>
              <q-item-section>
                <q-item-label>{{ selectedRole.roleName || "-" }}</q-item-label>
                <q-item-label caption>{{ selectedRole.roleDescription || "-" }}</q-item-label>
              </q-item-section>
              <q-item-section side v-if="data.roleAuthorities && data.authorities">
                {{ data.roleAuthorities.length + " / " + data.authorities.length }}
              </q-item-section>
            </q-item>
          </q-card-section>
          <q-card-section class="q-pa-none">
            <q-list separator v-if="!loading.roleAuthorities && data.roleAuthorities">
              <transition
                v-for="(authority,index) in data.authorities"
                :key="authority.aid"
                appear
                enter-active-class="animated fadeIn"
                leave-active-class="animated fadeOut"
              >
                <q-item clickable v-ripple>
                  <q-item-section avatar style="min-width: 0px;">
                    <q-icon :color="data.roleAuthorities.indexOf(authority.authorityName)>-1?'accent':''"
                            name="security"/>
                  </q-item-section>
                  <q-item-section>
                    <q-item-label v-if="data.roleAuthorities.indexOf(authority.authorityName)>-1" class="text-accent">
                      {{ authority.authorityName || '-' }}
                    </q-item-label>
                    <q-item-label v-else>{{ authority.authorityName || '-' }}</q-item-label>
                    <q-item-label caption>{{ authority.authorityDescription || '-' }}</q-item-label>
                  </q-item-section>
                  <q-item-section side>
                    <q-btn :disable="isGrantingRole(authority)"
                           :loading="isGrantingRole(authority)"
                           round flat
                           :icon="(data.roleAuthorities.indexOf(authority.authorityName)>-1?'remove':'add')"
                           @click="()=>grantRole(authority)"/>
                  </q-item-section>
                </q-item>
              </transition>
            </q-list>
          </q-card-section>
          <q-card-section style="height: 50px;">
            <q-inner-loading :showing="loading.roleAuthorities">
              <q-spinner-gears size="50px" color="accent"/>
            </q-inner-loading>
          </q-card-section>
          <q-card-actions align="right">
            <q-btn :label="$t('done')" :loading="loading.grantingRole.length>0" color="accent" v-close-popup/>
          </q-card-actions>
        </q-card>
      </q-dialog>
    </require-authorization>
  </q-page>
</template>

<script>
import RequireAuthorization from "components/RequireAuthorization.vue";

export default {
  name: "SystemSettings",
  components: {RequireAuthorization},
  data() {
    return {
      user_: null,
      data: {
        grantTypes: null,
        scopes: null,
        authorities: null,
        roles: null,
        roleAuthorities: null,
      },
      loading: {
        grantTypes: false,
        scopes: false,
        authorities: false,
        roles: false,
        roleAuthorities: false,
        grantingRole: []
      },
      selectedRole: null
    }
  },
  computed: {
    hasWriteAuthorityPermission() {
      return this.hasPermission("WRITE_AUTHORITY");
    },
    hasWriteGrantTypePermission() {
      return this.hasPermission("WRITE_TYPE");
    },
    hasWriteScopePermission() {
      return this.hasPermission("WRITE_SCOPE");
    },
    hasWriteRolePermission() {
      return this.hasPermission("WRITE_ROLE");
    },
    hasGrantRolePermission() {
      return this.hasPermission("GRANT_ROLE");
    }
  },
  methods: {
    loadGrantTypes() {
      if (this.loading.grantTypes)
        return;
      this.loading.grantTypes = true;
      this.$grantTypesApi.getGrantTypes("")
        .then(res => {
          this.data.grantTypes = res.data;
        }).finally(() => this.loading.grantTypes = false)
    },
    loadScopes() {
      if (this.loading.scopes)
        return;
      this.loading.scopes = true;
      this.$scopesApi.getScopes("")
        .then(res => {
          this.data.scopes = res.data;
        }).finally(() => this.loading.scopes = false)
    },
    loadAuthorities() {
      if (this.loading.authorities)
        return;
      this.loading.authorities = true;
      this.$authoritiesApi.getAuthorities("")
        .then(res => {
          this.data.authorities = res.data;
        }).finally(() => this.loading.authorities = false)
    },
    loadRoles() {
      if (this.loading.roles)
        return;
      this.loading.roles = true;
      this.$rolesApi.getRoles("")
        .then(res => {
          this.data.roles = res.data;
        }).finally(() => this.loading.roles = false)
    },
    updateGrantType(index, key, initValue) {
      this.$grantTypesApi.setGrantTypes([this.data.grantTypes[index]])
        .then((res) => this.showSuccessMessage())
        .catch(e => {
          this.data.grantTypes[index][key] = initValue;
        })
    },
    updateScope(index, key, initValue) {
      this.$scopesApi.setScopes([this.data.scopes[index]])
        .then((res) => this.showSuccessMessage())
        .catch(e => {
          this.data.scopes[index][key] = initValue;
        })
    },
    updateAuthority(index, key, initValue) {
      this.$authoritiesApi.setAuthorities([this.data.authorities[index]])
        .then((res) => this.showSuccessMessage())
        .catch(e => {
          this.data.authorities[index][key] = initValue;
        })
    },
    updateRole(index, key, initValue) {
      this.$rolesApi.setRoles([this.data.roles[index]])
        .then((res) => this.showSuccessMessage())
        .catch(e => {
          this.data.roles[index][key] = initValue;
        })
    },
    createGrantType() {
      this.showCreateDialog(this.$tt(this, "createGrantType"), this.$tt(this, "createGrantTypeMsg"),
        (name) => {
          this.loading.grantTypes = true;
          return this.$grantTypesApi.setGrantTypes([{name: name, description: name + " description"}])
            .then(() => {
              this.loading.grantTypes = false;
              this.loadGrantTypes();
            })
            .finally(() => this.loading.grantTypes = false)
        }
      );
    },
    deleteGrantType(type) {
      this.showDeleteDialog(this.$tt(this, "deleteGrantType") + " '" + type.name + "'",
        this.$tt(this, "deleteGrantTypeMsg"),
        () => {
          this.loading.grantTypes = true;
          return this.$grantTypesApi.deleteGrantTypes([type.tid])
            .then(() => {
              this.loading.grantTypes = false;
              this.loadGrantTypes();
            })
            .finally(() => this.loading.grantTypes = false)
        })
    },
    createScope() {
      this.showCreateDialog(this.$tt(this, "createScope"), this.$tt(this, "createScopeMsg"),
        (name) => {
          this.loading.scopes = true;
          return this.$scopesApi.setScopes([{
            name: name,
            subtitle: name + " subtitle",
            description: name + " description"
          }])
            .then(() => {
              this.loading.scopes = false;
              this.loadScopes();
            })
            .finally(() => this.loading.scopes = false)
        }
      );
    },
    deleteScope(scope) {
      this.showDeleteDialog(this.$tt(this, "deleteScope") + " '" + scope.name + "'",
        this.$tt(this, "deleteScopeMsg"),
        () => {
          this.loading.scopes = true;
          return this.$scopesApi.deleteScopes([scope.sid])
            .then(() => {
              this.loading.scopes = false;
              this.loadScopes();
            })
            .finally(() => this.loading.scopes = false)
        })
    },
    createAuthority() {
      this.showCreateDialog(this.$tt(this, "createAuthority"), this.$tt(this, "createAuthorityMsg"),
        (name) => {
          this.loading.authorities = true;
          return this.$authoritiesApi.setAuthorities([{
            authorityName: name,
            authorityDescription: name + " description"
          }])
            .then(() => {
              this.loading.authorities = false;
              this.loadAuthorities();
            })
            .finally(() => this.loading.authorities = false)
        }
      );
    },
    deleteAuthority(authority) {
      this.showDeleteDialog(this.$tt(this, "deleteAuthority") + " '" + authority.authorityName + "'",
        this.$tt(this, "deleteAuthorityMsg"),
        () => {
          this.loading.authorities = true;
          return this.$authoritiesApi.deleteAuthorities([authority.aid])
            .then(() => {
              this.loading.authorities = false;
              this.loadAuthorities();
            })
            .finally(() => this.loading.authorities = false)
        })
    },
    createRole() {
      this.showCreateDialog(this.$tt(this, "createRole"), this.$tt(this, "createRoleMsg"),
        (name) => {
          this.loading.roles = true;
          return this.$rolesApi.setRoles([{
            roleName: name,
            roleDescription: name + " description"
          }])
            .then(() => {
              this.loading.roles = false;
              this.loadRoles();
            })
            .finally(() => this.loading.roles = false)
        }
      );
    },
    deleteRole(role) {
      this.showDeleteDialog(this.$tt(this, "deleteRole") + " '" + role.roleName + "'",
        this.$tt(this, "deleteRoleMsg"),
        () => {
          this.loading.roles = true;
          return this.$rolesApi.deleteRoles([role.rid])
            .then(() => {
              this.loading.roles = false;
              this.loadRoles();
            })
            .finally(() => this.loading.roles = false)
        })
    },
    grantRole(authority) {
      if (this.loading.grantingRole.indexOf(authority.authorityName) >= 0)
        return;
      this.loading.grantingRole.push(authority.authorityName);
      let index = this.data.roleAuthorities.indexOf(authority.authorityName);
      let p = index >= 0 ?
        this.$authoritiesApi.deleteRoleAuthorities(this.selectedRole.rid, [authority.aid]) :
        this.$authoritiesApi.setRoleAuthorities(this.selectedRole.rid, [authority.aid]);
      p.then(() => {
        if (index >= 0) {
          this.data.roleAuthorities.splice(this.data.roleAuthorities.indexOf(authority.authorityName), 1);
        } else {
          this.data.roleAuthorities.push(authority.authorityName);
        }
      })
        .finally(() => this.loading.grantingRole.splice(this.loading.grantingRole.indexOf(authority.authorityName), 1));
    },
    isGrantingRole(authority) {
      return this.loading.grantingRole.indexOf(authority.authorityName) >= 0;
    },
    showSuccessMessage() {
      this.$q.notify({
        message: this.$t("updateSuccess"),
        type: 'positive'
      })
    },
    showDeleteSuccessMessage() {
      this.$q.notify({
        message: this.$t("deleteSuccess"),
        type: 'positive'
      })
    },
    showCreateSuccessMessage() {
      this.$q.notify({
        message: this.$t("createSuccess"),
        type: 'positive'
      })
    },
    showCreateDialog(title, msg, todo) {
      this.$q.dialog({
        title: title,
        message: msg,
        cancel: true,
        color: "accent",
        prompt: {
          model: '',
          isValid: val => val && val.trim().length > 0,
          type: 'text' // optional
        },
        ok: {
          label: this.$t("create")
        }
      }).onOk((data) => {
        if (todo != null)
          todo(data.trim()).then(() => this.showCreateSuccessMessage());
      })
    },
    showDeleteDialog(title, msg, todo) {
      this.$q.dialog({
        title: title,
        icon: "delete",
        message: msg,
        cancel: true,
        color: "negative",
        ok: {
          label: this.$t("delete")
        }
      }).onOk(() => {
        if (todo != null)
          todo().then(() => this.showDeleteSuccessMessage());
      })
    },
    showGrantRoleDialog(role) {
      this.loading.roleAuthorities = true;
      this.data.roleAuthorities = null;
      this.selectedRole = role;
      this.$authoritiesApi.getRoleAuthorities(role.rid)
        .then(res => {
          this.data.roleAuthorities = res.data;
        })
        .finally(() => {
          this.loading.roleAuthorities = false
        })
    },
    hasPermission(permission) {
      return this.user_ && this.user_.authorities && this.user_.authorities.indexOf(permission) >= 0;
    }
  },
  watch: {
    user_() {
      if (this.data.grantTypes == null)
        this.loadGrantTypes();
      if (this.data.authorities == null)
        this.loadAuthorities();
      if (this.data.scopes == null)
        this.loadScopes();
      if (this.data.roles == null)
        this.loadRoles();
    }
  }
}
</script>

<style scoped>

</style>
