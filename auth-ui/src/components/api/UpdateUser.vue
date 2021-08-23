<template>
  <edit-page>
    <template v-slot="{wide}">
      <div v-if="user">
        <q-form @submit="onsubmit">
          <q-card-section v-if="caption || title">
            <div v-if="title" class="text-h6">{{ title }}</div>
            <div v-if="caption" class="text-caption">{{ caption }}</div>
          </q-card-section>
          <slot v-bind="{user:(user_clone||(user&&user.uid?(user_clone=user):user)),busy}"/>
          <q-card-actions>
            <q-space/>
            <q-btn no-caps :disable="busy" @click="onCancel" flat :label="$t('cancel')"/>
            <q-btn :disable="disableSubmitButton" no-caps :loading="busy" type="submit" color="accent"
                   :label="$t('update')"/>
          </q-card-actions>
        </q-form>
      </div>
      <require-authorization v-else v-slot="{user,token,loading}">
        <q-form @submit="onsubmit">
          <q-card-section v-if="caption || title">
            <div v-if="title" class="text-h6">{{ title }}</div>
            <div v-if="caption" class="text-caption">{{ caption }}</div>
          </q-card-section>
          <slot v-bind="{user:(user_clone||(user&&user.uid?(user_clone=user):user)),token,loading,busy}"/>
          <q-card-actions v-if="loading">
            <q-space/>
            <q-skeleton class="q-mr-md" type="QBtn"/>
            <q-skeleton type="QBtn"/>
          </q-card-actions>
          <q-card-actions v-else>
            <q-space/>
            <q-btn no-caps :disable="busy" @click="()=>onCancel()" flat :label="$t('cancel')"/>
            <q-btn :disable="disableSubmitButton" no-caps :loading="busy" type="submit" color="accent"
                   :label="$t('update')"/>
          </q-card-actions>
        </q-form>
      </require-authorization>
    </template>
  </edit-page>
</template>

<script>
import RequireAuthorization from "../common/RequireAuthorization";
import EditPage from "../common/EditPage";

export default {
  name: "CommonTemplate",
  components: {EditPage, RequireAuthorization},
  props: {
    user: Object,
    title: String,
    caption: String,
    submit: Function,
    disableSubmitButton: {
      type: Boolean,
      default() {
        return false;
      }
    },
    onSuccess: {
      type: Function,
      default() {
        this.$router.back();
      }
    },
    onCancel: {
      type: Function,
      default() {
        this.$router.back();
      }
    }
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
          if (this.isUserUpdated())
            this.$s.storeUser(this.user_clone);

          this.busy = false;
          this.onSuccess();
        })
          .catch((e) => {
            this.busy = false;
          })
    }
  }
}
</script>

<style scoped>

</style>
