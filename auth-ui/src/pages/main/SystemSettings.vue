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
    </require-authorization>
  </q-page>
</template>

<script>
import RequireAuthorization from "../../components/common/RequireAuthorization";

export default {
  name: "SystemSettings",
  components: {RequireAuthorization},
  data() {
    return {
      user_: null,
      data: {
        grantTypes: null,
        scopes: null
      },
      loading: {
        grantTypes: false,
        scopes: false
      }
    }
  },
  computed: {
    hasWriteGrantTypePermission() {
      return this.hasPermission("WRITE_TYPE");
    },
    hasWriteScopePermission() {
      return this.hasPermission("WRITE_SCOPE");
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
      if (this.data.scopes == null)
        this.loadScopes();
    }
  }
}
</script>

<style scoped>

</style>
