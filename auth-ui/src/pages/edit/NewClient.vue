<template>
  <div>
    <require-authorization>
      <template v-slot="{user}">
        {{ "", user_ == null || user != null && user_.uid != user.uid ? user_ = user : "" }}
        <edit-page v-slot="{wide}" v-if="hasWriteClientPermission || hasCreateClientPermission">
          <q-form @submit="create">
            <q-card-section>
              <div class="text-h6">{{ $tt($options, "title") }}</div>
            </q-card-section>
            <q-card-section>
              <q-list>
                <!-- 应用名称 -->
                <q-item class="q-pa-none">
                  <q-item-section>
                    <q-item-label header class="q-pa-none">
                      {{ $tt('Client', 'clientName') }}
                    </q-item-label>
                    <q-item-label>
                      <q-input
                        dense
                        :disable="creating"
                        :placeholder="$tt('Client','clientName')"
                        v-model="data.name"
                        :rules="rules.name"
                        maxlength="64"
                        color="accent" filled>
                      </q-input>
                    </q-item-label>
                  </q-item-section>
                </q-item>

                <!-- 应用描述 -->
                <q-item class="q-pa-none q-mt-md">
                  <q-item-section>
                    <q-item-label header class="q-pa-none">
                      {{ $tt('Client', 'clientDescription') }}
                    </q-item-label>
                    <q-item-label>
                      <q-input
                        dense
                        :disable="creating"
                        :placeholder="$tt('Client','clientDescription')"
                        type="textarea"
                        v-model="data.description"
                        :rules="rules.description"
                        maxlength="256"
                        color="accent" filled/>
                    </q-item-label>
                  </q-item-section>
                </q-item>

                <!-- 应用回调地址 -->
                <q-item class="q-pa-none q-mt-md">
                  <q-item-section>
                    <q-item-label header class="q-pa-none">
                      {{ $tt('Client', 'clientRedirectUri') }}
                    </q-item-label>
                    <q-item-label>
                      <q-list>
                        <q-item
                          v-for="(uri,index) in data.redirectUri"
                          :key="index"
                          class="q-pa-none">
                          <q-item-section>
                            <q-input
                              :disable="creating"
                              :placeholder="'URL'"
                              v-model="data.redirectUri[index]"
                              dense color="accent" filled>
                              <template v-slot:prepend>
                                <q-icon name="link"/>
                              </template>
                              <template v-slot:after>
                                <q-btn dense round flat
                                       @click="data.redirectUri.splice(index,1)"
                                       icon="remove"/>
                              </template>
                            </q-input>
                          </q-item-section>
                        </q-item>

                        <q-item class="q-pa-none">
                          <q-item-section>
                            <q-form>
                              <q-input
                                :disable="creating"
                                :placeholder="'URL'"
                                v-model="data.newRedirectUri"
                                dense color="accent" filled>
                                <template v-slot:prepend>
                                  <q-icon name="link"/>
                                </template>
                                <template v-slot:after>
                                  <q-btn round dense flat type="submit"
                                         :disable="data.newRedirectUri.trim().length==0 || creating"
                                         @click="()=>{data.redirectUri.push(data.newRedirectUri.trim());data.newRedirectUri='';}"
                                         icon="add"/>
                                </template>
                              </q-input>
                            </q-form>
                          </q-item-section>
                        </q-item>
                      </q-list>
                    </q-item-label>
                  </q-item-section>
                </q-item>

                <div v-if="hasWriteClientPermission">

                  <!-- Access Token 有效期 -->
                  <q-item class="q-pa-none q-mt-md">
                    <q-item-section>
                      <q-item-label header class="q-pa-none">
                        {{ $tt($options, 'accessTokenValidity') }}
                      </q-item-label>
                      <q-item-label>
                        <q-input
                          dense
                          type="number"
                          min="0"
                          step="1"
                          :disable="creating"
                          :placeholder="$tt($options,'accessTokenValidityHint')"
                          v-model="data.accessTokenValidity"
                          color="accent" filled>
                          <template v-slot:prepend>
                            <q-icon name="timer"/>
                          </template>
                          <template v-slot:append>
                          </template>
                          <template v-slot:after>
                            <div class="q-pa-sm text-subtitle1">{{ $t('seconds') }}</div>
                          </template>
                        </q-input>
                      </q-item-label>
                    </q-item-section>
                  </q-item>

                  <!-- Refresh Token 有效期 -->
                  <q-item class="q-pa-none q-mt-md">
                    <q-item-section>
                      <q-item-label header class="q-pa-none">
                        {{ $tt($options, 'refreshTokenValidity') }}
                      </q-item-label>
                      <q-item-label>
                        <q-input
                          dense
                          type="number"
                          min="0"
                          step="1"
                          :disable="creating"
                          :placeholder="$tt($options,'refreshTokenValidityHint')"
                          v-model="data.refreshTokenValidity"
                          color="accent" filled>
                          <template v-slot:prepend>
                            <q-icon name="timer"/>
                          </template>
                          <template v-slot:append>
                          </template>
                          <template v-slot:after>
                            <div class="q-pa-sm text-subtitle1">{{ $t('seconds') }}</div>
                          </template>
                        </q-input>
                      </q-item-label>
                    </q-item-section>
                  </q-item>

                  <!-- 应用所有者 -->
                  <q-item class="q-pa-none q-mt-md">
                    <q-item-section>
                      <q-item-label header class="q-pa-none">
                        {{ $tt($options, 'ownerUid') }}
                      </q-item-label>
                      <q-item-label>
                        <q-item class="q-pa-none">
                          <q-item-section class="q-pa-sm" avatar style="min-width: 0px;">
                            <avatar :user="data.owner"/>
                          </q-item-section>
                          <q-item-section v-if="data.owner && data.owner.uid">
                            <q-item-label>
                              {{ data.owner.nickname.trim() || data.owner.username }}
                            </q-item-label>
                            <q-item-label caption>
                              {{ data.owner.email }}
                            </q-item-label>
                          </q-item-section>
                          <q-item-section v-else>
                            <q-item-label>
                              <q-skeleton type="text" width="15%"/>
                            </q-item-label>
                            <q-item-label caption>
                              <q-skeleton type="text" width="28%"/>
                            </q-item-label>
                          </q-item-section>
                          <q-item-section side>
                            <q-btn
                              @click="onSearchUser"
                              :disable="creating" flat round icon="person_search"/>
                          </q-item-section>
                        </q-item>
                      </q-item-label>
                    </q-item-section>
                  </q-item>
                </div>

              </q-list>
            </q-card-section>
            <div class="text-right q-mt-lg q-mb-md">
              <q-btn no-caps :loading="creating" type="submit" color="accent" :label="$t('create')"/>
            </div>
          </q-form>
        </edit-page>
        <!-- 权限不足页面 -->
        <q-page v-else class="text-center">
          <div class="q-pa-md">
            <div style="font-size: 10vh">
              <q-icon name="security"/>
            </div>
            <div class="text-h4" style="opacity:.4">
              {{ $t('needPermission') }}
            </div>
          </div>
        </q-page>
      </template>
    </require-authorization>

    <!-- 用户选择 -->
    <q-dialog style="max-width: 400px;" v-model="searchUser.search">
      <q-card class="full-width">
        <q-card-section class="q-pb-none">
          <div class="row items-center no-wrap q-mb-md">
            <div class="text-h6 col">{{ $tt($options, 'selectUser') }}</div>
          </div>
          <q-input
            :placeholder="$t('search')"
            v-model="searchUser.key"
            :loading="searchUser.searching"
            debounce="500"
            filled
            clearable
            type="search"
            color="accent" dense>
            <template v-slot:prepend>
              <q-icon name="search"/>
            </template>
          </q-input>
        </q-card-section>
        <q-card-section style="min-height: 150px;">
          <q-list separator v-if="searchUser.data && searchUser.data.length > 0">
            <transition
              v-for="u in searchUser.data" :key="u.uid"
              appear
              enter-active-class="animated fadeIn"
              leave-active-class="animated fadeOut">
              <q-item clickable v-ripple @click="()=>data.owner=u" v-close-popup>
                <q-item-section avatar>
                  <avatar :user="u"/>
                </q-item-section>
                <q-item-section>
                  <q-item-label>{{ u.nickname.trim() || u.username }}</q-item-label>
                  <q-item-label caption>{{ u.email }}</q-item-label>
                </q-item-section>
                <q-item-section side>
                  <q-icon name="keyboard_arrow_right"/>
                </q-item-section>
              </q-item>
            </transition>
          </q-list>
          <div class="text-caption text-grey q-pa-lg text-center" v-else>
            {{ $t("noSearchResults") }}
          </div>
        </q-card-section>
        <q-card-actions align="center">
          <q-pagination
            style="margin: 0 auto;"
            v-if="searchUser.count>0"
            color="accent"
            v-model="searchUser.page"
            :max="Math.ceil(searchUser.count / searchUser.limit)"
            :input="true"/>
        </q-card-actions>
      </q-card>
    </q-dialog>
  </div>
