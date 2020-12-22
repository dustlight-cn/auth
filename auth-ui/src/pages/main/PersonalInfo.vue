<template>
  <q-page padding>
    <div class="flex flex-center text-h5 gt-sm">
      {{ $tt(this, "title") }}
    </div>
    <div class="flex flex-center text-grey">
      {{ $tt(this, "subtitle") }}
    </div>
    <!-- 基础信息 -->
    <q-card bordered class="shadow-0 q-pa-md q-mt-md">
      <div class="text-h6 q-pb-md">
        {{ $tt(this, "baseInfo") }}
        <div class="text-caption">
          {{ $tt(this, "baseInfoDesc") }}
        </div>
      </div>
      <q-list>
        <!-- 头像 -->
        <q-item v-ripple clickable>
          <q-item-section>
            <q-item-label caption>
              {{ $tt(this, "avatar") }}
            </q-item-label>
          </q-item-section>
          <q-item-label>
            <q-btn dense flat rounded>
              <Avatar :user="user"/>
            </q-btn>
          </q-item-label>
        </q-item>
        <!-- 昵称 -->
        <q-item v-ripple clickable to="nickname">
          <q-item-section>
            <q-item-label caption>
              {{ $tt(this, "nickname") }}
            </q-item-label>
            <q-item-label>{{ user.nickname }}</q-item-label>
          </q-item-section>
          <q-item-label>
            <q-btn dense rounded flat icon="keyboard_arrow_right"/>
          </q-item-label>
        </q-item>
        <!-- 性别 -->
        <q-item v-ripple clickable>
          <q-item-section>
            <q-item-label caption>
              {{ $tt(this, "gender") }}
            </q-item-label>
            <q-item-label>{{ $t("gender." + user.gender) }}</q-item-label>
          </q-item-section>
          <q-item-label>
            <q-btn dense rounded flat icon="keyboard_arrow_right"/>
          </q-item-label>
        </q-item>
        <!-- 密码 -->
        <q-item v-ripple clickable>
          <q-item-section>
            <q-item-label caption>
              {{ $tt(this, "password") }}
            </q-item-label>
            <q-item-label>******</q-item-label>
          </q-item-section>
          <q-item-label>
            <q-btn dense rounded flat icon="keyboard_arrow_right"/>
          </q-item-label>
        </q-item>
      </q-list>
    </q-card>

    <!-- 联系方式 -->
    <q-card bordered class="shadow-0 q-pa-md q-mt-md">
      <div class="text-h6 q-pb-md">
        {{ $tt(this, "contact") }}
        <div class="text-caption">
          {{ $tt(this, "contactDesc") }}
        </div>
      </div>
      <q-list>
        <!-- 电子邮箱 -->
        <q-item v-ripple clickable>
          <q-item-section>
            <q-item-label caption>
              {{ $tt(this, "email") }}
            </q-item-label>
            <q-item-label>{{ user.email }}</q-item-label>
          </q-item-section>
          <q-item-label>
            <q-btn dense rounded flat icon="keyboard_arrow_right"/>
          </q-item-label>
        </q-item>
      </q-list>
    </q-card>

    <!-- 其他信息 -->
    <q-card bordered class="shadow-0 q-pa-md q-mt-md">
      <div class="text-h6 q-pb-md">
        {{ $tt(this, "other") }}
        <div class="text-caption">
          {{ $tt(this, "otherDesc") }}
        </div>
      </div>
      <q-list>
        <!-- 用户名 -->
        <q-item>
          <q-item-section>
            <q-item-label caption>
              {{ $tt(this, "username") }}
            </q-item-label>
            <q-item-label overline>{{ user.username }}</q-item-label>
          </q-item-section>
        </q-item>
        <!-- 角色 -->
        <q-item>
          <q-item-section>
            <q-item-label caption>
              {{ $tt(this, "roles") }}
            </q-item-label>
            <q-item-label overline>
              <q-chip v-for="(role,i) in user.roles" :key="i">
                {{ role.roleDescription }}
              </q-chip>
            </q-item-label>
          </q-item-section>
        </q-item>
        <!-- 角色 -->
        <q-item>
          <q-item-section>
            <q-item-label caption>
              {{ $tt(this, "regtime") }}
            </q-item-label>
            <q-item-label overline>
              {{ $util.dateFormat(user.createdAt) }}
            </q-item-label>
          </q-item-section>
        </q-item>
      </q-list>
    </q-card>
  </q-page>
</template>

<script>
import Avatar from "components/Avatar.vue";

export default {
  name: "PersonalInfo",
  components: {Avatar},
  data() {
    return {
      user: {}
    }
  },
  mounted() {
    if (this.$s.loadToken()) {
      this.user = this.$s.loadUser();
    }
  }
}
</script>

<style scoped>

</style>
