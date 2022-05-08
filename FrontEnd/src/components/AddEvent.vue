<script setup>
import { ref, onBeforeMount, computed } from 'vue'
import ManageAdd from './ManageAdd.vue'

const categories = ref([])
const getEventCategory = async () => {
    const res = await fetch('http://10.4.56.123:8080/api/eventcategory')
    // const res = await fetch('http://localhost:8080/api/eventcategory')
    // const res = await fetch('http://localhost:5000/eventCategory')
    if (res.status === 200) {
        categories.value = await res.json()
    }
}
onBeforeMount(async () => {
    await getEventCategory()
    await getEvents()
})
const events = ref([])

const getEvents= async () =>{ 
    /*const res = await fetch(`${import.meta.env.VITE_BASE_URL}/events`, {
    method: "GET",
  });*/
    const res = await fetch(`http://10.4.56.123:8080/api/events/` ,{
    // const res = await fetch(`http://localhost:8080/api/events`, {
    method: "GET",
  });
  //     const res = await fetch(`http://localhost:5000/event`, {
  //   method: "GET",
  // });
      if(res.status === 200){
        events.value = await res.json()
      } else{
        console.log('can not');
      }
  }

const createEvent = async (event) => {
    // const res = await fetch(`http://localhost:8080/api/events`, {
    const res = await fetch(`http://10.4.56.123:8080/api/events`, {
        method: 'POST',
        headers: { 'content-Type': 'application/json' },
        body: JSON.stringify({
            id: events.value.length+1, 
            eventCategory:{
                id: event.eventCategory.id
            },
            bookingName:event.bookingName,
            bookingEmail: event.bookingEmail,
            eventStartTime: `${event.eventStartTime}:00Z`,
            eventDuration:event.eventCategory.eventDuration,
            eventNotes: event.eventNotes 
        })
    })
    if (res.status == 201 || res.status == 200) {
        const addedEvent = await res.json()
        events.value.push(addedEvent)
        console.log('added successfully');
        alert("Added Successfully")
        location.reload()

    } else {
        console.log('error, can not add');
    }
}

</script>
 
<template>
    <div class="body">
        <h3 class="mx-auto mt-5" style="font-size: 40px;font-weight: bolder;">Add Event</h3>    
        <ManageAdd :categoryList="categories" @create="createEvent"/>  
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