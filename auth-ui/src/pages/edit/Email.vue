<template>
  <update-user
    :disable-submit-button="step<=1"
    :submit="onSubmit"
    :title="$tt(this,'operator')"
    :user="user"
    v-slot="{user,token,loading,busy}">
    <re-captcha v-if="step<=1" ref="captcha" v-model="model.recaptcha"/>
    <q-stepper
      div v-if="!loading"
      flat
      vertical
      v-model="step"
      header-nav
      ref="stepper"
      animated
    >
      <!-- 步骤1 -->
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
        <q-form @submit="sendEmailCode" class="rounded-borders q-gutter-md">
          <q-input
            :disable="isBusy"
            debounce="1000"
            color="accent"
            v-model="model.email == null ? (model.email = (model.user = user).email) : model.email"
            autocomplete="email"
            :label="$tt('SignUp','step1.email')"
            :hint="$tt('SignUp','step1.emailHint')"
            type="email"
            :rules="rule.email"
            filled
          >
          </q-input>
          <q-stepper-navigation>
            <q-btn class="q-mr-md" :loading="isBusy" type="submit" color="accent"
                   :label="$tt('SignUp','continue')"/>
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
        <q-input
          :disable="isBusy"
          color="accent"
          v-model="model.password"
          autocomplete="new-password"
          :label="$tt('SignIn','password')"
          :hint="$tt('SignIn','passwordHint')"
          type="password"
          :rules="rule.password"
          filled
        />
        <q-input
          :disable="isBusy"
          color="accent"
          v-model="model.code"
          autocomplete="off"
          :label="$tt('SignUp','step2.code')"
          :hint="$tt('SignUp','step2.codeHint')"
          :rules="rule.code"
          filled
        >
        </q-input>
        <q-stepper-navigation>
          <q-btn :disable="isBusy" flat @click="step--" color="accent" :label="$tt('SignUp','back')"
                 class="q-ml-sm"/>
        </q-stepper-navigation>
      </q-step>
    </q-stepper>
    <q-card-section v-else>
      <q-skeleton type="QInput"/>
    </q-card-section>
  </update-user>
</template>

<script>
import RequireAuthorization from "../../components/RequireAuthorization";
import UpdateUser from "../../components/UpdateUser";
import ReCaptcha from "../../components/ReCaptcha";

export default {
  name: "Email",
  components: {ReCaptcha, UpdateUser, RequireAuthorization},
  props: {
    user: Object
  },
  data() {
    return {
      isBusy: false,
      step: 1,
      model: {
        user: null,
        email: null,
        recaptcha: null,
        password: "",
        code: ""
      },
      rule: {
        email: [
          val => val && val.match(this.$cfg.pattern.email) || this.$tt("SignUp", "step1.emailRule"),
          val => this.querying.email ?
            this.querying.email :
            (this.querying.email = this.$userApi.isEmailExists(val)
                .then((res) => !res.data || this.$tt("SignUp", "step1.emailExists"))
                .finally(() => this.querying.email = null)
            )
        ],
        password: [val => val && val.match(this.$cfg.pattern.password) || this.$tt("SignUp", "step1.passwordRule")],
        code: [val => val && val.trim().length > 0 || this.$tt("SignUp", "step2.codeRule")]
      },
      querying: {
        email: null
      }
    }
  },
  computed: {
    notVerify() {
      return this.model.recaptcha == null || this.model.recaptcha.trim().length == 0;
    }
  },
  methods: {
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
      this.$codeApi.createUpdateEmailCode(this.model.recaptcha, this.model.email)
        .then(this.sendEmailCodeSuccess)
        .catch(this.sendEmailCodeFailed)
        .finally(() => {
          this.isBusy = false;
        })
    },
    sendEmailCodeSuccess(res) {
      this.step = 2;
      this.$refs.captcha.$emit("reload")
    },
    sendEmailCodeFailed(e) {
      this.$refs.captcha.$emit("reload")
    },
    onSubmit() {
      if (this.model.user.email == this.model.email)
        return;
      return this.$userApi.resetEmail(this.model.password, this.model.code)
        .then(() => {
          this.model.user.email = this.model.email;
        }).catch((e) => {
          console.log(e, "????????")
          this.model.code = this.model.password = "";
          this.step = 1;
          throw e;
        })
    }
  }
}
</script>

<style scoped>

</style>
