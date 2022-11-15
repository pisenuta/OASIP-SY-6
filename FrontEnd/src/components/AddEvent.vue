<script setup>
import { ref, onBeforeMount } from 'vue'
import moment from 'moment';
import ManageAdd from './ManageAdd.vue'

const newAccess = ref()
let token = localStorage.getItem("token")
const userRole = localStorage.getItem("role")
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
    getEventCategory()
    getUser()
  } else if (res.status === 401 || res.status === 403){
    localStorage.clear()
    window.location.href = "/sy6"
    console.log("plz log out");
  }
};

const refresh = () => {
  token = localStorage.setItem('token',`${newAccess.value.access_token}`)
}

const categories = ref([])
const getEventCategory = async () => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}categories`,{
        method: 'GET',
        // headers: {
        //     Authorization: `Bearer ${localStorage.getItem("token")}`,
        // }
    })
    if (res.status === 200) {
        categories.value = await res.json()
    } else if (res.status === 401 && token !== null) {
        RefreshToken();
    }
}

const users = ref([]);
const getUser = async () => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}users` , {
        method: "GET",
        headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
  });
  if (res.status === 200) {
    users.value = await res.json();
    users.value.sort();
  } else if (res.status === 401 && token !== null){
    RefreshToken();
  }
};

onBeforeMount(async () => {
    await getEventCategory();
    await getUser()
})

const addAlert = ref(false)
const errorName = ref(false)
const errorClinic = ref(false)
const errorEmail = ref(false)
const errorTime = ref(false)
const mailVali = ref(true)
const mailNotFound = ref(false)
const errorFuture = ref(false)
const overlap = ref(false)
const loading = ref(false)

