<script setup>
import { ref } from 'vue'
import Datepicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css';

defineEmits(['create'])
defineProps({
    categoryList: {
        type: Array,
        require: true
    },
    userList: {
        type: Array,
        require: true
    },
    errorName: {
        type: Boolean,
        default: false
    },
    errorClinic: {
        type: Boolean,
        default: false
    },
    errorEmail: {
        type: Boolean,
        default: false
    },
    errorTime: {
        type: Boolean,
        default: false
    },
    errorFuture: {
        type: Boolean,
        default: false
    },
    mailVali: {
        type: Boolean,
        default: false
    },
    mailNotFound: {
        type: Boolean,
        default: false
    },
    overlap: {
        type: Boolean,
        default: false
    }
})

const userRole = localStorage.getItem("role")
const userEmail = localStorage.getItem("email")

const newEvent = ref()

const adminEvent = ref({
    user: {email: ""},
    eventCategory: {},
    eventNotes: "",
    eventDuration: "",
    eventStartTime: ""
})

const studentEvent = ref({
    user: {email:userEmail},
    bookingName: "",
    eventCategory: {},
    eventNotes: "",
    eventDuration: "",
    eventStartTime: ""
})

const guestEvent = ref({
    user: null,
    bookingName: "",
    bookingEmail: "",
    eventCategory: {},
    eventNotes: "",
    eventDuration: "",
    eventStartTime: ""
})

const check = () => {
    if(userRole == 'admin'){
        newEvent.value = adminEvent.value
    } else if (userRole == 'student') {
        newEvent.value = studentEvent.value;
    } else if (userRole == 'guest') {
        newEvent.value = guestEvent.value;
    }
}

check();

const fileupload = ref()

const clearInput = () => {
    let fileInput = document.getElementById('fileInput')
	fileInput.type = 'text'
	fileInput.type = 'file'
	fileupload.value = ''
	dataTransfer.items.clear()
}

// const maxFileError = ref(false)
// const uploadFile = () => {
//     let maxFileSize = 10 * 1024 * 1024 //10MB
//     if (fileupload.value.files[0] != undefined && fileupload.value.files[0].size > maxFileSize) {
//         console.log('too big')
//         let fileInput = document.getElementById('fileInput')
//         fileInput.setCustomValidity('The file size cannot be larger than 10 MB.')
//         fileInput.reportValidity()
//         fileInput.type = 'text'
//         fileInput.type = 'file'
//         maxFileError.value == true
//     } else {
//       maxFileError.value == false
//       fileInput.setCustomValidity('')
//     }
// }

let dataTransfer = new DataTransfer()

