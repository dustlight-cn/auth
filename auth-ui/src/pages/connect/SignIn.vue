<template>
  <q-page padding>

    <re-captcha ref="captcha" v-model="model.recaptcha"/>

    <!-- 宽窗口 -->
    <q-page class="q-mt-lg q-mb-lg flex flex-center gt-xs">
      <q-form
        @submit="signIn" class="full-width shadow-2 q-pa-md rounded-borders"
        style="max-width: 599px">
        <div class="q-gutter-md">
          <div class="text-h4">{{ $tt(this, "title") }}</div>
          <q-input
            :disable="isBusy"
            color="accent"
            filled
            v-model="model.account"
            autocomplete="username"
            :label="$tt(this,'account')"
            :hint="$tt(this,'accountHint')"
            :rules="rule.account"
          />
          <q-input
            :disable="isBusy"
            color="accent"
            filled
            v-model="model.password"
            autocomplete="current-password"
            :label="$tt(this,'password')"
            :hint="$tt(this,'passwordHint')"
            type="password"
            :rules="rule.password"
          />
          <div class="q-mb-md flex">
            <q-btn
              replace
              :to="{name: 'reset-password', query: {redirect_uri: $route.fullPath}}"
              :label="$tt(this,'forgotPassword')" flat color="accent" class="q-ml-sm"/>
            <q-space/>
            <q-btn
              replace
              :to="{name: 'join', query: {redirect_uri: this.$route.fullPath}}"
              :label="$tt(this,'signUp')" flat color="accent" class="q-ml-sm"/>
            <q-btn type="submit" :loading="isBusy" :label="$tt(this,'signIn')"
                   color="accent" class="q-ml-sm"/>
          </div>
        </div>
      </q-form>
    </q-page>

    <!-- 窄窗口 -->
    <q-page class="lt-sm">
      <q-form
        @submit="signIn" class="full-width q-pt-lg">
        <div class="q-gutter-md">
          <div class="text-h4 q-pb-lg">{{ $tt(this, "title") }}</div>
          <q-input
            :disable="isBusy"
            filled
            v-model="model.account"
            autocomplete="username"
            color="accent"
            :label="$tt(this,'account')"
            :hint="$tt(this,'accountHint')"
            :rules="rule.account"
          />
          <q-input
            :disable="isBusy"
            filled
            v-model="model.password"
            autocomplete="current-password"
            color="accent"
            :label="$tt(this,'password')"
            :hint="$tt(this,'passwordHint')"
            type="password"
            :rules="rule.password"
          />
          <div class="flex q-gutter-sm">
            <q-btn :loading="isBusy" :label="$tt(this,'signIn')" type="submit" color="accent"
                   class="q-ml-sm"/>
            <q-space/>
            <q-btn
              replace
              :to="{name: 'join', query: {redirect_uri: this.$route.fullPath}}"
              :label="$tt(this,'signUp')" flat color="accent" class="q-ml-sm"/>
            <q-btn
              replace
              :to="{name: 'reset-password', query: {redirect_uri: $route.fullPath}}"
              :label="$tt(this,'forgotPassword')" flat color="accent" class="q-ml-sm"/>
          </div>
        </div>
      </q-form>
    </q-page>
  </q-page>
</template>

<script>
import reCaptcha from "../../components/ReCaptcha";

export default {
  name: "SignIn",
  components: {
    reCaptcha
  },
  props: {
    onSuccess: Function,
    redirectAfterSuccess: {
      type: Boolean,
      default() {
        return true;
      }
    }
  },
  data() {
    return {
      model: {
        account: "",
        password: "",
        recaptcha: ""
      },
      rule: {
        account: [val => val && val.match(this.$cfg.pattern.account) || this.$tt(this, "accountRule")],
        password: [val => val && val.match(this.$cfg.pattern.password) || this.$tt(this, "passwordRule")]
      },
      isBusy: false
    }
  },
  computed: {
    notVerify() {
      return this.model.recaptcha == null || this.model.recaptcha.trim().length == 0;
    }
  }
  ,
  methods: {
    verify(cb) {
      if (this.notVerify) {
        this.$refs.captcha.$emit("execute", cb)
        return false;
      }
      return true;
    }
    ,
    signIn() {
      if (!this.verify(this.signIn))
        return;
      this.isBusy = true
      this.$tokenApi.grantToken(this.model.account, this.model.password, this.model.recaptcha)
        .then((res) => this.onSignInSuccess(res.data))
        .catch(this.onSignInFail)
        .finally(() => this.isBusy = false);
    }
    ,
    /**
     * 登入成功
     * @param token : Access Token
     */
    onSignInSuccess(token) {
      this.$q.notify({
        message: this.$tt(this, "success"),
        type: "positive"
      })
      this.$s.storeToken(token);
      if (this.onSuccess != null)
        this.onSuccess(token)
      if (this.redirectAfterSuccess) {
        let redirect = this.$route.query.redirect_uri;
        this.$router.replace(redirect ? {path: redirect} : {name: 'Index'})
      }
    }
    ,
    /**
     * 登录失败
     * @param e
     */
    onSignInFail(e) {
      this.$refs.captcha.$emit("reload")
      this.model.password = ""
    }
  }
}
</script>

<style scoped>

</style>
