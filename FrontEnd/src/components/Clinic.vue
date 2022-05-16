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
</script>
 
<template>
    <div class="body">
        <h3 class="mx-auto mt-5" style="font-size: 40px;font-weight: bolder;">Clinic</h3>
        <div class="containerForCard" v-for="(category, index) in categories" :key="index" :value="category">
            <div class="cardDetail">
                <div class="face face1">
                    <div class="content">
                        <div v-if="category.id === 1">
                            <img src="https://api.iconify.design/carbon/id-management.svg?color=black">
                        </div>
                        <div v-if="category.id === 2">
                            <img src="https://api.iconify.design/carbon/id-management.svg?color=black">
                        </div>
                        <div v-if="category.id === 3">
                            <img src="https://api.iconify.design/fa6-solid/database.svg?color=black">
                        </div>
                        <div v-if="category.id === 4">
                            <a href="https://www.flaticon.com/free-icons/development" title="development icons"></a>
                        </div>
                        <div v-if="category.id === 5">
                            <img src="https://api.iconify.design/carbon/id-management.svg?color=black">
                        </div>
                        {{category.eventCategoryName}}
                    </div>
                </div>
                <div class="face face2">
                    <div class="content">
                        {{category.eventCategoryDescription}}
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
 
<style>
</style>