<script setup>
import { ref, onBeforeMount } from 'vue'
import EventList from './EventList.vue'
import moment from 'moment'
import Datepicker from '@vuepic/vue-datepicker'

const newAccess = ref()
const token = localStorage.getItem("token")
const refreshToken = localStorage.getItem("refreshToken");

const RefreshToken = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}refresh-token`,{
      method: 'get',
      headers: {
        Authorization: `Bearer ${refreshToken}`
      }
    }
  );
  if (res.status === 200) {
    newAccess.value = await res.json()
    refresh()
    getAllEvent();
    getEventCategory();
    countPastApp();
    countUpApp();

  } else if (res.status === 401 || res.status === 403){
    localStorage.clear()
    window.location.href = "/sy6"
    console.log("plz log out");
  }
};

const refresh = () => {
  token = localStorage.setItem('token',`${newAccess.value.access_token}`)
}

const events = ref([])
const filterEvent = ref()
const filterStatus = ref()
const filterDate = ref()

const SortByCategory = async (id) => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}events/clinic?eventCategoryId=${id}`, { 
    method: "GET" ,
    headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`
    }
  })
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
    res = await fetch(`${import.meta.env.VITE_BASE_URL}events/schedule-past?DateTime=${day}`, { 
      method: "GET", 
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`
      }
  })
    if (res.status === 200) {
      events.value = await res.json();
      events.value.sort(function (a, b) { return new Date(b.eventStartTime) - new Date(a.eventStartTime); });
    } else {
      console.log('can not');
    }
  }
  else if (filterStatus.value == 'Upcoming') {
    res = await fetch(`${import.meta.env.VITE_BASE_URL}events/schedule-comingup?DateTime=${day}`, { 
      method: "GET", 
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`
      }
    })
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
  const date = moment(selectDate).format().slice(0, 10)

  if (filterDate.value !== '') {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}events/datetime?Date=${date}`, { 
      method: "GET", 
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      }
    })
    if (res.status === 200) {
    events.value = await res.json();
    events.value.sort(function (a, b) { return new Date(a.eventStartTime) - new Date(b.eventStartTime); });
    } else {
    console.log('can not');
    }
  }
}

const removeEvent = async (removeEventId) => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}events/${removeEventId}`, { 
    method: 'DELETE' ,
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  })
  if (res.status === 200) {
    detail.value = false
    events.value = events.value.filter((event) => event.id !== removeEventId)
    console.log('deleted successfully')
  }
  else console.log('error, can not delete')
}

const overlap = ref(false)
const edited = ref(false)
const errorPast = ref(false)
const editEvent = async (editEvent, newFile) => {
  if (moment(editEvent.eventStartTime).isAfter(moment(new Date()))) {
    errorPast.value = false
  } else {
    errorPast.value = true
  }
  if(errorPast.value == true){
        return
  }

  let editformData = new FormData();
  let editeventwithfile = {
    bookingEmail: editEvent.bookingEmail,
    eventStartTime: editEvent.eventStartTime,
    eventNotes: editEvent.eventNotes.trim(),
    eventDuration: editEvent.eventDuration,
    eventCategory: editEvent.eventCategory
  }
  
  // if(fileById.value.length = 0){
  //   if(newFile === undefined){
  //   editformData.append("file", null);
  //   console.log('1');
  //   } else if(newFile !== undefined){
  //   editformData.append("file", newFile);
  //   console.log('2');
  //   }
  // } 
  
  if(newFile === undefined){
    editformData.append("file", fileById[0]);
    console.log('3');
  } else if(newFile !== undefined){
    editformData.append("file", newFile);
    console.log('4');
  }

  editformData.append( 'event',  JSON.stringify(editeventwithfile) );

  const res = await fetch(`${import.meta.env.VITE_BASE_URL}events/${editEvent.id}`, {
    method: 'PUT',
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
    body: editformData
  })
  if (res.status === 200) {
    edited.value = true
    detail.value = false
    overlap.value = false
    showFile(editEvent.id)
    getAllEvent()
    console.log('edited successfully');
  } else {
    edited.value = false
    overlap.value = true
  }
}

const countEvent = ref()
const getAllEvent = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}events`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
  if (res.status === 200) {
    events.value = await res.json();
    events.value.sort(function (a, b) { return new Date(b.eventStartTime) - new Date(a.eventStartTime); });
    countEvent.value = events.value.length
  } else if (res.status === 401 && token !== null){
    RefreshToken();
  }
}

const countPastEvent = ref()
const countUpcoming = ref()
const UpcomingEvent = ref()
const PastEvent = ref()
const countPastApp = async () => {
  const day = moment().format().slice(0, 19) + 'Z'
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}events/schedule-past?DateTime=${day}`, { 
      method: "GET", 
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`
      }
  })
  if (res.status === 200) {
    PastEvent.value = await res.json();
    countPastEvent.value = PastEvent.value.length
    console.log(PastEvent.value.length);
  } else {
    console.log('can not');
  }
}