const uploadFile = (e) => {
	let maxFileSize = 10 * 1024 * 1024 //10MB
	if (e.target.files[0].size > maxFileSize) {
		let fileInput = document.getElementById('fileInput')
		fileInput.setCustomValidity('The file size cannot be larger than 10 MB.')
		fileInput.reportValidity()
        console.log('1');
		if (fileupload.value === undefined || fileupload.value === '') {
			clearInput()
            console.log('2');

		} else {
			dataTransfer.items.clear()
			dataTransfer.items.add(fileupload.value)
			fileInput.files = dataTransfer.files
            console.log(dataTransfer);
            console.log(fileupload);
            console.log('3');
		}
	} else {
		fileupload.value = e.target.files[0]
		fileInput.setCustomValidity('')
        console.log('4');
	}
}
</script>
<template>
    <div class="body">
        <div class="form mx-5">
            <div class="marginForm">
                <table class="booking-line">
                    <tr>
                        <th class="label-signup">Clinic :</th>
                    </tr>
                </table>
                <select class="form-select style-form" style="width: 50%;"
                    :class="{ 'border border-danger': errorClinic }" v-model="newEvent.eventCategory">
                    <option disabled selected>Select Clinic Below</option>
                    <option v-for="(category, index) in categoryList" :key="index" :value="category">{{
                            category.eventCategoryName
                    }}</option>
                </select>
                <div v-if="errorClinic" class="error">Please select a clinic.</div>
            </div>

            <div class="marginForm">
                <table class="booking-line">
                    <tr>
                        <th class="label-signup">Name :</th>
                        <th v-if="userRole !== 'admin'" class="maxBooking" :class="{ 'maxinput': newEvent.bookingName.length == 100 }">
                            <span v-text="newEvent.bookingName.length"></span>/100
                        </th>
                    </tr>
                </table>

                <select v-if="userRole === 'admin'" class="form-select style-form" style="width: 50%;"
                    :class="{ 'border border-danger': errorName }" v-model="newEvent.user">
                    <option disabled selected>Select User Below</option>
                    <option v-for="(user, index) in userList" :key="index" :value="user">{{
                            user.name
                    }}</option>
                </select> 

                <input v-else class="form-control style-form" id="name" maxlength="100" v-model="newEvent.bookingName"
                    :class="{ 'border border-danger': errorName }">

                <div v-if="errorName" class="error"> Please enter name.</div>
            </div>

            <div class="marginForm">
                <table class="booking-line">
                    <tr>
                        <th class="label-signup">Email :</th>
                        <th v-if="userRole === 'guest'" class="maxBooking" :class="{ 'maxinput': newEvent.bookingEmail.length == 50 }">
                            <span v-text="newEvent.bookingEmail.length"></span>/50
                        </th>
                    </tr>
                </table>
                <input v-if="userRole === 'guest'" class="form-control style-form" id="email" maxlength="50" v-model="newEvent.bookingEmail"
                    :class="{ 'border border-danger': errorEmail || !mailVali }">

                <input v-else class="form-control style-form" id="email" maxlength="50" :value="newEvent.user.email" disabled readonly
                    :class="{ 'border border-danger': errorEmail || !mailVali }">

                <!-- <span v-if="!(userRole === 'admin')">{{ userEmail }}</span>  -->
                <!-- <input class="form-control style-form" id="email" maxlength="50" v-model="newEvent.bookingEmail"
                    :class="{ 'border border-danger': errorEmail || !mailVali }"> -->
                <div v-if="errorEmail" class="error">Please enter Email.</div>
                <div v-if="!mailVali && !errorEmail" class="error">Invalid Email.</div>
                <div v-if="mailNotFound" class="error">Not found email account.</div>
            </div>

            <div class="marginForm">
                <table class="booking-line">
                    <tr>
                        <th class="label-signup">Date - Time :</th>
                    </tr>
                </table>
                <Datepicker locale="th" :minDate="new Date()" v-model="newEvent.eventStartTime"
                    :class="{ 'border border-danger': errorTime || errorFuture || overlap }" class="datepicker"
                    style="font-size: 0.95vw;">
                </Datepicker>
                <div v-if="errorTime" class="error">Please choose start time.</div>
                <div v-if="errorFuture && !errorTime" class="error">Can not choose past time.</div>
                <div v-if="overlap" class="error">Time is overlapping !</div>
            </div>

            <div class="marginForm">
                <table class="booking-line">
                    <tr>
                        <th class="label-signup">Durations (minutes) :</th>
                    </tr>
                </table>
                <input class="form-control style-form" style="margin-top: 8px;" type="text" disabled readonly
                    :value="newEvent.eventCategory.eventDuration">
            </div>

            <div class="marginForm">
                <table class="booking-line">
                    <tr>
                        <th class="label-signup">Note :</th>
                        <th class="maxBooking" :class="{ 'maxinput': newEvent.eventNotes.length == 500 }">
                            <span v-text="newEvent.eventNotes.length"></span>/500
                        </th>
                    </tr>
                </table>
                <textarea class="form-control style-form" rows="3" maxlength="500"
                    v-model="newEvent.eventNotes"></textarea>
            </div>

            <div class="marginForm">
                <div class="input-group mx-auto" style="width:50%">
                    <input 
                        id="fileInput"
                        type="file" 
                        class="form-control style-form" 
                        style="font-size:auto"
                        @change="uploadFile($event)"
                    >
                    <button class="btn btn-outline-secondary" style="height: 2vw;" @click="clearInput">
                        <!-- <font-awesome-icon icon="fa-solid fa-trash-can" /> -->
                        <!-- <img class="clear-file-btn"
                            src="https://api.iconify.design/mdi/close-thick.svg?color=%236c757d"
                            style="width: 1vw"
                        /> -->
                        <b>x</b>
                    </button>
                </div>
            </div>
        </div>
        <div style="text-align: center;">
            <button type="button" class="btn btn-dark mx-auto addEventBtn" @click="$emit('create', newEvent,fileupload)">
                Booking
            </button>
        </div>

    </div>
</template>
 
<style>
@import url('https://fonts.googleapis.com/css2?family=Inter&family=Noto+Sans+Thai&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Radio+Canada:wght@600&display=swap');

.marginForm{
    margin-bottom: 1vw;
}
.error {
    color: red;
    font-size: 0.75vw;
    margin-left: 25%;
}

.dp__input{
    font-size: 1vw;
    border-color: transparent;
    height: 2vw;
    border: 1px solid #ced4da;
}
.dp__input:hover{
    border-color: #ced4da;
}
.maxBooking{
    width: 37vw;
    text-align: right;
    font-size: 0.7vw;
    color: #888888;
    font-weight: 100;
}
.booking-line{
    margin-left: 25%;
    margin-bottom: 0.5vw;
}
.booking-label{
    font-size: 1vw;
    width: 10vw;
}
.body {
    font-family: 'Inter';
    font-size: 17px;
}

.addEventBtn {
    background-image: linear-gradient(to right, #f857a6 0%, #ff5858 51%, #f857a6 100%);
    transition: 0.5s;
    background-size: 200% auto;
    box-shadow: 0 0 20px #eee;
    border-color: transparent;
    font-size: 1vw;
    color: #fff;
}

.addEventBtn:hover {
    background-position: right center;
    /* change the direction of the change here */
    color: #fff;
    text-decoration: none;
    border-color: transparent;
}

h3 {
    font-family: 'Radio Canada';
}

.date-form {
    height: 38px;
    width: 50%;
    margin-top: 10px;
    margin-left: 25%;
    border-radius: 5px;
    border-color: #ced4da;
}

.datepicker {
    width: 50%;
    margin-top: 10px;
    margin-left: 25%;
    border-radius: 5px;
    border-color: #ced4da;
}

.style-form {
    width: 50%;
    margin: auto;
    margin-bottom: 0.1vw;
    height: 2vw;
}

input,
textarea {
    font-family: 'Inter', 'Noto Sans Thai';
}
</style>