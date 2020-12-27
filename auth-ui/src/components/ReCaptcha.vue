<template>
  <vue-recaptcha ref="vueRecaptcha"
                 @verify="emitVerify"
                 @error="emitError"
                 @expired="emitExpired"
                 :sitekey="$cfg.recaptchaKey">
    <button hidden/>
  </vue-recaptcha>
</template>

<script>
import VueRecaptcha from 'vue-recaptcha';

export default {
  name: "ReCaptcha",
  components: {
    VueRecaptcha
  },
  props: {
    value: String
  },
  data() {
    return {
      callback: null
    }
  },
  methods: {
    reload() {
      this.$emit('input', null);
      this.$refs.vueRecaptcha.reset();
    },
    execute(callback) {
      this.callback = (callback);
      this.$refs.vueRecaptcha.execute();
    },
    emitVerify(response) {
      this.$emit('input', response);
      this.$emit('verify', response);
      if (this.callback != null) {
        this.callback(response)
      }
    },
    emitExpired() {
      this.$emit('input', null);
      this.$emit('expired');
    },
    emitError() {
      this.$emit('input', null);
      this.$emit('error');
    }
  }, mounted() {
    this.$on("execute", this.execute);
    this.$on("reload", this.reload);
  },
  meta: {
    // JS标记
    script: {
      recaptcha: {
        src: 'https://www.recaptcha.net/recaptcha/api.js?onload=vueRecaptchaApiLoaded&render=explicit',
        'async': '',
        'defer': ''
      }
    }
  }
}
</script>

<style scoped>

</style>
