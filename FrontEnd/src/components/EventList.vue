<script setup>
import { ref } from "vue";
defineEmits(['deleteEvent'])
defineProps({
    eventList: {
        type: Array,
        require: true,
    },
});
let DetailBtn = ref(false);
const openDetail = () => {
    DetailBtn.value = true;
};
const closeDetail = () => {
    DetailBtn.value = false;
};

const showIndex = ref(null);
</script>

<template>
    <div class="body">
        <table class="table table-hover mx-auto align-middle" id="list-table">
            <thead class="table-dark align-middle">
                <tr>
                    <th scope="col" class="mx-auto" style="padding-left: 25px;">#</th>
                    <th scope="col" style="letter-spacing: 3px">DATE</th>
                    <th scope="col" style="letter-spacing: 3px">TIME</th>
                    <th scope="col" style="letter-spacing: 3px">CLINIC</th>
                    <th scope="col" style="letter-spacing: 3px">
                        DURATION<br />
                        <span style="font-size: 12px; letter-spacing: 2px; font-weight: lighter;">(MINUTES)</span>
                    </th>
                    <th scope="col" style="letter-spacing: 3px">NAME</th>
                    <th scope="col"></th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="(event, index) in eventList" :key="index">
                    <td scope="row" style="padding-left: 25px;"><b>{{ index + 1 }}</b></td>
                    <td>{{ event.eventStartTime.slice(0, 10) }}</td>
                    <td>{{ event.eventStartTime.slice(11, 16) }}</td>
                    <td>{{ event.eventCategory.eventCategoryName }}</td>
                    <td>{{ event.eventDuration }}</td>
                    <td>{{ event.bookingName }}</td>
                    <td>
                        <span class="detail-Btn" v-on:click="showIndex = index" @click="openDetail" style="padding-right: 15px; font-weight: bold;">More</span>
                    </td>
                </tr>
            </tbody>
        </table>
        <div>
            <div class="container" v-if="DetailBtn == true">
                <ul>
                    <li v-for="(event, index) in eventList" :key="index">
                        <div class="card-body-main" v-if="DetailBtn == true">
                            <div class="card" style="width: 38rem; height: 27rem;" v-if="showIndex === index">
                                <div class="card-title">
                                    <div class="card-header" style="color: #e74694; font-weight: bold; letter-spacing: 1px;">Event #{{ index + 1 }}</div>
                                    <button class="close-detail" @click="closeDetail" v-on:click="showIndex = null">
                                        &times;
                                    </button>
                                </div>
                                <div class="card-body" v-if="showIndex === index">
                                    {{ event.bookingName }}<br />
                                    {{ event.bookingEmail }}<br /><br />
                                    <span style="font-weight: bold; color: #e74694">Clinic</span><br />
                                    {{ event.eventCategory.eventCategoryName }}<br />
                                    {{ event.eventStartTime.slice(0, 10) }} at
                                    {{ event.eventStartTime.slice(11, 16) }}<br />
                                    {{ event.eventDuration }} minutes<br/><br/>

                                    <p v-if="event.eventNotes == null || event.eventNotes == [] " style="color: #a2a5aa">
                                        No Message
                                    </p>
                                    <p v-else>
                                        <span style="font-weight: bold; color: #e74694">Note</span><br />{{
                                                event.eventNotes
                                        }}
                                    </p>
                                    <button class="btn btn-danger" @click="$emit('deleteEvent',event.id)" style="margin-top: 17px;">Cancel Schedule</button>
                                </div>
                                
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter&family=Noto+Sans+Thai&display=swap');
.body{
    font-family: 'Inter','Noto Sans Thai';
}
.container {
    position: fixed;
    min-width: 100%;
    max-height: 100%;
    overflow-x: hidden;
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.5);
    background-repeat: repeat-x;
    
}

.card-body-main {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    border-radius: 10px;
    text-align: center;
    font-size: 18px; 
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
}

.card-title {
    padding: 10px 15px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: -30px;
}

.card-title .close-detail {
    cursor: pointer;
    border: none;
    outline: none;
    background: none;
    font-size: 35px;
    font-weight: bold;
}

.close-detail:hover {
    color: #e74694;
}

.detail-Btn {
    color: #e74694;
    cursor: pointer;
}

.detail-Btn:hover {
    text-decoration: underline;
}

ul {
    list-style-type: none;
}

td {
    height: 80px;
}

thead {
    height: 82px;
    color: white;
}

#list-table {
    width: 70%;
    border-radius: 15px 15px 15px 15px;
    overflow: hidden;
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
    position: relative;
    text-align: center;
}
</style>
