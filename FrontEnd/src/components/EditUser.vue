<script setup>
import { computed } from 'vue'
defineEmits(['editUser', 'cancelEdit'])
const props = defineProps({
    userList: {
        type: Array,
        require: true
    },
    currentUser: {
        type: Object,
        default: {}
    },
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
    editUserPop: {
        type: Boolean,
        default: false
    },
    showIndex: {
        type: Number,
        default: null
    },
    errorRole: {
        type: Boolean,
        default: false
    },
    edited: {
        type: Boolean,
        default: false
    }
})

const newUser = computed(() => {
    return {
        userId: props.currentUser.userId,
        name: props.currentUser.name,
        email: props.currentUser.email,
        role: props.currentUser.role,
        password: props.currentUser.password,
    }
})


</script>
 
<template>
    <div class="card-body edit-user-popup">
        <div class="card edit-user-card">
            <div class="card-title title-detail">
                <div class="card-header" style="color: #e74694; font-weight: bold; letter-spacing: 1px;font-size: 0.95vw;">
                    EDIT USER
                </div>
                <button class="close-detail" @click="$emit('cancelEdit')">
                    &times;
                </button>
            </div>
            <div class="card-body" style="margin-bottom: 10px;">
                <div style="text-align: center; margin-top: 10px;">

                    <p class="label-clinic">Name : </p>

                    <input class="form-control clinic-form mb-3" maxlength="100" v-model="newUser.name"
                        :class="{ 'border border-danger': errorName || notUniqueName }">
                    <p class="error-clinic" v-if="errorName === true">Please enter Name.</p>
                    <p class="error-clinic" v-if="notUniqueName === true">Name must unique.</p>

                    <p class="label-clinic">Email :

                    </p>

                    <input class="form-control clinic-form mb-3" maxlength="50" v-model="newUser.email"
                        :class="{ 'border border-danger': errorEmail || notUniqueEmail || invaildEmail }">
                    <p class="error-clinic" v-if="errorEmail === true">Please enter Email.</p>
                    <p class="error-clinic" v-if="notUniqueEmail === true && errorEmail === false">Email must unique.
                    </p>
                    <p class="error-clinic" v-if="invaildEmail === true && errorEmail === false">Invaild Email.</p>
                    <p class="label-clinic">Role :</p>
                    <select class="form-select style-form" style="width: 20.5vw;height: 2vw; margin-top: -10px;margin-bottom: 0.5vw;"
                        :class="{ 'border border-danger': errorRole }" v-model="newUser.role">
                        <option disabled selected>Select Role Below</option>
                        <option value="admin">Admin</option>
                        <option value="lecturer">Lecturer</option>
                        <option value="student">Student</option>
                    </select>
                    <p class="error-clinic" v-if="errorRole === true">Please select your role.</p>
                </div>
            </div>
            <button type="button" class="save-btn-grad" @click="$emit('editUser', newUser)">Save</button>
        </div>
    </div>
</template>
 
<style>
.edit-user-card {
    border-radius: 20px;
    width: 31.6vw;
}

.edit-user-popup {
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
    padding: 5px 30px;
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
    margin-left: 15.5%;
}
</style>
