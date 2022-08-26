<script setup>
import { ref, onBeforeMount } from "vue";

const users = ref([]);
const getUser = async () => {
  // const res = await fetch(`http://intproj21.sit.kmutt.ac.th/sy6/api/users`, {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}users` , {
    method: "GET",
  })
  if (res.status === 200) {
    users.value = await res.json();
  }
}

onBeforeMount(async () => {
  await getUser();
});

const userLogin = ref({
  email: "",
  password: "",
})

const clear = () =>{
  userLogin.value.email = ""
  userLogin.value.password = ""
}

const match = ref(false)
const noMatch = ref(false)
const noEmail = ref(false)
const noPass = ref(false)
const passlessthen8 = ref(false)
const plzEmail = ref(false)
const matchPassword = async (user) => {
  if(user.email === null || user.email === ""){
    plzEmail.value = true
  } else {
    plzEmail.value = false
  }
  if(user.password.length < 8){
    passlessthen8.value = true
  } else {
    passlessthen8.value = false
  }
  if(user.password === null || user.password === ""){
    noPass.value = true
  } else {
    noPass.value = false
  }

  if (plzEmail.value == true || noEmail.value == true || noPass.value == true || passlessthen8.value == true) {
    return
  }
  // const res = await fetch(`http://intproj21.sit.kmutt.ac.th/sy6/api/match`, {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}match` , {
    method: 'POST',
    headers: { 'content-Type': 'application/json' },
    body: JSON.stringify({
      email: user.email.trim(),
      password: user.password
    })
  })
  if (res.status == 200) {
    match.value = true
  } else if (res.status == 401) {
    noMatch.value = true
  } else if (res.status == 404){
    noEmail.value = true
  }
}
</script>
 
<template>
  <div class="body">
    <div class="login-center">
      <h1 class="login-head mx-auto">LOGIN</h1>
      <input class="form-control clinic-form mb-3 input-email" maxlength="50" v-model="userLogin.email" placeholder="Email"
       :class="{ 'border border-danger': noEmail || plzEmail }">
      <p class="error-login" v-if="noEmail === true && plzEmail === false">Email does not exist !</p>
      <p class="error-login" v-if="plzEmail === true">Please enter email !</p>
      <input class="form-control clinic-form mb-3" maxlength="14" type="password" style="margin-top:10px;"
        placeholder="Password" v-model="userLogin.password" :class="{ 'border border-danger': noPass }">
        <p class="error-login" v-if="noPass === true">Please enter password !</p>
        <p class="error-login" v-if="passlessthen8 === true && noPass === false">Password must be between 8 and 14 character !</p>
      <button type="button" class="btn btn-secondary btn-login mx-auto"
        @click="matchPassword(userLogin)">Login</button>
    </div>

    <!-- match -->
    <div class="container" v-if="match === true">
      <div class="card deleted card-login" >
        <div class="card-body" style="margin-top: 10px;">
          <img src="https://api.iconify.design/healthicons/yes-outline.svg?color=%23198754" style="width: 90px">
          <p class="card-text" style="margin-top: 10px;">Password <b>Matched</b></p>
          <button type="button" class="btn btn-light btn-grad-ok" style="width: 100px; margin-top: 5px;"
          v-on:click="match = false" @click="clear()">OK</button>
        </div>
      </div>
    </div>

    <!-- not match -->
    <div class="container" v-if="noMatch === true">
      <div class="card deleted card-login">
        <div class="card-body" style="margin-top: 10px;">
          <img src="https://api.iconify.design/akar-icons/circle-x.svg?color=%23ea384d" style="width: 90px">
          <p class="card-text" style="margin-top: 10px; color: #D31027;">Password <b>Not Matched</b></p>
          <button type="button" class="btn btn-light btn-no-match" style="width: 100px; margin-top: 5px;"
          v-on:click="noMatch = false">OK</button>
        </div>
      </div>
    </div>
  </div>
</template>
 
<style>
.card-login{
  animation: animate 0.4s ease-in-out;
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
.login-center {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  justify-content: center;
}
.error-login{
  font-size: 15px;
  color: #D31027;
  margin: 0px;
}
.btn-no-match {
  background-image: linear-gradient(to right, #D31027 0%, #EA384D 51%, #D31027 100%);
  color: white;
  transition: 0.5s;
  border-color: transparent;
  background-size: 200% auto;
}

.btn-no-match:hover {
  background-position: right center;
  /* change the direction of the change here */
  color: #fff;
  text-decoration: none;
  border-color: transparent;
}
.login-head {
  margin-bottom: 30px;
}

.btn-login {
  background-image: linear-gradient(to right,
      #f857a6 0%,
      #ff5858 51%,
      #f857a6 100%);
  margin-top: 25px;
  padding: 10px 30px;
  text-align: center;
  text-transform: uppercase;
  transition: 0.5s;
  background-size: 200% auto;
  color: white;
  box-shadow: 0 0 20px #eee;
  border-radius: 10px;
  display: block;
  border: 0px;
  letter-spacing: 2px;

}

.btn-login:hover {
  background-position: right center;
  /* change the direction of the change here */
  color: #fff;
  text-decoration: none;
}
</style>