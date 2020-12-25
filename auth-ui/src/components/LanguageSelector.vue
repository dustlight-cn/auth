<template>
  <q-select
    v-model="lang"
    :options="langOptions"
    dense
    borderless
    emit-value
    map-options
    label-color="grey"
    table-colspan="black"
    color="accent"
    style="min-width: 100px"
  />
</template>

<script>
import languages from 'quasar/lang/index.json'

export default {
  name: "LanguageSelector",
  data() {
    return {
      lang: this.$q.lang.isoName
    }
  },
  watch: {
    lang(lang) {
      this.changeLanguage(lang);
    }
  },
  created() {
    this.langOptions = languages.filter(lang =>
      ['en-us', 'zh-hans'].includes(lang.isoName)
    ).map(lang => ({
      label: lang.nativeName, value: lang.isoName
    }))
  }
}
</script>

<style scoped>

</style>
