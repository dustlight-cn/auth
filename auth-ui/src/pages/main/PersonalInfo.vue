<template>
  <q-page padding>
    <require-authorization v-slot="{user,token}">
      <!-- 基础信息 -->
      <q-card bordered class="shadow-0 q-pa-md q-mt-md">
        <div class="text-h6 q-pb-md">
          {{ $tt($options, "baseInfo") }}
          <div class="text-caption">
            {{ $tt($options, "baseInfoDesc") }}
          </div>
        </div>
        <q-list>
          <!-- 头像 -->
          <q-item v-ripple clickable :to="{name:'avatar'}">
            <q-item-section>
              <q-item-label caption>
                {{ $tt($options, "avatar") }}
              </q-item-label>
            </q-item-section>
            <q-item-label>
              <q-btn dense flat rounded>
                <avatar :user="user"/>
              </q-btn>
            </q-item-label>
          </q-item>
          <!-- 昵称 -->
          <q-item v-ripple clickable :to="{name:'nickname'}">
            <q-item-section>
              <q-item-label caption>
                {{ $tt($options, "nickname") }}
              </q-item-label>
              <q-item-label>{{ user.nickname }}</q-item-label>
            </q-item-section>
            <q-item-label>
              <q-btn dense rounded flat icon="keyboard_arrow_right"/>
            </q-item-label>
          </q-item>
          <!-- 性别 -->
          <q-item v-ripple clickable :to="{name:'gender'}">
            <q-item-section>
              <q-item-label caption>
                {{ $tt($options, "gender") }}
              </q-item-label>
              <q-item-label>{{ $tt("gender." + user.gender) }}</q-item-label>
            </q-item-section>
            <q-item-label>
              <q-btn dense rounded flat icon="keyboard_arrow_right"/>
            </q-item-label>
          </q-item>
          <!-- 密码 -->
          <q-item v-ripple clickable :to="{name:'password'}">
            <q-item-section>
              <q-item-label caption>
                {{ $tt($options, "password") }}
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
          {{ $tt($options, "contact") }}
          <div class="text-caption">
            {{ $tt($options, "contactDesc") }}
          </div>
        </div>
        <q-list>
          <!-- 电子邮箱 -->
          <q-item v-ripple clickable :to="{name:'email'}">
            <q-item-section>
              <q-item-label caption>
                {{ $tt($options, "email") }}
              </q-item-label>
              <q-item-label>{{ user.email || "-" }}</q-item-label>
            </q-item-section>
            <q-item-label>
              <q-btn dense rounded flat icon="keyboard_arrow_right"/>
            </q-item-label>
          </q-item>
          <!-- 电子邮箱 -->
          <q-item v-ripple clickable :to="{name:'phone'}">
            <q-item-section>
              <q-item-label caption>
                {{ $tt($options, "phone") }}
              </q-item-label>
              <q-item-label>{{ user.phone || "-" }}</q-item-label>
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
          {{ $tt($options, "other") }}
          <div class="text-caption">
            {{ $tt($options, "otherDesc") }}
          </div>
        </div>
        <q-list>
          <!-- UID -->
          <q-item>
            <q-item-section>
              <q-item-label caption>UID</q-item-label>
              <q-item-label class="content">{{ user.uid }}</q-item-label>
            </q-item-section>
          </q-item>
          <!-- 用户名 -->
          <q-item>
            <q-item-section>
              <q-item-label caption>
                {{ $tt($options, "username") }}
              </q-item-label>
              <q-item-label class="content">{{ user.username }}</q-item-label>
            </q-item-section>
          </q-item>
          <!-- 角色 -->
          <q-item>
            <q-item-section>
              <q-item-label caption>
                {{ $tt($options, "roles") }}
              </q-item-label>
              <q-item-label class="content-small">
                <user-role-clients :editable="false" :current-user="user" :user="user"/>
              </q-item-label>
            </q-item-section>
          </q-item>
          <!-- 注册时间 -->
          <q-item>
            <q-item-section>
              <q-item-label caption>
                {{ $tt($options, "regtime") }}
              </q-item-label>
              <q-item-label class="content">
                {{ $util.dateFormat(user.createdAt) }}
              </q-item-label>
            </q-item-section>
          </q-item>
        </q-list>
      </q-card>
    </require-authorization>
  </q-page>
</template>

<script>
import Avatar from "../../components/api/Avatar";
import RequireAuthorization from "../../components/common/RequireAuthorization";
import UserRoleClients from "../../components/api/UserRoleClients";

export default {
  name: "PersonalInfo",
  components: {UserRoleClients, RequireAuthorization, Avatar},
}
</script>

<style scoped>
.content {
  font-family: Consolas;
  font-size: 16px;
}

.content-small {
  font-family: Consolas;
  font-size: 10px;
}
</style>
