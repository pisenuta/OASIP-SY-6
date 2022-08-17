<script setup>
import { ref, onBeforeMount } from "vue";
const users = ref([]);

const getUser = async () => {
  // const res = await fetch(`${import.meta.env.VITE_BASE_URL}users` , {
  const res = await fetch(`http://localhost:8080/api/users`, {
    method: "GET",
  });
  if (res.status === 200) {
    users.value = await res.json();
  }
};

onBeforeMount(async () => {
  await getUser();
});
</script>

<template>
  <div class="body">
    <h3 class="mx-auto mt-5" style="font-size: 40px; font-weight: bolder">
      User
    </h3>
    <div v-if="users.value === 0">
      <p style="color: #646464">No User</p>
    </div>
    <button class="btn-grad mx-auto">Add User</button>
    <div class="mt-5">
      <div class="row mx-auto row-cols-4">
        <div
          class="col-user"
          v-for="(user, index) in users"
          :key="index"
          :value="user"
        >
          <div class="card-body user-body" style="width: 25rem">
            <img src="../assets/cat.png" style="height: 200px" />
            <h5 class="username">{{ user.name }}</h5>
            <p>{{ user.email }}</p>
            <p style="color: #646464">{{ user.role }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- <div class="container" v-if="addUser == true"> -->
    <div class="container">
      <div class="card" id="center-popup" style="width: 30rem; height: 22rem">
        <h3 id="popup-head">Add User</h3>
        <div style="text-align: left">
          <p class="adduser-pop">Name :</p>
          <input class="form-control popup-form" maxlength="100" />
          <p class="adduser-pop">Email :</p>
          <input class="form-control popup-form" maxlength="50" />
          <p class="adduser-pop">Role :</p>
          <select class="form-select popup-form">
            <option selected>Open this select menu</option>
            <option value="1">Admin</option>
            <option value="2">Student</option>
            <option value="3">Lecturer</option>
          </select>
        </div>
      </div>
    </div>
  </div>
</template>

<style>
.col-user {
  transition: all 0.2s ease-in-out;
}

.col-user:hover {
  transform: scale(1.04);
}

#popup-head {
  margin-top: 20px;
  color: #e74694;
}
.adduser-pop {
  margin-left: 20px;
}
.popup-form {
  width: 25em;
  margin: auto;
  margin-top: -10px;
  margin-bottom: 20px;
}

.user-body {
  background-color: #ffffff;
  border-radius: 5px;
  margin-bottom: 30px;
  margin-left: auto;
  margin-right: auto;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
  text-align: center;
  font-size: 18px;
}
.username {
  color: #e74694;
  font-weight: bold;
}

.btn-grad {
  background-image: linear-gradient(
    to right,
    #f857a6 0%,
    #ff5858 51%,
    #f857a6 100%
  );
  margin-top: 20px;
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
}

.btn-grad:hover {
  background-position: right center; /* change the direction of the change here */
  color: #fff;
  text-decoration: none;
}
</style>
