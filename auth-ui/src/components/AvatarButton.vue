<template>
  <div>
    <q-btn v-if="token && !token.isExpired()" rounded dense flat>
      <Avatar :user="user"/>
      <q-menu v-if="user && user.uid">
        <div class="text-center q-pa-md">
          <Avatar class="q-ma-sm" size=50 :user="user"/>
          <div class="text-bold">{{ user.nickname }}</div>
          <div class="text-caption">{{ user.email }}</div>
        </div>
        <q-list style="min-width: 150px;">
          <q-separator/>
          <q-item @click="signOut" v-ripple clickable v-close-popup>
            <q-item-section>
              {{ $tt(this, "signOut") }}
            </q-item-section>
          </q-item>
        </q-list>
      </q-menu>
    </q-btn>
    <q-btn v-else :to="{name:'login',query: {redirect_uri: $route.fullPath}}" dense flat :label="$tt(this,'signIn')"/>
  </div>
</template>

<script>
import {UserApi} from "@dustlight/auth-client-axios"
import Avatar from './Avatar'

export default {
  name: "AvatarButton",
  components: {
    Avatar
  },
  data() {
    return {
      user: {},
      token: null
    }
  },
  methods: {
    signOut() {
      this.$tokenApi.deleteToken()
        .finally(() => {
          this.$s.clear();
          this.$root.$emit("onUserUpdate", {});
        })
    }
  },
  mounted() {
    this.$root.$on("onUserUpdate", (user) => {
      this.token = this.$s.loadToken();
      this.user = user
    });
    let token = this.$s.loadToken();
    if (token) {
      this.token = token;
      new UserApi(this.$apiCfg).getUser1().then(res => {
        this.$s.storeUser(res.data)
        this.$root.$emit("onUserUpdate", res.data);
      })
    }
  }
}
</script>

<style scoped>

</style>
