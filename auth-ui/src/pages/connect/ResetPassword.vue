<template>
  <center-page>
    <q-card :flat="!$q.screen.gt.xs">
      <q-card-section>
        <div class="text-h5 ">
          <q-btn dense flat round icon="arrow_back" @click="()=>$router.back()"/>
          <span>{{ $tt(this, "title") }}</span>
        </div>
      </q-card-section>
      <q-card-section>
        <q-list separator>
          <q-item clickable v-ripper :to="{name:'reset-password-phone'}">
            <q-item-section avatar>
              <q-icon name="phone"/>
            </q-item-section>
            <q-item-section>
              <q-item-label>
                {{ $tt(this, 'byPhone') }}
              </q-item-label>
            </q-item-section>
            <q-item-section side>
              <q-icon name="keyboard_arrow_right"/>
            </q-item-section>
          </q-item>
          <q-item clickable v-ripper :to="{name:'reset-password-email'}">
            <q-item-section avatar>
              <q-icon name="email"/>
            </q-item-section>
            <q-item-section>
              <q-item-label>
                {{ $tt(this, 'byEmail') }}
              </q-item-label>
            </q-item-section>
            <q-item-section side>
              <q-icon name="keyboard_arrow_right"/>
            </q-item-section>
          </q-item>
        </q-list>
      </q-card-section>
      <q-card-section/>
    </q-card>
  </center-page>
</template>

<script>
import ResetPasswordByEmail from "./ResetPasswordByEmail";
import CenterPage from "../../components/common/CenterPage";

export default {
  name: "ResetPassword",
  components: {CenterPage, ResetPasswordByEmail},
}
</script>

<style scoped>

</style>
