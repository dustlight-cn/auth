<template>
  <q-layout view="lHh Lpr lFf" class="bg-white text-black">
    <q-header class="bg-1 text-black shadow-2">
      <q-toolbar>
        <q-btn to="/" flat round dense icon="home"/>
        <q-toolbar-title>
          统一身份管理系统
        </q-toolbar-title>
        <!--        <q-btn flat round dense icon="sim_card" class="q-mr-xs"/>-->
        <!--        <q-btn flat round dense icon="gamepad"/>-->
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
