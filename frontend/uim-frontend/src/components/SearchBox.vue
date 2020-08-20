<template>
  <q-input class="GNL__toolbar-input"  outlined dense v-model="search" color="bg-grey-7 "
           placeholder="Search for topics, locations & sources">
    <template v-slot:prepend>
      <q-icon v-if="search === ''" name="search"/>
      <q-icon v-else name="clear" class="cursor-pointer" @click="search = ''"/>
    </template>
    <template v-slot:append>
      <q-btn
        flat
        dense
        round
        aria-label="Menu"
        icon="arrow_drop_down"
      >
        <q-menu anchor="bottom right" self="top right">
          <div class="q-pa-md" style="width: 400px">
            <div class="text-body2 text-grey q-mb-md">
              Narrow your search results
            </div>

          </div>
        </q-menu>
      </q-btn>
    </template>
  </q-input>
</template>
<script>
  export default {
    name: "SearchBox",
    data() {
      return {
        search: ""
      }
    }
  }
</script>
