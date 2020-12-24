<template>
  <q-page>
    <q-page class="flex flex-center gt-xs">
      <div class="full-width" style="max-width: 599px">
        <q-stepper
          vertical
          v-model="step"
          header-nav
          ref="stepper"
          animated
        >
          <div class="text-h4 q-pl-md q-pb-md">{{ $tt(this, "title") }}</div>
          <q-step
            :name="1"
            :title="$tt($options,'step1.title')"
            :caption="$tt($options,'step1.caption')"
            done-icon="person"
            :done="step > 1"
            color="grey"
            done-color="black"
            active-color="accent"
            :header-nav="step > 1"
          >
            <q-form @submit="sendEmailCode" class="q-pa-md rounded-borders q-gutter-md">
              <q-input
                :disable="isBusy"
                color="accent"
                v-model="model.username"
                :label="$tt(this,'step1.username')"
                :hint="$tt(this,'step1.usernameHint')"
                :rules="rule.username"
                filled
              />
              <q-input
                :disable="isBusy"
                color="accent"
                v-model="model.password"
                :label="$tt(this,'step1.password')"
                :hint="$tt(this,'step1.passwordHint')"
                type="password"
                :rules="rule.password"
                filled
              />
              <q-input
                :disable="isBusy"
                color="accent"
                v-model="model.email"
                :label="$tt(this,'step1.email')"
                :hint="$tt(this,'step1.emailHint')"
                type="email"
                :rules="rule.email"
                filled
              />
              <q-stepper-navigation>
                <q-btn :loading="isBusy" type="submit" color="accent" :label="$tt($options,'continue')"/>
              </q-stepper-navigation>
            </q-form>
          </q-step>

          <q-step
            :name="2"
            :title="$tt($options,'step2.title')"
            :caption="$tt($options,'step2.caption')"
            icon="email"
            color="grey"
            done-color="black"
            active-color="accent"
            :done="step > 2"
            :header-nav="step > 2"
          >
            <q-form @submit="register" class="q-pa-md rounded-borders q-gutter-md">
              <q-input
                :disable="isBusy"
                color="accent"
                v-model="model.code"
                :label="$tt(this,'step2.code')"
                :hint="$tt(this,'step2.codeHint')"
                :rules="rule.code"
                filled
              />
            </q-form>
            <q-stepper-navigation>
              <q-btn :loading="isBusy" type="submit" color="accent" :label="$tt($options,'done')"/>
              <q-btn :disable="isBusy" flat @click="step--" color="accent" :label="$tt($options,'back')"
                     class="q-ml-sm"/>
            </q-stepper-navigation>
          </q-step>
        </q-stepper>
      </div>
    </q-page>
    <q-page class="lt-sm">
      <div class="full-width">
        <q-stepper
          vertical
          v-model="step"
          header-nav
          ref="stepper"
          animated
          flat
        >
          <div class="text-h4 q-pl-md q-pb-md">{{ $tt(this, "title") }}</div>
          <q-step
            :name="1"
            :title="$tt($options,'step1.title')"
            :caption="$tt($options,'step1.caption')"
            done-icon="person"
            :done="step > 1"
            color="grey"
            done-color="black"
            active-color="accent"
            :header-nav="step > 1"
          >
            <q-form @submit="sendEmailCode" class="q-pa-md rounded-borders q-gutter-md">
              <q-input
                :disable="isBusy"
                color="accent"
                v-model="model.username"
                :label="$tt(this,'step1.username')"
                :hint="$tt(this,'step1.usernameHint')"
                :rules="rule.username"
                filled
              />
              <q-input
                :disable="isBusy"
                color="accent"
                v-model="model.password"
                :label="$tt(this,'step1.password')"
                :hint="$tt(this,'step1.passwordHint')"
                type="password"
                :rules="rule.password"
                filled
              />
              <q-input
                :disable="isBusy"
                color="accent"
                v-model="model.email"
                :label="$tt(this,'step1.email')"
                :hint="$tt(this,'step1.emailHint')"
                type="email"
                :rules="rule.email"
                filled
              />
              <q-stepper-navigation>
                <q-btn :loading="isBusy" type="submit" color="accent" :label="$tt($options,'continue')"/>
              </q-stepper-navigation>
            </q-form>
          </q-step>

          <q-step
            :name="2"
            :title="$tt($options,'step2.title')"
            :caption="$tt($options,'step2.caption')"
            icon="email"
            color="grey"
            done-color="black"
            active-color="accent"
            :done="step > 2"
            :header-nav="step > 2"
          >
            <q-form @submit="register" class="q-pa-md rounded-borders q-gutter-md">
              <q-input
                :disable="isBusy"
                color="accent"
                v-model="model.code"
                :label="$tt(this,'step2.code')"
                :hint="$tt(this,'step2.codeHint')"
                :rules="rule.code"
                filled
              />
            </q-form>
            <q-stepper-navigation>
              <q-btn :loading="isBusy" type="submit" color="accent" :label="$tt($options,'done')"/>
              <q-btn :disable="isBusy" flat @click="step--" color="accent" :label="$tt($options,'back')"
                     class="q-ml-sm"/>
            </q-stepper-navigation>
          </q-step>
        </q-stepper>
      </div>
    </q-page>

    <re-captcha ref="captcha" v-model="model.recaptcha"/>
  </q-page>
