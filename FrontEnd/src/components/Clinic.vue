-<script setup>
import { ref, onBeforeMount, computed } from 'vue'
import editClinic from '../components/editClinic.vue'
const categories = ref([])
const getEventCategory = async () => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/eventcategory` , {
    // const res = await fetch(`http://localhost:8080/api/eventcategory/`, {
        method: "GET",
    });
    if (res.status === 200) {
        categories.value = await res.json()
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
    if(clinic.eventCategoryName == null || clinic.eventCategoryName == ''){
        errorClinicName.value = true
    } else {
        errorClinicName.value = false
    }
    if(categories.value.find((c) => clinic.eventCategoryName === c.eventCategoryName)){
        notUnique.value = true
    } else {
        notUnique.value = false
    }
    if(clinic.eventDuration == null || clinic.eventDuration == '' || clinic.eventDuration == 0){
        errorDuration.value = true
    } else {
        errorDuration.value = false
    }

    if(clinic.eventDuration < 0 || clinic.eventDuration > 480){
        wrongDuration.value = true
    } else {
        wrongDuration.value = false
    }

    if(errorClinicName.value == true || errorDuration.value == true || wrongDuration.value == true || notUnique.value == false){
        return 
    }

    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/eventcategory/${clinic.id}`,{
      method: 'PUT',
      headers:{
        'content-type': 'application/json'
      },
      body: JSON.stringify({
        eventCategoryName: clinic.eventCategoryName,
        eventCategoryDescription:clinic.eventCategoryDescription,
        eventDuration:clinic.eventDuration
      })
    })
    if(res.status === 200){
        const modifyClinic = await res.json()
        categories.value = categories.value.map((clinic) => 
        clinic.id === modifyClinic.id ? {
            ...clinic,
            eventCategoryName: modifyClinic.eventCategoryName,
            eventCategoryDescription:modifyClinic.eventCategoryDescription,
            eventDuration:modifyClinic.eventDuration
            }: clinic)
        editedPop.value = true
        notUnique.value = false
        editingClinic.value = {}
        console.log('edited successfully');
    } else {
        console.log('can not edit');
    }
}
    

const editingClinic = ref({})
const toEditingMode = (editClinic) =>{
    editingClinic.value = editClinic
    console.log(editingClinic.value)
}

const cancelPop = () =>{
    editClinicPop.value = false
    errorClinicName.value = false
    errorDuration.value = false
    wrongDuration.value = false
    notUnique.value == false
}

</script>
 
<template>
    <div class="body">
        <h3 class="mt-5" style="font-size: 40px;font-weight: bold;">Clinic</h3>
        <div class="containerClinic mt-5">
            <div class="row mx-auto">
                <div class="col-4 col-clinic" v-for="(category, index) in categories" :key="index" :value="category">
                    <div class="card-body clinic-body">
                        <img src="https://api.iconify.design/akar-icons/edit.svg?color=white" class="edit-icon"
                            v-on:click="showIndex = index, editClinicPop = true" @click="toEditingMode(category)">
                        <h5 class="clinic-title">{{ category.eventCategoryName }}</h5>
                        <p class="duration-text"> {{ category.eventDuration }} Minutes</p>
                        <div
                            v-if="category.eventCategoryDescription === null || category.eventCategoryDescription === ''">
                            <p style="color: #8a8a8a;">No Description.</p>
                        </div>
                        <p class="card-text">{{ category.eventCategoryDescription }}</p>
                    </div>
                </div>
            </div>
        </div>
        <div>
            <div class="container" v-if="editClinicPop == true">
            <ul>
                <li v-for="(category, index) in categories" :key="index" :value="category">
                    <div class="card-body clinic-popup">
                        <div class="card" style="width: 38rem;" v-if="showIndex === index">
                            <div class="card-title">
                                <div class="card-header header"
                                    style="color: #e74694; font-weight: bold; letter-spacing: 1px; font-size: 20px;">
                                    Edit Clinic
                                </div>
                            </div>
                            <div class="card-body edit-clinic">
                                <div style="text-align: center;">
                                    <editClinic
                                        :currentClinic="editingClinic"
                                        :errorClinicName="errorClinicName"
                                        :errorDuration="errorDuration"
                                        :wrongDuration="wrongDuration"
                                        :notUnique="notUnique"
                                        @updateClinic="modifyClinic"/>  
                                    <button type="button" class="btn btn-secondary" @click="cancelPop">Cancel</button>
                                </div>
                                
                            </div>
                        </div>
                    </div>
                </li>
            </ul>
            </div>
        </div>
        <div class="container" v-if="editedPop === true">
            <div class="card" id="add" style="width: 23rem; height: 15rem;">
                <div class="card-body" style="margin-top: 10px;">
                    <img src="https://api.iconify.design/healthicons/yes-outline.svg?color=%23198754&width=90&height=90">
                    <p class="card-text" style="margin-top: 10px;">Edit Clinic Successfully</p>
                    <router-link to="/clinic"><button type="button" class="btn btn-success" v-on:click="editClinicPop = false , editedPop = false" style="width: 100px; margin-top: 5px;">OK</button></router-link>
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
.clinic-popup{
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    border-radius: 10px;
}
.header {
    padding: 10px 15px;
    text-align: center;
}

.clinic-form {
    width: 25rem;
    margin-top: -10px;
    margin-right: auto;
    margin-left: auto;
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
    height: 20px;
}

.body {
    font-family: 'Inter', 'Noto Sans Thai';
}

.clinic-title {
    margin-top: 7px;
    text-align: center;
}

.duration-text {
    background-color: #e74694;
    border-radius: 10px;
}

.clinic-body {
    background-color: #212529;
    border-radius: 10px;
    height: 12rem;
    margin-bottom: 30px;
    width: 35rem;
    margin-left: auto;
    margin-right: auto;
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
}

.clinic-body h5 {
    color: #e74694;
    font-weight: bold;
}

.clinic-body p {
    color: white;
    text-align: center;
    margin-top: 13px;
}

.col-clinic {
    transition: all .2s ease-in-out;
}

.col-clinic:hover {
    transform: scale(1.04);
}
</style>