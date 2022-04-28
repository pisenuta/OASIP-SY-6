<script setup>
import { ref } from 'vue';
    defineProps({
        eventList:{
            type: Array,
            require: true
        }
    })
    let DetailBtn = ref(false)
    const openDetail = () => {
        DetailBtn.value = true
    }
    const close = () =>{
        DetailBtn.value = false
    }
</script>

<template>
    <div>
        <ul>
            <li v-for="(event,index) in eventList" :key="index">
                <p><b>นัดหมายที่ {{index+1}}</b></p>
            <div class="card-body-main">
                <div class="card" style="width: 45rem;">
                    <div class="card-body">
                        <b>วันที่ - เวลานัดหมาย :</b> {{event.eventStartTime}}<br>
                        <b>ระยะเวลา :</b> {{event.eventDuration}} นาที <br>
                        <b>คลีนิก :</b> {{event.eventCategory}}<br>
                        <b>ชื่อผู้นัดหมาย :</b> {{event.bookingName}}<br>
                        <button @click="openDetail" v-if="DetailBtn == false">รายละเอียด</button>   
                        <button @click="close" v-if="DetailBtn == true">ซ่อน</button>    
                        <div class="card-detail" v-if="DetailBtn == true">
                            <b>Email :</b> {{event.bookingEmail}}<br>
                            <b>Note : </b>{{event.eventNotes}}<br>
                        </div> 
                    </div>
                </div>    
            </div>         
            </li>
        </ul>
        
    </div>
</template>

<style scoped>
.card-body-main{
    display: flex;
    justify-content: center;
    margin-bottom: 30px;
}
ul{
    list-style-type: none;
}
p{
    margin-left: 30%;
}
</style>