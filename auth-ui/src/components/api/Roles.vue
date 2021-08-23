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
                       @click.st.stop="()=>grant(role)"/>
              </q-item-section>
              <q-item-section no-wrap side v-if="removable" :style="editable?'padding-left: 0px':''">
                <q-btn flat dense icon="delete" round
                       @click.st.stop="()=>remove(role)"/>
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
    editable: Boolean
  },
  data() {
    return {
      roles: [],
      loading: false
    }
  },
  methods: {
    load() {
      if (this.loading) return
      this.loading = true
      this.$rolesApi.getClientRoles(this.client.cid)
        .then(res => this.roles = res.data)
        .finally(() => this.loading = false)
    },
    edit(role) {
      console.log("edit", role)
      this.$q.dialog({
        component: EditRole,
        parent: this
      })
    },
    remove(role) {
      console.log("remove", role)
    },
    grant(role) {
      console.log("grant", role)
    },
    add() {
      console.log("add")
    }
  },
  mounted() {
    this.load()
  }
}
</script>

<style scoped>

</style>
