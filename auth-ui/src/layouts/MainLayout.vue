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
          <q-route-tab no-caps
                       v-if="hasPermission(menu.getAuthorities())"
                       :name="menu.title"
                       :to="menu.to"
                       :label="$t('menus.' + menu.title)"/>
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
          <q-route-tab no-caps
                       v-if="hasPermission(menu.getAuthorities())"
                       :name="menu.title"
                       :to="menu.to"
                       :label="$t('menus.' + menu.title)"
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

    <q-footer style="z-index: -1">
      <Footer/>
    </q-footer>

  </q-layout>
</template>

<script>
import AvatarButton from "../components/common/AvatarButton";
import Logo from "../components/common/Logo";
import Footer from "../components/common/Footer";
import LanguageSelector from "../components/common/LanguageSelector";

const c = {
  name: 'MainLayout',
  components: {LanguageSelector, Logo, AvatarButton, Footer},
  data() {
    return {
      left: false,
      tab: "home",
      context: null,
      user: null
    }
  },
  methods: {
    updateTitle(compeonent) {
      if (compeonent != null && compeonent.showTitle != false) {
        this.context = compeonent;
      } else {
        this.context = null;
      }
    },
    hasPermission(authorities) {
      if (authorities == null)
        return true;
      if (typeof authorities == "string")
        return this.user && this.user.authorities && this.user.authorities.indexOf(authorities) >= 0;
      if (authorities.length == 0)
        return true;
      if (this.user == null || this.user.authorities == null || this.user.authorities.length == 0)
        return false;
      for (let key in authorities) {
        if (this.user.authorities.indexOf(authorities[key]) >= 0)
          return true;
      }
      return false;
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
    this.user = this.$s.loadUser();
    this.$s.onUserUpdate((u) => this.user = u);
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
