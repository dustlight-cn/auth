<template>
  <q-page padding>
    <div class="flex flex-center text-h5 gt-sm">
      {{$tt(this,"title")}}
    </div>
    <div class="flex flex-center text-grey">
      {{$tt(this,"subtitle")}}
    </div>
  </q-page>
</template>

<script>
  export default {
    name: "PageTemplate"
  }
</script>

<style scoped>

</style>