const createEvent = async (event, newFile) => {
    if(userRole === 'guest') {
        if (event.bookingName == null || event.bookingName == '') {
        errorName.value = true
        } else {
        errorName.value = false
        }
        if (event.bookingEmail == null || event.bookingEmail == '') {
        errorEmail.value = true
        } else {
        errorEmail.value = false
        }
        var emailValidate = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

        if (event.bookingEmail.match(emailValidate)) {
        mailVali.value = true
        } else {
        mailVali.value = false
        console.log('not validate');
        }
    }
    else if(userRole === 'admin') {
        if (event.user.name === undefined){
        errorName.value = true
        } else {
        errorName.value = false
        }
    } else if(userRole === 'student') {
        if (event.bookingName == null || event.bookingName == '') {
        errorName.value = true
        } else {
        errorName.value = false
        }
    }
    
    if (Object.keys(event.eventCategory).length === 0) {
        errorClinic.value = true
    } else {
        errorClinic.value = false
    }

    if (event.eventStartTime === null || event.eventStartTime == '') {
        errorTime.value = true
    } else {
        errorTime.value = false
    }
    if (moment(event.eventStartTime).isAfter(moment(new Date()))) {
        errorFuture.value = false
    } else {
        errorFuture.value = true
    }


    // if (errorName.value == true || errorEmail.value == true || errorClinic.value == true || errorTime.value == true || mailVali.value == false || errorFuture.value == true) {
    //     return
    // }
    if (errorName.value == true || errorClinic.value == true || errorTime.value == true || errorFuture.value == true || mailVali.value == false) {
        return
    }
    // console.log(newFile.files);
    // console.log(newFile.files[0]);
    if(userRole === 'admin'){
        let adminformData = new FormData();
        let adminEvent = {
            eventCategory: {
                    id: event.eventCategory.id
            },
            userId: event.user.id,
            bookingName: event.user.name,
            bookingEmail: event.user.email,
            eventStartTime: event.eventStartTime,
            eventDuration: event.eventCategory.eventDuration,
            eventNotes: event.eventNotes.trim()
        }

        // if (newFile.files.length != 0) {
        //     adminformData.append("file", newFile.files[0]);
        // }
        adminformData.append("file", newFile);
        adminformData.append( 'event',  JSON.stringify(adminEvent) );
        loading.value = true

        const res = await fetch(`${import.meta.env.VITE_BASE_URL}events`, {
            method: 'POST',
            headers: {
                Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
            body: adminformData
        })
        if (res.status == 201 || res.status == 200) {
            loading.value = false
            console.log('added successfully');
            addAlert.value = true
        } else if (res.status == 400 || res.status == 415) {
            loading.value = false
            overlap.value = true
            console.log('error, can not add');
        }
        
    } else if(userRole === 'student'){
        let stdformData = new FormData();
        let stdEvent = {
            eventCategory: {
                id: event.eventCategory.id
            },
            userId: event.user.id,
            bookingName: event.bookingName.trim(),
            bookingEmail: event.user.email,
            eventStartTime: event.eventStartTime,
            eventDuration: event.eventCategory.eventDuration,
            eventNotes: event.eventNotes.trim()
        }

        // if (newFile.files.length != 0) {
        //     adminformData.append("file", newFile.files[0]);
        // }
        stdformData.append("file", newFile);
        stdformData.append( 'event',  JSON.stringify(stdEvent) );
        loading.value = true

        const res = await fetch(`${import.meta.env.VITE_BASE_URL}events`, {
            method: 'POST',
            headers: {
                Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
            body: stdEvent
        })

        if (res.status == 201 || res.status == 200) {
            loading.value = false
            console.log('added successfully');
            addAlert.value = true
        } else if (res.status == 400) {
            loading.value = false
            overlap.value = true
            console.log('error, can not add');
        }
    } else if (userRole === 'guest'){
        let guestformData = new FormData();
        let guestEvent = {
            eventCategory: {
                id: event.eventCategory.id
            },
            userId: null,
            bookingName: event.bookingName.trim(),
            bookingEmail: event.bookingEmail.trim(),
            eventStartTime: event.eventStartTime,
            eventDuration: event.eventCategory.eventDuration,
            eventNotes: event.eventNotes.trim()
        }

        // if (newFile.files.length != 0) {
        //     adminformData.append("file", newFile.files[0]);
        // }
        guestformData.append("file", newFile);
        guestformData.append( 'event',  JSON.stringify(guestEvent) );
        loading.value = true
        
        const res = await fetch(`${import.meta.env.VITE_BASE_URL}events/guest`, {
            method: 'POST',
            body: guestformData
        })
        loading.value = true
        if (res.status == 201 || res.status == 200) {
            console.log('added successfully');
            loading.value = false
            addAlert.value = true
        } else if (res.status == 400) {
            loading.value = false
            overlap.value = true
            console.log('error, can not add');
        } else if (res.status == 500) {
            loading.value = false
            mailNotFound.value = true
        }
    }
    
}

const loginAlert = ref(true)

</script>
 
<template>
    <div v-if="userRole === 'lecturer'" class="body">
        <div class="noUser mx-auto">
            <img src="../assets/403.gif" style="width: 40%; margin-left:25%;margin-bottom:-2vw"/>
            <img src="../assets/403-text.png" style="width: 100%;"/>
        </div>
    </div>

    <div class="body">
        <h3 class="mx-auto" style="font-size: 2.1vw;font-weight: bolder; margin-top: 2.5vw;">Booking</h3>
        <ManageAdd :categoryList="categories" :userList="users" :errorName="errorName" :errorClinic="errorClinic" :errorEmail="errorEmail"
            :errorTime="errorTime" :mailVali="mailVali" :errorFuture="errorFuture" :overlap="overlap" :mailNotFound="mailNotFound"
            @create="createEvent" />
        <!-- plz login -->
        <!-- <div class="Plzlogin" v-if="token === null || token === undefined">
            <div class="card alertPlzlogin">
                <div class="card-body" style="margin-top: 10px">
                    <img src="https://api.iconify.design/clarity/warning-line.svg?color=%23efbc3c"
                        style="width: 5.5vw" />
                    <p class="card-text" style="margin-top: 0.5vw;margin-bottom: 1vw;">
                        Please login to booking
                    </p>
                    <router-link to="/login"><button type="button" class="btn btn-warning btn-plzlogin mx-auto"
                            style="margin-bottom: 1vw">
                            OK
                        </button></router-link>
                </div>
            </div>
        </div> -->

        <div class="Plzlogin" v-if="(token === null || token === undefined || role === 'guest') && loginAlert == true">
            <div class="card alertPlzlogin">
                <div class="card-body" style="margin-top: 10px">
                    <img src="https://api.iconify.design/clarity/warning-line.svg?color=%23efbc3c"
                        style="width: 5.5vw" />
                    <p class="card-text" style="margin-top: 0.5vw;margin-bottom: 1vw;">
                        Do you want to booking as guest?
                    </p>
                    <div  style="margin-bottom: 1vw">
                        <button type="button" class="btn btn-success btn-grad-ok" style="margin-right: 1vw;" v-on:click="loginAlert = false"> Yes </button>
                        <router-link to="/login">
                            <button type="button" class="btn btn-warning btn-plzlogin" style="text-transform: capitalize;"> No </button>
                        </router-link>
                    </div>
                </div>
            </div>
        </div>

        <div class="container" style="background-color: transparent;" v-if="loading === true">
            <div class="card" id="center-popup" style="background-color: transparent;box-shadow:none;border-color: transparent;">
                <img src="../assets/loading.gif" style="width: 10vw;">
            </div>
            
        </div>

        <div class="container" v-if="addAlert === true">
            <div class="card" id="center-popup" style="width: 23rem; height: 15rem;">
                <div class="card-body" style="margin-top: 10px;">
                    <img
                        src="https://api.iconify.design/healthicons/yes-outline.svg?color=%23198754&width=90&height=90">
                    <p class="card-text" style="margin-top: 10px;">Added Event Successfully</p>
                    <router-link to="/appointment"><button type="button" class="btn btn-success btn-grad-ok" v-on:click="addAlert = false"
                            style="width: 100px; margin-top: 5px;">OK</button></router-link>
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

#center-popup {
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
    font-size: 0.82vw;
    padding: auto;
}

label {
    margin-left: 23%;
    font-weight: bold;
}
</style>