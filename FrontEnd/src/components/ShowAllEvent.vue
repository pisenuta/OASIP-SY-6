<script setup>
  import {ref, onBeforeMount} from 'vue'
  import EventList from './EventList.vue'
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

  const removeEvent = async (removeEventId) =>{
    if (confirm(`Do you want to cancel this event ?`) == false){

    } else {
      // const res = await fetch(`http://localhost:8080/api/events/${removeEventId}`,{method: 'DELETE'})
      const res = await fetch(`http://localhost:5000/event/${removeEventId}`,{method: 'DELETE'})
      // const res = await fetch(`http://10.4.56.123:8080/api/events/${removeEventId}`,{method: 'DELETE'})
      if(res.status === 200) {
        events.value = events.value.filter((event) => /*event.bookingId*/ event.Id !== removeEventId)
        console.log('deleted successfullly')
        location.reload();
      }
      else console.log('error, can not delete')
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

</script>
 
<template>
    <div class="body">
        <h3 class="mx-auto mt-5" style="font-size: 40px;font-weight: bolder;">Schedule</h3>
        <h5 class="mt-5">{{schedule()}}</h5>
        <div v-if="events.length !== 0">
          <EventList :eventList="events" @deleteEvent="removeEvent" />
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
</style>