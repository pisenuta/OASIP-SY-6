<script setup>
import { ref, onBeforeMount } from 'vue'
import ManageAdd from './ManageAdd.vue'

const categories = ref([])
const getEventCategory = async () => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}eventcategory`)
    // const res = await fetch('http://10.4.56.123:8080/api/eventcategory')
    // const res = await fetch('http://localhost:8080/api/eventcategory')
    if (res.status === 200) {
        categories.value = await res.json()
    }
}
onBeforeMount(async () => {
    await getEventCategory()
    await getEvents()
})
const events = ref([])

const getEvents = async () => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}events` ,{
    // const res = await fetch(`http://10.4.56.123:8080/api/events/`, {
    // const res = await fetch(`http://localhost:8080/api/events`, {
        method: "GET",
    });
    if (res.status === 200) {
        events.value = await res.json()
    } else {
        console.log('can not');
    }
}

const errorName = ref(false)
const errorClinic = ref(false)
const errorEmail = ref(false)
const errorTime = ref(false)
const mailVali = ref(true)

const createEvent = async (event) => {
    if(event.bookingName == null || event.bookingName == ''){
        errorName.value = true
    } else {
        errorName.value = false
    }

    if(event.bookingEmail == null || event.bookingEmail == ''){
        errorEmail.value = true
    } else {
        errorEmail.value = false
    }

    if(Object.keys(event.eventCategory).length === 0){
        errorClinic.value = true
    } else {
        errorClinic.value = false
    }

    if(event.eventStartTime === null || event.eventStartTime == ''){
        errorTime.value = true
    } else {
        errorTime.value = false
    }
    
    var emailValidate = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;

    if(event.bookingEmail.match(emailValidate)){
        mailVali.value = true
    } else {
        mailVali.value = false
        console.log('not validate');
    }
    if(errorName.value == true || errorEmail.value == true || errorClinic.value == true || errorTime.value == true || mailVali.value == false){
        return
    }
 
        // const res = await fetch(`http://10.4.56.123:8080/api/events/`, {
        const res = await fetch(`http://localhost:8080/api/events/`, {
    // const res = await fetch(`${import.meta.env.VITE_BASE_URL}events`, {
        method: 'POST',
        headers: { 'content-Type': 'application/json' },
        body: JSON.stringify({
            id: events.value.length + 1,
            eventCategory: {
                id: event.eventCategory.id
            },
            bookingName: event.bookingName,
            bookingEmail: event.bookingEmail,
            eventStartTime: `${event.eventStartTime}:00Z`,
            eventDuration: event.eventCategory.eventDuration,
            eventNotes: event.eventNotes
        })
    })
    if (res.status == 201 || res.status == 200) {
        const addedEvent = await res.json()
        events.value.push(addedEvent)
        console.log('added successfully');
        addAlert.value = true
    } else {
        console.log('error, can not add');
    }
    
       
}
const addAlert = ref(false)
const added = () => {
    addAlert.value = false
}

</script>
 
<template>
    <div class="body">
        <h3 class="mx-auto mt-5" style="font-size: 40px;font-weight: bolder;">Add Event</h3>
        <ManageAdd 
        :categoryList="categories" 
        :errorName="errorName" 
        :errorClinic="errorClinic"
        :errorEmail="errorEmail"
        :errorTime="errorTime"
        :mailVali="mailVali"
        @create="createEvent" 
        />
        <div class="container" v-if="addAlert === true">
            <div class="card" style="width: 23rem; height: 15rem;">
                <div class="card-body" style="margin-top: 10px;">
                    <img src="https://api.iconify.design/healthicons/yes-outline.svg?color=%23198754&width=90&height=90">
                    <p class="card-text" style="margin-top: 10px;">Added Event Successfully</p>
                    <router-link to="/show-all-events"><button type="button" class="btn btn-success" @click="added" style="width: 100px; margin-top: 5px;">OK</button></router-link>
                </div>
            </div>
        </div>
    </div>
</template>
 
<style>
@import url('https://fonts.googleapis.com/css2?family=Inter&family=Noto+Sans+Thai&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Radio+Canada:wght@600&display=swap');

.container {
    position: fixed;
    min-width: 100%;
    max-height: 100%;
    overflow-x: hidden;
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.5);
    background-repeat: repeat-x;
}

.card {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    border-radius: 10px;
    text-align: center;
    font-size: 18px; 
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
}

.body {
    font-family: 'Inter';
    font-size: 17px;
}

h3 {
    font-family: 'Radio Canada';
}

.date-form {
    height: 38px;
    width: 50%;
    margin-top: 10px;
    margin-left: 25%;
    border-radius: 5px;
    border-color: #ced4da;
}

.style-form {
    width: 50%;
    margin: auto;
}

label {
    margin-left: 23%;
    font-weight: bold;
}
</style>