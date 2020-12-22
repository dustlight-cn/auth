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
      <div class="q-pl-sm q-pr-sm text-center">
        © Dustlight
      </div>
    </q-footer>

  </q-layout>
</template>

<script>
export default {
  name: 'ChildLayout',
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