const countUpApp = async () => {
  const day = moment().format().slice(0, 19) + 'Z'
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}events/schedule-comingup?DateTime=${day}`, { 
      method: "GET", 
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`
      }
  })
  if (res.status === 200) {
    UpcomingEvent.value = await res.json();
    countUpcoming.value = UpcomingEvent.value.length
    console.log(UpcomingEvent.value.length);
  } else {
    console.log('can not');
  }
}

const fileById = ref('')

const showFile = async (id) => {
  showDetail();

  const res = await fetch(`${import.meta.env.VITE_BASE_URL}files/${id}`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });
    if (res.status === 200) {
      fileById.value = await res.json();
      console.log(fileById);
    } else if (res.status === 401 && token !== null){
      fileById.value = null
      // RefreshToken();
    }
}

const downloadFile = async (id) => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}files/download-file/${id}/${fileById.value[0]}`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });
    if (res.status === 200) {
      window.open(res.url, '_blank');
      console.log('yes');
    } else if (res.status === 401 && token !== null){
      RefreshToken();
    }
}

const removeFile = async (id) => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}files/delete-file/${id}/${fileById.value[0]}`, { 
    method: 'DELETE' ,
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  })
  if (res.status === 200) {
    console.log('deleted successfully')
    // alert('Delete file successfully')
    detail.value = true
    showFile(id)
    // showDetail();
    fileById.value = ''
    // location.reload()
  }
  else console.log('error, can not delete')
}

onBeforeMount(async () => {
  await getAllEvent();
  await getEventCategory();
  await showFile();
  await downloadFile();
  await countPastApp();
  await countUpApp();
})

