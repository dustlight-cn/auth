<template>
  <transition
    appear
    :enter-active-class="animation"
  >
    <q-btn
      flat
      rounded
      dense
      :icon="icon"
      :to="{name:'Index'}"
    />
  </transition>
</template>

<script>
export default {
  name: "Logo",
  props: {
    icon: {
      type: String,
      default() {
        return "img:/icons/favicon-128x128.png"
      }
    },
    animation: {
      type: String,
      default() {
        return "animated rotateIn";
      }
    }
  }
}
</script>

<style scoped>

</style>
