<script setup>
import { ref, onBeforeMount } from 'vue'
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
  } else if (res.status === 401){
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
        headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
    });
    if (res.status === 200) {
        categories.value = await res.json()
    } else if (res.status === 401 && token !== null){
        RefreshToken()
    }
}
onBeforeMount(async () => {
    await getEventCategory()
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

</script>
 
<template>
    <div class="body">
        <h3 style="font-size: 2.1vw;font-weight: bold;margin-top: 2.5vw;">Clinic</h3>
        <!-- plz login -->
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
            Please login to see clinic
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
        <div class="containerClinic" style="margin-top: 2vw;">
            <div class="row mx-auto">
                <div class="col-4 col-clinic" v-for="(category, index) in categories" :key="index" :value="category">
                    <div class="card-body clinic-body">
                        <img v-if="role === 'lecturer' || role === 'admin'" src="https://api.iconify.design/akar-icons/edit.svg?color=white" class="edit-icon"
                            v-on:click="showIndex = index, editClinicPop = true" @click="toEditingMode(category)">
                        <h5 class="clinic-title" style="padding-top:20px;">{{ category.eventCategoryName }}</h5>
                        <p class="duration-text"> {{ category.eventDuration }} Minutes</p>
                        <div
                            v-if="category.eventCategoryDescription === null || category.eventCategoryDescription === ''">
                            <p style="color: #8a8a8a;font-size: 0.9vw;">No Description.</p>
                        </div>
                        <p class="card-text clinic-des">{{ category.eventCategoryDescription }}</p>
                    </div>
                </div>
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
                                        style="color: #e74694; font-weight: bold; letter-spacing: 1px; font-size: 1.2vw;">
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
    color: #e74694;
}

.edit-icon {
    cursor: pointer;
    float: right;
    height: 1.1vw;
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
    font-size: 1vw;
    margin-top: 7px;
    text-align: center;
}

.clinic-des {
    font-size: 0.9vw;
    margin-left: 0.5vw;
    margin-right: 0.5vw;
}

.duration-text {
    background-color: #e74694;
    border-radius: 10px;
    font-size: 1vw;
}

.clinic-body {
    background-color: #212529;
    border-radius: 10px;
    height: 11vw;
    margin-bottom: 30px;
    width: 30vw;
    margin-left: auto;
    margin-right: auto;
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
    font-family: 'Mitr', 'Noto Sans Thai';
}

.clinic-body h5 {
    color: #e74694;
    font-weight: bold;
}

.clinic-body p {
    color: white;
    text-align: center;
}

.col-clinic {
    transition: all .2s ease-in-out;
}

.col-clinic:hover {
    transform: scale(1.03);
}
</style>