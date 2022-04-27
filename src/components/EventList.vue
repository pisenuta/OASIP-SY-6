<script setup>
    defineProps({
        eventList:{
            type: Array,
            require: true
        }
    })
</script>

<template>
    <div>
        <ul>
            <li v-for="(event,index) in eventList" :key="index">
                <p><b>นัดหมายที่ {{event.bookingId}}</b></p>
            <div class="card-body-main">
                <div class="card" style="width: 45rem;">
                    <div class="card-body">
                        <b>วันที่ - เวลานัดหมาย :</b> {{event.eventStartTime}}<br>
                        <b>ระยะเวลา :</b> {{event.eventDuration}} นาที <br>
                        <b>คลีนิก :</b> {{event.eventCategory}}<br>
                        <b>ชื่อผู้นัดหมาย :</b> {{event.bookingName}}        
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