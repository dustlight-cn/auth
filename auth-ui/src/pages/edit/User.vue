<template>
  <div>
    <edit-page v-slot="{wide}">
      {{ wide }}
    </edit-page>
  </div>
</template>

<script>
import EditPage from "../../components/EditPage";

export default {
  name: "User",
  components: {EditPage}
}
</script>

<style scoped>

</style>
