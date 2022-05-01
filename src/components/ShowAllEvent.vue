<script setup>
  import {ref, onBeforeMount} from 'vue'
  import EventList from './EventList.vue'
  const events = ref([])
  const evevtCategory = ref([])

  const getEvents= async () =>{ 
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/events`, {
    method: "GET",
  });
      if(res.status === 200){
        events.value = await res.json()
      }
  }
  // const getEventCategory= async () =>{ 
  //   const res = await fetch('http://localhost:5000/eventCategory') 
  //     if(res.status === 200){
  //       evevtCategory.value = await res.json()
  //     }
  // }
  onBeforeMount(async () =>{
    await getEvents()
    // await getEventCategory()
  })

  const schedule = () =>{
    if(events.value.length == 0){
      return "ไม่มีนัดหมายในขณะนี้"
    }
  }

</script>
 
<template>
    <div class="body">
        <h3 class="mx-auto mt-5" style="width: 200px">นัดหมายทั้งหมด</h3>
        <h5 class="mt-5">{{schedule()}}</h5>
        <EventList :eventList="events"/>
    </div>
</template>
 
<style>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+Thai&display=swap');
.body{
    font-family: 'Noto Sans Thai', sans-serif;
}
h5,h3{
    text-align: center;
}
h5{
    color: #646464;
}
</style>