<script setup>
import { ref, onBeforeMount } from 'vue'
import moment from "moment";
import editClinic from '../components/EditClinic.vue'

const newAccess = ref()
let token = localStorage.getItem("token")
const role = localStorage.getItem("role")
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
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}categories`, {
        method: "GET",
        // headers: {
        //     Authorization: `Bearer ${localStorage.getItem("token")}`,
        // },
    });
    if (res.status === 200) {
        categories.value = await res.json()
    } else if (res.status === 401 && token !== null){
        RefreshToken()
    }
}
onBeforeMount(async () => {
    await getEventCategory()
    await viewBlindEvent()
})

const showIndex = ref(null);

const editClinicPop = ref(false)
const errorClinicName = ref(false)
const errorDuration = ref(false)
const notUnique = ref(false)
const wrongDuration = ref(false)
const editedPop = ref(false)
const modifyClinic = async (clinic) => {
    if (clinic.eventCategoryName == null || clinic.eventCategoryName == '') {
        errorClinicName.value = true
    } else {
        errorClinicName.value = false
    }
    if (categories.value.find((c) => clinic.eventCategoryName === c.eventCategoryName) && clinic.eventCategoryName !== editingClinic.value.eventCategoryName) {
        notUnique.value = true
    } else {
        notUnique.value = false
    }
    if (clinic.eventDuration == null || clinic.eventDuration == '' || clinic.eventDuration == 0) {
        errorDuration.value = true
    } else {
        errorDuration.value = false
    }

    if (clinic.eventDuration < 0 || clinic.eventDuration > 480) {
        wrongDuration.value = true
    } else {
        wrongDuration.value = false
    }

    if (errorClinicName.value == true || errorDuration.value == true || wrongDuration.value == true || notUnique.value == true) {
        return
    }

    const res = await fetch(`${import.meta.env.VITE_BASE_URL}categories/${clinic.id}`, {
        method: 'PUT',
        headers: {
            'content-type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
        body: JSON.stringify({
            eventCategoryName: clinic.eventCategoryName,
            eventCategoryDescription: clinic.eventCategoryDescription,
            eventDuration: clinic.eventDuration
        })
    })
    if (res.status === 200) {
        const modifyClinic = await res.json()
        categories.value = categories.value.map((clinic) =>
            clinic.id === modifyClinic.id ? {
                ...clinic,
                eventCategoryName: modifyClinic.eventCategoryName.trim(),
                eventCategoryDescription: modifyClinic.eventCategoryDescription.trim(),
                eventDuration: modifyClinic.eventDuration
            } : clinic)
        editedPop.value = true
        notUnique.value = false
        editingClinic.value = {}
        console.log('edited successfully');
    } else if (res.status === 401 && token !== null){
        RefreshToken()
    }
}

const editingClinic = ref({})
const toEditingMode = (editClinic) => {
    editingClinic.value = editClinic
    console.log(editingClinic.value)
}

const cancelPop = () => {
    editClinicPop.value = false
    errorClinicName.value = false
    errorDuration.value = false
    wrongDuration.value = false
    notUnique.value = false
}
const blindEvent = ref([])
const viewBlindEvent = async() =>{
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}events/blind-events`, {
    method: "GET",
    // headers: {
    //   Authorization: `Bearer ${localStorage.getItem("token")}`,
    // },
  })
  if (res.status === 200) {
    blindEvent.value = await res.json()
  } else if (res.status === 401 && token !== null){
    RefreshToken();
  }
}

function formateTime(date) {
  const options = { hour: "numeric", minute: "numeric" };
  return new Date(date).toLocaleString("th-TH", options);
}

const showBlind = ref(false)
const toggleShow = () => {
  if(showBlind.value === true){
    showBlind.value = false
  } else {
    showBlind.value = true
  }
}

</script>
 
