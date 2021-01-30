<template>
  <div>
    <edit-page v-slot="{wide}">
      NewUser<span>{{ wide }}</span>
    </edit-page>
  </div>
</template>

<script>
import EditPage from "../../components/EditPage";

export default {
  name: "NewUser",
  components: {EditPage}
}
</script>

<style scoped>

</style>
