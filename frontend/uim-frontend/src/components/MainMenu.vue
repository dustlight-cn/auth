<template>
  <div>
    <q-scroll-area style="height:300px; margin-top: 150px; border-right: 1px solid #ddd">
      <q-list padding>
        <q-separator/>
        <q-item to="/manage/info" clickable v-ripple>
          <q-item-section avatar>
            <q-icon name="person"/>
          </q-item-section>
          <q-item-section>
            账号
          </q-item-section>
        </q-item>

        <q-item to="/manage/clients" clickable v-ripple>
          <q-item-section avatar>
            <q-icon name="apps"/>
          </q-item-section>
          <q-item-section>
            应用
          </q-item-section>
        </q-item>

        <q-item to="/manage/templates" v-if="hasAuthority('READ_TEMPLATE')" clickable v-ripple>
          <q-item-section avatar>
            <q-icon name="mail"/>
          </q-item-section>
          <q-item-section>
            模板
          </q-item-section>
        </q-item>

      </q-list>
    </q-scroll-area>

    <q-img class="absolute-top" style="height: 150px">
      <div class="absolute-bottom bg-transparent absolute-top">
        <q-avatar size="56px" class="q-mb-sm">
          <q-skeleton v-if="loading()" type="QAvatar"/>
          <img v-else src="/statics/img/boy-avatar.jpg">
        </q-avatar>
        <q-skeleton v-if="loading()" type="text"/>
        <div v-else class="text-weight-bold  text-grey-8">{{nickname()}}</div>
        <q-skeleton v-if="loading()" type="text"/>
        <div class="text-black" v-else>{{email()}}</div>
        <q-skeleton v-if="loading()" type="text"/>
        <div class="text-italic text-grey-8" v-else>{{user().roleDescription}}</div>
      </div>
    </q-img>
    <div class="absolute-top" style="height: 150px">

    </div>
  </div>
</template>

<script>
  import axios from 'axios'

  export default {
    name: 'MainMenu',
    inject: ["loading", "nickname", "email", "hasAuthority", "user"],
  }
</script>
