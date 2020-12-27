<template>
  <require-authorization>
    <template v-slot="{user,token}">
      <q-btn rounded dense flat>
        <avatar :user="user"/>
        <q-menu v-if="user && user.uid">
          <div class="text-center q-pa-md">
            <avatar class="q-ma-sm" :size="avatarSize" :user="user"/>
            <div class="text-bold">{{ user.nickname }}</div>
            <div class="text-caption">{{ user.email }}</div>
          </div>
          <q-list style="min-width: 150px;">
            <q-separator/>
            <q-item @click="signOut" v-ripple clickable v-close-popup>
              <q-item-section>
                {{ $tt($options, "signOut") }}
              </q-item-section>
            </q-item>
          </q-list>
        </q-menu>
      </q-btn>
    </template>

    <template v-slot:unauthorized>
      <q-btn rounded color="black"
             :label="$tt($options,'signIn')"
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
