<template>
  <require-authorization :on-ready="getAuthorization">
    <template v-slot="{user,token,userLoading}">
      <transition
        appear
        enter-active-class="animated fadeIn"
        leave-active-class="animated fadeOut"
      >
        <q-page v-if="!error" class="authorization" padding>
          <div class="q-pt-lg flex flex-center">
            <div>
              <client-logo class="flex flex-center" :client="client" size="128"/>
              <div class="flex flex-center text-h6 q-pl-md q-pr-md">
                <q-skeleton class="full-width" v-if="loading" type="text"/>
                <div v-else>{{ client.name }}</div>
              </div>
              <div class="flex flex-center text-caption text-grey">
                <q-skeleton class="full-width" v-if="loading" type="text"/>
                <div v-else>{{ client.description }}</div>
              </div>
            </div>
          </div>
          <!-- 详情骨架 -->
          <div v-if="(loading || userLoading)">
            <q-list class="q-ma-sm" bordered>
              <q-item>
                <q-item-section avatar>
                  <q-skeleton type="QAvatar" size="36"/>
                </q-item-section>
                <q-item-section>
                  <q-item-label>
                    <q-skeleton type="text" width="20%"/>
                  </q-item-label>
                  <q-item-label caption>
                    <q-skeleton type="text" width="40%"/>
                  </q-item-label>
                </q-item-section>
              </q-item>
              <q-item dense v-for="index in 2" :key="index" tag="label" v-ripple>
                <q-item-section class="q-pb-sm">
                  <q-item-label overline>
                    <q-skeleton type="text" width="30%"/>
                  </q-item-label>
                  <q-item-label class="q-pl-sm" caption>
                    <q-skeleton type="rect" width="60%"/>
                  </q-item-label>
                </q-item-section>
                <q-item-section side top>
                  <q-skeleton type="QToggle" size="26px"/>
                </q-item-section>
              </q-item>
              <q-separator/>
              <q-item class="q-pt-lg">
                <q-item-section class="q-ma-md">
                  <q-skeleton type="QButton" class="full-width"/>
                </q-item-section>
                <q-space/>
                <q-item-section class="q-ma-md">
                  <q-skeleton type="QButton" class="full-width"/>
                </q-item-section>
              </q-item>
              <div class="q-pb-md">
                <q-skeleton style="left: 30%;" type="text" width="40%"/>
                <div>
                  <q-skeleton style="left: 20%;" type="text" width="60%"/>
                </div>
              </div>
            </q-list>

            <q-list class="q-ma-sm q-mt-lg" bordered>
              <q-item v-for="index in 3" :key="index">
                <q-item-section>
                  <q-item-label overline>
                    <q-skeleton type="text" width="22%"/>
                  </q-item-label>
                  <q-item-label caption>
                    <q-skeleton type="rect" width="40%"/>
                  </q-item-label>
                </q-item-section>
              </q-item>
            </q-list>
          </div>
          <!-- 详情 -->
          <div v-else>
            <q-list class="q-ma-sm" bordered>
              <q-item>
                <q-item-section avatar>
                  <avatar-button :user="user" size="36"/>
                </q-item-section>
                <q-item-section>
                  <q-item-label>
                    {{ client.name }}
                  </q-item-label>
                  <q-item-label caption>
                    {{ $tt($options, "require") }}<b>{{ user.nickname }}</b>
                  </q-item-label>
                </q-item-section>
              </q-item>

              <q-item dense v-for="(scope,i) in client.scopes" :key="scope.sid" tag="label" v-ripple>
                <q-item-section>
                  <q-item-label overline>{{ scope.subtitle }}</q-item-label>
                  <q-item-label v-if="scope.value" caption>{{ $tt($options, 'approved') }}</q-item-label>
                  <q-item-label class="q-pl-sm" v-if="scope.description && !scope.value" caption>{{ scope.description }}
                  </q-item-label>
                </q-item-section>
                <q-item-section side top>
                  <q-checkbox color="grey" v-if="scope && scope.approved" disable v-model="scope.value"/>
                  <q-checkbox color="grey" v-else v-model="scope.value"/>
                </q-item-section>
              </q-item>
              <q-separator/>
              <q-item class="q-pt-lg">
                <q-item-section class="q-ma-md">
                  <q-btn class="full-width"
                         :disable="approving || authorization.approved"
                         :loading="canceling"
                         :label="$tt($options,'cancel')"
                         type="submit"
                         color="white"
                         text-color="dark"
                         no-caps
                         @click="cancel"
                         outline/>
                </q-item-section>
                <q-space/>
                <q-item-section class="q-ma-md">
                  <q-btn class="full-width"
                         :disable="canceling || !anyApproved"
                         :loading="authorization.approved || approving"
                         :label="$tt($options,'approve')"
                         type="submit"
                         color="positive"
                         @click="approve"
                         no-caps/>
                </q-item-section>
              </q-item>
              <div class="q-pb-md text-caption text-grey text-center">
                {{ $tt($options, 'redirect') }}
                <div><b>{{ $route.query.redirect_uri }}</b></div>
              </div>
            </q-list>

            <q-list class="q-ma-sm q-mt-lg" bordered>
              <q-item>
                <q-item-section>
                  <q-item-label overline>
                    {{ $tt($options, 'developer') }}
                  </q-item-label>
                  <q-item-label caption>
                    <q-btn v-if="owner && owner.uid" rounded dense flat no-caps
                           :to="{name:'user',params:{id:owner.uid}}">
                      <avatar :user="owner"/>
                      <span class="q-pl-xs">{{ ownerName }}</span>
                    </q-btn>
                  </q-item-label>
                </q-item-section>
              </q-item>
              <q-item v-if="client.createdAt">
                <q-item-section>
                  <q-item-label overline>
                    {{ $tt($options, 'createdAt') }}
                  </q-item-label>
                  <q-item-label caption>
                    {{ $util.dateFormat(client.createdAt, "YYYY/mm/dd") }}
                  </q-item-label>
                </q-item-section>
              </q-item>
              <q-item v-if="authorization.count != null">
                <q-item-section>
                  <q-item-label overline>
                    {{ $tt($options, 'count') }}
                  </q-item-label>
                  <q-item-label caption>
                    {{ authorization.count }}
                  </q-item-label>
                </q-item-section>
              </q-item>
            </q-list>
          </div>
        </q-page>
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
      </transition>
    </template>
    <template v-slot:unauthorized>
      <transition
        appear
        enter-active-class="animated fadeIn"
        leave-active-class="animated fadeOut"
      >
        <sign-in :on-success="getAuthorization" :redirect-after-success="false"/>
      </transition>
    </template>
  </require-authorization>

