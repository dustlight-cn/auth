<template>
  <require-authorization>
    <template v-slot="{user}">
      {{ "", user_ == null || user != null && user_.uid != user.uid ? user_ = user : "" }}
      <q-file
        style="width: 0px;height: 0px;"
        ref="logoPicker"
        @input="onLogoPicked"
        accept="image/*"
        hide-hint
        hide-bottom-space
        borderless
      >
      </q-file>
      <edit-page v-if="error == null">
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
              <q-separator class="q-ma-none"/>
              <q-item class="q-pa-none q-mt-sm">
                <q-item-section avatar class="q-pa-none" style="min-width: 40px;">
                  <avatar :size="36" :user="owner"/>
                </q-item-section>
                <q-item-section v-if="owner">
                  <q-item-label>
                    <q-btn :to="{name:'user',params:{id:owner.uid}}" no-caps dense color="accent" flat
                           :label="ownerName"/>
                    {{ $tt($options, "createdAt") }}
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
              <!-- 应用 ID, Client ID-->
              <q-item class="q-pa-none q-mt-md">
                <q-item-section>
                  <q-item-label header class="q-pl-none">{{ $tt($options, "clientId") }}</q-item-label>
                  <q-item-label class="code">{{ client.cid }}</q-item-label>
                </q-item-section>
              </q-item>

              <!-- 应用密钥, Client Secret -->
              <q-item class="q-pa-none q-mt-md">
                <q-item-section>
                  <q-item-label header class="q-pl-none">{{ $tt($options, "clientSecret") }}</q-item-label>
                  <q-item-label class="code">{{ client.secret || "******" }}</q-item-label>
                </q-item-section>
                <q-item-section side>
                  <q-btn v-if="hasWriteClientPermission" :disable="secretRegenerating" :loading="secretRegenerating"
                         dense no-caps
                         color="negative" :label="$tt($options,'regenerateSecret')"
                         @click="regenerateSecret"
                         icon="vpn_key"/>
                </q-item-section>
              </q-item>

              <!-- 应用图标, Client Logo -->
              <q-item class="q-pa-none q-mt-md">
                <q-item-section>
                  <q-item-label header class="q-pl-none">{{ $tt($options, "clientLogo") }}</q-item-label>
                  <q-item-label>
                    <q-btn flat dense @click="changeLogo" v-if="hasWriteClientPermission">
                      <client-logo :src="logo" :size="118" :client="client"/>
                    </q-btn>
                    <client-logo v-else :src="logo" :size="118" :client="client"/>
                  </q-item-label>
                </q-item-section>
                <q-item-section side>
                  <q-btn v-if="hasWriteClientPermission"
                         :disable="logoUploading" :loading="logoUploading"
                         @click="changeLogo" dense no-caps
                         :label="$tt($options,'upload')" icon="upload"/>
                </q-item-section>
              </q-item>

              <!-- 应用名称, Client Name -->
              <q-item class="q-pa-none q-mt-md">
                <q-item-section>
                  <q-item-label header class="q-pl-none">{{ $tt($options, "clientName") }}</q-item-label>
                  <q-item-label v-if="edit.name==null" class="content">
                    {{ client.name || "-" }}
                  </q-item-label>
                  <q-item-label class="text-right" v-else>
                    <q-form @submit="updateName">
                      <q-input :placeholder="$tt($options, 'clientName')"
                               :disable="updating.name"
                               :loading="updating.name"
                               dense filled
                               class="q-mb-sm"
                               maxlength="64"
                               :rules="rules.name"
                               color="accent"
                               v-model="edit.name"/>
                      <q-btn :disable="updating.name" no-caps flat :label="$t('cancel')" color="accent" class="q-mr-sm"
                             @click="()=>edit.name=null"/>
                      <q-btn :loading="updating.name" type="submit" no-caps :label="$t('update')" color="accent"
                             icon="check"/>
                    </q-form>
                  </q-item-label>
                </q-item-section>
                <q-item-section side top v-if="edit.name==null">
                  <q-btn @click="updateName" v-if="hasWriteClientPermission" flat round size="12px" no-caps
                         icon="edit"/>
                </q-item-section>
              </q-item>

              <!-- 应用描述, Client Description -->
              <q-item class="q-pa-none q-mt-md">
                <q-item-section>
                  <q-item-label header class="q-pl-none">{{ $tt($options, "clientDescription") }}</q-item-label>
                  <q-item-label v-if="edit.description==null" class="content">
                    {{ client.description || "-" }}
                  </q-item-label>
                  <q-item-label class="text-right" v-else>
                    <q-form @submit="updateDescription">
                      <q-input :placeholder="$tt($options, 'clientDescription')"
                               :disable="updating.description"
                               :loading="updating.description"
                               dense filled
                               class="q-mb-sm"
                               type="textarea"
                               maxlength="256"
                               :rules="rules.description"
                               color="accent"
                               v-model="edit.description"/>
                      <q-btn :disable="updating.description" no-caps flat :label="$t('cancel')" color="accent"
                             class="q-mr-sm"
                             @click="()=>edit.description=null"/>
                      <q-btn :loading="updating.description" type="submit" no-caps :label="$t('update')" color="accent"
                             icon="check"/>
                    </q-form>
                  </q-item-label>
                </q-item-section>
                <q-item-section side top v-if="edit.description==null">
                  <q-btn @click="updateDescription" v-if="hasWriteClientPermission" flat round size="12px" no-caps
                         icon="edit"/>
                </q-item-section>
              </q-item>

              <!-- 应用回调地址, Client Redirect Uri -->
              <q-item class="q-pa-none q-mt-md">
                <q-item-section>
                  <q-item-label header class="q-pl-none">{{ $tt($options, "clientRedirectUri") }}</q-item-label>
                  <q-item-label>
                    <q-list>
                      <q-item dense v-for="(uri,index) in client.redirectUri" :key="index">
                        <q-item-section avatar>
                          <q-icon name="link"/>
                        </q-item-section>
                        <q-item-section>
                          <q-item-label>{{ uri }}</q-item-label>
                        </q-item-section>
                      </q-item>
                    </q-list>
                    <div v-if="client.redirectUri == null || client.redirectUri.length == 0"
                         class="text-center text-caption text-grey">{{ $t("noResults") }}
                    </div>
                  </q-item-label>
                </q-item-section>
                <q-item-section side top>
                  <q-btn v-if="hasWriteClientPermission" flat round size="12px" no-caps icon="edit"/>
                </q-item-section>
              </q-item>

              <!-- 应用授权作用域, Client Scopes -->
              <q-item class="q-pa-none q-mt-md">
                <q-item-section>
                  <q-item-label header class="q-pl-none">{{ $tt($options, "clientScopes") }}</q-item-label>
                  <q-item-label>
                    <q-list>
                      <q-item class="" v-for="(scope,index) in client.scopes" :key="scope.sid">
                        <q-item-section avatar>
                          <q-icon name="policy"/>
                        </q-item-section>
                        <q-item-section>
                          <q-item-label overline>{{ scope.name }}</q-item-label>
                          <q-item-label>{{ scope.subtitle }}</q-item-label>
                          <q-item-label caption>{{ scope.description }}</q-item-label>
                        </q-item-section>
                      </q-item>
                    </q-list>
                    <div v-if="client.scopes == null || client.scopes.length == 0"
                         class="text-center text-caption text-grey">{{ $t("noResults") }}
                    </div>
                  </q-item-label>
                </q-item-section>
                <q-item-section side top>
                  <q-btn v-if="hasWriteClientPermission" flat round size="12px" no-caps icon="edit"/>
                </q-item-section>
              </q-item>

              <!-- 应用权限, Client Authorities -->
              <q-item class="q-pa-none q-mt-md">
                <q-item-section>
                  <q-item-label header class="q-pl-none">{{ $tt($options, "clientAuthorities") }}</q-item-label>
                  <q-item-label>
                    <q-chip icon="security" :size="wide?'14px':'12px'" class=""
                            v-for="(authority,index) in client.authorities"
                            :key="authority.aid">
                      {{ authority }}
                    </q-chip>
                    <div v-if="client.authorities == null || client.authorities.length == 0"
                         class="text-center text-caption text-grey">{{ $t("noResults") }}
                    </div>
                  </q-item-label>
                </q-item-section>
                <q-item-section side top>
                  <q-btn v-if="hasGrantClientPermission" flat round size="12px" no-caps icon="edit"/>
                </q-item-section>
              </q-item>

              <!-- 应用授权模式, Client Grant Types -->
              <q-item class="q-pa-none q-mt-md">
                <q-item-section>
                  <q-item-label header class="q-pl-none">{{ $tt($options, "clientGrantTypes") }}</q-item-label>
                  <q-item-label>
                    <q-list>
                      <q-item dense v-for="(type,index) in client.grantTypes" :key="index">
                        <q-item-section avatar>
                          <q-icon name="electrical_services"/>
                        </q-item-section>
                        <q-item-section>
                          <q-item-label>{{ type }}</q-item-label>
                        </q-item-section>
                      </q-item>
                    </q-list>
                    <div v-if="client.grantTypes == null || client.grantTypes.length == 0"
                         class="text-center text-caption text-grey">{{ $t("noResults") }}
                    </div>
                  </q-item-label>
                </q-item-section>
                <q-item-section side top>
                  <q-btn v-if="hasWriteClientPermission" flat round size="12px" no-caps icon="edit"/>
                </q-item-section>
              </q-item>
            </q-list>
          </div>
        </template>
      </edit-page>
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
    </template>
  </require-authorization>
