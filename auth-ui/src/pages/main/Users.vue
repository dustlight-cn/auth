<template>
  <q-page padding>
    <require-authorization v-slot="{user}">

      <q-card bordered flat class="q-pa-md q-mt-md">
        <div>
          <q-item>
            <q-item-section class="text-h6 q-pb-md">
              {{ $tt($options, "title") }}
              <div class="text-caption">
                {{ $tt($options, "description") }}
              </div>
            </q-item-section>
            <q-item-section side>
              <q-btn color="accent" :to="{name:'new-user'}" :label="$t('create')"/>
            </q-item-section>
          </q-item>
        </div>
        <div class="q-pa-sm" align="center" style="max-width: 400px; margin: 0 auto">
          <q-input
            v-model="keywords"
            debounce="500"
            filled
            clearable
            dense
            color="accent"
            :loading="searching"
            :placeholder="$t('search')"
          >
            <template v-slot:prepend>
              <q-icon name="search"/>
            </template>
          </q-input>
        </div>
      </q-card>
    </require-authorization>
  </q-page>
</template>

<script>
import RequireAuthorization from "../../components/RequireAuthorization";

export default {
  name: "Users",
  components: {RequireAuthorization},
  data() {
    return {
      keywords: this.$route.query.q || "",
      searching: false
    }
  },
  watch: {
    keywords() {
      this.$router.replace({query: {q: this.keywords}})
    }
  }
}
</script>

<style scoped>

</style>
