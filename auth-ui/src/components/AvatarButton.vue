<template>
  <require-authorization>
    <template v-slot="{user,token}">
      <q-btn flat round>
        <avatar :user="user"/>
        <q-menu v-if="user && user.uid">
          <div class="text-center q-pa-md">
            <q-btn class="q-ma-sm" rounded dense flat :to="{name: 'avatar'}">
              <avatar :size="avatarSize" :user="user"/>
            </q-btn>
            <div class="text-bold">{{ user.nickname }}</div>
            <div class="text-caption">{{ user.email }}</div>
          </div>
          <q-list style="min-width: 150px;">
            <q-separator/>
            <q-item class="text-black" v-ripple clickable v-close-popup :to="{name:'personal-info'}">
              <q-item-section class="text-center">
                {{ $tt($options, "settings") }}
              </q-item-section>
            </q-item>
            <q-item @click="signOut" v-ripple clickable v-close-popup>
              <q-item-section class="text-center">
                {{ $tt($options, "signOut") }}
              </q-item-section>
            </q-item>
          </q-list>
        </q-menu>
      </q-btn>
    </template>

    <template v-slot:unauthorized>
      <q-btn rounded color="accent"
             :label="$tt($options,'signIn')"
             replace
             :to="{name:'login',query: {redirect_uri: $route.fullPath}}"/>
    </template>
  </require-authorization>
</template>

<script>
import Avatar from './Avatar'
import RequireAuthorization from './RequireAuthorization';

export default {
  name: "AvatarButton",
  components: {
    Avatar, RequireAuthorization
  },
  props: {
    avatarSize: {
      type: Number,
      default() {
        return 50;
      }
    }
  },
  methods: {
    signOut() {
      this.$tokenApi.deleteToken()
        .then(() => {
          this.$q.notify({
            message: this.$tt(this, "signOutSuccess"),
            type: 'info'
          })
        })
        .finally(() => {
          this.$s.clear();
        })
    }
  }
}
</script>

<style scoped>

</style>