</template>

<script>
import RequireAuthorization from "../../components/RequireAuthorization";
import EditPage from "../../components/EditPage";
import Avatar from "../../components/Avatar";
import NoResults from "../../components/NoResults";

export default {
  name: "NewClient",
  components: {NoResults, Avatar, EditPage, RequireAuthorization},
  data() {
    return {
      user_: null,
      creating: false,
      data: {
        name: "",
        description: "",
        redirectUri: [],
        newRedirectUri: "",
        owner: null,
        scopes: [],
        grantTypes: [],
        accessTokenValidity: 7200,
        refreshTokenValidity: 86400
      },
      rules: {
        name: [val => val && val.length <= 64 && (val = val.trim()).length > 0 || this.$tt('Client', "clientNameRule")],
        description: [val => val && val.length <= 256 && (val = val.trim()).length > 0 || this.$tt('Client', "clientDescriptionRule")]
      },
      searchUser: {
        search: false,
        searching: false,
        key: "",
        page: 1,
        limit: 5,
        data: [],
        count: 0,
        neverSearch: true
      }
    }
  },
  watch: {
    user_() {
      if (this.user_ && this.user_.uid &&
        (!this.data.owner || !this.data.owner.uid || this.data.owner.uid.trim().length == 0)
      )
        this.data.owner = this.user_;
    },
    "searchUser.key"() {
      if (this.searchUser.key == null)
        this.searchUser.key = "";
      this.doSearchUser(true);
    },
    "searchUser.page"() {
      this.doSearchUser(false);
    }
  },
  computed: {
    hasCreateClientPermission() {
      return this.hasPermission("CREATE_CLIENT");
    },
    hasWriteClientPermission() {
      return this.hasPermission("WRITE_CLIENT");
    },
    computedRedirectUri() {
      let result = "";
      if (this.data.redirectUri) {
        let set = new Set();
        this.data.redirectUri.forEach(uri => {
          if (uri && (uri = uri.trim()).length > 0)
            set.add(uri);
        })
        set.forEach(uri => {
          if (result.length > 0)
            result += ',';
          result += uri;
        })
      }
      return result;
    }
  },
  methods: {
    hasPermission(authority) {
      return this.user_ && this.user_.authorities && this.user_.authorities.indexOf(authority) >= 0;
    },
    showCreateSuccess() {
      this.$q.notify({
        message: this.$t("createSuccess"),
        type: 'positive'
      })
    },
    create() {
      if (this.creating)
        return;
      this.creating = true;
      (this.hasWriteClientPermission ?
        this.$clientApi.createClient(this.data.owner.uid, this.data.name, this.data.description, this.computedRedirectUri,
          this.data.scopes, this.data.grantTypes, this.data.accessTokenValidity, this.data.refreshTokenValidity, "", 1) :
        this.$clientApi.createUserClient(this.user_.uid, this.data.name, this.data.description, this.computedRedirectUri, this.data.scopes,
          this.data.grantTypes))
        .then((res) => {
          this.showCreateSuccess();
          this.$router.push({
            name: 'client',
            params: {id: res.data.cid},
            query: {
              uid: this.hasWriteClientPermission ? this.data.owner.uid : this.user_.uid
            }
          })
        }).finally(() => this.creating = false)
    },
    onSearchUser() {
      this.searchUser.search = true;
      if (this.searchUser.neverSearch) {
        this.searchUser.neverSearch = false;
        this.doSearchUser(true);
      }
    },
    doSearchUser(refresh) {
      if (this.searchUser.searching)
        return;
      if (refresh) {
        this.searchUser.page = 1;
      }
      let k = this.searchUser.key;
      this.searchUser.searching = true;
      this.$usersApi.getUsers(k,
        this.searchUser.limit * (this.searchUser.page - 1),
        this.searchUser.limit,
        [])
        .then(res => {
          this.searchUser.count = res.data.count;
          this.searchUser.data = res.data.data;
          return res;
        })
        .finally(() => {
          this.searchUser.searching = false;
          if (k != this.searchUser.key)
            this.searchUser(refresh);
        });
    }
  }
}
</script>

<style scoped>

</style>
