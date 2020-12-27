<template>
  <q-layout view="hHh LpR fff">
    <q-header bordered class="bg-primary text-black" height-hint="98">
      <!-- Logo和标题 -->
      <q-toolbar>
        <Logo/>
        <q-toolbar-title>
          <i18n path="title"/>
        </q-toolbar-title>
        <language-selector/>
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
        <div>
          <div v-if="title" class="flex flex-center text-h5 gt-sm text-center">
            {{ title }}
          </div>
          <div v-if="subtitle" class="flex flex-center text-grey text-center">
            {{ subtitle }}
          </div>
          <router-view ref="page"/>
        </div>
      </transition>
    </q-page-container>

    <q-footer>
      <Footer/>
    </q-footer>

  </q-layout>
</template>

<script>
import AvatarButton from "../components/AvatarButton";
import Logo from "../components/Logo";
import Footer from "../components/Footer";
import LanguageSelector from "../components/LanguageSelector";

export default {
  name: 'MainLayout',
  components: {LanguageSelector, Logo, AvatarButton, Footer},
  data() {
    return {
      left: false,
      tab: "home",
      title: "",
      subtitle: ""
    }
  },
  methods: {
    updateTitle(compeonent) {
      if (compeonent != null && compeonent.showTitle != false) {
        this.title = this.$tt(compeonent, "title");
        this.subtitle = this.$tt(compeonent, "subtitle");
      } else {
        this.title = this.subtitle = "";
      }
    }
  },
  mounted() {
    this.updateTitle(this.$refs.page);
  },
  watch: {
    '$route': {
      handler: function (t, f) {
        console.log(t.matched[t.matched.length - 1])
        if (t.matched.length > 0 && t.matched[t.matched.length - 1].components.default != null) {
          this.updateTitle(t.matched[t.matched.length - 1].components.default)
        }
      }
    }
  }
}
</script>
