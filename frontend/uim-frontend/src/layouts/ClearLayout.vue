<template>
  <q-layout view="lHh Lpr lFf">
    <q-header class="bg-blue-grey-6 text-white">
      <q-toolbar>
<!--        <q-btn flat round dense icon="assignment_ind"/>-->
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
  </q-layout>
</template>

<script>
  export default {
    name: 'MainLayout',
  }
</script>
