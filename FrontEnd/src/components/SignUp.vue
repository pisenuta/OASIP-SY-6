<script setup>
import { ref, onBeforeMount } from "vue";

const newUser = ref({
  name: "",
  email: "",
  password: "",
  confirmPassword: "",
  role: ""
})
const errorStatus = ref({
  filedErrors: { email: "", name: "" }
})

const addUserPop = ref(false);
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
    addUserPop.value = true;
    console.log("added successfully");
  } else if (res.status == 400) {
    errorStatus.value = await res.json()
    console.log(errorStatus.value);
  }
};

//showPassword
let showPassword = ref(false);
let showConfirmPassword = ref(false);
const toggleShow = () => {
  showPassword.value = !showPassword.value;
};
const toggleShowConfirm = () => {
  showConfirmPassword.value = !showConfirmPassword.value;
};

</script>
 
<template>
  <div class="body">
    <div class="login-center" style="top: 55%;">
      <div class="card shadow-lg o-hidden border-0 my-5" style="width: 35vw">
        <div class="card-body p-0">
          <div class="row">
            <div class="col-lg-12">
              <div class="p-5">
                <div class="text-center">
                  <h4 class="text-dark mb-4" style="font-size: 1.25vw;">Sign Up!</h4>
                </div>

                <form class="user">
                  <div class="signup-form">
                    <table class="signup-line">
                      <tr>
                        <!-- <th class="label-signup">Name :</th> -->
                        <th class="maxSignup" :class="{ 'maxinput': newUser.name.length == 100 }">
                          <span v-text="newUser.name.length"></span>/100
                        </th>
                      </tr>
                    </table>
                    <input class="form-control" maxlength="100" v-model="newUser.name" placeholder="Name"
                      :class="{ 'styleError': errorAddName || errorStatus.filedErrors.name }">
                    <p class="error-signup" v-if="errorAddName === true">Please enter Name.</p>
                    <p class="error-signup" v-if="errorStatus.filedErrors.name === 'User name is already exists'">Name
                      is already taken.</p>
                  </div>

                  <div class="signup-form">
                    <table class="signup-line">
                      <tr>
                        <!-- <th class="label-signup">Email :</th> -->
                        <th class="maxSignup" :class="{ 'maxinput': newUser.email.length == 50 }">
                          <span v-text="newUser.email.length"></span>/50
                        </th>
                      </tr>
                    </table>
                    <input class="form-control" maxlength="50" v-model="newUser.email" placeholder="Email"
                      :class="{ 'styleError': errorAddEmail || invaildAddEmail || errorStatus.filedErrors.email === 'Email is already exists' }">
                    <p class="error-signup" v-if="errorAddEmail === true">Please enter Email.</p>
                    <p class="error-signup" v-if="errorStatus.filedErrors.email === 'Email is already exists'">Looks
                      like you
                      already have a OASIP account with this email. <router-link to="/login" style="color:red">Login
                      </router-link>
                      instead?</p>
                    <p class="error-signup" v-if="invaildAddEmail === true && errorAddEmail === false">Invaild Email.
                    </p>
                    <p class="error-login" v-if="noPass === true">
                      Please enter password !
                    </p>
                  </div>

                  <div class="signup-form">
                    <table class="signup-line">
                      <tr>
                        <!-- <th class="label-signup">Password :</th> -->
                        <th class="maxSignup" :class="{ 'maxinput': newUser.password.length == 14 }">
                          <span v-text="newUser.password.length"></span>/14
                        </th>
                      </tr>
                    </table>

                    <div class="input-group mx-auto">
                      <input :type="showPassword ? 'text' : 'Password'" class="form-control" minlength="8" maxlength="14" v-model="newUser.password"
                        placeholder="Password" :class="{ 'styleError': errorAddPass || passLess}">
                      <button class="btn btn-outline-primary" 
                        :class="(errorAddPass || passLess) ? 'btn-outline-danger' : 'btn-outline-primary'"
                        @click="toggleShow" 
                        style="border-radius: 0 0.375rem 0.375rem 0;"
                      >
                        <font-awesome-icon icon="fa-solid fa-eye" v-if="showPassword"/>
                        <font-awesome-icon icon="fa-solid fa-eye-slash" v-if="!showPassword"/>
                      </button>
                    </div>
                    <p class="error-signup" v-if="errorAddPass === true">Please enter Password.</p>
                    <p class="error-signup" v-if="passLess === true && errorAddPass === false">Password must be between
                      8 and 14
                      characters.</p>
                  </div>

                  <div class="signup-form">
                    <table class="signup-line">
                      <tr>
                        <!-- <th class="label-signup">Confirm Password :</th> -->
                        <th class="maxSignup" :class="{ 'maxinput': newUser.confirmPassword.length == 14 }">
                          <span v-text="newUser.confirmPassword.length"></span>/14
                        </th>
                      </tr>
                    </table>

                    <div class="input-group mx-auto">
                      <input :type="showConfirmPassword ? 'text' : 'Password'" class="form-control" minlength="8" maxlength="14" v-model="newUser.confirmPassword"
                        placeholder="Confirm Password" :class="{ 'styleError': errorConfirm || notMatch}">
                      <button class="btn btn-outline-primary" 
                        :class="(errorConfirm || notMatch) ? 'btn-outline-danger' : 'btn-outline-primary'"
                        @click="toggleShowConfirm" 
                        style="border-radius: 0 0.375rem 0.375rem 0;"
                      >
                        <font-awesome-icon icon="fa-solid fa-eye" v-if="showConfirmPassword"/>
                        <font-awesome-icon icon="fa-solid fa-eye-slash" v-if="!showConfirmPassword"/>
                      </button>
                    </div>
                    <p class="error-signup" v-if="errorConfirm === true && notMatch === false">Please confirm Password.
                    </p>
                    <p class="error-signup" v-if="notMatch === true">Password not match.</p>
                  </div>

                  <div class="signup-form">
                    <table class="signup-line">
                      <tr>
                        <!-- <th class="label-signup">Role :</th> -->
                        <th class="maxSignup">
                          Select Role
                        </th>
                      </tr>
                    </table>

                    <select class="form-select" :class="{ 'styleError': errorAddRole }"
                      v-model="newUser.role"> 
                      <option disabled selected>Select Role Below</option>
                      <option value="admin">Admin</option>
                      <option value="lecturer">Lecturer</option>
                      <option value="student">Student</option>
                    </select>
                    <p class="error-signup" v-if="errorAddRole === true">Please select your role.</p>
                  </div>

                  <!-- <hr /> -->
                  <button type="button" class="btn btn-secondary d-block btn-user w-100 btn-login"
                    @click=createUser(newUser)>
                    Sign Up
                  </button>
                  <hr />
                </form>
                <router-link to="/login">
                  <div class="text-center" style="font-size:0.9vw">
                    <a class="small">Already have an Account!</a>
                  </div>
                </router-link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- signup success -->
    <div class="container" v-if="addUserPop === true">
      <div class="card deleted card-login">
        <div class="card-body body-canLogin" style="padding: 0;">
          <img src="https://api.iconify.design/healthicons/yes-outline.svg?color=%23198754" style="width: 4.5vw;">
          <p class="card-text" style="margin-top: 10px;margin-bottom: 1vw">Sign Up <b>Successful</b></p>
          <router-link to="/login"><button type="button" class="btn btn-light btn-grad-ok" style="width: 5vw; height: 2.3vw;"
            v-on:click="addUserPop = false">OK</button></router-link>
        </div>
      </div>
    </div>
  </div>
</template>
 
<style>
select {
  font-size: 16px;
}

.signup-form {
  margin-bottom: 2%;
}

.error-signup {
  color: red;
  font-size: 0.7vw;
  margin-top: 1%;
  margin-bottom: -0.5vw;
}

.label-signup {
  font-weight: bold;
  font-size: 1vw;
  width: 10vw;
}

.maxSignup {
  width: 29.5vw;
  text-align: right;
  font-size: 0.7vw;
  color: #888888;
  font-weight: 100;
}

.maxinput {
  color: red;
}

.signup-line {
  margin-left: 30%;
  margin-bottom: 0.5vw;
}
.form-control,.form-select{
  font-size: 0.82vw;
}
/* input{
  height: 2vw;
  font-size: 0.82vw;
}
select{
  height: 2vw;
  font-size: 0.82vw;
} */
</style>