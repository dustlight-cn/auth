<template>
  <q-dialog :persistent="persistentOnBusying && busying" ref="dialog" @hide="onDialogHide">
    <q-card class="full-width">
      <q-form @submit="save">
        <q-card-section>
          <div class="text-h6">
            {{ role && role.rid ? $tt($options, "edit") : $tt($options, "new") }}
          </div>
        </q-card-section>
        <q-card-section>
          <div class="q-gutter-sm">
            <q-input :loading="busying" :disable="busying"
                     color="accent" filled
                     :rules="rules.name"
                     v-model="model.name"
                     :label="$tt($options,'roleName')"/>

            <q-input :loading="busying" :disable="busying"
                     color="accent" filled
                     :rules="rules.description"
                     v-model="model.description"
                     :label="$tt($options,'roleDescription')"/>
          </div>
        </q-card-section>

        <div class="text-right q-gutter-sm q-pa-md">
          <q-btn :label="$t('cancel')"
                 color="grey-7"
                 flat
                 :disable="busying"
                 @click.stop="onCancelClick"
                 v-close-popup/>
          <q-btn :label="role && role.rid ? $t('update') : $t('create')"
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
  name: "EditRole",
  props: {
    client: Object,
    currentUser: Object,
    role: Object,
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
        name: this.role ? this.role.roleName : "",
        description: this.role ? this.role.roleDescription : "",
      },
      rules: {
        name: [val => val && val.trim().length > 0 || this.$tt(this, 'roleNameRule')],
        description: [val => val && val.trim().length > 0 || this.$tt(this, 'roleDescriptionRule')],
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
      this.$rolesApi.setRoles([{
        rid: this.role ? this.role.rid : null,
        roleName: this.model.name.trim(),
        roleDescription: this.model.description.trim()
      }], this.client.cid)
        .then((res) => {
          if (this.role) {
            this.role.roleName = this.model.name.trim()
            this.role.roleDescription = this.model.description.trim()
            this.showUpdateSuccessMessage()
            if (this.onSaved)
              this.onSaved(this.role)
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
        if (this.role && this.role.rid) {
          this.onSave(this.role)
        } else {
          this.onSave({
            roleName: this.model.name.trim(),
            roleDescription: this.model.description.trim()
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
