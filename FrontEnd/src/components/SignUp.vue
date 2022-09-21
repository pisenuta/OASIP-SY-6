<script setup>
import { ref, onBeforeMount } from "vue";
import addUser from "../components/Register.vue";


const newUser = ref({
    name: "",
    email: "",
    password:"",
    confirmPassword: "",
    role: ""
})

const addUserPop = ref(false);
const addedUser = ref(false);
const notUniqueAddName = ref(false);
const notUniqueAddEmail = ref(false);
const errorAddName = ref(false);
const errorAddEmail = ref(false);
const errorAddRole = ref(false);
const errorAddPass = ref(false);
const invaildAddEmail = ref(false);
const notMatch = ref(false);
const errorConfirm = ref(false);
const passLess = ref(false);
const cancelAdd = () => {
  addUserPop.value = false;
  notUniqueAddName.value = false;
  notUniqueAddEmail.value = false;
  errorAddName.value = false;
  errorAddRole.value = false;
  errorAddEmail.value = false;
  invaildAddEmail.value = false;
  errorAddPass.value = false;
  notMatch.value = false;
  errorConfirm.value = false;
  passLess.value = false;
};

const createUser = async (user) => {
  if (user.name == null || user.name == "") {
    errorAddName.value = true;
  } else {
    errorAddName.value = false;
  }

  if (user.password.length < 8) {
    passLess.value = true;
  } else {
    passLess.value = false;
  }
  if (
    (user.password == null || user.password == "") &&
    (user.confirmPassword == null || user.confirmPassword == "")
  ) {
    errorAddPass.value = true;
    errorConfirm.value = true;
  } else if (
    (user.password !== null || user.password !== "") &&
    (user.confirmPassword == null || user.confirmPassword == "")
  ) {
    errorAddPass.value = false;
    errorConfirm.value = true;
  } else if (
    (user.password !== null || user.password !== "") &&
    (user.confirmPassword !== null || user.confirmPassword !== "")
  ) {
    if (user.confirmPassword === user.password) {
      notMatch.value = false;
      errorConfirm.value = false;
    } else {
      notMatch.value = true;
    }
  }

  if (
    users.value.find((u) => user.name.trim() === u.name.trim()) &&
    user.name !== editingUser.value.name
  ) {
    notUniqueAddName.value = true;
  } else {
    notUniqueAddName.value = false;
  }
  if (
    users.value.find((u) => user.email.trim() === u.email.trim()) &&
    user.email !== editingUser.value.email
  ) {
    notUniqueAddEmail.value = true;
  } else {
    notUniqueAddEmail.value = false;
  }
  if (user.email == null || user.email == "") {
    errorAddEmail.value = true;
  } else {
    errorAddEmail.value = false;
  }
  if (user.role == null || user.role == "") {
    errorAddRole.value = true;
  } else {
    errorAddRole.value = false;
  }
  var emailValidate =
    /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

  if (user.email.match(emailValidate)) {
    invaildAddEmail.value = false;
  } else {
    invaildAddEmail.value = true;
  }

  if (
    errorAddName.value == true ||
    notUniqueAddName.value == true ||
    notUniqueAddEmail.value == true ||
    errorAddEmail.value == true ||
    errorAddRole.value == true ||
    invaildAddEmail.value == true ||
    errorAddPass.value == true ||
    notMatch.value == true ||
    passLess.value == true
  ) {
    return;
  }

  const res = await fetch(`${import.meta.env.VITE_BASE_URL}users/register`, {
    // const res = await fetch(`http://localhost:8443/api/users/`, {
    // const res = await fetch(`https://intproj21.sit.kmutt.ac.th/sy6/api/users`, {
    method: "POST",
    headers: { 
      "content-Type": "application/json"
    },
    body: JSON.stringify({
      name: user.name.trim(),
      email: user.email.trim(),
      password: user.password,
      role: user.role,
    }),
  });
  if (res.status == 201 || res.status == 200) {
    const addedEvent = await res.json();
    users.value.push(addedEvent);
    getUser();
    addedUser.value = true;
    console.log("added successfully");
  } else if (res.status == 400) {
    console.log("error, can not add");
  }
};
</script>
 
