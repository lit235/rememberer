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
        <b-button variant="primary">Submit</b-button>
    </b-form>
</template>

<script lang="ts">
    import {
        Component,
        Prop,
        Vue
    } from "nuxt-property-decorator"

    @Component({name: "submit-record"})
    export default class SubmitRecord extends Vue {
        text: string = "";
    }
</script>

<style scoped>

</style>
