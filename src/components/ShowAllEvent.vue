<script setup>
    import {ref, onBeforeMount} from 'vue'
    import EventList from './EventList.vue'
    const events = ref([])

  // GET
  const getEvents= async () =>{ 
    //await คือ รอบรรทัดนั้นทำให้เสร็จก่อนแล้วจะทำบรรทัดต่อไป 
    const res = await fetch('http://localhost:5000/event') //ปกติไม่นิยมวางลิ้งค์
    if(res.status === 200){
      events.value = await res.json() //json() เป็น promise, get data and เปลี่ยนเป็น js object
      console.log(events.value)
    } else {
      console.log("error, can not get events")
    }
    
  }
  onBeforeMount(async () =>{
    await getEvents()
  })
</script>
 
<template>
    <div class="body">
        <h3 class="mx-auto mt-5" style="width: 200px">นัดหมายทั้งหมด</h3>
        <p v-if="events.values.length === 0">ไม่มีนัดหมายในขณะนี้</p>
        <div v-else>
            <EventList :eventList="events"/>
        </div>
    </div>
</template>
 
<style>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+Thai&display=swap');
.body{
    font-family: 'Noto Sans Thai', sans-serif;
}

</style>