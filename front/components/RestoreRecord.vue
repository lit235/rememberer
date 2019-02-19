<template>
    <b-form>
    <b-form-group id="codeGroup" label="Код записи" label-for="code">
        <b-form-input
                id="code"
                v-model="code"
                placeholder="Enter something"
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

    @Component({name: "restore-record"})
    export default class RestoreRecord extends Vue {
        code: string = "";
    }
</script>

<style scoped>

</style>