</template>

<script>
import RequireAuthorization from "../../components/RequireAuthorization";
import SignIn from "./SignIn";
import AvatarButton from "../../components/AvatarButton";
import ClientLogo from "../../components/ClientLogo";
import Avatar from "../../components/Avatar";

export default {
  name: "Authorize",
  components: {
    Avatar,
    ClientLogo,
    AvatarButton,
    SignIn,
    RequireAuthorization
  },
  data() {
    return {
      authorization: null,
      loading: false,
      approving: false,
      canceling: false,
      error: null
    }
  },
  computed: {
    client() {
      return this.authorization ? this.authorization.client : null;
    },
    owner() {
      return this.authorization ? this.authorization.owner : null;
    },
    ownerName() {
      if (this.owner != null)
        return this.owner.nickname && this.owner.nickname.trim() ?
          this.owner.nickname : this.owner.username;
      return "-";
    },
    approved() {
      return this.authorization ? this.authorization.approved : null;
    },
    anyApproved() {
      if (this.client.scopes == null)
        return false;
      for (let index in this.client.scopes)
        if (this.client.scopes[index].value)
          return true;
      return false;
    }
  },
  methods: {
    getAuthorization() {
      if (this.loading)
        return;
      this.loading = true;
      let query = this.$route.query;
      this.$authorizationAi.getAuthorization(query.client_id,
        query.response_type,
        query.redirect_uri,
        query.scope,
        query.state)
        .then(res => {
          if (res.data.client && res.data.client.scopes)
            res.data.client.scopes.forEach(s => {
              s.value = true;
            })
          this.authorization = res.data;
          if (this.authorization.approved)
            window.location = res.data.redirect
        })
        .catch(e => this.error = e)
        .finally(() => this.loading = false)
    },
    approve() {
      if (this.approving)
        return;
      this.approving = true;
      let s = "";
      this.client.scopes.forEach(scope => {
        if (scope.value) {
          if (s.length > 0)
            s += ",";
          s += scope.name;
        }
      })
      this.$authorizationAi.createAuthorization(true, s)
        .then(res => window.location = res.data.redirect)
        .catch((e) => this.approving = false);
    },
    cancel() {
      if (this.canceling)
        return;
      this.canceling = true;
      this.$authorizationAi.createAuthorization(false, [])
        .then(res => window.location = res.data.redirect)
        .catch((e) => this.canceling = false);
    }
  }
  ,
  mounted() {

  }
}
</script>

<style scoped>
.authorization {
  margin: 0 auto;
  max-width: 600px;
}
</style>
