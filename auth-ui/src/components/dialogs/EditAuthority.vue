<template>
  <q-dialog :persistent="persistentOnBusying && busying" ref="dialog" @hide="onDialogHide">
    <q-card class="full-width">
      <q-form @submit="save">
        <q-card-section>
          <div class="text-h6">
            {{ authority && authority.aid ? $tt($options, "edit") : $tt($options, "new") }}
          </div>
        </q-card-section>
        <q-card-section>
          <div class="q-gutter-sm">
            <q-input :loading="busying" :disable="busying"
                     color="accent" filled
                     :rules="rules.name"
                     v-model="model.name"
                     :label="$tt($options,'authorityName')"/>

            <q-input :loading="busying" :disable="busying"
                     color="accent" filled
                     :rules="rules.description"
                     v-model="model.description"
                     :label="$tt($options,'authorityDescription')"/>
          </div>
        </q-card-section>

        <div class="text-right q-gutter-sm q-pa-md">
          <q-btn :label="$t('cancel')"
                 color="grey-7"
                 flat
                 :disable="busying"
                 @click.stop="onCancelClick"
                 v-close-popup/>
          <q-btn :label="authority && authority.aid ? $t('update') : $t('create')"
                 type="submit"
                 color="accent"
                 :disable="busying"
                 :loading="busying"/>
        </div>
      </q-form>
    </q-card>
  </q-dialog>
</template>

<script>
export default {
  name: "EditAuthority",
  props: {
    client: Object,
    currentUser: Object,
    authority: Object,
    onSaved: Function,
    onSave: Function,
    persistentOnBusying: {
      type: Boolean,
      default() {
        return true
      }
    }
  },
  data() {
    return {
      busying: false,
      model: {
        name: this.authority ? this.authority.authorityName : "",
        description: this.authority ? this.authority.authorityDescription : "",
      },
      rules: {
        name: [val => val && val.trim().length > 0 || this.$tt(this, 'authorityNameRule')],
        description: [val => val && val.trim().length > 0 || this.$tt(this, 'authorityDescriptionRule')],
      }
    }
  },
  mounted() {

  },
  methods: {
    save() {
      if (this.busying)
        return
      this.busying = true
      this.$authoritiesApi.setAuthorities([{
        aid: this.authority ? this.authority.rid : null,
        authorityName: this.model.name.trim(),
        authorityDescription: this.model.description.trim()
      }], this.client.cid)
        .then((res) => {
          if (this.authority && this.authority.aid) {
            this.authority.authorityName = this.model.name.trim()
            this.authority.authorityDescription = this.model.description.trim()
            this.showUpdateSuccessMessage()
            if (this.onSaved)
              this.onSaved(this.authority)
          } else {
            this.showCreateSuccessMessage()
            if (this.onSaved)
              this.onSaved(res.data[0])
          }
          if (this.persistentOnBusying)
            this.hide()
        })
        .finally(() => this.busying = false)
      if (this.onSave) {
        if (this.authority && this.authority.aid) {
          this.onSave(this.authority)
        } else {
          this.onSave({
            authorityName: this.model.name.trim(),
            authorityDescription: this.model.description.trim()
          })
        }
      }
      if (!this.persistentOnBusying)
        this.hide()
    },
    // 以下方法是必需的
    // (不要改变它的名称 --> "show")
    show() {
      this.$refs.dialog.show()
    },

    // 以下方法是必需的
    // (不要改变它的名称 --> "hide")
    hide() {
      this.$refs.dialog.hide()
    },

    onDialogHide() {
      // QDialog发出“hide”事件时
      // 需要发出
      this.$emit('hide')
    },

    onOKClick() {
      // 按OK，在隐藏QDialog之前
      // 发出“ok”事件（带有可选的有效负载）
      // 是必需的
      this.$emit('ok')
      // 或带有有效负载：this.$emit('ok', { ... })

      // 然后隐藏对话框
      this.hide()
    },

    onCancelClick() {
      // 我们只需要隐藏对话框
      this.hide()
    },
    showUpdateSuccessMessage() {
      this.$q.notify({
        message: this.$t("updateSuccess"),
        type: 'positive'
      })
    },
    showCreateSuccessMessage() {
      this.$q.notify({
        message: this.$t("createSuccess"),
        type: 'positive'
      })
    }
  }
}
</script>

<style scoped>

</style>
