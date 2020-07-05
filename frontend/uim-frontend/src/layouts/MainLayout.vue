<template>
  <q-layout view="hHh Lpr lff" class="shadow-2 rounded-borders">
    <q-header class="bg-blue-grey-6 text-white">
      <q-toolbar>
        <q-btn
          flat
          dense
          round
          icon="menu"
          aria-label="Menu"
          @click="leftDrawerOpen = !leftDrawerOpen"
        />
        <q-toolbar-title>
          <q-btn @click="$router.push({path:'/'})" flat label="统一身份管理系统"/>
        </q-toolbar-title>
      </q-toolbar>
    </q-header>

    <q-drawer
      v-model="leftDrawerOpen"
      :width="200"
      :breakpoint="500"
      bordered
      show-if-above
      content-class="bg-grey-3"
    >
      <MainMenu/>
    </q-drawer>

    <q-page-container>
      <router-view/>
    </q-page-container>
  </q-layout>
</template>

<script>
  import MainMenu from 'components/MainMenu'

  export default {
    name: 'MainLayout',

    components: {
      MainMenu
    },

    data() {
      return {
        leftDrawerOpen: false,
      }
    }
  }
</script>
