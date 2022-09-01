<script setup>//Test
import { computed } from 'vue'
defineEmits(['updateClinic'])
const props = defineProps({
    currentClinic: {
        type: Object,
        default: {}
    },
    errorClinicName:{
        type: Boolean,
        default:false
    },
    notUnique:{
        type: Boolean,
        default:false
    },
    errorDuration:{
        type: Boolean,
        default:false
    },
    wrongDuration:{
        type: Boolean,
        default:false
    }
})

const newClinic = computed(() => {
    return {
        id: props.currentClinic.id,
        eventCategoryName: props.currentClinic.eventCategoryName,
        eventCategoryDescription: props.currentClinic.eventCategoryDescription,
        eventDuration: props.currentClinic.eventDuration
    }
})
</script>
 
<template>
    <p class="label-clinic">Clinic :</p>
    <input class="form-control clinic-form mb-3" maxlength="100" v-model="newClinic.eventCategoryName"
        :class="{ 'border border-danger': errorClinicName || notUnique }">
    <p class="error-clinic" v-if="errorClinicName === true">Enter Clinic name.</p>
    <p class="error-clinic" v-if="notUnique === true">Category Name is not unique.</p>
    <p class="label-clinic">Duration (minutes) :</p>
    <input type="number" min="1" max="480" class="form-control clinic-form mb-3" v-model="newClinic.eventDuration"
        :class="{ 'border border-danger': errorDuration || wrongDuration }">
    <p class="error-clinic" v-if="errorDuration === true">Enter Duration.</p>
    <p class="error-clinic" v-if="wrongDuration === true">Duration must between 1 and 480.</p>
    <p class="label-clinic">Description :</p>
    <textarea class="form-control clinic-form mb-4" rows="3" maxlength="500"
        style="height: 7.5vw;"
        v-model="newClinic.eventCategoryDescription"></textarea>
    <button type="button" class="btn btn-success btn-grad-ok" style="margin-right: 40px;"
        @click="$emit('updateClinic', newClinic)">Save</button>
</template>
 
<style>
.label-clinic{
    text-align: left;
    margin-left: 12%;
    margin-bottom: 0.5vw;
    font-size: 0.9vw;
}
.error-clinic{
    color: red;
    text-align: left;
    font-size: 0.7vw;
    margin-top: -10px;
    margin-left: 15.5%;
}
</style>
