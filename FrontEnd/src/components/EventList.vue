<script setup>
import { ref } from "vue";
import moment from 'moment';
defineEmits(['delete','edit'])
defineProps({
    eventList: {
        type: Array,
        require: true,
    },
});
const DetailBtn = ref(false);
const openDetail = () => {
    DetailBtn.value = true;
};
const closeDetail = () => {
    DetailBtn.value = false;
};

const showIndex = ref(null);
const deleteAlert = ref(false)
const showAlert = () => { deleteAlert.value = true }
const hideAlert = () => { deleteAlert.value = false }
const deleted = ref(false)
const showDeleted = () => {
    deleteAlert.value = false
    deleted.value = true
    DetailBtn.value = false
}
const closeDeleted = () => {
    deleted.value = false
}
</script>

<template>
    <div class="body">
        <table class="table table-hover mx-auto align-middle" id="list-table">
            <thead class="table-dark align-middle">
                <tr>
                    <th scope="col" class="mx-auto" style="padding-left: 25px;">#</th>
                    <th scope="col" style="letter-spacing: 3px;">DATE</th>
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
                    <td>{{moment(event.eventStartTime).format('ddd, D MMM YYYY')}} </td>
                    <td>{{ event.eventStartTime.slice(11, 16) }}</td>
                    <td>{{ event.eventCategory.eventCategoryName }}</td>
                    <td>{{ event.eventDuration }}</td>
                    <td style="width: 40%;">{{ event.bookingName }}</td>
                    <td>
                        <span class="detail-Btn" v-on:click="showIndex = index" @click="openDetail"
                            style="padding-right: 15px; font-weight: bold;">More</span>
                    </td>
                </tr>
            </tbody>
        </table>
        <div>
            <div class="container" v-if="DetailBtn == true">
                <ul>
                    <li v-for="(event, index) in eventList" :key="index">
                        <div class="card-body-main" v-if="DetailBtn == true">
                            <div class="card" style="width: 38rem; " v-if="showIndex === index">
                                <div class="card-title">
                                    <div class="card-header"
                                        style="color: #e74694; font-weight: bold; letter-spacing: 1px;">Event #{{ index
                                                + 1
                                        }}</div>
                                    <button class="close-detail" @click="closeDetail" v-on:click="showIndex = null">
                                        &times;
                                    </button>
                                </div>
                                <div class="card-body" v-if="showIndex === index">
                                    {{ event.bookingName }}<br />
                                    {{ event.bookingEmail }}<br /><br />
                                    <span style="font-weight: bold; color: #e74694">Clinic</span><br />
                                    {{ event.eventCategory.eventCategoryName }}<br />
                                    {{moment(event.eventStartTime).format('ddd, D MMM YYYY')}} at
                                    {{ event.eventStartTime.slice(11, 16) }}<br />
                                    {{ event.eventDuration }} minutes<br /><br />

                                    <p v-if="event.eventNotes == null || event.eventNotes == []" style="color: #a2a5aa">
                                        No Message
                                    </p>
                                    <p v-else>
                                        <span style="font-weight: bold; color: #e74694">Note</span><br />{{
                                                event.eventNotes
                                        }}
                                    </p>
                                    <button class="btn btn-warning detail-btn-each" style="margin-right: 40px;" @click="$emit('edit', event)">Edit Appointment</button>
                                    <button class="btn btn-danger detail-btn-each" @click="showAlert">Cancel Appointment</button>
                                    <div class="containerV2" v-if="deleteAlert === true || deleted === true">
                                        <div class="card alert" v-if="deleteAlert === true">
                                            <div class="card-body">
                                                <img
                                                    src="https://api.iconify.design/akar-icons/circle-alert.svg?color=white&width=75&height=75">
                                                <p class="card-text" style="margin-top: 20px;"><b>Are you sure you want
                                                        to cancel event #{{ index + 1 }} ?</b></p>
                                                <button type="button" class="btn btn-warning"
                                                    style="padding: 5px 20px 5px 20px;"
                                                    @click="$emit('delete', event.id)"
                                                    v-on:click="showDeleted">OK</button>
                                                <button type="button" class="btn btn-secondary"
                                                    style="margin-left: 30px;" @click="hideAlert">Cancel</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
            <div class="containerV2" v-if="deleted === true">
                <div class="card deleted" id="deleted">
                    <div class="card-body" style="margin-top: 10px;">
                        <img
                            src="https://api.iconify.design/healthicons/yes-outline.svg?color=white&width=90&height=90">
                        <p class="card-text" style="margin-top: 10px;"><b>Deleted</b> Event Successfully</p>
                        <button type="button" class="btn btn-light" style="width: 100px; margin-top: 5px;"
                            @click="closeDeleted">OK</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter&family=Noto+Sans+Thai&display=swap');

.body {
    font-family: 'Inter', 'Noto Sans Thai';
}

.card .alert {
    background-color: #bb2d3b;
    width: 28rem;
    color: white;
}

.deleted {
    width: 28rem;
    height: 15.5rem;
    background-color: #198754;
    color: white;
}

.containerV2 {
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
    background-color: rgba(0, 0, 0, 0);
    background-repeat: repeat-x;
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
    font-size: 45px;
    font-weight: bold;
    margin-top: -2%;
}

.close-detail:hover {
    color: #e74694;
}

.detail-btn-each{
    margin-top: 17px;
    margin-bottom: 10px;
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
