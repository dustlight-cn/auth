<template>
  <q-layout view="hHh LpR fff">
    <q-header bordered class="bg-primary text-black" height-hint="98">
      <!-- Logo和标题 -->
      <q-toolbar>
        <Logo/>
        <q-toolbar-title>
          <i18n path="title"/>
        </q-toolbar-title>
      </q-toolbar>
      <q-toolbar>
        <q-btn dense rounded flat icon="arrow_back" @click="$router.back()"/>
        <q-toolbar-title>{{ title }}</q-toolbar-title>
      </q-toolbar>
    </q-header>

    <q-page-container>
      <transition
        appear
        enter-active-class="animated fadeIn"
      >
        <router-view class="q-mt-lg q-mb-lg gt-xs" ref="details"/>
        <router-view class="lt-sm" ref="details"/>
      </transition>
    </q-page-container>

    <q-footer>
      <Footer/>
    </q-footer>

  </q-layout>
</template>

<script>
import AvatarButton from "../components/AvatarButton";
import Logo from "../components/Logo";
import Footer from "../components/Footer";
import LanguageSelector from "../components/LanguageSelector";

export default {
  name: "DetailsLayout",
  components: {LanguageSelector, Logo, AvatarButton, Footer},
  data() {
    return {
      title: ""
    }
  },
  mounted() {
    this.title = this.$tt(this.$refs.details.$options, "title");
  }

}
</script>

<style scoped>

</style>
