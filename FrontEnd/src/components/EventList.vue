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
    const showIndex = ref(null)
</script>

<template>
    <div>
        <ul>
            <li v-for="(event,index) in eventList" :key="index">
                <p><b>นัดหมายที่ {{index+1}}</b></p>
            <div class="card-body-main">
                <div class="card" style="width: 45rem;">
                    <div class="card-body">
                        <b>วันที่ - เวลานัดหมาย :</b> {{event.eventStartTime.slice(0,10)}} {{event.eventStartTime.slice(11,19)}}<br>
                        <b>ระยะเวลา :</b> {{event.eventCategory.eventDuration}} นาที <br>
                        <b>คลีนิก :</b> {{event.eventCategory.eventCategoryName}}<br>
                        <b>ชื่อผู้นัดหมาย :</b> {{event.bookingName}}<br>

                        <div class="card-detail" v-if="DetailBtn == true" >
                            <div v-if="showIndex === index"> 
                                <b>Email :</b> {{event.bookingEmail}}<br>
                                <b>Note : </b>{{event.eventNotes}}<br>
                                <button type="button" class="btn btn-danger" @click="close" v-on:click="showIndex = null"> ซ่อน </button>
                            </div>      
                        </div> 
                        <button type="button" class="btn btn-primary" v-on:click="showIndex = index" @click="openDetail" v-if="showIndex !== index"> รายละเอียด </button>       
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
button{
    margin-top: 1%;
}

</style>