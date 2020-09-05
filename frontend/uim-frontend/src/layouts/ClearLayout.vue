<template>
  <q-layout view="hHh Lpr lff" class="bg-white text-black">
    <q-header class="bg-1 text-black" reveal bordered>
      <q-toolbar>
        <q-btn
          flat
          dense
          round
          :icon="'ion-logo-google'"
          :color="'blue'"
          aria-label="Home Page"
          to="/"
        />
        <q-toolbar-title>光尘</q-toolbar-title>
      </q-toolbar>
    </q-header>
    <q-page-container>
      <router-view/>
    </q-page-container>
    <!-- 尾部 -->
    <q-footer>
      <CommonFooter/>
    </q-footer>
  </q-layout>

</template>

<script>
  import CommonFooter from "components/CommonFooter";

  export default {
    name: 'MainLayout',
    components: {CommonFooter},
  }
</script>
