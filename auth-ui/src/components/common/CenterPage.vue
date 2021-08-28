<template>
  <q-page :class="$q.screen.gt.xs?'flex flex-center':''" >
    <div class="full-width" :style="$q.screen.gt.xs?'max-width: 599px':''">
      <slot/>
    </div>
  </q-page>
</template>

<script>
export default {
  name: "CenterPage"
}
</script>

<style scoped>

</style>
