<script setup>
import { ref, computed } from "vue";
import moment from 'moment';
import Datepicker from '@vuepic/vue-datepicker';

defineEmits([
    'delete', 
    'edit', 
    'toEditingMode', 
    'cancelEdit', 
    'showDetail', 
    'closeEdited', 
    'showFile', 
    'downloadFile',
    'removeFile',
    'toEditingMode'
])
const props = defineProps({
    eventList: {
        type: Array,
        require: true,
    },
    overlap:{
        type: Boolean,
        defalut: false
    },
    edited:{
        type: Boolean,
        default: false
    },    
    errorPast:{
        type: Boolean,
        default: false
    },
    detail:{
        type: Boolean,
        default: false
    },
    fileById:{
        require: true
    },
    currentEvent: {
        type: Object,
        default: {}
    },
});


const role = localStorage.getItem('role');

const DetailPopUp = ref(false);
const editingMode = ref(false)
const showIndex = ref(null);
const confirmDelete = ref(false)
const deleted = ref(false)
const showDeleted = () => {
    confirmDelete.value = false
    deleted.value = true
    DetailPopUp.value = false
}

const newEvent = computed(() => {
    return {
        id: props.currentEvent.id,
        bookingEmail: props.currentEvent.bookingEmail,
        eventCategory: props.currentEvent.eventCategory,
        eventDuration: props.currentEvent.eventDuration,
        eventStartTime: props.currentEvent.eventStartTime,
        eventNotes: props.currentEvent.eventNotes
    }
})

function formateTime(date) {
    const options = {hour: "numeric", minute: "numeric"};
    return new Date(date).toLocaleString("th-TH", options);
}

const editfileupload = ref()
let dataTransfer = new DataTransfer()

const uploadFile = (e) => {
	let maxFileSize = 10 * 1024 * 1024 //10MB
	if (e.target.files[0].size > maxFileSize) {
		let fileInput = document.getElementById('fileInput')
		fileInput.setCustomValidity('The file size cannot be larger than 10 MB.')
		fileInput.reportValidity()
        console.log('1');
		if (editfileupload.value === undefined || editfileupload.value === '') {
			clearInput()
            console.log('2');

		} else {
			dataTransfer.items.clear()
			dataTransfer.items.add(editfileupload.value)
			fileInput.files = dataTransfer.files
            console.log(dataTransfer);
            console.log(editfileupload);
            console.log('3');
		}
	} else {
        editfileupload.value = e.target.files[0]
		fileInput.setCustomValidity('')
        console.log('4');
	}
}

const clearInput = () => {
    let fileInput = document.getElementById('fileInput')
	fileInput.type = 'text'
	fileInput.type = 'file'
	fileupload.value = ''
	dataTransfer.items.clear()
}

const deleteFileAlert = ref(false)
const deletedFile = ref(false)
const showDeletedFile = () => {
    deleteFileAlert.value = false
    deletedFile.value = true
}
</script>

