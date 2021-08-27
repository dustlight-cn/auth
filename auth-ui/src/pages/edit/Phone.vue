<template>
  <update-user
    :on-success="onSuccess"
    :on-cancel="onCancel"
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
        <q-form @submit="sendPhoneCode" class="rounded-borders q-gutter-md">
          <q-input
            :disable="isBusy"
            debounce="1000"
            color="accent"
            v-model="model.phone == null ? (model.phone = ((model.user = user).phone || '')) : model.phone"
            autocomplete="phone"
            :label="$tt('SignUp','step1.phone')"
            :hint="$tt('SignUp','step1.phoneHint')"
            type="phone"
            :rules="rule.phone"
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
        :title="$tt($options,withoutPassword?'step2.titleWithoutPassword':'step2.title')"
        :caption="$tt($options,withoutPassword?'step2.captionWithoutPassword':'step2.caption')"
        icon="sms"
        color="grey"
        done-color="black"
        active-color="accent"
        :done="step > 2"
        :header-nav="step > 2"
      >
        <q-input
          v-if="!withoutPassword"
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
import RequireAuthorization from "../../components/common/RequireAuthorization";
import UpdateUser from "../../components/api/UpdateUser";
import ReCaptcha from "../../components/common/ReCaptcha";

export default {
  name: "Phone",
  components: {ReCaptcha, UpdateUser, RequireAuthorization},
  props: {
    user: Object,
    withoutPassword: Boolean,
    onSuccess: Function,
    onCancel: Function
  },
  data() {
    return {
      isBusy: false,
      step: 1,
      model: {
        user: null,
        phone: null,
        recaptcha: null,
        password: "",
        code: ""
      },
      rule: {
        phone: [
          val => val && val.match(this.$cfg.pattern.phone) || this.$tt("SignUp", "step1.phoneRule"),
          val => this.querying.phone ?
            this.querying.phone :
            (this.querying.phone = this.$userApi.isPhoneExists(val)
                .then((res) => !res.data || this.$tt("SignUp", "step1.phoneExists"))
                .finally(() => this.querying.phone = null)
            )
        ],
        password: [val => val && val.match(this.$cfg.pattern.password) || this.$tt("SignUp", "step1.passwordRule")],
        code: [val => val && val.trim().length > 0 || this.$tt("SignUp", "step2.codeRule")]
      },
      querying: {
        phone: null
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
    sendPhoneCode() {
      if (!this.verify(this.sendPhoneCode))
        return;
      this.isBusy = true;
      this.$codeApi.createUpdatePhoneCode(this.model.recaptcha, this.model.phone)
        .then(this.sendPhoneCodeSuccess)
        .catch(this.sendPhoneCodeFailed)
        .finally(() => {
          this.isBusy = false;
        })
    },
    sendPhoneCodeSuccess(res) {
      this.step = 2;
      this.$refs.captcha.$emit("reload")
    },
    sendPhoneCodeFailed(e) {
      this.$refs.captcha.$emit("reload")
    },
    onSubmit() {
      if (this.model.user.phone == this.model.phone) {
        if (this.onCancel != null)
          this.onCancel();
        return;
      }
      return (
        this.withoutPassword ?
          this.$usersApi.updateUserPhone(this.model.user.uid, this.model.code) :
          this.$userApi.resetPhone(this.model.password, this.model.code)
      )
        .then(() => {
          this.model.user.phone = this.model.phone;
        }).catch((e) => {
          this.model.code = this.model.password = "";
          throw e;
        })
    }
  }
}
</script>

<style scoped>

</style>
