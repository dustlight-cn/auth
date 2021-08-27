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
              <q-icon name="security"/>
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
          </q-item>
        </transition>
      </q-list>
    </div>
    <div v-else>
      <div v-if="authorities && authorities.length > 0">
        <q-list>
          <transition
            v-for="authority in authorities"
            :key="authority.aid"
            appear
            enter-active-class="animated fadeIn"
            leave-active-class="animated fadeOut">
            <q-item class="q-pl-sm q-pr-sm" :clickable="editable" v-ripple="editable"
                    :disable="updatingAuthorities.indexOf(authority.aid) >= 0"
                    @click="()=>edit(authority)">
              <q-item-section avatar>
                <q-icon name="security"/>
              </q-item-section>
              <q-item-section>
                <q-item-label>
                  {{ authority.authorityName && authority.authorityName.trim() ? authority.authorityName.trim() : "-" }}
                </q-item-label>
                <q-item-label caption>
                  {{
                    authority.authorityDescription && authority.authorityDescription.trim() ? authority.authorityDescription.trim() : "-"
                  }}
                </q-item-label>
              </q-item-section>

              <q-item-section no-wrap side v-if="removable" :style="editable?'padding-left: 0px':''">
                <q-btn flat dense icon="delete" round
                       :loading="updatingAuthorities.indexOf(authority.aid) >= 0"
                       @click.stop="()=>remove(authority)"/>
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
import EditAuthority from "../dialogs/EditAuthority";
import EditRole from "../dialogs/EditRole";

export default {
  name: "Authorities",
  components: {NoResults},
  props: {
    client: Object,
    removable: Boolean,
    editable: Boolean,
    currentUser: Object
  },
  data() {
    return {
      authorities: [],
      loading: false,
      isEditing: false,
      updatingAuthorities: []
    }
  },
  methods: {
    load() {
      if (this.loading) return
      this.loading = true
      this.$authoritiesApi.getAuthorities(null, this.client.cid)
        .then(res => this.authorities = res.data)
        .finally(() => this.loading = false)
    },
    edit(authority) {
      if (this.isEditing)
        return
      this.isEditing = true
      this.$q.dialog({
        component: EditAuthority,
        parent: this,
        client: this.client,
        currentUser: this.currentUser,
        authority: authority,
        persistentOnBusying: false,
        onSave: (authority) => {
          this.updatingAuthorities.push(authority.aid)
        },
        onSaved: (authority) => {
          this.updatingAuthorities.splice(this.updatingAuthorities.indexOf(authority.aid), 1)
        }
      })
        .onDismiss(() => this.isEditing = false)
    },
    remove(authority) {
      if (this.updatingAuthorities.indexOf(authority.aid) >= 0)
        return
      this.showDeleteDialog(this.$tt(this, "deleteAuthority") + " '" + authority.authorityName + "'",
        this.$tt(this, "deleteAuthorityMsg"),
        () => {
          this.updatingAuthorities.push(authority.aid)
          return this.$authoritiesApi.deleteAuthorities([authority.aid], this.client.cid)
            .then(() => {
              this.authorities.splice(this.authorities.indexOf(authority), 1)
            })
            .finally(() => this.updatingAuthorities.splice(this.updatingAuthorities.indexOf(authority.aid), 1))
        })
    },
    add() {
      this.$q.dialog({
        component: EditAuthority,
        parent: this,
        client: this.client,
        currentUser: this.currentUser,
        persistentOnBusying: false,
        onSave: (authority) => {
          authority.aid = authority.authorityName + "__TEMPORARY_ID"
          this.authorities.push(authority)
          this.updatingAuthorities.push(authority.aid)
        },
        onSaved: (authority) => {
          this.updatingAuthorities.splice(this.updatingAuthorities.indexOf(authority.authorityName + "__TEMPORARY_ID"), 1)
          for (let i in this.authorities)
            if (this.authorities[i].authorityName == authority.authorityName) {
              this.authorities[i].aid = authority.aid
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
