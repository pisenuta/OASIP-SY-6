<script setup>
import { ref } from "vue";
import moment from 'moment';
import Datepicker from '@vuepic/vue-datepicker';
defineEmits(['delete', 'edit', 'toEditingMode'])
const props = defineProps({
    eventList: {
        type: Array,
        require: true,
    }
});
const DetailBtn = ref(false);
const openDetail = () => {
    DetailBtn.value = true;
};
const closeDetail = () => {
    DetailBtn.value = false;
};
const edit = ref(false)
const editMode = () => {
    edit.value = true
}
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
const edited = ref(false)
const editTime = ref("")
const editNote = ref("")
const editEvent = (event) => {
    if(editTime.value === null || editTime.value === ''){
        event.eventNotes = editNote.value
    } else {
        event.eventStartTime = editTime.value
        event.eventNotes = editNote.value
        return event
    } 
}
const resetEditData = () => {
    editTime.value = ""
    editNote.value = ""
}
function formateTime(date) {
    const options = {hour: "numeric", minute: "numeric"};
    return new Date(date).toLocaleString("th-TH", options);
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
                    <td>{{ moment(event.eventStartTime).format('ddd, D MMM YYYY') }} </td>
                    <td>{{ formateTime(event.eventStartTime) }}</td>
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
                        <div class="card-body " v-if="DetailBtn == true">
                            <div class="card popDetail" style="width: 38rem;" v-if="showIndex === index">
                                <div class="card-title">
                                    <div class="card-header"
                                        style="color: #e74694; font-weight: bold; letter-spacing: 1px;">Event #{{ index
                                                + 1
                                        }}</div>
                                    <button class="close-detail" @click="closeDetail" v-on:click="showIndex = null">
                                        &times;
                                    </button>
                                </div>
                                <div class="card-body" v-if="showIndex === index" style="text-align: center;">
                                    {{ event.bookingName }}<br />
                                    {{ event.bookingEmail }}<br /><br />
                                    <span style="font-weight: bold; color: #e74694">Clinic</span><br />
                                    {{ event.eventCategory.eventCategoryName }}<br />
                                    {{ moment(event.eventStartTime).format('ddd, D MMM YYYY')}} at
                                    {{ formateTime(event.eventStartTime) }}<br />
                                    {{ event.eventDuration }} minutes<br /><br />

                                    <p v-if="event.eventNotes == null || event.eventNotes == []" style="color: #a2a5aa">
                                        No Message
                                    </p>
                                    <p v-else>
                                        <span style="font-weight: bold; color: #e74694">Note</span><br />{{
                                                event.eventNotes
                                        }}
                                    </p>
                                    <!-- Edit -->
                                    <button class="btn btn-warning detail-btn-each" style="margin-right: 40px;"
                                        v-on:click="editMode">Edit Appointment</button>
                                    <div class="containerV2" v-if="edit === true">
                                        <div class="card popEdit" style="width: 38rem;" >
                                            <div class="card-body">
                                                <div class="card-title">
                                                    <div class="card-header"
                                                        style="color: #e74694; font-weight: bold; letter-spacing: 1px;">
                                                        Event #{{ index + 1 }}</div>
                                                </div>
                                                <div v-if="showIndex === index">
                                                    {{ event.bookingName }}<br />
                                                    {{ event.bookingEmail }}<br /><br />
                                                    <span style="font-weight: bold; color: #e74694">Clinic</span><br />
                                                    {{ event.eventCategory.eventCategoryName }}<br />
                                                    <Datepicker 
                                                        :minDate="new Date()" 
                                                        v-model="editTime"
                                                        class="datepicker" 
                                                        style="margin-bottom: 10px;" 
                                                    />
                                                    <!-- <input type="datetime-local"
                                                        :min="new Date().toISOString().split('T')[0] + `T00:00`"
                                                        id="meeting-time" name="meeting-time" class="date-form mx-auto"
                                                        style="margin-bottom: 10px;" v-model="editTime"><br> -->
                                                    {{ event.eventDuration }} minutes<br /><br />
                                                    <p>Note :</p>
                                                    <textarea class="form-control style-form" rows="3" maxlength="500" v-model="editNote"></textarea>
                                                    <div style="margin-top: 30px;">
                                                        <button type="button" class="btn btn-success"
                                                            style="margin-right: 40px;"
                                                            v-on:click="edited = true"
                                                            @click="$emit('edit', editEvent(event), resetEditData())"
                                                            >Submit</button>
                                                        <button type="button" class="btn btn-secondary"
                                                            v-on:click="edit = false"
                                                            @click="resetEditData()">Cancel</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- Delete -->
                                    <button class="btn btn-danger detail-btn-each" @click="showAlert">Cancel
                                        Appointment</button>
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
            <div class="containerV2" v-if="edited === true">
                <div class="card alertEdit">
                    <div class="card-body" style="margin-top: 10px;">
                        <img
                            src="https://api.iconify.design/healthicons/yes-outline.svg?color=%23198754&width=90&height=90">
                        <p class="card-text" style="margin-top: 10px;"><b>Edited</b> Event Successfully</p>
                        <button type="button" class="btn btn-light" style="width: 100px; margin-top: 5px;"
                            v-on:click="edited = false, edit = false">OK</button>
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
.popEdit{
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    border-radius: 10px;
    text-align: center;
    font-size: 18px;
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
}
.alert {
    background-color: #bb2d3b;
    width: 28rem;
    color: white;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
}
.alertEdit{
    width: 28rem;
    color: white;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
    text-align: center;
    color: black;
}
.deleted {
    width: 28rem;
    height: 15.5rem;
    background-color: #198754;
    color: white;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.5);
    text-align: center;
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
.popDetail {
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
.detail-btn-each {
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