<template>
  <q-layout view="lHh Lpr lFf">
    <q-header class="bg-cyan text-white" elevated>
      <q-toolbar>
        <q-btn flat round dense icon="assignment_ind"/>
        <q-toolbar-title>UIM</q-toolbar-title>
        <q-btn flat round dense icon="sim_card" class="q-mr-xs"/>
        <q-btn flat round dense icon="gamepad"/>
      </q-toolbar>
      <q-toolbar inset>
        <q-breadcrumbs active-color="white" style="font-size: 16px">
          <q-breadcrumbs-el label="Home" icon="home"/>
          <q-breadcrumbs-el label="Components" icon="widgets"/>
          <q-breadcrumbs-el label="Toolbar"/>
        </q-breadcrumbs>
      </q-toolbar>
    </q-header>
    <q-page-container>
      <router-view/>
    </q-page-container>
  </q-layout>
</template>

<script>
  import EssentialLink from 'components/EssentialLink'

  export default {
    name: 'MainLayout',

    components: {
      EssentialLink
    },

    data() {
      return {
        essentialLinks: [
          {
            title: 'Docs',
            caption: 'quasar.dev',
            icon: 'school',
            link: 'https://quasar.dev'
          },
          {
            title: 'Github',
            caption: 'github.com/quasarframework',
            icon: 'code',
            link: 'https://github.com/quasarframework'
          },
          {
            title: 'Discord Chat Channel',
            caption: 'chat.quasar.dev',
            icon: 'chat',
            link: 'https://chat.quasar.dev'
          },
          {
            title: 'Forum',
            caption: 'forum.quasar.dev',
            icon: 'record_voice_over',
            link: 'https://forum.quasar.dev'
          },
          {
            title: 'Twitter',
            caption: '@quasarframework',
            icon: 'rss_feed',
            link: 'https://twitter.quasar.dev'
          },
          {
            title: 'Facebook',
            caption: '@QuasarFramework',
            icon: 'public',
            link: 'https://facebook.quasar.dev'
          },
          {
            title: 'Quasar Awesome',
            caption: 'Community Quasar projects',
            icon: 'favorite',
            link: 'https://awesome.quasar.dev'
          }
        ]
      }
    }
  }
</script>
