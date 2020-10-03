<template>
  <q-page padding>
    <div class="flex flex-center text-h5 gt-sm">
      {{$tt(this,"title")}}
    </div>
    <div class="flex flex-center text-grey">
      {{$tt(this,"subtitle")}}
    </div>
    <q-card bordered class="shadow-0 q-pa-md">

    </q-card>
  </q-page>
</template>

<script>
  export default {
    name: "PersonalInfo",
    mounted() {
      this.$api.getSession()
        .then((user) => console.log(user))
        .catch(e => console.error(e))
    }
  }
</script>

<style scoped>

</style>