<template>
<div class="body">
    <h3
      class="mx-auto"
      style="font-size: 2.1vw;font-weight: bolder; margin-top: 2.5vw;"
    >
      Sign Up
    </h3>
    <div class="form" style="margin-top:1.5vw;">
        <div class="marginForm">
            <table class="signup-line">
                <tr>
                    <th class="label-signup">Name :</th>
                    <th class="maxSignup" :class="{ 'maxinput': newUser.name.length == 100 }">
                        <span v-text="newUser.name.length"></span>/100
                    </th>
                </tr>
            </table>
        <input class="form-control signup-form" maxlength="100" v-model="newUser.name"
            :class="{ 'border border-danger': errorName || notUniqueName }">
        <p class="error-clinic" v-if="errorName === true">Please enter Name.</p>
        <p class="error-clinic" v-if="notUniqueName === true">Name must unique.</p>
        </div>

        <div class="marginForm">
            <table class="signup-line">
                <tr>
                    <th class="label-signup">Email :</th>
                    <th class="maxSignup" :class="{ 'maxinput': newUser.email.length == 50 }">
                        <span v-text="newUser.email.length"></span>/50
                    </th>
                </tr>
            </table>
            <input class="form-control signup-form" maxlength="50" v-model="newUser.email"
                :class="{ 'border border-danger': errorEmail || notUniqueEmail || invaildEmail }">
            <p class="error-clinic" v-if="errorEmail === true">Please enter Email.</p>
            <p class="error-clinic" v-if="notUniqueEmail === true && errorEmail === false">Email must unique.</p>
            <p class="error-clinic" v-if="invaildEmail === true && errorEmail === false">Invaild Email.</p>       
        </div>

        <div class="marginForm">
            <table class="signup-line">
                <tr>
                    <th class="label-signup">Password :</th>
                    <th class="maxSignup" :class="{ 'maxinput': newUser.password.length == 14 }">
                        <span v-text="newUser.password.length"></span>/14
                    </th>
                </tr>
            </table>
            <input class="form-control signup-form" minlength="8" maxlength="14" v-model="newUser.password"
                type="password" :class="{ 'border border-danger': errorPass || passLess}">
            <p class="error-clinic" v-if="errorPass === true">Please enter Password.</p>
            <p class="error-clinic" v-if="passLess === true && errorPass === false">Password must be between 8 and 14 characters.</p>
        </div>

        <div class="marginForm">
            <table class="signup-line">
                <tr>
                    <th class="label-signup">Confirm Password :</th>
                    <th class="maxSignup" :class="{ 'maxinput': newUser.confirmPassword.length == 14 }">
                        <span v-text="newUser.confirmPassword.length"></span>/14
                    </th>
                </tr>
            </table>
            <input class="form-control signup-form" maxlength="14" v-model="newUser.confirmPassword"
                type="password" :class="{ 'border border-danger': errorConfirm || notMatch}">
            <p class="error-clinic" v-if="errorConfirm === true && notMatch === false">Please confirm Password.</p>
            <p class="error-clinic" v-if="notMatch === true">Password not match.</p>
        </div>

        <div class="marginForm">
            
        </div>
    </div>

        <!-- 
                    <div>
                        <p class="label-clinic label-add">Confirm Password :
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

                    <p class="label-clinic" style="margin-bottom: 1vw;">Role :</p>
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
        </div> -->
    </div>
</template>
 
<style>
.signup-form{
    width: 40%;
    margin: auto;
    margin-bottom: 0.1vw;
    height: 2vw;
}
.label-signup {
    font-weight: bold;
    font-size: 1vw;
    width: 10vw;
}
.maxSignup{
    width: 29.5vw;
    text-align: right;
    font-size: 0.7vw;
    color: #888888;
    font-weight: 100;
}
.maxinput {
    color: red;
}
.signup-line{
    margin-left: 30%;
    margin-bottom: 0.5vw;
}
</style>