</template>

<script>
import EditPage from "../../components/EditPage";
import Avatar from "../../components/Avatar";
import RequireAuthorization from "../../components/RequireAuthorization";
import ClientLogo from "../../components/ClientLogo";

export default {
  name: "Client",
  components: {ClientLogo, RequireAuthorization, Avatar, EditPage},
  data() {
    return {
      user_: null,
      uid: this.$route.query.uid,
      clientId: this.$route.params.id,
      client: null,
      loading: false,
      error: null,
      owner: null,
      logo: null,
      logoUploading: false,
      reader: new FileReader(),
      secretRegenerating: false,
      edit: {
        name: null,
        description: null
      },
      updating: {
        name: false,
        description: false
      },
      rules: {
        name: [val => val && val.length <= 64 && (val = val.trim()).length > 0 || this.$tt(this, "clientNameRule")],
        description: [val => val && val.length <= 256 && (val = val.trim()).length > 0 || this.$tt(this, "clientDescriptionRule")]
      }
    }
  },
  methods: {
    hasPermission(permission) {
      return this.user_ && this.user_.authorities && this.user_.authorities.indexOf(permission) >= 0;
    },
    loadClient() {
      if (this.loading)
        return;
      this.loading = true;
      (this.uid == null ?
          this.$clientApi.getClient(this.clientId) :
          this.$clientApi.getUserClient(this.uid, this.clientId)
      )
        .then((res) => {
          this.client = res.data
          let usr = this.user_;
          if (usr && usr.uid == this.client.uid)
            this.owner = usr;
          else
            this.$usersApi.getUser(this.client.uid).then((res) => this.owner = res.data);
        })
        .catch(e => this.error = e)
        .finally(() => this.loading = false)
    },
    showUpdateSuccessMessage() {
      this.$q.notify({
        message: this.$t("updateSuccess"),
        type: 'positive'
      })
    },
    changeLogo(p) {
      if (this.logoUploading)
        return;
      this.$refs.logoPicker.pickFiles(p);
    },
    onLogoPicked(file) {
      if (file == null || this.logoUploading)
        return;
      this.logoUploading = true;
      (this.uid == null ?
          this.$clientApi.updateClientLogo(this.clientId, file) :
          this.$clientApi.updateUserClientLogo(this.uid, this.clientId, file)
      ).then(res => {
        this.reader.readAsDataURL(file);
        let cb = (r) => this.logo = r;
        this.reader.onload = function () {
          cb(this.result)
        }
        this.showUpdateSuccessMessage();
      }).finally(() => this.logoUploading = false)
    },
    regenerateSecret() {
      if (this.secretRegenerating)
        return;
      this.$q.dialog({
        title: this.$tt(this, "regenerateSecret"),
        message: this.$tt(this, "regenerateSecretMsg"),
        color: "negative",
        ok: {},
        cancel: {
          flat: true
        }
      }).onOk(() => {
        if (this.secretRegenerating)
          return;
        this.secretRegenerating = true;
        (this.uid == null ?
            this.$clientApi.updateClientSecret(this.clientId) :
            this.$clientApi.updateUserClientSecret(this.uid, this.clientId)
        )
          .then(res => {
            this.client.secret = res.data;
            this.showUpdateSuccessMessage();
          })
          .finally(() => this.secretRegenerating = false)
      })
    },
    updateName() {
      if (this.edit.name == null)
        this.edit.name = this.client.name;
      else {
        if (this.updating.name)
          return;
        if (this.edit.name == this.client.name) {
          this.edit.name = null;
          return;
        }
        this.updating.name = true;
        (this.uid == null ?
            this.$clientApi.updateClientName(this.clientId, this.edit.name) :
            this.$clientApi.updateUserClientName(this.uid, this.clientId, this.edit.name)
        ).then(() => {
          this.client.name = this.edit.name;
          this.edit.name = null;
          this.showUpdateSuccessMessage();
        }).finally(() => {
          this.updating.name = false;
        })
      }
    },
    updateDescription() {
      if (this.edit.description == null)
        this.edit.description = this.client.description;
      else {
        if (this.updating.description)
          return;
        if (this.edit.description == this.client.description) {
          this.edit.description = null;
          return;
        }
        this.updating.description = true;
        (this.uid == null ?
            this.$clientApi.updateClientDescription(this.clientId, this.edit.description) :
            this.$clientApi.updateUserClientDescription(this.uid, this.clientId, this.edit.description)
        ).then(() => {
          this.client.description = this.edit.description;
          this.edit.description = null;
          this.showUpdateSuccessMessage();
        }).finally(() => {
          this.updating.description = false;
        })
      }
    }
  },
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
    hasWriteClientPermission() {
      return this.isOwner || this.hasPermission("WRITE_CLIENT");
    },
    hasGrantClientPermission() {
      return this.hasPermission("GRANT_CLIENT");
    }
  },
  watch: {
    user_() {
      if (this.user_ && this.user_.uid)
        this.loadClient();
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
