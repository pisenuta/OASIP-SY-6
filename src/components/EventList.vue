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
                นัดหมายที่ {{event.bookingId}}<br>
                คลีนิก : {{event.eventCategory}}<br>
                ชื่อผู้นัดหมาย : {{event.bookingName}}<br>
                Email : {{event.bookingEmail}}<br>
                รายละเอียด : {{event.eventNotes}}<br>
                วันที่ - เวลานัดหมาย : {{event.eventStartTime}}<br>
                ระยะเวลา : {{event.eventDuration}}
            </li>
        </ul>
    </div>
</template>

<style scoped>

</style>