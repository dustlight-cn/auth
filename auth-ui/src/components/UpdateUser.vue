<template>
  <q-page padding>
    <!-- 宽窗口 -->
    <div class="q-mt-lg q-mb-lg gt-xs" style="max-width: 599px;margin: 0 auto;">
      <q-card v-if="user" class="q-pa-md shadow-0" bordered>
        <q-form @submit="onsubmit">
          <q-card-section v-if="caption || title">
            <div v-if="title" class="text-h6">{{ title }}</div>
            <div v-if="caption" class="text-caption">{{ caption }}</div>
          </q-card-section>
          <slot v-bind="{user:(user_clone||(user&&user.uid?(user_clone=user):user)),busy}"/>
          <q-card-actions>
            <q-space/>
            <q-btn no-caps :disable="busy" @click="$router.back()" flat>{{ $t("cancel") }}</q-btn>
            <q-btn no-caps :loading="busy" type="submit" color="accent">{{ $t("update") }}</q-btn>
          </q-card-actions>
        </q-form>
      </q-card>
      <require-authorization v-else v-slot="{user,token,loading}">
        <q-card class="q-pa-md shadow-0" bordered>
          <q-form @submit="onsubmit">
            <q-card-section v-if="caption || title">
              <div v-if="title" class="text-h6">{{ title }}</div>
              <div v-if="caption" class="text-caption">{{ caption }}</div>
            </q-card-section>
            <slot v-bind="{user:(user_clone||(user&&user.uid?(user_clone=user):user)),token,loading,busy}"/>
            <q-card-actions>
              <q-space/>
              <q-btn no-caps :disable="busy" @click="$router.back()" flat>{{ $t("cancel") }}</q-btn>
              <q-btn no-caps :loading="busy" type="submit" color="accent">{{ $t("update") }}</q-btn>
            </q-card-actions>
          </q-form>
        </q-card>
      </require-authorization>
    </div>

    <!-- 窄窗口 -->
    <div class="lt-sm">
      <q-card v-if="user" class="q-pa-sm shadow-0">
        <q-form @submit="onsubmit">
          <q-card-section v-if="caption || title">
            <div v-if="title" class="text-h6">{{ title }}</div>
            <div v-if="caption" class="text-caption">{{ caption }}</div>
          </q-card-section>
          <slot v-bind="{user:(user_clone||(user&&user.uid?(user_clone=user):user)),busy}"/>
          <q-card-actions>
            <q-space/>
            <q-btn no-caps :disable="busy" @click="$router.back()" flat>{{ $t("cancel") }}</q-btn>
            <q-btn no-caps :loading="busy" type="submit" color="accent">{{ $t("update") }}</q-btn>
          </q-card-actions>
        </q-form>
      </q-card>
      <require-authorization v-else v-slot="{user,token,loading}">
        <q-card class="q-pa-sm shadow-0">
          <q-form @submit="onsubmit">
            <q-card-section v-if="caption || title">
              <div v-if="title" class="text-h6">{{ title }}</div>
              <div v-if="caption" class="text-caption">{{ caption }}</div>
            </q-card-section>
            <slot v-bind="{user:(user_clone||(user&&user.uid?(user_clone=user):user)),token,loading,busy}"/>
            <q-card-actions>
              <q-space/>
              <q-btn no-caps :disable="busy" @click="$router.back()" flat>{{ $t("cancel") }}</q-btn>
              <q-btn no-caps :loading="busy" type="submit" color="accent">{{ $t("update") }}</q-btn>
            </q-card-actions>
          </q-form>
        </q-card>
      </require-authorization>
    </div>
  </q-page>
</template>

<script>
import RequireAuthorization from "./RequireAuthorization";

export default {
  name: "CommonTemplate",
  components: {RequireAuthorization},
  props: {
    user: Object,
    title: String,
    caption: String,
    submit: Function
  },
  data() {
    return {
      busy: false,
      user_clone: null
    }
  },
  methods: {
    isUserUpdated() {
      let current_user;
      if (this.user_clone == null || (current_user = this.$s.loadUser()) == null ||
        this.user_clone.uid != current_user.uid)
        return false;
      return !this.$util.objectEquals(current_user, this.user_clone);
    },
    onsubmit() {
      if (this.submit == null || this.busy)
        return;
      this.busy = true;
      let p = this.submit();
      if (p == null)
        this.busy = false;
      else
        p.then(() => {
          this.$q.notify({
            type: "positive",
            message: this.$t("updateSuccess")
          })
        })
          .finally(() => {
            if (this.isUserUpdated())
              this.$s.storeUser(this.user_clone);
            this.busy = false;
          })
    }
  }
}
</script>

<style scoped>

</style>
