<template>
  <center-page>
    <re-captcha ref="captcha" v-model="model.recaptcha"/>
    <q-stepper
      vertical
      v-model="step"
      header-nav
      ref="stepper"
      animated
      :flat="!$q.screen.gt.xs"
    >
      <div class="text-h5 q-pl-md q-pb-md">
        <q-btn dense flat round icon="arrow_back" @click="()=>$router.back()"/>
        <span>{{ $tt(this, "title") }}</span>
      </div>
      <!-- 步骤1 -->
      <q-step
        :name="1"
        :title="$tt($options,'step1.title')"
        :caption="$tt($options,'step1.caption')"
        done-icon="send"
        :done="step > 1"
        color="grey"
        done-color="black"
        active-color="accent"
        :header-nav="step > 1"
      >
        <q-form @submit="sendEmailCode" class="q-pa-md rounded-borders q-gutter-md">
          <q-input
            :disable="isBusy"
            debounce="1000"
            color="accent"
            v-model="model.email"
            autocomplete="email"
            :label="$tt(this,'step1.email')"
            :hint="$tt(this,'step1.emailHint')"
            type="email"
            :rules="rule.email"
            filled
          />
          <q-stepper-navigation>
            <q-btn class="q-mr-md" :loading="isBusy" type="submit" color="accent"
                   :label="$tt($options,'continue')"/>
          </q-stepper-navigation>
        </q-form>
      </q-step>

      <!-- 步骤2 -->
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
        <q-form @submit="resetPassword" class="q-pa-md rounded-borders q-gutter-md">
          <q-input
            :disable="isBusy"
            color="accent"
            v-model="model.password"
            autocomplete="new-password"
            :label="$tt(this,'step2.password')"
            :hint="$tt(this,'step2.passwordHint')"
            type="password"
            :rules="rule.password"
            filled
          />
          <q-input
            :disable="isBusy"
            color="accent"
            v-model="model.confirmPassword"
            autocomplete="new-password"
            :label="$tt(this,'step2.confirmPassword')"
            :hint="$tt(this,'step2.confirmPasswordHint')"
            type="password"
            :rules="rule.confirmPassword"
            filled
          />
          <q-input
            :disable="isBusy"
            color="accent"
            v-model="model.code"
            autocomplete="off"
            :label="$tt(this,'step2.code')"
            :hint="$tt(this,'step2.codeHint')"
            :rules="rule.code"
            filled
          />
          <q-stepper-navigation>
            <q-btn :loading="isBusy" type="submit" color="accent" :label="$tt($options,'done')"/>
            <q-btn :disable="isBusy" flat @click="step--" color="accent" :label="$tt($options,'back')"
                   class="q-ml-sm"/>
          </q-stepper-navigation>
        </q-form>
      </q-step>
    </q-stepper>
  </center-page>
</template>

<script>
import reCaptcha from '../../components/common/ReCaptcha'
import CenterPage from "../../components/common/CenterPage";

export default {
  name: "ResetPasswordByEmail",
  components: {CenterPage, reCaptcha},
  data() {
    return {
      step: 1,
      isBusy: false,
      model: {
        recaptcha: null,
        password: "",
        confirmPassword: "",
        email: "",
        code: ""
      },
      rule: {
        password: [val => val && val.match(this.$cfg.pattern.password) || this.$tt(this, "step2.passwordRule")],
        confirmPassword: [val => val == this.model.password || this.$tt(this, "step2.confirmPasswordRule")],
        email: [
          val => val && val.match(this.$cfg.pattern.email) || this.$tt(this, "step1.emailRule"),
          val => this.querying.email ?
            this.querying.email :
            (this.querying.email = this.$userApi.isEmailExists(val)
                .then((res) => res.data || this.$tt(this, "step1.emailNotExists"))
                .finally(() => this.querying.email = null)
            )
        ],
        code: [val => val && val.trim().length > 0 || this.$tt(this, "step2.codeRule")]
      },
      querying: {
        email: null
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
      this.$codeApi.createUpdatePasswordEmailCode(this.model.recaptcha, this.model.email)
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

    },
    resetPassword() {
      this.isBusy = true;
      this.$userApi.resetPassword1(this.model.password, this.model.code)
        .then(this.resetPasswordSuccess)
        .catch(this.resetPasswordFailed)
        .finally(() => {
          this.isBusy = false;
        })
    },
    resetPasswordSuccess(res) {
      this.$q.notify({
        message: this.$tt(this, "success"),
        type: "positive"
      })
      let redirect = this.$route.query.redirect_uri;
      this.$router.replace(redirect ? {path: redirect} : {name: 'login'})
    },
    resetPasswordFailed(e) {
      this.model.code = "";
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
