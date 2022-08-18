<script setup>
import { computed } from 'vue'
defineEmits(['editUser','cancelEdit'])
const props = defineProps({
    userList:{
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
    showIndex:{
        type: Number,
        default: null
    },
    errorRole:{
        type: Boolean,
        default: false
    }
})

const newUser = computed(() => {
    return {
        userId: props.currentUser.userId,
        name: props.currentUser.name,
        email: props.currentUser.email,
        role: props.currentUser.role
    }
})
</script>
 
<template>
    <div class="card-body edit-user-popup">
        <div class="card edit-user-card" style="width: 38rem; height: 25rem;">
            <div class="card-title title-detail">
                <div class="card-header" style="color: #e74694; font-weight: bold; letter-spacing: 1px">
                  EDIT USER
                </div>
                <button class="close-detail" @click="$emit('cancelEdit')">
                  &times;
                </button>
              </div>
            <div class="card-body">
                <div style="text-align: center;">
                    <p class="label-clinic">Name :</p>
                    <input class="form-control clinic-form mb-3" maxlength="100" v-model="newUser.name"
                        :class="{ 'border border-danger': errorName || notUniqueName }">
                    <p class="error-clinic" v-if="errorName === true">Please enter Name.</p>
                    <p class="error-clinic" v-if="notUniqueName === true">Name must unique.</p>
                    <p class="label-clinic">Email :</p>
                    <input class="form-control clinic-form mb-3" maxlength="50" v-model="newUser.email"
                        :class="{ 'border border-danger': errorEmail || notUniqueEmail }">
                    <p class="error-clinic" v-if="errorEmail === true">Please enter Email.</p>
                    <p class="error-clinic" v-if="notUniqueEmail === true">Email must unique.</p>
                    <p class="error-clinic" v-if="invaildEmail === true">Invaild Email.</p>
                    <p class="label-clinic">Role :</p>
                    <select class="form-select style-form" style="width: 25rem;"
                        :class="{ 'border border-danger': errorRole }" v-model="newUser.role">
                        <option disabled selected>Select Role Below</option>
                        <option v-for="(user, index) in userList" :key="index" :value="user">{{ user.role }}</option>
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
}

.edit-user-popup {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
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
    font-size: 14px;
    margin-top: -10px;
    margin-left: 15.5%;
}
</style>