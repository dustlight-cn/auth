<template>
  <q-layout view="hHh LpR fff">
    <q-header bordered class="bg-primary text-black" height-hint="98">
      <!-- Logo和标题 -->
      <q-toolbar>
        <Logo/>
        <q-toolbar-title>
          <i18n path="siteName"/>
        </q-toolbar-title>
        <language-selector/>
        <avatar-button/>
      </q-toolbar>
      <q-toolbar>
        <q-btn dense rounded flat icon="arrow_back" @click="$router.back()"/>
        <q-toolbar-title>{{ title }}</q-toolbar-title>
      </q-toolbar>
    </q-header>

    <q-page-container>
      <div v-if="subtitle" class="flex flex-center text-grey text-center q-mt-md">
        {{ subtitle }}
      </div>
      <transition
        appear
        enter-active-class="animated fadeIn"
      >
        <router-view ref="page"/>
      </transition>
    </q-page-container>

    <q-footer>
      <Footer/>
    </q-footer>

  </q-layout>
</template>

<script>
import AvatarButton from "../components/common/AvatarButton";
import Logo from "../components/common/Logo";
import Footer from "../components/common/Footer";
import LanguageSelector from "../components/common/LanguageSelector";

export default {
  name: "DetailsLayout",
  components: {LanguageSelector, Logo, AvatarButton, Footer},
  data() {
    return {
      context: null
    }
  },
  methods: {
    updateTitle(compeonent) {
      if (compeonent != null && compeonent.showTitle != false) {
        this.context = compeonent;
      } else {
        this.context = null;
      }
    }
  },
  computed: {
    title() {
      return this.context ? this.$tt(this.context, "title", true) : "";
    },
    subtitle() {
      return this.context && this.context.showSubtitle ? this.$tt(this.context, "subtitle", true) : "";
    }
  },
  mounted() {
    this.updateTitle(this.$refs.page.$options);
  },
  watch: {
    '$route': {
      handler: function (t, f) {
        if (t.matched.length > 0 && t.matched[t.matched.length - 1].components.default != null) {
          this.updateTitle(t.matched[t.matched.length - 1].components.default)
        }
      }
    }
  }

}
</script>

<style scoped>

</style>
