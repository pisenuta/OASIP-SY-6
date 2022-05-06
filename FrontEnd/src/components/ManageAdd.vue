<script setup>
import { computed, onBeforeMount } from 'vue'

defineEmits(['create'])
defineProps({
  currEvent: {
    type: Object,
    default: {}
  },
  categoryList:{
      type: Array,
      require: true
  }
})

const events = computed(() => {
  return { 
      id: currEvent.id, 
      bookingName: currEvent.bookingName, 
      bookingEmail: currEvent.bookingEmail, 
      eventCategory:currEvent.eventCategory,
      eventNotes: currEvent.eventNotes, 
      eventStartTime: currEvent.eventStartTime
    //   eventDuration:currEvent.eventCategory.eventDuration
    }
})
</script>
<template>
<div class="body">
        <h3 class="mx-auto mt-5" style="font-size: 40px;font-weight: bolder;">Add Event</h3>
        <div class="form mx-5 mb-5 mt-5">
            <div class="mb-3">
                <label for="clinic" class="form-label">Clinic :</label>
                <select class="form-select style-form" style="width: 50%;">
                    <option selected>Select Clinic</option>
                    <option v-for="(category, index) in categoryList" :key="index" >{{ category.eventCategoryName }}</option>
                </select>
            </div>
            <div class="mb-3">
                <label for="name" class="form-label">Name :</label>
                <input class="form-control style-form" id="name">
            </div>
            <div class="mb-3">
                <label for="email" class="form-label">Email :</label>
                <input class="form-control style-form" id="email">
            </div>
            <div class="mb-3">
                <label for="meeting-time" >Date - Time :</label><br>
                <input type="datetime-local" id="meeting-time" name="meeting-time" class="date-form" >
            </div>
            <div class="mb-3" style="margin-left: 23%;">
                <p><b>Durations :</b><b>min.</b></p>
            </div>
            <div class="mb-3">
                <label for="meeting-time" >Durations :</label><br>
                <input class="form-control style-form" type="text" disabled readonly>
            </div>
            <div class="mb-3">
                <label for="note" class="form-label">Note :</label>
                <textarea class="form-control style-form" rows="3"  ></textarea>
            </div>
        </div>
        <div style="text-align: center;">
            <button type="button" class="btn btn-dark mx-auto" 
                @click="$emit('create')">
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
</style>