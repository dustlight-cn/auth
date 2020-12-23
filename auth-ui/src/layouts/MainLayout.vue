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

        <AvatarButton/>
      </q-toolbar>

      <!-- 顶部导航栏 -->
      <q-tabs v-model="tab" v-if="!left">
        <transition
          appear
          enter-active-class="animated fadeIn"
          v-for="(menu,i) in this.$menus.main" :key="i"
        >
          <q-route-tab :name="menu.title" :to="menu.link" :label="$t('menus.' + menu.title)"/>
        </transition>
      </q-tabs>
    </q-header>

    <!-- 侧边导航栏 -->
    <q-drawer show-if-above v-model="left" side="left" bordered>
      <q-tabs v-model="tab" vertical>
        <transition
          appear
          enter-active-class="animated fadeIn"
          v-for="(menu,i) in this.$menus.main" :key="i"
        >
          <q-route-tab :name="menu.title" :to="menu.link" :label="$t('menus.' + menu.title)"
                       :icon="menu.icon"/>
        </transition>
      </q-tabs>
    </q-drawer>

    <q-page-container>
      <transition
        appear
        enter-active-class="animated fadeIn"
      >
        <router-view/>
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
import AvatarButton from "../components/AvatarButton";

export default {
  name: 'MainLayout',
  components: {AvatarButton},
  data() {
    return {
      left: false,
      tab: "home"
    }
  }
}
</script>
