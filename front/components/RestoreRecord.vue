<template>
    <b-form>
    <b-form-group id="codeGroup" label="Код записи" label-for="code">
        <b-form-input
                id="code"
                v-model="code"
                placeholder="Enter something"
        />
    </b-form-group>
        <b-button @click="submit" variant="primary">Submit</b-button>
    </b-form>
</template>

<script lang="ts">
    import {
        Component,
        Prop,
        Vue
    } from "nuxt-property-decorator"
    import {ApiResponse} from "~/types/api";
    import {recordsApi} from "~/services/api";

    @Component({name: "restore-record"})
    export default class RestoreRecord extends Vue {
        code: string = "";

        async submit() {
            const respose: ApiResponse = await recordsApi.restoreRecord(this.code);
            console.log(respose);
        }
    }
</script>

<style scoped>

</style>
