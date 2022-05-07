<script setup>
import { ref, onBeforeMount, computed } from 'vue'
import ManageAdd from './ManageAdd.vue'

const categories = ref([])
const getEventCategory = async () => {
    const res = await fetch('http://10.4.56.123:8080/api/eventcategory')
    // const res = await fetch('http://localhost:8080/api/eventcategory')
    // const res = await fetch('http://localhost:5000/eventCategory')
    if (res.status === 200) {
        categories.value = await res.json()
    }
}
onBeforeMount(async () => {
    await getEventCategory()
})

const createEvent = async (story) => {
    if ((story.title == null || story.story == null) || (story.title == '' || story.story == '')) {
        error.value = true
        return
    }

    const res = await fetch(`http://localhost:5000/stories`, {
        method: 'POST',
        headers: { 'content-Type': 'application/json' },
        body: JSON.stringify(story)
    })
    if (res.status == 201) {
        const addedStory = await res.json()
        list.value.push(addedStory)
        currStory.value = {}
        error.value = false
        console.log('added successfully');

    } else {
        console.log('error, cannot add');
    }
}

</script>
 
<template>
    <div class="body">
        <h3 class="mx-auto mt-5" style="font-size: 40px;font-weight: bolder;">Add Event</h3>    
        <ManageAdd :categoryList="categories" />  
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