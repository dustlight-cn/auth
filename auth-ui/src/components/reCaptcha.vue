<template>
  <vue-recaptcha ref="vueRecaptcha" @verify="(response)=>$emit('input', response)" :sitekey="$cfg.recaptchaKey"/>
</template>

<script>
import VueRecaptcha from 'vue-recaptcha';

export default {
  name: "reCaptcha",
  components: {
    VueRecaptcha
  },
  props: {
    value: String
  },
  methods: {
    reload() {
      this.$emit('input', null);
      this.$refs.vueRecaptcha.reset();
    }
  }, mounted() {
    this.$root.$on("reloadCaptcha", this.reload)
  }
}
</script>

<style scoped>

</style>
