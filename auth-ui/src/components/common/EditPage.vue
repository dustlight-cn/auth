<template>
  <q-page padding>
    <!-- 宽窗口 -->
    <div v-if="$q.screen.gt.xs" style="max-width: 599px;margin: 0 auto;">
      <q-card class="q-pa-md shadow-0" bordered>
        <slot v-bind="{wide:true}"/>
      </q-card>
    </div>
    <!-- 窄窗口 -->
    <div v-else>
      <q-card class="q-pa-sm shadow-0">
        <slot v-bind="{wide:false}"/>
      </q-card>
    </div>
  </q-page>
</template>

<script>
export default {
  name: "EditPage"
}
</script>

<style scoped>

</style>