<template>
    <div class="body" >
        <div class="card" v-for="event in eventList" :key="event.id" style="margin-top: 1vw;">
            <div class="card-body" style="box-shadow: 0 2px 2px #00000005, 0 4px 4px #0000000a; font-size: 0.9vw;">
                <!-- <img src="../assets/calendar.png" class="calendar-img"/> -->
                <img src="../assets/project.png" class="calendar-img" v-if="event.eventCategory.id === 1"/>
                <img src="../assets/devops.png" class="calendar-img" v-if="event.eventCategory.id === 2"/>
                <img src="../assets/database.png" class="calendar-img" v-if="event.eventCategory.id === 3"/>
                <img src="../assets/front.png" class="calendar-img" v-if="event.eventCategory.id === 4"/>
                <img src="../assets/back.png" class="calendar-img" v-if="event.eventCategory.id === 5"/>
                <b style="font-size: 1.1vw;">{{ event.bookingName.slice(0, 40) }} <a v-if="event.bookingName.length > 40">...</a></b>
                <p style="margin-top: 0.5vw;"><b>Category :</b> {{ event.eventCategory.eventCategoryName }}</p>
                <p><b>Duration :</b> {{ event.eventDuration }} Minutes</p>
                <p style="margin-bottom: 0.8vw;"><b>Date :</b> {{ moment(event.eventStartTime).format('ddd, D MMM YYYY') }}  {{ formateTime(event.eventStartTime) }}</p>
                <button class="btn detail-Btn" 
                    v-on:click="showIndex = event.id , DetailPopUp = true"
                    @click="$emit('showFile', event.id)"
                    style="font-weight: bold;">More ></button>
            </div>
        </div>

        <div>
            <div class="container" v-if="DetailPopUp == true">
                <ul>
                    <li v-for="event in eventList" :key="event.id" >
                        <div class="card-body " v-if="DetailPopUp == true">
                            <div class="card popDetail" style="width: 31.5vw;" v-if="showIndex === event.id">
                                <div class="card-title">
                                    <div class="card-header"
                                        style="color: #e74694; font-weight: bold; letter-spacing: 1px; font-size: 1vw;">EVENT DETAIL
                                    </div>
                                    <button class="close-detail" @click="$emit('showDetail')" v-on:click="showIndex = null , DetailPopUp = false">
                                        &times;
                                    </button>
                                </div>
                                <div class="card-body" v-if="showIndex === event.id" style="text-align: center; font-size: 0.95vw;">
                                    <b style="font-size: 1vw;">{{ event.bookingName }}</b><br />
                                    <p>{{ event.bookingEmail }}</p>
                                    <span style="font-weight: bold; color: #e74694;font-size: 1vw;">Clinic</span><br />
                                    {{ event.eventCategory.eventCategoryName }}<br />
                                    {{ moment(event.eventStartTime).format('ddd, D MMM YYYY')}} at
                                    {{ formateTime(event.eventStartTime) }}<br />
                                    {{ event.eventDuration }} minutes<br /><br />

                                    <p v-if="event.eventNotes == null || event.eventNotes == []" style="color: #a2a5aa">
                                        No Message
                                    </p>
                                    <p style="margin-bottom: 1vw;" v-else>
                                        <span style="font-weight: bold; color: #e74694">Note</span><br />{{
                                                event.eventNotes
                                        }}
                                    </p>

                                    <p style="font-weight: bold; color: #e74694;margin-bottom: 0.5vw;margin-top: 1.4vw;">File</p>
                                    <div v-if="fileById !== null && fileById[0] !== undefined" style="margin-bottom: 1vw;">
                                        <div class="input-group" style="width: 20vw; left: 18%;">
                                            <!-- <input type="text" class="form-control" v-model="fileById[0]" disabled> -->
                                            <div type="text" class="form-control" style="cursor:default; background-color: #e9ecef;height: 2vw;">
                                                {{fileById[0].slice(0, 25)}}
                                                <a v-if="fileById[0].length > 28">...</a>
                                            </div>
                                            <button 
                                                class="input-group-text btn btn-outline-success" 
                                                type="button" 
                                                @click="$emit('downloadFile', event.id)" 
                                                style="height: 2vw;">
                                                <font-awesome-icon icon="fa-solid fa-file-arrow-down" />
                                            </button>

                                            <button 
                                                class="input-group-text btn btn-outline-danger" 
                                                type="button" 
                                                style="height: 2vw;"
                                                v-on:click="deleteFileAlert = true">
                                                <font-awesome-icon icon="fa-solid fa-trash-can" />
                                            </button>
                                        </div>
                                    </div>

                                    <div v-else style="color:#a2a5aa;font-size: 0.8vw;margin-bottom: 0.5vw;">None</div>
                                    
                                    
                                    <!-- Edit -->
                                    <button v-if="role !== 'lecturer'" class="btn btn-warning edit-event-btn detail-btn-each" style="margin-right: 2vw;"
                                        v-on:click="editingMode = true" @click="$emit('toEditingMode', event)">Edit Appointment</button>
                                    <div class="containerV2" v-if="editingMode === true">
                                        <div class="card popEdit" style="width: 31.5vw; font-size: 0.95vw;" >
                                            <div class="card-body">
                                                <div class="card-title">
                                                    <div class="card-header"
                                                        style="color: #e74694; font-weight: bold; letter-spacing: 1px;font-size: 1vw;">
                                                        EDIT EVENT</div>
                                                </div>
                                                <div class="card-body" v-if="showIndex === event.id">
                                                    <b>{{ event.bookingName }}</b><br />
                                                    {{ event.bookingEmail }}<br /><br />
                                                    <span style="font-weight: bold; color: #e74694">Clinic</span><br />
                                                    {{ event.eventCategory.eventCategoryName }}<br />
                                                    <p v-if="errorPast && !overlap" class="error">Can not choose past time.</p>
                                                    <p v-if="overlap" class="error">Time is overlapping</p>
                                                    <Datepicker 
                                                        :minDate="new Date()" 
                                                        v-model="newEvent.eventStartTime"
                                                        class="datepicker mx-auto" 
                                                        :class="{'border border-danger' : overlap}"
                                                        style="margin-bottom: 10px; width: 70%;" 
                                                    />
                                                    <!-- <p class="noti">* If you not insert start date, The date will remain the same date</p> -->
                                                    {{ event.eventDuration }} minutes<br /><br />
                                                    <span style="font-weight: bold; color: #e74694"><p>Note :</p></span>
                                                    <textarea class="form-control style-form" style="width: 70%;" rows="3" maxlength="500" v-model="newEvent.eventNotes"></textarea>
                                                    
                                                    <div>
                                                        <span style="font-weight: bold; color: #e74694;"><p style="margin-top:1vw">File :</p></span>
                                                        <div v-if="fileById !== null && fileById[0] !== undefined" style="margin-bottom: 1vw;">
                                                            <div class="input-group mx-auto" style="width: 70%;">
                                                                <!-- <input type="text" class="form-control" v-model="fileById[0]" disabled> -->
                                                                <div type="text" class="form-control" style="cursor:default; background-color: #e9ecef;height: 2vw;">
                                                                    {{fileById[0].slice(0, 25)}}
                                                                    <a v-if="fileById[0].length > 28">...</a>
                                                                </div>
                                                                <button 
                                                                    class="input-group-text btn btn-outline-success" 
                                                                    type="button" 
                                                                    @click="$emit('downloadFile', event.id)" 
                                                                    style="height: 2vw;">
                                                                    <font-awesome-icon icon="fa-solid fa-file-arrow-down" />
                                                                </button>

                                                                <button 
                                                                    class="input-group-text btn btn-outline-danger" 
                                                                    type="button" 
                                                                    style="height: 2vw;"
                                                                    v-on:click="deleteFileAlert = true">
                                                                    <font-awesome-icon icon="fa-solid fa-trash-can" />
                                                                </button>
                                                            </div>
                                                        </div>

                                                        <div class="input-group mx-auto" style="width: 70%;">
                                                            <input 
                                                                id="fileInput"
                                                                type="file" 
                                                                class="form-control style-form" 
                                                                style="font-size:auto"                         
                                                                @change="uploadFile($event)"
                                                                v-on:change="fileById[0]"
                                                            >
                                                            <button class="btn btn-outline-secondary" style="height: 2vw;" @click="clearInput">
                                                                <!-- <font-awesome-icon icon="fa-solid fa-x" /> -->
                                                                <!-- <img class="clear-file-btn"
                                                                    src="https://api.iconify.design/mdi/close-thick.svg?color=%236c757d"
                                                                    style="width: 1vw"
                                                                /> -->
                                                                <b>x</b>
                                                            </button>
                                                        </div>     
                                                    </div>     
                                                    
                                                    <div style="margin-top: 30px;">
                                                        <button type="button" class="btn btn-success confirm-edit-btn"
                                                            style="margin-right: 40px;"
                                                            @click="$emit('edit', newEvent, editfileupload)"
                                                            >Submit</button>
                                                        <button type="button" class="btn btn-secondary"
                                                            style="font-size: 0.9vw;"
                                                            v-on:click="editingMode = false"
                                                            @click="$emit('cancelEdit')">Cancel</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- Delete -->
                                    <button v-if="role !== 'lecturer'" class="btn btn-danger cancel-event-btn detail-btn-each" v-on:click="confirmDelete = true">Cancel
                                        Appointment</button>
                                    <div class="containerV2" v-if="confirmDelete === true || deleted === true">
                                        <div class="card alert" v-if="confirmDelete === true">
                                            <div class="card-body">
                                                <img
                                                    src="https://api.iconify.design/akar-icons/circle-alert.svg?color=%23bb2d3b" style="width: 4.5vw">
                                                <p class="card-text" style="margin-top: 20px;"><b>Are you sure you want
                                                        to cancel event ?</b></p>
                                                <button type="button" class="btn btn-danger cancel-event-btn"
                                                    style="padding: 5px 20px 5px 20px;"
                                                    @click="$emit('delete', event.id)"
                                                    v-on:click="showDeleted">Sure</button>
                                                <button type="button" class="btn btn-secondary"
                                                    style="margin-left: 30px;" v-on:click="confirmDelete = false">Cancel</button>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- Delete File -->
                                    <div class="containerV2" v-if="deleteFileAlert === true || deletedFile === true">
                                        <div class="card alert" v-if="deleteFileAlert === true">
                                            <div class="card-body">
                                                <img
                                                    src="https://api.iconify.design/akar-icons/circle-alert.svg?color=%23bb2d3b" style="width: 4.5vw">
                                                <p class="card-text" style="margin-top: 20px;"><b>Are you sure you want
                                                        to delete file ?</b></p>
                                                <button type="button" class="btn btn-danger cancel-event-btn"
                                                    style="padding: 5px 20px 5px 20px;"
                                                    @click="$emit('removeFile', event.id)" 
                                                    v-on:click="showDeletedFile">Sure</button>
                                                <button type="button" class="btn btn-secondary"
                                                    style="margin-left: 30px;" v-on:click="deleteFileAlert = false">Cancel</button>
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
                <div class="card alertEdit" id="deleted">
                    <div class="card-body" style="margin-top: 10px;">
                        <img
                            src="https://api.iconify.design/healthicons/yes-outline.svg?color=%23198754&width=90&height=90">
                        <p class="card-text" style="margin-top: 10px;"><b>Deleted</b> Event Successfully</p>
                        <button type="button" class="btn btn-light" style="width: 100px; margin-top: 5px;"
                            v-on:click="deleted = false">OK</button>
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
                            v-on:click="edited = false, editingMode = false, editfileupload = null"
                            @click="$emit('closeEdited')">OK</button>
                    </div>
                </div>
            </div>

            <div class="containerV2" v-if="deletedFile === true">
                <div class="card alertEdit" id="deleted">
                    <div class="card-body" style="margin-top: 10px;">
                        <img
                            src="https://api.iconify.design/healthicons/yes-outline.svg?color=%23198754&width=90&height=90">
                        <p class="card-text" style="margin-top: 10px;"><b>Deleted</b> File Successfully</p>
                        <button type="button" class="btn btn-light" style="width: 100px; margin-top: 5px;"
                            v-on:click="deletedFile = false">OK</button>
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

