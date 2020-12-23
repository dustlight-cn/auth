<template>
  <div>
    <slot v-bind="{user,token}" v-if="authorized"/>
    <slot v-else name="unauthorized">
      <div class="flex flex-center q-mt-md">
        <q-icon name="lock" size="64px"/>
      </div>
      <div class="flex flex-center q-mt-md text-h6">
        {{ $tt($options, "tips") }}
      </div>
      <div class="flex flex-center q-mt-lg q-mb-md">
        <q-btn :to="{name:'login',query:{redirect_uri:$route.fullPath}}" :label="$tt($options,'signIn')"
               color="accent"/>
      </div>
    </slot>
  </div>
</template>

<script>
import {UserApi} from "@dustlight/auth-client-axios";

/**
 * 排斥锁，保证同时只有一个请求
 * @type {boolean}
 */
let lock = false;
export default {
  name: "RequireAuthorization",
  data() {
    return {
      user: {},
      token: null
    }
  },
  methods: {
    reload(user) {
      this.user = user || {};
      this.token = this.$s.loadToken();
    }
  },
  computed: {
    authorized() {
      return this.token && !this.token.isExpired();
    }
  },
  mounted() {
    this.$root.$on("onUserUpdate", this.reload)
    let token = this.$s.loadToken();
    if (token) {
      this.reload(this.$s.loadUser());
      this.token = token;
      if (!lock) {
        lock = true;
        new UserApi(this.$apiCfg).getUser1().then(res => {
          this.$s.storeUser(res.data)
          this.$root.$emit("onUserUpdate", res.data);
        })
          .finally(() => lock = false)
      }
    }
  }
}
</script>

<style scoped>

</style>
