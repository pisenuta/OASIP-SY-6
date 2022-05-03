<script setup>
    import {ref, onBeforeMount} from 'vue'
    import moment from 'moment'
    const categories = ref([])
    const getEventCategory= async () =>{ 
        const res = await fetch('http://localhost:5000/eventCategory') 
        if(res.status === 200){
            categories.value = await res.json()
        }
    }
    onBeforeMount(async () =>{
        await getEventCategory()
  })
</script>
 
<template>
    <div class="body">
        <div class="mb-3">
            <label for="clinic" class="form-label">คลีนิก :</label>
            <select class="form-select">
                <option selected>เลือกคลีนิก</option>
                <option v-for="(category,index) in categories" :key="index">{{category.eventCategoryName}}</option>
            </select>           
        </div>       
        <div class="mb-3">
            <label for="name" class="form-label">ชื่อผู้นัดหมาย :</label>
            <input class="form-control" id="name">
        </div>
        <div class="mb-3">
            <label for="email" class="form-label">Email :</label>
            <input class="form-control" id="email">
        </div>
        <div class="mb-3">
            <label for="meeting-time">วันที่ - เวลานัดหมาย :</label>
            <input type="datetime-local" id="meeting-time" name="meeting-time">
        </div>
        <div class="input-group mb-3">
            <p>ระยะเวลา :</p>
        </div>
        <div class="form-floating">
            <textarea class="form-control" placeholder="Leave a comment here" id="floatingTextarea"></textarea>
            <label for="floatingTextarea">Note :</label>
        </div>
        <button type="button" class="btn btn-primary">นัดหมาย</button>
    </div>
</template>
 
<style>

</style>