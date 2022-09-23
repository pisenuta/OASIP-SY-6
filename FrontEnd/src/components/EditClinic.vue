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
    <p class="edit-clinic-label">Clinic :</p>
    <input class="form-control edit-clinic-input" maxlength="100" v-model="newClinic.eventCategoryName"
        :class="{ 'border border-danger': errorClinicName || notUnique }">
    <p class="error-edit-clinic" v-if="errorClinicName === true">Enter Clinic name.</p>
    <p class="error-edit-clinic" v-if="notUnique === true">Clinic Name is already taken.</p>
    <p class="edit-clinic-label">Duration (minutes) :</p>
    <input type="number" min="1" max="480" class="form-control edit-clinic-input" v-model="newClinic.eventDuration"
        :class="{ 'border border-danger': errorDuration || wrongDuration }">
    <p class="error-edit-clinic" v-if="errorDuration === true">Enter Duration.</p>
    <p class="error-edit-clinic" v-if="wrongDuration === true">Duration must between 1 and 480.</p>
    <p class="edit-clinic-label">Description :</p>
    <textarea class="form-control edit-clinic-input" rows="3" maxlength="500"
        style="height: 7.5vw;"
        v-model="newClinic.eventCategoryDescription"></textarea>
    <button type="button" class="btn btn-success btn-grad-ok" style="margin-right: 2vw; margin-top: 0.2vw;margin-bottom: 1.2vw;"
        @click="$emit('updateClinic', newClinic)">Save</button>
</template>
 
<style>
.edit-clinic-label{
    text-align: left;
    font-size: 0.9vw;
    font-weight: bold;
    margin-left: 8%;
}
.edit-clinic-input{
    width: 28vw;
    height: 2vw;
    margin-right: auto;
    margin-left: auto;
    font-size: 0.9vw;
    margin-bottom: 1.2vw;
}
.error-edit-clinic{
    color: red;
    text-align: left;
    font-size: 0.7vw;
    margin-top: -0.8vw;
    margin-left: 9%;
}
.maxClinic {
    width: 8vw;
    text-align: right;
    font-size: 0.7vw;
    color: #888888;
    font-weight: 100;
}
.edit-clinic-line{
    margin-left: 8%;
    margin-bottom: 0.5vw;
}
</style>