</template>

<script>
import reCaptcha from '../../components/reCaptcha'
import {CAPTCHAApi, UserApi} from "@dustlight/auth-client-axios"

export default {
  name: "SignUp",
  components: {reCaptcha},
  data() {
    return {
      step: 1,
      isBusy: false,
      model: {
        recaptcha: null,
        username: "",
        password: "",
        email: "",
        code: ""
      },
      rule: {
        username: [val => val && val.match(this.$cfg.pattern.username) || this.$tt(this, "step1.usernameRule")],
        password: [val => val && val.match(this.$cfg.pattern.password) || this.$tt(this, "step1.passwordRule")],
        email: [val => val && val.match(this.$cfg.pattern.email) || this.$tt(this, "step1.emailRule")],
        code: [val => val && val.trim().length > 0 || this.$tt(this, "step2.codeRule")]
      }
    }
  }, methods: {
    verify(cb) {
      if (this.notVerify) {
        this.$refs.captcha.$emit("execute", cb)
        return false;
      }
      return true;
    },
    sendEmailCode() {
      if (!this.verify(this.sendEmailCode))
        return;
      this.isBusy = true;
      new CAPTCHAApi(this.$apiCfg).createRegistrationCode(this.model.recaptcha, this.model.email)
        .then(this.sendEmailCodeSuccess)
        .catch(this.sendEmailCodeFailed)
        .finally(() => {
          this.$refs.captcha.$emit("reload")
          this.isBusy = false;
        })
    },
    sendEmailCodeSuccess(res) {
      this.step = 2;
    },
    sendEmailCodeFailed(e) {
      this.$q.dialog({
        title: this.$tt(this, "error"),
        message: e.response && e.response.data && e.response.data.details || e.message,
        color: "accent"
      })
    },
    register() {
      this.isBusy = true;
      new UserApi(this.$apiCfg).createUser1(this.model.username, this.model.password, this.model.code)
        .then(this.registerSuccess)
        .catch(this.registerFailed)
        .finally(() => {
          this.isBusy = false;
        })
    },
    registerSuccess(res) {
      let redirect = this.$route.redirect_uri;
      this.$router.push(redirect ? {path: redirect} : {name: 'login'})
    },
    registerFailed(e) {
      this.model.code = "";
      this.$q.dialog({
        title: this.$tt(this, "error"),
        message: e.response && e.response.data && e.response.data.details || e.message,
        color: "accent"
      })
    }
  }, computed: {
    notVerify() {
      return this.model.recaptcha == null || this.model.recaptcha.trim().length == 0;
    }
  }
}
</script>

<style scoped>

</style>
