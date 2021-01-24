<template>
  <q-layout view="hHh LpR fff">
    <q-header bordered class="bg-primary text-black" height-hint="98">
      <!-- Logo和标题 -->
      <q-toolbar>
        <Logo/>
        <q-toolbar-title>
          <i18n path="siteName"/>
        </q-toolbar-title>
        <language-selector/>
        <avatar-button/>
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
      <div v-if="title" class="flex flex-center text-h5 gt-sm text-center q-mt-md">
        {{ title }}
      </div>
      <div v-if="subtitle" class="flex flex-center text-grey text-center q-mt-md">
        {{ subtitle }}
      </div>
      <transition
        appear
        enter-active-class="animated fadeIn"
      >
        <router-view ref="page"/>
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

const c = {
  name: 'MainLayout',
  components: {LanguageSelector, Logo, AvatarButton, Footer},
  data() {
    return {
      left: false,
      tab: "home",
      context: null
    }
  },
  methods: {
    updateTitle(compeonent) {
      if (compeonent != null && compeonent.showTitle != false) {
        this.context = compeonent;
      } else {
        this.context = null;
      }
    }
  },
  computed: {
    title() {
      return this.context ? this.$tt(this.context, "title", true) : "";
    },
    subtitle() {
      return this.context ? this.$tt(this.context, "subtitle", true) : "";
    }
  },
  mounted() {
    this.updateTitle(this.$refs.page.$options);
  },
  watch: {
    '$route': {
      handler: function (t, f) {
        if (t.matched.length > 0 && t.matched[t.matched.length - 1].components.default != null) {
          this.updateTitle(t.matched[t.matched.length - 1].components.default)
        }
      }
    }
  }
}
export default c;
</script>
