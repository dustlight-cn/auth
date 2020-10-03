<template>
  <q-page padding>
  </q-page>
</template>

<script>
  export default {
    name: "ChildrenTemplate",
    mounted() {
      this.$emit("setSubtitle", this.$tt(this, "title"))
    }
  }
</script>

<style scoped>

</style>
