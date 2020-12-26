<template>
  <q-layout view="hHh LpR fff">
    <q-header bordered class="bg-primary text-black" height-hint="98">
      <!-- Logo和标题 -->
      <q-toolbar>
        <transition
          appear
          enter-active-class="animated rotateIn"
        >
          <q-btn
            flat
            rounded
            dense
            icon="img:/icons/favicon-128x128.png"
            to="/"
          />
        </transition>
        <q-toolbar-title>
          <i18n path="title"/>
        </q-toolbar-title>
      </q-toolbar>
      <q-toolbar>
        <q-btn dense rounded flat icon="arrow_back" @click="$router.back()"/>
        <q-toolbar-title>{{ subtitle }}</q-toolbar-title>
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
  name: "DetailsLayout",
  components: {Logo, Footer}
}
</script>

<style scoped>

</style>
