<script setup>
import { ref } from 'vue'
import Datepicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css';

defineEmits(['create'])
defineProps({
  categoryList:{
      type: Array,
      require: true
  },
  errorName:{
    type: Boolean,
    default:false
  },
  errorClinic:{
    type: Boolean,
    default:false
  },
  errorEmail:{
    type: Boolean,
    default:false
  },
  errorTime:{
    type: Boolean,
    default:false
  },
  errorFuture:{
    type: Boolean,
    default:false
  },
  mailVali:{
    type: Boolean,
    default:false
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
        <div class="form mx-5 mb-4 mt-3">
            <div class="mb-3">
                <label for="clinic" class="form-label">Clinic :</label>
                <select class="form-select style-form" style="width: 50%;" :class="{'border border-danger' : errorClinic}" v-model="newEvent.eventCategory">
                    <option disabled selected>Select Clinic Below</option>
                    <option v-for="(category, index) in categoryList" :key="index" :value="category">{{ category.eventCategoryName }}</option>
                </select>
                <div v-if="errorClinic" class="error">Please select a clinic.</div>
            </div>
            <div class="mb-3">
                <label for="name" class="form-label">Name :</label>
                <input class="form-control style-form" id="name" maxlength="100" v-model="newEvent.bookingName" :class="{'border border-danger' : errorName}">
                <div v-if="errorName" class="error"> Please enter name.</div>
            </div>
            <div class="mb-3">
                <label for="email" class="form-label">Email :</label>
                <input class="form-control style-form" id="email" maxlength="45" v-model="newEvent.bookingEmail" :class="{'border border-danger' : errorEmail || !mailVali}">
                <div v-if="errorEmail" class="error">Please enter Email.</div>
                <div v-if="!mailVali && !errorEmail" class="error">Validate your mail.</div>
            </div>
            <div class="mb-3">
                <label for="meeting-time" >Date - Time :</label><br>
                <!-- <input type="datetime-local" :min="new Date().toISOString().split('T')[0] + new Date().toISOString().slice(10,16)" id="meeting-time" name="meeting-time" class="date-form" v-model="newEvent.eventStartTime" :class="{'border border-danger' : errorTime || errorFuture}"> -->
                <Datepicker 
                    :minDate="new Date()" 
                    v-model="newEvent.eventStartTime" 
                    :class="{'border border-danger' : errorTime || errorFuture}"
                    class="datepicker"
                />
                <div v-if="errorTime" class="error">Please choose start time.</div>
                <div v-if="errorFuture && !errorTime" class="error">Can not choose past time.</div>
                
            </div>
            <div class="mb-3">
                <label for="meeting-time" >Durations (minutes) :</label><br>
                <input class="form-control style-form" style="margin-top: 8px;" type="text" disabled readonly :value="newEvent.eventCategory.eventDuration">
            </div>
            <div class="mb-3">
                <label for="note" class="form-label">Note :</label>
                <textarea class="form-control style-form" rows="3" maxlength="500" v-model="newEvent.eventNotes"></textarea>
            </div>
        </div>
        <div style="text-align: center;">
            <button type="button" class="btn btn-dark mx-auto" 
                @click="$emit('create',newEvent)">
                Add Event
            </button>
        </div>
        
    </div>
</template>
 
<style>
@import url('https://fonts.googleapis.com/css2?family=Inter&family=Noto+Sans+Thai&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Radio+Canada:wght@600&display=swap');

.error{
    color: red;
    font-size: 14px;
    margin-left: 25%;
}
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

.datepicker{
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