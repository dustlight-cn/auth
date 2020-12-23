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
    </q-header>

    <q-page-container>
      <transition
        appear
        enter-active-class="animated fadeIn"
      >
        <router-view @setSubtitle="setSubtitle"/>
      </transition>
    </q-page-container>

    <q-footer class="text-black">
      <Footer/>
    </q-footer>

  </q-layout>
</template>

<script>
import Logo from "../components/Logo";
import Footer from "../components/Footer";

export default {
  name: 'ConnectLayout',
  components: {Logo, Footer},
  data() {
    return {
      left: false,
      tab: "home",
      subtitle: ""
    }
  },
  methods: {
    setSubtitle(subtitle) {
      this.subtitle = subtitle
    }
  }
}
</script>
