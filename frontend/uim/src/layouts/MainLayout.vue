<template>
  <q-layout view="hHh LpR fff">

    <q-header bordered class="bg-primary text-black" height-hint="98">
      <q-toolbar>
        <q-btn
          flat
          rounded
          dense
          icon="img:/icons/favicon-128x128.png"
          to="/"
        ></q-btn>
        <q-toolbar-title>
          <i18n path="title">x</i18n>
        </q-toolbar-title>
      </q-toolbar>

      <q-tabs v-if="!left">
        <q-route-tab to="." label="Page One"/>
        <q-route-tab to="/page2" label="Page Two"/>
        <q-route-tab to="/page3" label="Page Three"/>
      </q-tabs>
    </q-header>

    <q-drawer show-if-above v-model="left" side="left" bordered>
      <!-- drawer content -->
      <div class="q-pa-sm text-center">
        Menus
      </div>
    </q-drawer>

    <q-page-container>
      <router-view/>
    </q-page-container>

    <q-footer class="text-black">
      <div class="q-pa-sm text-center">
        Footer
      </div>
    </q-footer>

  </q-layout>
</template>

<script>
  export default {
    name: 'MainLayout',
    data() {
      return {
        left: false,
      }
    }
  }
</script>
