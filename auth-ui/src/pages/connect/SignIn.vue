<template>
  <q-page padding>
    <!-- 宽窗口 -->
    <q-page class="flex flex-center gt-xs">
      <q-form
        @submit="signIn" class="full-width shadow-2 q-pa-md rounded-borders"
        style="max-width: 599px">
        <div class="q-gutter-md">
          <div class="text-h4">{{ $tt(this, "title") }}</div>
          <q-input
            :disable="isBusy"
            color="accent"
            v-model="model.account"
            :label="$tt(this,'account')"
            :hint="$tt(this,'accountHint')"
            lazy-rules
            :rules="rule.account"
          />
          <q-input
            :disable="isBusy"
            color="accent"
            v-model="model.password"
            :label="$tt(this,'password')"
            :hint="$tt(this,'passwordHint')"
            type="password"
            lazy-rules
            :rules="rule.password"
          />
          <re-captcha v-model="model.recaptch"/>
          <div class="q-mb-md flex">
            <q-btn :label="$tt(this,'signUp')" @click="signUp" flat color="accent" class="q-ml-sm"/>
            <q-space/>
            <q-btn :label="$tt(this,'forgotPassword')" @click="forgotPassword" flat color="accent" class="q-ml-sm"/>
            <q-btn :disable="notVerify" :loading="isBusy" :label="$tt(this,'signIn')" type="submit"
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
            color="accent"
            :label="$tt(this,'account')"
            :hint="$tt(this,'accountHint')"
            lazy-rules
            :rules="rule.account"
          />
          <q-input
            :disable="isBusy"
            filled
            v-model="model.password"
            color="accent"
            :label="$tt(this,'password')"
            :hint="$tt(this,'passwordHint')"
            type="password"
            lazy-rules
            :rules="rule.password"
          />
          <re-captcha v-model="model.recaptch"/>
          <div class="flex q-gutter-sm">
            <q-btn :label="$tt(this,'signUp')" @click="signUp" flat color="accent" class="q-ml-sm"/>
            <q-space/>
            <q-btn :label="$tt(this,'forgotPassword')" @click="forgotPassword" flat color="accent" class="q-ml-sm"/>
            <q-btn :disable="notVerify" :loading="isBusy" :label="$tt(this,'signIn')" type="submit" color="accent"
                   class="q-ml-sm"/>
          </div>
        </div>
      </q-form>
    </q-page>
  </q-page>
</template>

<script>
import reCaptcha from "../../components/reCaptcha";

export default {
  name: "SignIn",
  components: {
    reCaptcha
  },
  data() {
    return {
      model: {
        account: "",
        password: "",
        recaptch: ""
      },
      rule: {
        account: [val => val.match(this.$cfg.pattern.account) || this.$tt(this, "accountRule")],
        password: [val => val.match(this.$cfg.pattern.password) || this.$tt(this, "passwordRule")]
      },
      isBusy: false
    }
  },
  computed: {
    notVerify() {
      return this.model.recaptch == null || this.model.recaptch.trim().length == 0;
    }
  }
  ,
  methods: {
    verify() {

      return true;
    }
    ,
    signIn() {
      if (!this.verify())
        return;
      this.isBusy = true
      this.$tokenApi.grantToken(this.model.account, this.model.password, this.model.recaptch)
        .then((res) => this.onSignInSuccess(res.data))
        .catch(this.onSignInFail)
        .finally(() => {
          this.isBusy = false
        })
    }
    ,
    signUp() {
      this.$router.push({path: 'join', query: {redirect_uri: this.$route.fullPath}})
    }
    ,
    forgotPassword() {
      this.$router.push({path: 'reset-password', query: {redirect_uri: this.$route.fullPath}})
    }
    ,
    /**
     * 登入成功
     * @param token : Access Token
     */
    onSignInSuccess(token) {
      token.expiredAt = new Date(new Date() + token.expires_in);
      this.$q.sessionStorage.set("token", token);
      location.href = this.$route.query.redirect_uri ? this.$route.query.redirect_uri : ""
    }
    ,
    /**
     * 登录失败
     * @param e
     */
    onSignInFail(e) {
      this.$root.$emit("reloadCaptcha")
      this.model.password = ""
      let title = this.$t("error");
      let msg = e.message
      if (e.response != null && e.response.data != null) {
        title = e.response.data.message || title
        msg = e.response.data.details || msg
      }
      this.$q.dialog({
        title: title,
        message: msg,
        color: "accent"
      })
    }
  }
  ,
  mounted() {

  }
}
</script>

<style scoped>

</style>
