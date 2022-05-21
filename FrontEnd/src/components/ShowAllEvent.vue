<script setup>
import { ref, onBeforeMount } from 'vue'
import EventList from './EventList.vue'
import moment from 'moment'
import Datepicker from '@vuepic/vue-datepicker'

const events = ref([])
const filterEvent = ref('All')
const filterStatus = ref('All')
const filterDate = ref()
const SortByCategory = async (id) => {
  let res
  if (filterEvent.value !== '' && filterEvent.value !== 'All') {
    console.log(filterEvent.value);
    res = await fetch(`http://localhost:8080/api/events/clinic?eventCategoryId=${id}`, { method: "GET", })
  }
  else {
    res = await fetch(`http://localhost:8080/api/events/`, { method: "GET", })
  }

  if (res.status === 200) {
    events.value = await res.json();
    events.value.sort(function (a, b) { return new Date(b.eventStartTime) - new Date(a.eventStartTime); });
  } else {
    console.log('can not');
  }
}

const SortByStatus = async () => {
  let res
  const day = moment().format().slice(0, 19) + 'Z'
  if (filterStatus.value == 'Past') {
    res = await fetch(`http://localhost:8080/api/events/schedule-past?DateTime=${day}`, { method: "GET", })
    if (res.status === 200) {
      events.value = await res.json();
      events.value.sort(function (a, b) { return new Date(b.eventStartTime) - new Date(a.eventStartTime); });
    } else {
      console.log('can not');
    }
  }
  else if (filterStatus.value == 'Upcoming') {
    res = await fetch(`http://localhost:8080/api/events/schedule-comingup?DateTime=${day}`, { method: "GET", })
    if (res.status === 200) {
      events.value = await res.json();
      events.value.sort(function (a, b) { return new Date(a.eventStartTime) - new Date(b.eventStartTime); });
    } else {
      console.log('can not');
    }
  } else {
    res = await fetch(`http://localhost:8080/api/events/`, { method: "GET", })
    if (res.status === 200) {
      events.value = await res.json();
      events.value.sort(function (a, b) { return new Date(b.eventStartTime) - new Date(a.eventStartTime); });
    } else {
      console.log('can not');
    }
  }
}

const SortByDate = async () => {
  let res
  const date = moment(filterDate).format().slice(0, 10)
  if (filterDate.value !== '') {
    res = await fetch(`http://localhost:8080/api/events/datetime?Date=${date}`, { method: "GET", })
    if (res.status === 200) {
      events.value = await res.json();
      events.value.sort(function (a, b) { return new Date(b.eventStartTime) - new Date(a.eventStartTime); });
    } else {
      console.log('can not');
    }
  } else if (filterDate.value === '') {
    res = await fetch(`http://localhost:8080/api/events/`, { method: "GET", })
    if (res.status === 200) {
      events.value = await res.json();
      events.value.sort(function (a, b) { return new Date(b.eventStartTime) - new Date(a.eventStartTime); });
    } else {
      console.log('can not');
    }
  }
}

const removeEvent = async (removeEventId) => {
  // const res = await fetch(`${import.meta.env.VITE_BASE_URL}events/${removeEventId}`,{method: 'DELETE'})
  // const res = await fetch(`http://10.4.56.123:8080/api/events/${removeEventId}`,{method: 'DELETE'})
  const res = await fetch(`http://localhost:8080/api/events/${removeEventId}`, { method: 'DELETE' })
  if (res.status === 200) {
    events.value = events.value.filter((event) => event.id !== removeEventId)
    console.log('deleted successfully')
  }
  else console.log('error, can not delete')
}

const overlap = ref(false)
const edited = ref(false)
const editEvent = async (editEvent) => {
  // const res = await fetch(`${import.meta.env.VITE_BASE_URL}/events/${editEvent.id}`,{
  // const res = await fetch(`http://10.4.56.123:8080/api/events/${editEvent.id}`,{
  const res = await fetch(`http://localhost:8080/api/events/${editEvent.id}`, {
    method: 'PUT',
    headers: {
      'content-type': 'application/json'
    },
    body: JSON.stringify({
      eventStartTime: editEvent.eventStartTime,
      eventNotes: editEvent.eventNotes.trim(),
      eventDuration: editEvent.eventDuration.trim(),
      eventCategory: editEvent.eventCategory
    })
  })
  if (res.status === 200) {
    edited.value = true
    console.log('edited successfully');
  } else {
    edited.value = false
    overlap.value = true
    console.log('can not edit');
  }
}

onBeforeMount(async () => {
  await SortByCategory();
  await SortByStatus();
  await SortByDate();
  await getEventCategory();

})

const schedule = () => {
  if (events.value.length == 0) {
    return "No Scheduled Event"
  }
}
const categories = ref([])
const getEventCategory = async () => {
  // const res = await fetch(`${import.meta.env.VITE_BASE_URL}eventcategory`)
  // const res = await fetch(`http://10.4.56.123:8080/api/eventcategory`)
  const res = await fetch(`http://localhost:8080/api/eventcategory/`)
  if (res.status === 200) {
    categories.value = await res.json()
  }
}

</script>
 
<template>
  <div class="body">
    <h3 class="mx-auto mt-5" style="font-size: 40px;font-weight: bolder;">Schedule</h3>
    <div v-if="events.length > 8" class="scroll-down"></div>
    <div style="display: flex">
      <select class="form-select filter-form  mt-4" v-model="filterEvent" style="display: block;justify-content: center;">
        <option selected>All</option>
        <option v-for="(category, index) in categories" :key="index" :value="category.id">{{ category.eventCategoryName
        }}
        </option>
      </select>
      <select class="form-select filter-form mt-4" style="width: 8rem;display: block;" v-model="filterStatus">
        <option selected>All</option>
        <option value="Past">Past</option>
        <option value="Upcoming">Upcoming</option>
      </select>
      <Datepicker :enableTimePicker="false" v-model="filterDate" class="datepicker" style="width: 12rem;display: block;" />
      <img src="https://api.iconify.design/fa6-solid/magnifying-glass.svg?color=%23212529" 
        @click="SortByCategory(filterEvent), SortByStatus(filterStatus), SortByDate(filterDate)" class="filter-btn">
    </div>

    <h5 class="mt-4">{{ schedule() }}</h5>
    <div v-if="events.length !== 0">
      <EventList :eventList="events" :overlap="overlap" :edited="edited" @delete="removeEvent" @edit="editEvent" />
    </div>

  </div>
</template>
 
<style>
@import url('https://fonts.googleapis.com/css2?family=Inter&family=Noto+Sans+Thai&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Radio+Canada:wght@600&display=swap');

.body {
  font-family: 'Radio Canada', 'Noto Sans Thai';
}

.filter-btn {
  cursor: pointer;
  width: 20px;
}

.filter-form {
  width: 16rem;
  margin: auto;
}

h5,
h3 {
  text-align: center;
}

h5 {
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