<template>
    <div class="body">
        <h3 style="font-size: 2.1vw;font-weight: bold;margin-top: 2.5vw;">Clinic</h3>

        <div class="containerClinic" style="margin-top: 2vw;">
            <div class="row mx-auto">
                <div class="col col-clinic" v-for="(category, index) in categories" :key="index" :value="category">
                    <div class="card-body clinic-body ">
                        <img v-if="role === 'lecturer' || role === 'admin'" src="https://api.iconify.design/akar-icons/edit.svg?color=black" class="edit-icon"
                            v-on:click="showIndex = index, editClinicPop = true" @click="toEditingMode(category)">
                        <img src="../assets/project.png" class="clinic-pic" v-if="category.id === 1">
                        <img src="../assets/devops.png" class="clinic-pic" v-if="category.id === 2">
                        <img src="../assets/database.png" class="clinic-pic" v-if="category.id === 3">
                        <img src="../assets/front.png" class="clinic-pic" v-if="category.id === 4">
                        <img src="../assets/back.png" class="clinic-pic" v-if="category.id === 5">
                        <h5 class="clinic-title" style="padding-top:2vh;">{{ category.eventCategoryName }}</h5>
                        <p class="duration-text"> {{ category.eventDuration }} Minutes</p>
                        <div class="overflow-auto" style="height:25vh;">
                            <p v-if="category.eventCategoryDescription === null || category.eventCategoryDescription === ''" style="color: #8a8a8a;font-size: 0.9vw;">No Description.</p>
                            <p v-else class="card-text clinic-des ">{{ category.eventCategoryDescription }}</p>
                        </div>
                        
                        <div style="padding-bottom: 0.5vw;text-align: center;margin-top: 0.3vw;" v-if="showBlind == true">
                            <p style="color: #f857a6; font-weight: bold;margin-bottom: 0;">BLIND EVENTS</p>
                            <div class="overflow-auto" style="height:13.8vh;">
                                <t v-for="(blind, index) in blindEvent" :key="index">
                                    <span v-if="blind.categoryId.id == category.id">
                                        {{ moment(blind.eventStartTime).format('ddd, D MMM YYYY') }} - {{ formateTime(blind.eventStartTime) }}<br>
                                    </span>
                                </t>
                            </div>                       
                        </div>
                        
                    </div>
                </div>
            </div>
            <div style="text-align: center; margin-bottom: 1vw;">
                <button class="showBlind-btn mx-auto" @click="toggleShow()">
                    <span v-if="showBlind == false">SHOW BLIND EVENTS</span>
                    <span v-if="showBlind == true">HIDE BLIND EVENTS</span>
                </button>
            </div>
            
        </div>
        <div>
            <div class="container" v-if="editClinicPop == true">
                <ul>
                    <li v-for="(category, index) in categories" :key="index" :value="category">
                        <div class="card-body clinic-popup">
                            <div class="card" style="width: 35vw;" v-if="showIndex === index">
                                <div class="card-title">
                                    <div class="card-header header"
                                        style="color: #f857a6; font-weight: bold; letter-spacing: 1px; font-size: 1.2vw;">
                                        Edit Clinic
                                    </div>
                                </div>
                                <div class="card-body edit-clinic">
                                    <div style="text-align: center;">
                                        <editClinic :currentClinic="editingClinic" :errorClinicName="errorClinicName"
                                            :errorDuration="errorDuration" :wrongDuration="wrongDuration"
                                            :notUnique="notUnique" @updateClinic="modifyClinic" />
                                        <button type="button" class="btn btn-secondary" style="font-size: 0.95vw;margin-top: 0.2vw;margin-bottom: 1.2vw;"
                                            @click="cancelPop">Cancel</button>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
        <div class="container" v-if="editedPop === true">
            <div class="card" id="center-popup" style="width: 23rem; height: 15rem;">
                <div class="card-body" style="margin-top: 10px;">
                    <img
                        src="https://api.iconify.design/healthicons/yes-outline.svg?color=%23198754&width=90&height=90">
                    <p class="card-text" style="margin-top: 10px;">Edit Clinic Successfully</p>
                    <router-link to="/clinic"><button type="button" class="btn btn-success btn-grad-ok"
                            v-on:click="editClinicPop = false, editedPop = false"
                            style="width: 100px; margin-top: 5px;">OK</button></router-link>
                </div>
            </div>
        </div>
    </div>
</template>
 
<style>
@import url('https://fonts.googleapis.com/css2?family=Inter&family=Noto+Sans+Thai&display=swap');

ul {
    list-style-type: none;
}

.showBlind-btn {
    background-image: linear-gradient(to right, #f857a6 0%, #ff5858 51%, #f857a6 100%);
    text-align: center;
    padding: 0.3vw 0.5vw;
    transition: 0.5s;
    background-size: 200% auto;
    color: white;
    font-size: 0.9vw;
    border-color: transparent;
    border-radius: 10px;
}

.showBlind-btn:hover {
    background-position: right center;
    color: #fff;
    text-decoration: none;
    border-color: transparent;
}

.clinic-pic{
    width: 10vw;  
    margin-left:25%;
    margin-top: 10%;
}
.clinic-popup {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    border-radius: 1vw;
}

.header {
    padding: 10px 15px;
    text-align: center;
}

.clinic-form {
    width: 20.5vw;
    height: 2vw;
    margin-right: auto;
    margin-left: auto;
    font-size: 0.9vw;
}

.close-popclinic {
    cursor: pointer;
    border: none;
    outline: none;
    background: none;
    font-size: 45px;
    font-weight: bold;
    margin-top: -2%;
}

.close-popclinic:hover {
    color: #f857a6;
}

.edit-icon {
    cursor: pointer;
    float: right;
    height: 2.2vh;
    margin-right: 10px;
    margin-top: 10px;
    transition: all 0.2s ease-in-out;
}

.edit-icon:hover {
    transform: scale(1.5);
}


.body {
    font-family: 'Inter', 'Noto Sans Thai';
}

.clinic-title {
    font-size: 1.2vw;
    margin-top: 0.5vw;
    text-align: center;
}

.clinic-des {
    font-size: 0.9vw;
    margin-left: 0.5vw;
    margin-right: 0.5vw;
    word-wrap: break-word;
}

.duration-text {
    background-color: #f857a6;
    border-radius: 1vw;
    font-size: 1vw;
    color: white;
}

.clinic-body {
    /* background-color: #212529; */
    background-color: white;
    border-radius: 10px;
    /* height: 30vw; */
    margin-bottom: 30px;
    width: 18.5vw;
    margin-left: auto;
    margin-right: auto;
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
    font-family: 'Mitr', 'Noto Sans Thai';
}

.clinic-body h5 {
    color: #f857a6;
    font-weight: bold;
}

.clinic-body p {
    text-align: center;
}

.col-clinic {
    transition: all .2s ease-in-out;
    padding-left: 0;
    padding-right: 0;
}

.col-clinic:hover {
    transform: scale(1.03);
}
</style>