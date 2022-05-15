<script setup>
  import {ref, onBeforeMount} from 'vue'
  import EventList from './EventList.vue'
  const events = ref([])

  const getEvents= async () =>{ 
    // const res = await fetch(`http://localhost:8080/api/events` ,{
    const res = await fetch(`http://10.4.56.123:8080/api/events/` ,{
    // const res = await fetch(`${import.meta.env.VITE_BASE_URL}events/` ,{
    method: "GET",
  });

      if(res.status === 200){
        events.value = await res.json()
      } else{
        console.log('can not');
      }
  }

  const removeEvent = async (removeEventId) =>{
      // const res = await fetch(`http://localhost:8080/api/events/${removeEventId}`,{method: 'DELETE'})
      const res = await fetch(`http://10.4.56.123:8080/api/events/${removeEventId}`,{method: 'DELETE'})
      // const res = await fetch(`${import.meta.env.VITE_BASE_URL}events/${removeEventId}`,{method: 'DELETE'})
      if(res.status === 200) {
        events.value = events.value.filter((event) => event.id !== removeEventId)
        console.log('deleted successfully')
      }
      else console.log('error, can not delete')

  }
  const editEvent = async (editEvent) => {
      const res = await fetch(`http://10.4.56.123:8080/api/events/${editEvent.id}`,{
    // const res = await fetch(`http://localhost:8080/api/events/${editEvent.id}`,{
    // const res = await fetch(`${import.meta.env.VITE_BASE_URL}/events/${editEvent.id}`,{
      method: 'PUT',
      headers:{
        'content-type': 'application/json'
      },
      body: JSON.stringify({
        id: editEvent.id,
            eventCategory: {
                id: editEvent.eventCategory.id
            },
            bookingName: editEvent.bookingName,
            bookingEmail: editEvent.bookingEmail,
            eventDuration: editEvent.eventCategory.eventDuration,

          eventStartTime: `${editEvent.eventStartTime}:00Z`,
          eventNotes: editEvent.eventNotes
      })
    })
    if(res.status === 200){
      console.log('edited successfully');
    } else {
      console.log('can not edit');
    }
  }

  onBeforeMount(async () =>{
    await getEvents();
  })

  const schedule = () =>{
    if(events.value.length == 0){
      return "No Scheduled Event"
    }
  }

  // const byDate = (a,b) => {
  //   return new Date(b.eventStartTime).valueOf() - new Date(a.eventStartTime).valueOf();
  // }

</script>
 
<template>
    <div class="body">
        <h3 class="mx-auto mt-5" style="font-size: 40px;font-weight: bolder;">Schedule</h3>
        <div v-if="events.length>8" class="scroll-down"></div>
        <h5 class="mt-5">{{schedule()}}</h5>
        <div v-if="events.length !== 0">
          <EventList :eventList="events" @delete="removeEvent" @edit="editEvent" @toEditingMode="ToEditingMode"/>
        </div>
        
    </div>
</template>
 
<style>
@import url('https://fonts.googleapis.com/css2?family=Inter&family=Noto+Sans+Thai&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Radio+Canada:wght@600&display=swap');
.body{
    font-family: 'Radio Canada','Noto Sans Thai';
}

h5,h3{
    text-align: center;
}
h5{
    color: #646464;
}

.scroll-down {
  height: 50px;
  width: 30px;
  border: 2px solid black;
  position: fixed;
  left: 50%;
  bottom: 20px;
  border-radius: 50px;
  cursor: pointer;
  transform: translate(2500%, 0);

}
.scroll-down::before,
.scroll-down::after {
  content: "";
  position: absolute;
  top: 20%;
  left: 50%;
  height: 10px;
  width: 10px;
  transform: translate(-50%, -100%) rotate(45deg);
  border: 2px solid black;
  border-top: transparent;
  border-left: transparent;
  animation: scroll-down 1s ease-in-out infinite;
}
.scroll-down::before {
  top: 30%;
  animation-delay: 0.3s;
  /* animation: scroll-down 1s ease-in-out infinite; */
}

@keyframes scroll-down {
  0% {
    /* top:20%; */
    opacity: 0;
  }
  30% {
    opacity: 1;
  }
  60% {
    opacity: 1;
  }
  100% {
    top: 90%;
    opacity: 0;
  }
}
</style>