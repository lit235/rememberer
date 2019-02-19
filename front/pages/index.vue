<template>
  <b-container fluid>
    <b-row>
      <b-col></b-col>
      <b-col>
        <b-card no-body>
          <b-tabs card>
            <b-tab title="Tab 1" active>
              <submit-record></submit-record>
            </b-tab>
            <b-tab title="Tab 2">
              <restore-record></restore-record>
            </b-tab>
          </b-tabs>
        </b-card>
      </b-col>
      <b-col></b-col>
    </b-row>
  </b-container>
</template>

<script lang="ts">
import {
  Component,
  Vue
} from "nuxt-property-decorator"
import SubmitRecord from "~/components/SubmitRecord.vue";
import RestoreRecord from "~/components/RestoreRecord.vue";

@Component({
  components: {
    RestoreRecord,
    SubmitRecord
  }
})
export default class extends Vue {
}
</script>

<style scoped>

</style>