.edit-event-btn {
    background-image: linear-gradient(to right, #ffb347 0%, #ffcc33  51%, #ffb347  100%);
    border-color: transparent;
    transition: 0.5s;
    background-size: 200% auto;
    color: white;
}
.edit-event-btn:hover{
  background-position: right center; /* change the direction of the change here */
  text-decoration: none;
  border-color: transparent;
}  
.calendar-img{
    float: left;    
    width: 10vw;
    margin-right: 1vw;
}
.error{
    color: red;
    font-size: 13px;
    text-align: left;
    margin-bottom: -8px;
}
.cancel-event-btn{
    background-image: linear-gradient(to right, #D31027 0%, #EA384D  51%, #D31027  100%);
    border-color: transparent;
    transition: 0.5s;
    background-size: 200% auto;
    color: white;
}
.cancel-event-btn:hover{
  background-position: right center; /* change the direction of the change here */
  color: #fff;
  text-decoration: none;
  border-color: transparent;
}  
.confirm-edit-btn{
    background-image: linear-gradient(
    to right,
    #1d976c 0%,
    #93f9b9 51%,
    #1d976c 100%
  );
  color: white;
  transition: 0.5s;
  border-color: transparent;
  background-size: 200% auto;
  font-size:0.95vw;
}

.confirm-edit-btn:hover{
  background-position: right center; /* change the direction of the change here */
  color: #fff;
  text-decoration: none;
  border-color: transparent;
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
    background-color: #fff;
    width: 28rem;
    color: black;
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
.noti{
    font-size: 13px;
    color:red;
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
    z-index: 2;
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
    /* font-size: 45px; */
    font-size: 2.3vw;
    font-weight: bold;
    margin-top: -2%;
}
.close-detail:hover {
    color: #e74694;
}
.detail-btn-each {
    margin-top: 1vw;
    margin-bottom: 0.8vw;
    font-size: 0.85vw;
}

.detail-Btn {
    text-align: center;
    font-size: 0.85vw;
    color: white; 
    cursor: pointer;
    transition: 0.5s;
    background-size: 200% auto;
    background-image: linear-gradient(to right, #4568DC 0%, #B06AB3  51%, #4568DC  100%);
    border-radius: 1vw;
    border-color: transparent;
}
.detail-Btn:hover {
    background-position: right center; /* change the direction of the change here */
    color: #fff;
    text-decoration: none;
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
    width: 90%;
    border-radius: 15px 15px 15px 15px;
    overflow: hidden;
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
    position: relative;
    text-align: center;
}
</style>