<script setup>
import { ref } from 'vue'
import useVuelidate from '@vuelidate/core'
import { required } from '@vuelidate/validators'

defineEmits(['create'])
defineProps({
  categoryList:{
      type: Array,
      require: true
  }
})

const newEvent = ref({
    bookingName: "",
    bookingEmail: "",
    eventCategory: {},
    eventNotes: "",
    eventDuration: "",
    eventStartTime:""
})


</script>
<template>
<div class="body">
        <div class="form mx-5 mb-5 mt-5 needs-validation">
            <div class="mb-3">
                <label for="clinic" class="form-label">Clinic :</label>
                <select class="form-select style-form" style="width: 50%;" v-model="newEvent.eventCategory">
                    <option disabled selected>Select Clinic Below</option>
                    <option v-for="(category, index) in categoryList" :key="index" :value="category">{{ category.eventCategoryName }}</option>
                </select>
            </div>
            <div class="mb-3">
                <label for="name" class="form-label">Name :</label>
                <input class="form-control style-form" id="name" v-model="newEvent.bookingName">
            </div>
            <div class="mb-3">
                <label for="email" class="form-label">Email :</label>
                <input class="form-control style-form" id="email" v-model="newEvent.bookingEmail">
            </div>
            <div class="mb-3">
                <label for="meeting-time" >Date - Time :</label><br>
                <input type="datetime-local" id="meeting-time" name="meeting-time" class="date-form" v-model="newEvent.eventStartTime">
            </div>
            <div class="mb-3">
                <label for="meeting-time" >Durations :</label><br>
                <input class="form-control style-form" style="margin-top: 10px;" type="text" disabled readonly :value="newEvent.eventCategory.eventDuration">
            </div>
            <div class="mb-3">
                <label for="note" class="form-label">Note :</label>
                <textarea class="form-control style-form" rows="3" v-model="newEvent.eventNotes"></textarea>
            </div>
        </div>
        <div style="text-align: center;">
            <button type="reset" class="btn btn-dark mx-auto" 
                @click="$emit('create',newEvent)">
                Add Event
            </button>
        </div>
        
    </div>
</template>
 
<style>
@import url('https://fonts.googleapis.com/css2?family=Inter&family=Noto+Sans+Thai&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Radio+Canada:wght@600&display=swap');

.body {
    font-family: 'Inter';
    font-size: 17px;
}

h3 {
    font-family: 'Radio Canada';
}

.date-form{
    height: 38px; 
    width: 50%; 
    margin-top: 10px;
    margin-left: 25%;
    border-radius: 5px;
    border-color: #ced4da;
}

.style-form{
    width: 50%; 
    margin: auto;
}
label{
    margin-left: 23%;
    font-weight: bold;
}
input,textarea{
    font-family: 'Inter','Noto Sans Thai';
}
</style>