<template>
  <q-page>
    <q-parallax :height="300" :speed="0.5">
      <template v-slot:media>
        <img src="/statics/img/parallax1.jpg">
      </template>
      <div class="absolute-bottom custom-caption">
        <div class="text-h4">统一身份管理系统</div>
        <div class="text-subtitle1">OAuth2脚手架</div>
      </div>
    </q-parallax>

    <q-carousel
      v-model="slide"
      transition-prev="scale"
      transition-next="scale"
      swipeable
      animated
      control-color="white"
      navigation
      padding
      autoplay
      infinite
      arrows
      height="300px"
      class="bg-light-green text-white"
    >
      <q-carousel-slide v-for="(i,index) in data" :key="index" :name="i.name" class="column no-wrap flex-center">
        <q-icon :name="i.icon" size="56px"/>
        <div class="q-mt-md text-center" v-html="i.description"/>
        <q-btn v-if="i.to" :to="i.to" icon="link" flat round dense/>
      </q-carousel-slide>
    </q-carousel>

    <div class="bg-light-blue-7 text-white" style="height: 300px;padding-top: 50px;">
      <div class="text-h4 text-center">
        <q-icon size="100px" name="ion-logo-github"/>
        开放源代码
      </div>
      <div class="text-center" style="padding-top: 50px">
        <q-btn @click="openGithubPage" icon="ion-logo-github"
               color="light-blue-9" label="Github Page"/>
      </div>
    </div>

  </q-page>
</template>

<script>
  export default {
    name: 'PageIndex',
    data() {
      return {
        data: [
          {
            name: "account",
            icon: "person",
            description: "管理包括邮箱、昵称、性别等账号相关信息",
            to: "/info"
          }, {
            name: "apps",
            icon: "apps",
            description: "创建OAuth应用，获取AppKey和AppSecret以调用API",
            to: "/clients"
          }, {
            name: "template",
            icon: "mail",
            description: "轻松管理邮件模板，并且提供可视化编辑功能",
            to: "/templates"
          }, {
            name: "authorities",
            icon: "security",
            description: "创建、编辑、删除权限列表",
            to: "/authorities"
          }, {
            name: "roles",
            icon: "admin_panel_settings",
            description: "添加、删除或管理角色，为角色分配相应权限",
            to: "/roles"
          }, {
            name: "scopes",
            icon: "app_settings_alt",
            description: "添加、删除或授权作用域（Scope），为其分配相应权限",
            to: "/scopes"
          }
        ],
        slide: 'account'
      }
    },
    methods: {
      openGithubPage() {
        window.open('https://github.com/Hansin1997/Dustlight')
      }
    }
  }
</script>

<style lang="sass" scoped>
  .custom-caption
    text-align: center
    padding: 12px
    color: white
    background-color: rgba(0, 0, 0, .3)
</style>
