<script setup>
import { ref } from 'vue'
defineEmits(['addUser', 'cancelAdd'])
defineProps({
    errorName: {
        type: Boolean,
        default: false
    },
    notUniqueName: {
        type: Boolean,
        default: false
    },
    invaildEmail: {
        type: Boolean,
        default: false
    },
    notUniqueEmail: {
        type: Boolean,
        default: false
    },
    errorEmail: {
        type: Boolean,
        default: false
    },
    errorRole: {
        type: Boolean,
        default: false
    },
    errorPass: {
        type: Boolean,
        default: false
    },
    notMatch: {
        type: Boolean,
        default: false
    },
    errorConfirm: {
        type: Boolean,
        default: false
    },
    passLess:{
        type: Boolean,
        default: false
    },
    added: {
        type: Boolean,
        default: false
    }
})

const newUser = ref({
    name: "",
    email: "",
    password:"",
    confirmPassword: "",
    role: ""
})

</script>
 
<template>
    <div class="card-body add-user-popup">
        <div class="card add-user-card" style="width: 31.6vw;">
            <div class="card-title title-detail">
                <div class="card-header" style="color: #e74694; font-weight: bold; letter-spacing: 1px;font-size: 0.95vw;">
                    ADD USER
                </div>
                <button class="close-detail" style="font-size: 2.5vw;" @click="$emit('cancelAdd')">
                    &times;
                </button>
            </div>
            <div class="card-body">
                <div style="text-align: center; margin-top: 10px;">
                    <div>
                        <p class="label-clinic">Name :
                            <p class="maxtext" :class="{ 'maxinput': newUser.name.length == 100 }">
                                <span v-text="newUser.name.length"></span>/100
                            </p>
                        </p>
                    </div>
                    <input class="form-control clinic-form mb-3" maxlength="100" v-model="newUser.name"
                        :class="{ 'border border-danger': errorName || notUniqueName }">
                    <p class="error-clinic" v-if="errorName === true">Please enter Name.</p>
                    <p class="error-clinic" v-if="notUniqueName === true">Name must unique.</p>

                    <div>
                        <p class="label-clinic">Email :
                            <p class="maxtext" :class="{ 'maxinput': newUser.email.length == 50 }">
                                <span v-text="newUser.email.length"></span>/50
                            </p>
                        </p>
                    </div>
                    <input class="form-control clinic-form mb-3" maxlength="50" v-model="newUser.email"
                        :class="{ 'border border-danger': errorEmail || notUniqueEmail || invaildEmail }">
                    <p class="error-clinic" v-if="errorEmail === true">Please enter Email.</p>
                    <p class="error-clinic" v-if="notUniqueEmail === true && errorEmail === false">Email must unique.
                    </p>
                    <p class="error-clinic" v-if="invaildEmail === true && errorEmail === false">Invaild Email.</p>
                    
                    <div>
                        <p class="label-clinic">Password :
                            <p class="maxtext" :class="{ 'maxinput': newUser.password.length == 14 }">
                                <span v-text="newUser.password.length"></span>/14
                            </p>
                        </p>
                        
                    </div>
                    <input class="form-control clinic-form mb-3" minlength="8" maxlength="14" v-model="newUser.password"
                        type="password"
                        :class="{ 'border border-danger': errorPass || passLess}">
                    <p class="error-clinic" v-if="errorPass === true">Please enter Password.</p>
                    <p class="error-clinic" v-if="passLess === true && errorPass === false">Password must be between 8 and 14 characters.</p>

                    <div>
                        <p class="label-clinic">Confirm Password :
                            <p class="maxtext" :class="{ 'maxinput': newUser.confirmPassword.length == 14 }">
                                <span v-text="newUser.confirmPassword.length"></span>/14
                            </p>
                        </p>                  
                    </div>
                    <input class="form-control clinic-form mb-3" maxlength="14" v-model="newUser.confirmPassword"
                        type="password"
                        :class="{ 'border border-danger': errorConfirm || notMatch}">
                    <p class="error-clinic" v-if="errorConfirm === true && notMatch === false">Please confirm Password.</p>
                    <p class="error-clinic" v-if="notMatch === true">Password not match.</p>

                    <p class="label-clinic">Role :</p>
                    <select class="form-select style-form" style="width: 20.5vw;height: 2vw; margin-top: -10px;margin-bottom: 0.5vw;"
                        :class="{ 'border border-danger': errorRole }" v-model="newUser.role">
                        <option disabled selected>Select Role Below</option>
                        <option value="admin">Admin</option>
                        <option value="lecturer">Lecturer</option>
                        <option value="student">Student</option>
                    </select>
                    <p class="error-clinic" style="margin-top:5px; margin-bottom: 0;" v-if="errorRole === true">Please select your role.
                    </p>
                </div>
            </div>
            <button type="button" class="save-btn-grad" @click="$emit('addUser', newUser)">Save</button>
        </div>
    </div>
</template>
 
<style>
.add-user-card {
    border-radius: 20px;
}

.add-user-popup {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    animation: animate 0.3s ease-in-out;
}

@keyframes animate {
  0% {
    opacity: 0;
    transition: all 0.2s ease-in-out;
    transform: scale(0.1);
    transform: translate(-50%, -50%);
  }

  100% {
    opacity: 1;
    transition: all 0.2s ease-in-out;
    transform: scale(1);
    transform: translate(-50%, -50%);

  }
}

.save-btn-grad {
    background-image: linear-gradient(to right, #1D976C 0%, #93F9B9 51%, #1D976C 100%);
    font-size: 0.95vw;
    text-align: center;
    text-transform: uppercase;
    transition: 0.5s;
    background-size: 200% auto;
    color: white;
    box-shadow: 0 0 20px #eee;
    border-radius: 0px 0px 20px 20px;
    display: block;
    border: 0px;
}

.save-btn-grad:hover {
    background-position: right center;
    /* change the direction of the change here */
    color: #fff;
    text-decoration: none;
}

.label-clinic {
    text-align: left;
    margin-left: 12%;
}

.error-clinic {
    color: red;
    text-align: left;
    font-size: 0.75vw;
    margin-top: -10px;
    margin-left: 13%;
}

.maxtext {
    font-size: 0.7vw;
    text-decoration: none;
    color: #888888;
    pointer-events: none;
    text-align: right;
    margin:0;
    margin-top: -4%;
    margin-right: 20%;
    padding-bottom: 10px;
}

.maxinput {
    color: red;
}
</style>
