<script setup>
import { ref, onBeforeMount } from 'vue'
const categories = ref([])
const getEventCategory = async () => {
    const res = await fetch('http://localhost:8080/api/eventcategory')
    if (res.status === 200) {
        categories.value = await res.json()
    }
}
onBeforeMount(async () => {
    await getEventCategory()
})

const selectedCategory = ref('')

</script>
 
<template>
    <div class="body">
        <h3 class="mx-auto mt-5" style="font-size: 40px;font-weight: bolder;">Add Event</h3>
        <div class="form mx-5 mb-5 mt-5">
            <div class="mb-3">
                <label for="clinic" class="form-label">Clinic :</label>
                <select class="form-select style-form" style="width: 50%;" v-model="selectedCategory">
                    <option selected>Select Clinic</option>
                    <option v-for="(category, index) in categories" :key="index" >{{ category.eventCategoryName }}</option>
                </select>
            </div>
            <div class="mb-3">
                <label for="name" class="form-label">Name :</label>
                <input class="form-control style-form" id="name">
            </div>
            <div class="mb-3">
                <label for="email" class="form-label">Email :</label>
                <input class="form-control style-form" id="email">
            </div>
            <div class="mb-3">
                <label for="meeting-time" >Date - Time :</label><br>
                <input type="datetime-local" id="meeting-time" name="meeting-time" class="date-form">
            </div>
            <div class="mb-3" style="margin-left: 23%;">
                <p><b>Durations :</b> {{categories.filter(x => x.eventCategoryName === selectedCategory)}} <b>min.</b></p>
            </div>
            <div class="mb-3">
                <label for="note" class="form-label">Note :</label>
                <textarea class="form-control style-form" rows="3"></textarea>
            </div>
        </div>
        <div style="text-align: center;">
            <button type="button" class="btn btn-dark mx-auto">Add Event</button>
        </div>
        
    </div>
</template>
 
<style>
@import url('https://fonts.googleapis.com/css2?family=Inter&family=Noto+Sans+Thai&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Radio+Canada:wght@600&display=swap');

.body {
    font-family: 'Inter';
    font-size: 17px;
}

h3 {
    font-family: 'Radio Canada';
}

.date-form{
    height: 38px; 
    width: 50%; 
    margin-top: 10px;
    margin-left: 25%;
    border-radius: 5px;
    border-color: #ced4da;
}

.style-form{
    width: 50%; 
    margin: auto;
}
label{
    margin-left: 23%;
    font-weight: bold;
}
</style>