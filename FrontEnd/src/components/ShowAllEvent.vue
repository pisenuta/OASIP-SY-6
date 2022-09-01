<script setup>
import { ref, onBeforeMount } from 'vue'
import EventList from './EventList.vue'
import moment from 'moment'
import Datepicker from '@vuepic/vue-datepicker'

const events = ref([])
const filterEvent = ref()
const filterStatus = ref()
const filterDate = ref()

const SortByCategory = async (id) => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}events/clinic?eventCategoryId=${id}`, { method: "GET" })
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
    res = await fetch(`${import.meta.env.VITE_BASE_URL}events/schedule-past?DateTime=${day}`, { method: "GET", })
    if (res.status === 200) {
      events.value = await res.json();
      events.value.sort(function (a, b) { return new Date(b.eventStartTime) - new Date(a.eventStartTime); });
    } else {
      console.log('can not');
    }
  }
  else if (filterStatus.value == 'Upcoming') {
    res = await fetch(`${import.meta.env.VITE_BASE_URL}events/schedule-comingup?DateTime=${day}`, { method: "GET", })
    if (res.status === 200) {
      events.value = await res.json();
      //Ascending
      events.value.sort(function (a, b) { return new Date(a.eventStartTime) - new Date(b.eventStartTime); });
    } else {
      console.log('can not');
    }
  }
}

const SortByDate = async (selectDate) => {
  let res
  const date = moment(selectDate).format().slice(0, 10)
  if (filterDate.value !== '') {
    res = await fetch(`${import.meta.env.VITE_BASE_URL}events/datetime?Date=${date}`, { method: "GET", })
  }

  if (res.status === 200) {
    events.value = await res.json();
    events.value.sort(function (a, b) { return new Date(a.eventStartTime) - new Date(b.eventStartTime); });
  } else {
    console.log('can not');
  }
}

const removeEvent = async (removeEventId) => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}events/${removeEventId}`, { method: 'DELETE' })
  if (res.status === 200) {
    events.value = events.value.filter((event) => event.id !== removeEventId)
    console.log('deleted successfully')
  }
  else console.log('error, can not delete')
}

const overlap = ref(false)
const edited = ref(false)
const errorPast = ref(false)
const editEvent = async (editEvent) => {
  if (moment(editEvent.eventStartTime).isAfter(moment(new Date()))) {
    errorPast.value = false
  } else {
    errorPast.value = true
  }
  if(errorPast.value == true){
        return
  }
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}events/${editEvent.id}`, {
    method: 'PUT',
    headers: {
      'content-type': 'application/json'
    },
    body: JSON.stringify({
      eventStartTime: editEvent.eventStartTime,
      eventNotes: editEvent.eventNotes.trim(),
      eventDuration: editEvent.eventDuration,
      eventCategory: editEvent.eventCategory
    })
  })
  if (res.status === 200) {
    edited.value = true
    overlap.value = false
    console.log('edited successfully');
  } else {
    edited.value = false
    overlap.value = true
  }
}

const getAllEvent = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}events`, {
    method: "GET",
  });
  if (res.status === 200) {
    events.value = await res.json();
    events.value.sort(function (a, b) { return new Date(b.eventStartTime) - new Date(a.eventStartTime); });
  } else {
    console.log('can not');
  }
}

onBeforeMount(async () => {
  await getAllEvent();
  await getEventCategory();
})

const schedule = () => {
  if (events.value.length == 0) {
    return "No Scheduled Event"
  }
}
const categories = ref([])
const getEventCategory = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}categories`)
  if (res.status === 200) {
    categories.value = await res.json()
  }
}
const cancelEdit = () => {
  if (overlap.value == true || errorPast.value == true) {
    location.reload()
  }
}
const useSortCategory = ref(false)
const useSortStatus = ref(false)
const useSortDate = ref(false)
</script>
 
<template>
  <div class="body">
    <h3 class="mx-auto" style="font-size: 2.1vw;margin-top: 2.5vw;font-weight: bolder;">Reservation</h3>
    <div v-if="events.length > 8" class="scroll-down"></div>

    <div style="justify-content: space-around;display: flex;margin-bottom: 40px;margin-top: 1vw;">
      <button type="button" class="btn btn-dark sortBtn"
        v-on:click="useSortCategory = true, useSortStatus = false, useSortDate = false">Sort By Category</button>
      <div v-if="useSortCategory == true">
        <form class="form-inline">
          <select class="form-select filter-form" v-model="filterEvent">
            <option v-for="(category, index) in categories" :key="index" :value="category.id">{{
                category.eventCategoryName
            }} </option>
          </select>
          <img src="https://api.iconify.design/fa6-solid/magnifying-glass.svg?color=%23212529"
            @click="SortByCategory(filterEvent)" class="filter-btn">
        </form>
      </div>

      <button type="button" class="btn btn-dark sortBtn"
        v-on:click="useSortCategory = false, useSortStatus = true, useSortDate = false">Sort By Status</button>
      <div v-if="useSortStatus == true">
        <form class="form-inline">
          <select class="form-select filter-form" style="width: 8rem;" v-model="filterStatus">
            <option value="Past">Past</option>
            <option value="Upcoming">Upcoming</option>
          </select>
          <img src="https://api.iconify.design/fa6-solid/magnifying-glass.svg?color=%23212529"
            @click="SortByStatus(filterStatus)" class="filter-btn">
        </form>
      </div>

      <button type="button" class="btn btn-dark sortBtn"
        v-on:click="useSortCategory = false, useSortStatus = false, useSortDate = true">Sort By Date</button>
      <div v-if="useSortDate == true">
        <form class="form-inline">
          <Datepicker :enableTimePicker="false" v-model="filterDate" class="datepicker"
            style="width: 12rem; margin-top: 0px;" />
          <img src="https://api.iconify.design/fa6-solid/magnifying-glass.svg?color=%23212529"
            @click="SortByDate(filterDate)" class="filter-btn" style="margin-top: -40px;">
        </form>
      </div>

      <button type="button" class="btn btn-dark all-btn" @click="getAllEvent()"
        v-on:click="useSortCategory = false, useSortStatus = false, useSortDate = false">All</button>
    </div>

    <h5 class="mx-auto Noschedule" >{{ schedule() }}</h5>
    <div v-if="events.length !== 0">
      <EventList :eventList="events" :overlap="overlap" :edited="edited" :errorPast="errorPast" @delete="removeEvent" @edit="editEvent"
        @cancelEdit="cancelEdit" />
    </div>

  </div>
</template>
 
<style>
@import url('https://fonts.googleapis.com/css2?family=Inter&family=Noto+Sans+Thai&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Radio+Canada:wght@600&display=swap');

.body {
  font-family: 'Radio Canada', 'Noto Sans Thai';
}
.Noschedule{
  font-size: 1.1vw;
  color: #646464;
  position: absolute;
  top: 50%;
  left: 50%;
  -webkit-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
}
.all-btn {
  background-color: #e74694;
  border-color: #e74694;
  color: black;
  padding-left: 20px;
  padding-right: 20px;
  font-size: 0.9vw;
}
.sortBtn{
  font-size: 0.9vw;
}
.form-inline {
  display: flex;
  flex-flow: row wrap;
  align-items: center;
  height: 1vw;
}

.filter-btn {
  cursor: pointer;
  width: 1.3vw;
  margin-left: 15px;
  
}

.filter-form {
  width: 15vw;
  font-size: 0.9vw;
}

h5,
h3 {
  text-align: center;
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