const schedule = () => {
  if (events.value.length == 0) {
    return "No Scheduled Event"
  }
}
const categories = ref([])
const getEventCategory = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}categories`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  })
  if (res.status === 200) {
    categories.value = await res.json()
  } else if (res.status === 401 && token !== null){
    RefreshToken();
  }
}
const cancelEdit = () => {
  if (overlap.value == true || errorPast.value == true) {
    location.reload()
  }
}
const filterNothing = () => {
  if((filterEvent.value === "" || filterEvent.value === null) && (filterStatus.value === "" || filterStatus.value === null) && (filterDate.value === "" || filterDate.value === null) ) {
    getAllEvent()
  }
}
const reset = () => {
  filterEvent.value = ""
  filterStatus.value = ""
  filterDate.value = ""
}

const detail = ref(true)
const showDetail = () => {
  if(detail.value === false){
    detail.value = true
  } else {
    detail.value = false
    fileById.value = ''
  }
}

const closeEdited = () => {
  edited.value = false
}
const editingEvent = ref({})
const toEditingMode = (editEvent) => {
  editingEvent.value = editEvent
}

</script>
 
<template>
  <div class="body">
    <h3 class="mx-auto" style="font-size: 2.1vw;margin-top: 2.5vw;margin-bottom: 1.5vw; font-weight: bolder;">Appointment</h3>
    <!-- <div v-if="events.length > 8" class="scroll-down"></div> -->
    <div class="container-event">
      <div class="row" style="justify-content: center;">
        <div class="col-md-6 col-xl-7" style="flex-basis: auto !important; width: 50vw !important; height:35vw">
          <div class="card" style="border-color: transparent; margin-top:-1vw;">
            <p style="font-size: 0.8vw; color: #646464;">
              <span class="count-hover" @click="getAllEvent(), reset()" style="cursor: pointer;">  All Appointment : {{countEvent}} </span> |
              <span class="count-hover" v-on:click="filterStatus = 'Past'" @click="SortByStatus(filterStatus)" style="cursor: pointer;"> Past Appointment : {{countPastEvent}} </span> |
              <span class="count-hover" v-on:click="filterStatus = 'Upcoming'" @click="SortByStatus(filterStatus)" style="cursor: pointer;"> Upcoming Appointment : {{countUpcoming}}</span>
            </p>
            <div class="card-body overflow-auto" style="height:35.5vw; margin: 1vw; margin-top: 0;margin-bottom: 0; padding-top: 0;">
              <div v-if="events.length !== 0">
                <EventList :eventList="events" :overlap="overlap" :edited="edited" :errorPast="errorPast" @delete="removeEvent" @edit="editEvent"
                  @cancelEdit="cancelEdit" :detail="detail" @showDetail="showDetail" @showFile="showFile" @closeEdited="closeEdited" 
                  :fileById="fileById" @downloadFile="downloadFile" @removeFile="removeFile"
                  :currentEvent="editingEvent" @toEditingMode="toEditingMode"/>
              </div>
              <h5 class="mx-auto Noschedule" >
                {{ schedule() }}
              </h5>
            </div>
          </div>
        </div>
        
        <div class="col-md-6 col-xl-3" style="flex-basis: auto !important; width: 30vw !important;">
          <div class="card" 
            style="box-shadow: 0 2px 2px #00000005, 0 4px 4px #0000000a; position: static;">
            <div class="card-body">
              <h2 class="card-title" style="text-align: center; font-weight: bold; font-size: 1.7vw;">Filter</h2>
              <div style="margin: 1vw;">

              <p class="filter-head">Category </p>
              <select class="form-select filter-form" v-model="filterEvent"
                :disabled="filterStatus || filterDate ? '' : disabled ">
                <option v-for="(category, index) in categories" :key="index" :value="category.id">{{
                  category.eventCategoryName
                }} </option>
              </select>
                  
              <p class="filter-head">Status</p>
              <select class="form-select filter-form" v-model="filterStatus"
                :disabled="filterEvent || filterDate ? '' : disabled ">
                <option value="Past">Past</option>
                <option value="Upcoming">Upcoming</option>
              </select>

              <p class="filter-head">Date</p>
              <div>
                <input type="date" class="form-control" v-model="filterDate" style="width: 25vw;margin-left:0; z-index:-1;" :disabled="filterStatus || filterEvent ? '' : disabled ">
                <!-- <Datepicker :style="detail ? { visibility: 'hidden'} : {visibility: 'visible'}"  -->
                <!-- <Datepicker
                  :enableTimePicker="false" v-model="filterDate" class="datepicker filter-form"
                  style="width: 25vw;margin-left:0; z-index:-1;" 
                  :disabled="filterStatus || filterEvent ? '' : disabled "
                  ></Datepicker> -->
              </div>

              <button type="button" class="btn btn-danger all-btn" @click="getAllEvent(), reset()">Reset</button>
              
              <button class="search-btn" 
                @click="filterNothing(),SortByDate(filterDate),SortByCategory(filterEvent),SortByStatus(filterStatus)">search</button>
              </div>
              </div>
          </div>
        </div>
      </div>
  </div>
    <!-- login -->
    <div class="Plzlogin"
      v-if="token === null || token === undefined"
    >
      <div class="card alertPlzlogin">
        <div class="card-body" style="margin-top: 10px">
          <img
            src="https://api.iconify.design/clarity/warning-line.svg?color=%23efbc3c"
            style="width: 5.5vw"
          />
          <p class="card-text" style="margin-top: 0.5vw;margin-bottom: 1vw;">
            Please login to see appointment
          </p>
          <router-link to="/login"><button
            type="button"
            class="btn btn-warning btn-plzlogin mx-auto"
            style="margin-bottom: 1vw"
          >
            OK
          </button></router-link>
        </div>
      </div>
    </div>
  </div>
</template>
 
<style>
@import url('https://fonts.googleapis.com/css2?family=Inter&family=Noto+Sans+Thai&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Radio+Canada:wght@600&display=swap');

.body {
  font-family: 'Radio Canada', 'Noto Sans Thai';
}
.count-hover:hover{
  color: #F857A2;
  font-weight: bold;
}         
.search-btn {
  font-size: 0.9vw;
  background-image: linear-gradient(to right, #f857a6 0%, #ff5858  51%, #f857a6  100%);
  padding: 0.7vw 10vw;
  text-align: center;
  text-transform: uppercase;
  transition: 0.5s;
  background-size: 200% auto;
  color: white;            
  box-shadow: 0 0 20px #eee;
  border-radius: 10px;
  display: block;
  border-color: transparent;
  margin-top: 1vw;
}

.search-btn:hover {
  background-position: right center; /* change the direction of the change here */
  color: #fff;
  text-decoration: none;
  border-color: transparent;
}
         
.filter-head{
  font-weight: bold;
  font-size: 0.89vw;
  margin-top: 0.5vw;
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
  background-image: linear-gradient(to right, #D31027 0%, #EA384D  51%, #D31027  100%);
  border-color: transparent;
  transition: 0.5s;
  background-size: 200% auto;
  color: white;
  padding-left: 20px;
  padding-right: 20px;
  font-size: 0.9vw;
  margin-top: 1vw;
}
.all-btn:hover{
  background-position: right center; /* change the direction of the change here */
  color: #fff;
  text-decoration: none;
  border-color: transparent;
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
  width: 25vw;
  font-size: 0.9vw;
}

h5,
h3 {
  text-align: center;
}
::-webkit-scrollbar{
  width: 0.8vw;
  margin-right:3vw;
  height: 1vw;
}
::-webkit-scrollbar-track{
  border-radius: 0.5vw;
  box-shadow: inset 0 0 6px rgba(0,0,0,0.25);
}
::-webkit-scrollbar-thumb{
  border-radius: 0.5vw;
  background-color: #F857A2;                                 
}
/* 
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
/*}

@keyframes scroll-down {
  0% {
    /* top:20%; 
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
} */
</style>