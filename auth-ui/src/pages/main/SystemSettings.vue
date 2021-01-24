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
            <q-btn @click="createGrantType" :loading="loading.grantTypes" :disable="loading.grantTypes" dense
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
                <q-icon name="link"/>
              </q-item-section>
              <q-item-section>
                <q-item-label>
                  {{ type.name }}
                  <q-popup-edit @save="(val,initVal)=>updateGrantType(index,'name',initVal)"
                                :title="$t('name')"
                                color="accent" v-model="type.name"
                                buttons>
                    <q-input color="accent" type="text" v-model="type.name" dense autofocus/>
                  </q-popup-edit>
                </q-item-label>
                <q-item-label caption>
                  {{ type.description }}
                  <q-popup-edit @save="(val,initVal)=>updateGrantType(index,'description',initVal)"
                                :title="$t('description')"
                                color="accent" v-model="type.description"
                                buttons>
                    <q-input color="accent" type="text" v-model="type.description" dense autofocus/>
                  </q-popup-edit>
                </q-item-label>
              </q-item-section>
              <q-item-section side>
                <q-btn @click="()=>deleteGrantType(type)" rounded flat dense icon="delete"/>
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
            <q-btn :loading="loading.scopes" :disable="loading.scopes" dense color="accent" rounded icon="add"/>
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
                <q-icon name="security"/>
              </q-item-section>
              <q-item-section>
                <q-item-label overline>
                  {{ scope.name }}
                  <q-popup-edit @save="(val,initVal)=>updateScope(index,'name',initVal)"
                                :title="$t('name')"
                                color="accent" v-model="scope.name"
                                buttons>
                    <q-input color="accent" type="text" v-model="scope.name" dense autofocus/>
                  </q-popup-edit>
                </q-item-label>
                <q-item-label>
                  {{ scope.subtitle }}
                  <q-popup-edit @save="(val,initVal)=>updateScope(index,'subtitle',initVal)"
                                :title="$t('title')"
                                color="accent" v-model="scope.subtitle"
                                buttons>
                    <q-input color="accent" type="text" v-model="scope.subtitle" dense autofocus/>
                  </q-popup-edit>
                </q-item-label>
                <q-item-label caption>
                  {{ scope.description }}
                  <q-popup-edit @save="(val,initVal)=>updateScope(index,'description',initVal)"
                                :title="$t('description')"
                                color="accent" v-model="scope.description"
                                buttons>
                    <q-input color="accent" type="text" v-model="scope.description" dense autofocus/>
                  </q-popup-edit>
                </q-item-label>
              </q-item-section>
              <q-item-section side>
                <q-btn rounded flat dense icon="delete"/>
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
            <q-btn :loading="loading.authorities" :disable="loading.authorities" dense color="accent" rounded
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
                  {{ authority.authorityName }}
                  <q-popup-edit @save="(val,initVal)=>updateAuthority(index,'authorityName',initVal)"
                                :title="$t('name')"
                                color="accent" v-model="authority.authorityName"
                                buttons>
                    <q-input color="accent" type="text" v-model="authority.authorityName" dense autofocus/>
                  </q-popup-edit>
                </q-item-label>
                <q-item-label caption>
                  {{ authority.authorityDescription }}
                  <q-popup-edit @save="(val,initVal)=>updateAuthority(index,'authorityDescription',initVal)"
                                :title="$t('description')"
                                color="accent" v-model="authority.authorityDescription"
                                buttons>
                    <q-input color="accent" type="text" v-model="authority.authorityDescription" dense autofocus/>
                  </q-popup-edit>
                </q-item-label>
              </q-item-section>
              <q-item-section side>
                <q-btn rounded flat dense icon="delete"/>
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
            <q-btn :loading="loading.roles" :disable="loading.roles" dense color="accent" rounded
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
                  {{ role.roleName }}
                  <q-popup-edit @save="(val,initVal)=>updateRole(index,'roleName',initVal)"
                                :title="$t('name')"
                                color="accent" v-model="role.roleName"
                                buttons>
                    <q-input color="accent" type="text" v-model="role.roleName" dense autofocus/>
                  </q-popup-edit>
                </q-item-label>
                <q-item-label caption>
                  {{ role.roleDescription }}
                  <q-popup-edit @save="(val,initVal)=>updateRole(index,'roleDescription',initVal)"
                                :title="$t('description')"
                                color="accent" v-model="role.roleDescription"
                                buttons>
                    <q-input color="accent" type="text" v-model="role.roleDescription" dense autofocus/>
                  </q-popup-edit>
                </q-item-label>
              </q-item-section>
              <q-item-section side>
                <q-btn rounded flat dense icon="security"/>
              </q-item-section>
              <q-item-section side>
                <q-btn rounded flat dense icon="delete"/>
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
        roles: null
      },
      loading: {
        grantTypes: false,
        scopes: false,
        authorities: false,
        roles: false
      }
    }
  },
  computed: {},
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
          return this.$grantTypesApi.setGrantTypes([{name: name, description: name}])
            .then(() => {
              this.loading.grantTypes = false;
              this.loadGrantTypes();
            })
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
        })
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
