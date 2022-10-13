<script setup>
import { ref } from "vue";

const userLogin = ref({
  email: "",
  password: "",
});

const clear = () => {
  userLogin.value.email = "";
  userLogin.value.password = "";
  noMatch.value = false;
  noEmail.value = false;
  window.location.href = "/sy6";
};

const token = ref();
const match = ref(false);
const noMatch = ref(false);
const noEmail = ref(false);
const noPass = ref(false);
const passlessthen8 = ref(false);
const wrongEmail = ref(false);
const errorLogin = ref(false);
const plzEmail = ref(false);
const matchPassword = async (user) => {
  if (user.email === null || user.email === "") {
    plzEmail.value = true;
  } else {
    plzEmail.value = false;
  }
  var emailValidate =
    /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

  if (user.email.match(emailValidate)) {
    wrongEmail.value = false;
  } else {
    wrongEmail.value = true;
  }
  if (user.password.length < 8) {
    passlessthen8.value = true;
  } else {
    passlessthen8.value = false;
  }
  if (user.password === null || user.password === "") {
    noPass.value = true;
  } else {
    noPass.value = false;
  }
  if (
    plzEmail.value == true ||
    noPass.value == true ||
    passlessthen8.value == true ||
    wrongEmail.value == true
  ) {
    return;
  }

  const res = await fetch(`${import.meta.env.VITE_BASE_URL}login`, {
    method: "POST",
    headers: { "content-Type": "application/json" },
    body: JSON.stringify({
      email: user.email,
      password: user.password,
    }),
  });
  if (res.status == 200) {
    match.value = true;
    token.value = await res.json();
    saveLocal();
  } else if (res.status == 401) {
    noMatch.value = true
    noEmail.value = false
    // errorLogin.value = true;
  } else if (res.status == 404) {
    noEmail.value = true
  }
};

const saveLocal = () => {
  localStorage.setItem("token", `${token.value.access_token}`);
  localStorage.setItem("refreshToken", `${token.value.refresh_token}`);
  localStorage.setItem("email", `${token.value.email}`);
  localStorage.setItem("role", `${token.value.role}`);
};

let showPassword = ref(false);
const toggleShow = () => {
  showPassword.value = !showPassword.value;
}

</script>

<template>
  <div class="body">
    <div class="login-center">
      <div class="card shadow-lg o-hidden border-0 my-5" style="width: 30vw">
        <div class="card-body p-0">
          <div class="row">
            <div class="col-lg-12">
              <div class="p-5">
                <div class="text-center">
                  <h4 class="text-dark mb-4" style="font-size: 1.25vw;">Welcome Back!</h4>
                </div>
                <form class="user">
                  <div class="mb-3">
                    <input
                      class="form-control"
                      maxlength="50"
                      v-model="userLogin.email"
                      placeholder="Email"
                      :class="{
                        'styleError':
                          noEmail || plzEmail || wrongEmail || errorLogin,
                      }"
                      style="font-size: 0.85vw"
                    />
                    <!-- <input id="email" class="form-control form-control-user" type="email" aria-describedby="emailHelp" placeholder="Email" name="email" required v-model="userLogin.email"
                                      :class="{ 'border border-danger': noEmail }"/> -->
                    <p
                      class="error-login"
                      v-if="
                        noEmail === true &&
                        plzEmail === false &&
                        wrongEmail === false
                      "
                    >
                      Email does not exist !
                    </p>
                    <p class="error-login" v-if="plzEmail === true">
                      Please enter email !
                    </p>
                    <p
                      class="error-login"
                      v-if="wrongEmail === true && plzEmail === false"
                    >
                      Invaild Email !
                    </p>
                  </div>
                  <div class="mb-3">
                    <div class="input-group mx-auto">
                      <input 
                        :type="showPassword ? 'text' : 'Password'"
                        class="form-control" 
                        minlength="8" maxlength="14" 
                        v-model="userLogin.password"
                        placeholder="Password" 
                        :class="{ 
                          'styleError': 
                          noPass || noMatch || errorLogin || passlessthen8
                        }"
                      >
                      <button class="btn btn-outline-primary" 
                        :class="(noPass || noMatch || errorLogin || passlessthen8) ? 'btn-outline-danger' : 'btn-outline-primary'"
                        @click="toggleShow" 
                        style="border-radius: 0 0.375rem 0.375rem 0;"
                      >
                        <div>
                          <font-awesome-icon icon="fa-solid fa-eye" v-if="showPassword"/>
                        <font-awesome-icon icon="fa-solid fa-eye-slash" v-if="!showPassword"/>
                        </div>
                        
                      </button>
                    </div>

                    <p class="error-login" v-if="noPass === true">
                      Please enter password !
                    </p>
                    <p class="error-login" v-if="noMatch === true">
                      Password Incorrect !
                    </p>
                    <p
                      class="error-login"
                      v-if="passlessthen8 === true && noPass === false"
                    >
                      Password must be between 8 and 14 character !
                    </p>
                    <p class="error-login" v-if="errorLogin === true">
                      Your password is incorrect or this account doesn't exist.
                    </p>
                  </div>
                  <button
                    type="button"
                    class="btn btn-secondary d-block btn-user w-100 btn-login"
                    @click="matchPassword(userLogin)"
                  >
                    Login
                  </button>
                  <hr />
                </form>
                <!-- <div class="text-center"><a class="small" href="forgot-password.html">Forgot Password?</a></div>  -->
                <router-link to="/signup">
                  <div class="text-center" style="font-size:0.9vw">
                    <a class="small">Create an Account!</a>
                  </div>
                </router-link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- match -->
    <div class="container" v-if="match === true">
      <div class="card deleted card-login" style="padding: 0.5vw;">
        <div class="card-body body-canLogin">
          <img
            src="https://api.iconify.design/healthicons/yes-outline.svg?color=%23198754"
            style="width: 4.5vw"
          />
          <p class="card-text" style="margin-top: 10px; margin-bottom: 1vw">
            Login <b>Successful</b>
          </p>
          <button
            type="button"
            class="btn btn-light btn-grad-ok"
            style="width: 5vw; height: 2.3vw"
            v-on:click="match = false"
            @click="clear()"
          >
            OK
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style>
.card-login {
  animation: animate 0.4s ease-in-out;
}

.container-login {
  position: absolute;
  left: 0;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  -ms-transform: translateY(-50%);
  -moz-transform: translateY(-50%);
  -webkit-transform: translateY(-50%);
  -o-transform: translateY(-50%);
}

.styleError{
  border-color: red;
}

.login-input {
  border: 1px solid #ced4da;
  padding-left: 0.5vw;
  font-size: 0.85vw;
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
.error-login {
  font-size: 0.75vw;
  color: red;
  margin-top: 0.5vh;
}
.btn-no-match {
  background-image: linear-gradient(
    to right,
    #d31027 0%,
    #ea384d 51%,
    #d31027 100%
  );
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
  margin-bottom: 1.5vw;
  font-size: 2.1vw;
}

.btn-login {
  background-image: linear-gradient(
    to right,
    #f857a6 0%,
    #ff5858 51%,
    #f857a6 100%
  );
  margin-top: 1vw;
  padding: 0.5vw 1.5vw;
  font-size: 0.8vw;
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
