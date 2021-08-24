<template>
  <div>
    <div v-if="loading">
      <q-list>
        <transition
          v-for="i in 5"
          :key="i"
          appear
          enter-active-class="animated fadeIn"
          leave-active-class="animated fadeOut">
          <q-item class="q-pl-sm q-pr-sm" clickable v-ripple>
            <q-item-section avatar>
              <q-icon name="person"/>
            </q-item-section>
            <q-item-section>
              <q-item-label>
                <q-skeleton type="text" width="3em"/>
              </q-item-label>
              <q-item-label caption>
                <q-skeleton type="text" width="6em"/>
              </q-item-label>
            </q-item-section>
            <q-item-section side>
              <q-skeleton type="QBtn" size="1.5em"/>
            </q-item-section>
            <q-item-section side>
              <q-skeleton type="QBtn" size="1.5em"/>
            </q-item-section>
          </q-item>
        </transition>
      </q-list>
    </div>
    <div v-else>
      <div v-if="roles && roles.length > 0">
        <q-list>
          <transition
            v-for="role in roles"
            :key="role.rid"
            appear
            enter-active-class="animated fadeIn"
            leave-active-class="animated fadeOut">
            <q-item class="q-pl-sm q-pr-sm"
                    :ref="'role-' + role.rid"
                    :disable="updatingRoles.indexOf(role.rid) >= 0"
                    :clickable="editable" v-ripple="editable"
                    @click="()=>edit(role)">
              <q-item-section avatar>
                <q-icon name="person"/>
              </q-item-section>
              <q-item-section>
                <q-item-label>
                  {{ role.roleName && role.roleName.trim() ? role.roleName.trim() : "-" }}
                </q-item-label>
                <q-item-label caption>
                  {{
                    role.roleDescription && role.roleDescription.trim() ? role.roleDescription.trim() : "-"
                  }}
                </q-item-label>
              </q-item-section>

              <q-item-section no-wrap side v-if="editable">
                <q-btn flat dense icon="security" round
                       :disable="updatingRoles.indexOf(role.rid) >= 0"
                       @click.stop="()=>grant(role)"/>
              </q-item-section>
              <q-item-section no-wrap side v-if="removable" :style="editable?'padding-left: 0px':''">
                <q-btn flat dense icon="delete" round
                       :loading="updatingRoles.indexOf(role.rid) >= 0"
                       @click.stop="()=>remove(role)"/>
              </q-item-section>
            </q-item>
          </transition>
        </q-list>
      </div>
      <no-results v-else/>
    </div>
  </div>
</template>

<script>
import NoResults from "../common/NoResults";
import EditRole from "../dialogs/EditRole";

export default {
  name: "Roles",
  components: {EditRole, NoResults},
  props: {
    client: Object,
    removable: Boolean,
    editable: Boolean,
    currentUser: Object
  },
  data() {
    return {
      roles: [],
      loading: false,
      isEditing: false,
      updatingRoles: []
    }
  },
  methods: {
    load() {
      if (this.loading) return
      this.loading = true
      this.$rolesApi.getRoles(null, this.client.cid)
        .then(res => this.roles = res.data)
        .finally(() => this.loading = false)
    },
    edit(role) {
      if (this.isEditing)
        return
      this.isEditing = true
      this.$q.dialog({
        component: EditRole,
        parent: this,
        client: this.client,
        currentUser: this.currentUser,
        role: role,
        persistentOnBusying: false,
        onSave: (role) => {
          this.updatingRoles.push(role.rid)
        },
        onSaved: (role) => {
          this.updatingRoles.splice(this.updatingRoles.indexOf(role.roleName), 1)
        }
      })
        .onDismiss(() => this.isEditing = false)
    },
    remove(role) {
      if (this.updatingRoles.indexOf(role.rid) >= 0)
        return
      this.showDeleteDialog(this.$tt(this, "deleteRole") + " '" + role.roleName + "'",
        this.$tt(this, "deleteRoleMsg"),
        () => {
          this.updatingRoles.push(role.rid)
          return this.$rolesApi.deleteRoles([role.rid])
            .then(() => {
              this.roles.splice(this.roles.indexOf(role), 1)
            })
            .finally(() => this.updatingRoles.splice(this.updatingRoles.indexOf(role.rid), 1))
        })
    },
    grant(role) {
      console.log("grant", role)
    },
    add() {
      this.$q.dialog({
        component: EditRole,
        parent: this,
        client: this.client,
        currentUser: this.currentUser,
        persistentOnBusying: false,
        onSave: (role) => {
          role.rid = role.roleName
          this.roles.push(role)
          this.updatingRoles.push(role.rid)
        },
        onSaved: (role) => {
          this.updatingRoles.splice(this.updatingRoles.indexOf(role.roleName), 1)
          for (let i in this.roles)
            if (this.roles[i].roleName == role.roleName) {
              this.roles[i].rid = role.rid
              break;
            }
        }
      })
    },
    showDeleteDialog(title, msg, todo) {
      this.$q.dialog({
        title: title,
        message: msg,
        cancel: {
          color: "grey-7",
          flat: true
        },
        color: "negative",
        ok: {
          label: this.$t("delete")
        },
      }).onOk(() => {
        if (todo != null)
          todo().then(() => this.showDeleteSuccessMessage());
      })
    },
    showDeleteSuccessMessage() {
      this.$q.notify({
        message: this.$t("deleteSuccess"),
        type: 'positive'
      })
    }
  },
  mounted() {
    this.load()
  }
}
</script>

<style scoped>

</style>
