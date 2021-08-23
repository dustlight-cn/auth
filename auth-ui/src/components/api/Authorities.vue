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

export default {
  name: "Authorities",
  components: {NoResults},
  props: {
    client: Object,
    removable: Boolean,
    editable: Boolean
  },
  data() {
    return {
      authorities: [],
      loading: false
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
      console.log("edit", authority)
    },
    remove(authority) {
      console.log("remove", authority)
    },
    add(){
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
