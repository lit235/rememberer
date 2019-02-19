<template>
    <b-form>
    <b-form-group id="textGroup" label="Текст записи" label-for="text">
    <b-form-textarea
                id="text"
                v-model="text"
                placeholder="Enter something"
                rows="3"
                max-rows="6"
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
    } from "nuxt-property-decorator";

    import {recordsApi} from "~/services/api";
    import {ApiResponse} from "~/types/api";

    @Component({name: "submit-record"})
    export default class SubmitRecord extends Vue {
        text: string = "";

        async submit() {
            const respose: ApiResponse = await recordsApi.storeRecord({text: this.text});
            console.log(respose);
        }
    }
</script>

<style scoped>

</style>
