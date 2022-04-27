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
                นัดหมายที่ {{event.id}}
                คลีนิก : {{event.eventCategory}}
                ชื่อผู้นัดหมาย : {{event.bookingName}}
                Email : {{event.bookingEmail}}
                รายละเอียด : {{event.eventNotes}}
                วันที่ - เวลานัดหมาย : {{event.eventStartTime}}
            </li>
        </ul>
    </div>
</template>

<style scoped>

</style>