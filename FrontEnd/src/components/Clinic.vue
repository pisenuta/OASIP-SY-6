<script setup>
import { ref, onBeforeMount } from 'vue'
const categories = ref([])
const getEventCategory = async () => {
    // const res = await fetch(`${import.meta.env.VITE_BASE_URL}eventcategory`)
    const res = await fetch(`http://10.4.56.123:8080/api/eventcategory`)
    if (res.status === 200) {
        categories.value = await res.json()
    }
}
onBeforeMount(async () => {
    await getEventCategory()
})
// v-for="(category, index) in categories" :key="index"
//                 :value="category"
</script>
 
<template>
    <div class="body">
        <h3 class="mx-auto mt-5" style="font-size: 40px;font-weight: bolder;">Clinic</h3>
        <div class="containerClinic mt-5">
            <div class="row">
                <div class="col-4" v-for="(category, index) in categories" :key="index" :value="category">
                    <div class="card-body">
                        <h5 class="card-title">{{category.eventCategoryName}}</h5>
                        <div v-if="category.eventCategoryDescription === null || category.eventCategoryDescription === ''">
                            <p>No Description.</p>
                        </div>
                        <p class="card-text">{{category.eventCategoryDescription}}</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
 
<style>
@import url('https://fonts.googleapis.com/css2?family=Inter&family=Noto+Sans+Thai&display=swap');

.body{
    font-family: 'Inter','Noto Sans Thai';
}
.card-body{
    background-color: #212529;
    border-radius: 10px;
    height: 10rem;
    margin-bottom: 20px;
    width: 35rem;
}
.card-body h5{
    color:#e74694;
    font-weight: bold;
}
.card-body p{
    color:white;
}
</style>