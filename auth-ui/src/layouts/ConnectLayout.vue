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
      </q-toolbar>
    </q-header>

    <q-page-container>
      <transition
        appear
        enter-active-class="animated fadeIn"
      >
        <router-view/>
      </transition>
    </q-page-container>

    <q-footer style="z-index: -1">
      <Footer/>
    </q-footer>

  </q-layout>
</template>

<script>
import LanguageSelector from '../components/common/LanguageSelector'
import Logo from "../components/common/Logo";
import Footer from "../components/common/Footer";

export default {
  name: 'ConnectLayout',
  components: {Logo, Footer, LanguageSelector}
}
</script>
