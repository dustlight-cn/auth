<template>
  <div>
    <slot v-bind="{user,token,loading}" v-if="authorized"/>
    <slot v-else name="unauthorized">
      <div class="flex flex-center q-mt-md">
        <q-icon name="lock" size="64px"/>
      </div>
      <div class="flex flex-center q-mt-md text-h6 text-grey text-center">
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
/**
 * 排斥锁，保证同时只有一个请求
 * @type {boolean}
 */
let lock = false;
/**
 * 上一次获取用户的时间戳
 * @type {number}
 */
let lastTimestamp = null;

export default {
  name: "RequireAuthorization",
  data() {
    return {
      user: {},
      token: null,
      initialized: false
    }
  },
  props: {
    onReady: Function
  },
  methods: {
    reload(user) {
      this.user = user || {};
      this.token = this.$s.loadToken();
    },
    tokenUpdate(token) {
      if (token) {
        this.reload(this.$s.loadUser());
        this.token = token;
        if (this.onReady != null)
          this.onReady();
        if (!lock &&
          (lastTimestamp == null || new Date().getTime() - lastTimestamp > this.$cfg.getUserFrequency)) {
          lock = true;
          lastTimestamp = new Date().getTime();
          this.$userApi.getUser1()
            .then(res => this.$s.storeUser(res.data))
            .finally(() => lock = false)
        }
      }
    }
  },
  computed: {
    authorized() {
      if (!this.initialized) {
        this.initialized = true;
        let token = this.$s.loadToken();
        this.tokenUpdate(token);
      }
      return this.token && !this.token.isExpired();
    },
    loading() {
      return lock;
    }
  },
  mounted() {
    this.$s.onTokenUpdate(this.tokenUpdate);
    this.$s.onUserUpdate(this.reload);
  }
}
</script>

<style scoped>

</style>
