<script setup>
import { ref, onBeforeMount } from "vue";
import moment from "moment";
import editUser from "../components/EditUser.vue";
import addUser from "../components/AddUser.vue";

const users = ref([]);

const newAccess = ref()
let token = localStorage.getItem("token")
const refreshToken = localStorage.getItem("refreshToken");

const RefreshToken = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}refresh-token`,{
      method: 'get',
      headers: {
        Authorization: `Bearer ${refreshToken}`
      }
    }
  );
  if (res.status === 200) {
    newAccess.value = await res.json()
    refresh()
    getUser()
  } else if (res.status === 401){
    localStorage.clear()
    window.location.href = "/sy6"
    console.log("plz log out");
  }
};

const refresh = () => {
  token = localStorage.setItem('token',`${newAccess.value.accessToken}`)
}

const noUser = () => {
  if (users.value.length == 0) {
    return "No Users";
  }
};

const cantReach = ref(false)

const getUser = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}users` , {
    method: "GET",
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
  if (res.status === 200) {
    cantReach.value = false
    users.value = await res.json();
    users.value.sort();
  } else if (res.status === 401 && token !== null){
    RefreshToken();
  } else if (res.status === 403){
    cantReach.value = true
  }
};

const removeUser = async (removeUserId) => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}users/${removeUserId}`,{ 
    method: "DELETE", 
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
});
  if (res.status === 200) {
    users.value = users.value.filter((user) => user.userId !== removeUserId);
    console.log("deleted successfully");
  } else if (res.status === 401 && token !== null){
    RefreshToken();
  }
};

onBeforeMount(async () => {
  await getUser();
});

const UserDetail = ref(false);
const checkDel = ref(false);
const showIndex = ref(null);
const deleted = ref(false);

const editUserPop = ref(false);
const editedUser = ref(false);
const notUniqueName = ref(false);
const notUniqueEmail = ref(false);
const errorName = ref(false);
const errorEmail = ref(false);
const errorRole = ref(false);
const invaildEmail = ref(false);
const remainSame = ref(false);
const editingUser = ref({});
const toEditingMode = (editUser) => {
  editingUser.value = editUser;
  console.log(editingUser.value);
};
const cancelEdit = () => {
  editUserPop.value = false;
  notUniqueName.value = false;
  notUniqueEmail.value = false;
  errorName.value = false;
  errorEmail.value = false;
  errorRole.value = false;
  invaildEmail.value = false;
};

const modifyUser = async (user) => {
  if (user.name == null || user.name == "") {
    errorName.value = true;
  } else {
    errorName.value = false;
  }
  if (
    users.value.find((u) => user.name.trim() === u.name.trim()) &&
    user.name !== editingUser.value.name
  ) {
    notUniqueName.value = true;
  } else {
    notUniqueName.value = false;
  }
  if (
    users.value.find((u) => user.email.trim() === u.email.trim()) &&
    user.email !== editingUser.value.email
  ) {
    notUniqueEmail.value = true;
  } else {
    notUniqueEmail.value = false;
  }
  if (user.email == null || user.email == "") {
    errorEmail.value = true;
  } else {
    errorEmail.value = false;
  }
  if (user.role == null || user.role == "") {
    errorRole.value = true;
  } else {
    errorRole.value = false;
  }
  var emailValidate =
    /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

  if (user.email.match(emailValidate)) {
    invaildEmail.value = false;
  } else {
    invaildEmail.value = true;
  }

  if (
    errorName.value == true ||
    notUniqueName.value == true ||
    notUniqueEmail.value == true ||
    errorEmail.value == true ||
    errorRole.value == true ||
    invaildEmail.value == true
  ) {
    return;
  }
  if (
    user.name == editingUser.value.name &&
    user.email == editingUser.value.email &&
    user.role == editingUser.value.role
  ) {
    remainSame.value = true;
    return;
  }

  const res = await fetch(`${import.meta.env.VITE_BASE_URL}users/${user.userId}`,{
      method: "PUT",
      headers: {
        "content-type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`
      },
      body: JSON.stringify({
        name: user.name,
        email: user.email,
        role: user.role,
        password: user.password,
      }),
    }
  );
  if (res.status === 200) {
    const modifyUser = await res.json();
    users.value = users.value.map((user) =>
      user.userId === modifyUser.userId
        ? {
            ...user,
            name: modifyUser.name.trim(),
            email: modifyUser.email.trim(),
            role: modifyUser.role,
            password: modifyUser.password,
          }
        : user
    );
    getUser();
    editedUser.value = true;
    editUserPop.value = false;
    remainSame.value = false;
    editingUser.value = {};
    console.log("edited successfully");
  } else if (res.status === 401 && token !== null){
    RefreshToken();
  }
};

function formateTime(date) {
  const options = { hour: "numeric", minute: "numeric" };
  return new Date(date).toLocaleString("th-TH", options);
}

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
    method: "POST",
    headers: { 
      "content-Type": "application/json",
      Authorization: `Bearer ${localStorage.getItem("token")}` 
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
  } else if (res.status === 401 && token !== null){
    RefreshToken();
  }
};

const userRole = localStorage.getItem("role")
</script>

<template>

  <div v-if="cantReach = true && userRole !== 'admin'" class="body">
    <div class="noUser mx-auto">
      <img src="../assets/403.gif" style="width: 40%; margin-left:25%;margin-bottom:-2vw"/>
      <img src="../assets/403-text.png" style="width: 100%;"/>
    </div>
  </div>

  <div v-else class="body">
    <h3
      class="mx-auto"
      style="font-size: 2.1vw; font-weight: bolder; margin-top: 2.5vw"
    >
      User
    </h3>
    <button class="btn-grad-add mx-auto" v-on:click="addUserPop = true">
      Add User
    </button>
    <div class="noUser mx-auto">
      <h5 style="color: #646464; font-size: 1.1vw">
        {{ noUser() }}
      </h5>
    </div>

    <!-- add -->
    <div>
      <div class="container" v-if="addUserPop == true">
        <addUser
          @cancelAdd="cancelAdd"
          :errorName="errorAddName"
          :errorEmail="errorAddEmail"
          :errorRole="errorAddRole"
          :notUniqueName="notUniqueAddName"
          :notUniqueEmail="notUniqueAddEmail"
          :invaildEmail="invaildAddEmail"
          :errorPass="errorAddPass"
          :notMatch="notMatch"
          :errorConfirm="errorConfirm"
          :passLess="passLess"
          @addUser="createUser"
        />
      </div>
    </div>
    
    <!-- plz login -->
    <div class="Plzlogin"
      v-if="token === null || token === undefined"
    >
      <div class="card alertPlzlogin">
        <div class="card-body" style="margin-top: 10px">
          <img
            src="https://api.iconify.design/clarity/warning-line.svg?color=%23efbc3c"
            style="width: 5.5vw"
          />
          <p class="card-text" style="margin-top: 0.5vw;margin-bottom: 1vw;">
            Please login to see user
          </p>
          <router-link to="/login"><button
            type="button"
            class="btn btn-warning btn-plzlogin mx-auto"
            style="margin-bottom: 1vw"
          >
            OK
          </button></router-link>
        </div>
      </div>
    </div>

    <!-- can add -->
    <div class="containerV2" v-if="addedUser === true">
      <div class="card alertEdit">
        <div class="card-body" style="margin-top: 10px">
          <img
            src="https://api.iconify.design/healthicons/yes-outline.svg?color=%23198754&"
            style="width: 4.5vw"
          />
          <p class="card-text" style="margin-top: 10px">
            <b>Added</b> User Successfully
          </p>
          <button
            type="button"
            class="btn btn-success btn-grad-ok mx-auto"
            style="margin-bottom: 1vw"
            v-on:click="(addedUser = false), (addUserPop = false)"
          >
            OK
          </button>
        </div>
      </div>
    </div>

    <!-- list -->
    <div class="mt-5">
      <div
        class="row mx-auto row-cols-4 overflow-auto"
        style="padding-left: 4vw; padding-right: 4vw; height: 67vh; padding-top: 1vw;
              margin-top: 0; "
      >
        <div
          class="col-user"
          v-for="user in users"
          :key="user.userId"
          :value="user"
        >
          <div class="card-body user-body" style="width: 19vw;">
            <img
              src="https://api.iconify.design/icomoon-free/bin.svg?color=%23e74694"
              class="delete-icon"
              v-on:click="(showIndex = user.userId), (checkDel = true)"
            />
            <div
              v-on:click="(showIndex = user.userId), (UserDetail = true)"
              style="cursor: pointer"
            >
              <div class="hovertext" data-hover="click for more details">
                <div v-if="user.role == 'student'">
                  <img src="../assets/cat.png" class="profile" />
                </div>

                <div v-if="user.role == 'lecturer'">
                  <img src="../assets/bear.png" class="profile" />
                </div>
                
                <div v-if="user.role == 'admin'">
                  <img src="../assets/cow.png" class="profile" />
                </div>
              
                <h5 class="username">
                  {{ user.name.slice(0, 27) }}
                  <a v-if="user.name.length > 30">...</a>
                </h5>
                <p>{{ user.email }}</p>
                <p style="color: #646464; padding-bottom: 20px">
                  {{ user.role }}
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- detail -->
    <div class="container" v-if="UserDetail == true || checkDel == true">
      <ul>
        <li v-for="user in users" :key="user.userId">
          <div class="card-body" v-if="UserDetail == true">
            <div
              class="card popUserDetail"
              style="width: 32%"
              v-if="showIndex === user.userId"
            >
              <div class="card-title title-detail">
                <div
                  class="card-header"
                  style="
                    color: #e74694;
                    font-weight: bold;
                    letter-spacing: 1px;
                    font-size: 0.95vw;
                  "
                >
                  USER DETAIL
                </div>
                <button
                  class="close-detail"
                  v-on:click="(showIndex = null), (UserDetail = false)"
                >
                  &times;
                </button>
              </div>
              <div
                class="card-body"
                v-if="showIndex === user.userId"
                style="text-align: center"
              >
              <div v-if="user.role == 'student'">
                  <img src="../assets/cat.png" class="profile" style="cursor: default"/>
                </div>

                <div v-if="user.role == 'lecturer'">
                  <img src="../assets/bear.png" class="profile" style="cursor: default"/>
                </div>
                
                <div v-if="user.role == 'admin'">
                  <img src="../assets/cow.png" class="profile" style="cursor: default"/>
                </div>

                <h5 class="username" style="font-size: 1.1vw">
                  {{ user.name }}
                </h5>
                <p style="font-size: 0.95vw">{{ user.email }}</p>
                <p style="font-weight: bold; color: black; font-size: 0.95vw">
                  {{ user.role }}
                </p>
                <p
                  style="color: #888888; font-size: 0.8vw; margin-bottom: -5px"
                >
                  Created on {{ moment(user.createdOn).format("D MMM YYYY") }}
                  {{ formateTime(user.createdOn) }}
                </p>
                <p style="color: #888888; font-size: 0.8vw">
                  Updated on {{ moment(user.updatedOn).format("D MMM YYYY") }}
                  {{ formateTime(user.updatedOn) }}
                </p>
              </div>
              <button
                class="btn-grad"
                style="border-radius: 0px 0px 20px 20px"
                v-on:click="(showIndex = user.userId), (editUserPop = true)"
                @click="toEditingMode(user)"
              >
                Edit User
              </button>
            </div>
          </div>

          <!-- delete -->
          <div
            class="card alertDel"
            v-if="checkDel == true && showIndex === user.userId"
          >
            <div class="card-body" style="margin-top: 0.5vw;">
              <img
                src="https://api.iconify.design/akar-icons/circle-alert.svg?color=%23bb2d3b"
                style="width: 4.5vw"
              />
              <p class="card-text" style="margin-top: 10px">
                Do you want to remove <b>{{ user.name }}</b> ?
              </p>
              <button
                type="button"
                class="btn btn-warning delUserBtn"
                style="padding: 0.5vw 1.3vw; margin-bottom: 1vw"
                @click="removeUser(user.userId)"
                v-on:click="(deleted = true), checkDel == false"
              >
                Remove
              </button>
              <button
                type="button"
                class="btn btn-secondary"
                style="margin-left: 30px; margin-bottom: 1vw; font-size: 0.95vw; padding: 0.5vw 1.3vw; "
                v-on:click="(checkDel = false), (showIndex = null)"
              >
                Cancel
              </button>
            </div>
          </div>
        </li>
      </ul>
    </div>

    <!-- can delete -->
    <div class="container" v-if="deleted === true">
      <div class="card deleted" id="deleted">
        <div class="card-body">
          <img
            src="https://api.iconify.design/healthicons/yes-outline.svg?color=%23198754"
            style="width: 4.5vw"
          />
          <p class="card-text" style="margin-top: 10px">
            <b>Removed</b> User Successfully
          </p>
          <button
            type="button"
            class="btn btn-light btn-grad-ok"
            style="margin-bottom: 1vw"
            v-on:click="
              (deleted = false), (checkDel = false), (showIndex = null)
            "
          >
            OK
          </button>
        </div>
      </div>
    </div>

    <!-- edit -->
    <div>
      <div class="container" v-if="editUserPop == true">
        <ul>
          <li v-for="user in users" :key="user.userId" :value="user">
            <div v-if="showIndex === user.userId">
              <editUser
                :userList="users"
                :currentUser="editingUser"
                :errorName="errorName"
                :errorEmail="errorEmail"
                :errorRole="errorRole"
                :notUniqueName="notUniqueName"
                :notUniqueEmail="notUniqueEmail"
                :invaildEmail="invaildEmail"
                @cancelEdit="cancelEdit"
                @editUser="modifyUser"
              />
            </div>
          </li>
        </ul>
      </div>
    </div>
    <!--edit data is same-->
    <div class="containerV2" v-if="remainSame === true">
      <div class="card alertEdit">
        <div class="card-body" style="margin-top: 10px">
          <img
            src="https://api.iconify.design/healthicons/yes-outline.svg?color=%23198754&"
            style="width: 4.5vw"
          />
          <p class="card-text" style="margin-top: 10px">
            User's data is remain the same
          </p>
          <button
            type="button"
            class="btn btn-success btn-grad-ok mx-auto"
            style="margin-bottom: 1vw"
            v-on:click="(remainSame = false), (editUserPop = false)"
          >
            OK
          </button>
        </div>
      </div>
    </div>
    <!-- can edit-->
    <div class="containerV2" v-if="editedUser === true">
      <div class="card alertEdit">
        <div class="card-body" style="margin-top: 10px">
          <img
            src="https://api.iconify.design/healthicons/yes-outline.svg?color=%23198754&"
            style="width: 4.5vw"
          />
          <p class="card-text" style="margin-top: 10px">
            <b>Edited</b> User Successfully
          </p>
          <button
            type="button"
            class="btn btn-grad-ok mx-auto"
            style="margin-bottom: 1vw"
            v-on:click="(editedUser = false), (editUserPop = false)"
          >
            OK
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style>
.body {
  max-width: 1920px;
}

.noUser {
  position: absolute;
  top: 50%;
  left: 50%;
  -webkit-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
}

.delUserBtn {
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
  font-size: 0.95vw;
}

.delUserBtn:hover {
  background-position: right center;
  /* change the direction of the change here */
  color: #fff;
  text-decoration: none;
  border-color: transparent;
}

.edit-user-card {
  border-radius: 20px;
}

.hovertext:before {
  content: attr(data-hover);
  visibility: hidden;
  opacity: 0;
  color: #888888;
  width: 45%;
  font-size: 0.8vw;
  text-align: center;
  border-radius: 5px;
  padding-left: 10px;
  padding-right: 10px;
  border-radius: 20px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
  transition: opacity 0.5s ease-in-out;
  margin-left: auto;
  margin-right: auto;
  position: absolute;
  left: 0;
  right: 0;
  top: 10%;
  margin-bottom: 10px;
}

.hovertext:hover:before {
  opacity: 1;
  visibility: visible;
}

.alertEdit {
  width: 23.5%;
  height: 26.8%;
  color: white;
  justify-content: center;
  font-size: 0.95vw;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
  border-radius: 1vw;
  text-align: center;
  color: black;
  animation: animate 0.3s ease-in-out;
}

.alertPlzlogin{
  width: 23.5%;
  height: 26.8%;
  color: white;
  justify-content: center;
  font-size: 0.95vw;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
  border-radius: 1vw;
  text-align: center;
  color: black;
  animation: animate 0.3s ease-in-out;
}
.Plzlogin{
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
.deleted {
  width: 23.5%;
  height: 26.8%;
  background-color: #fff;
  color: #198754;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.5);
  text-align: center;
  font-size: 0.95vw;
  border-radius: 1vw;
  padding: 1.3vw;
}

.profile {
  height: 10.5vw;
  display: block;
  margin-left: auto;
  margin-right: auto;
  cursor: pointer;
  transition: all 0.2s ease-in-out;
}

.profile:hover {
  transform: scale(1.1);
}

.alertDel {
  background-color: #fff;
  width: 23.5%;
  height: 26.8%;
  color: black;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
  font-size: 0.95vw;
  border-radius: 1vw;
  text-align: center;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  animation: animate 0.3s ease-in-out;
}

.delete-icon {
  cursor: pointer;
  height: 1.5vw;
  margin-top: 2vh;
  margin-left: 80%;
  transition: all 0.2s ease-in-out;
}

.delete-icon:hover {
  transform: scale(1.5);
}

.col-user {
  transition: all 0.2s ease-in-out;
}

.col-user:hover {
  transform: scale(1.04);
}

.title-detail {
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
  font-size: 2.5vw;
  font-weight: bold;
  margin-top: -2%;
  transition: all 0.2s ease-in-out;
}

.close-detail:hover {
  color: #e74694;
  transform: scale(1.3);
}

.popUserDetail {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  border-radius: 20px;
  text-align: center;
  font-size: 18px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
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
  border-radius: 1vw;
  margin-bottom: 1.8vw;
  margin-left: auto;
  margin-right: auto;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
  text-align: center;
  font-size: 1vw;
}

.user-body h5 {
  font-size: 1.1vw;
}

.username {
  color: #e74694;
  font-weight: bold;
}

.btn-grad-ok {
  background-image: linear-gradient(
    to right,
    #1d976c 0%,
    #93f9b9 51%,
    #1d976c 100%
  );
  color: white;
  transition: 0.5s;
  width: 5vw;
  border-color: transparent;
  background-size: 200% auto;
  font-size: 0.95vw;
}

.btn-grad-ok:hover {
  background-position: right center;
  /* change the direction of the change here */
  color: #fff;
  text-decoration: none;
  border-color: transparent;
}

.btn-plzlogin {
  background-image: linear-gradient(
    to right,
    #f09819 0%,
    #edde5d 51%,
    #f09819 100%
  );
  text-align: center;
  text-transform: uppercase;
  transition: 0.5s;
  background-size: 200% auto;
  width: 5vw;
  color: white;
  box-shadow: 0 0 20px #eee;
  border-color: transparent;
  font-size: 0.95vw;
}

.btn-plzlogin:hover {
  background-position: right center; /* change the direction of the change here */
  color: #fff;
  border-color: transparent;
  text-decoration: none;
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
}

.btn-grad {
  background-image: linear-gradient(
    to right,
    #f857a6 0%,
    #ff5858 51%,
    #f857a6 100%
  );
  padding: 0.5vw 1vw;
  text-align: center;
  text-transform: uppercase;
  transition: 0.5s;
  background-size: 200% auto;
  color: white;
  box-shadow: 0 0 20px #eee;
  border-radius: 10px;
  display: block;
  font-size: 0.9vw;
  border: 0px;
}

.btn-grad:hover {
  background-position: right center;
  /* change the direction of the change here */
  color: #fff;
  text-decoration: none;
}

.btn-grad-add {
  background-image: linear-gradient(
    to right,
    #f857a6 0%,
    #ff5858 51%,
    #f857a6 100%
  );
  margin-top: 1vw;
  padding: 0.5vw 1.5vw;
  text-align: center;
  text-transform: uppercase;
  transition: 0.5s;
  background-size: 200% auto;
  color: white;
  box-shadow: 0 0 20px #eee;
  border-radius: 10px;
  display: block;
  font-size: 0.9vw;
  border: 0px;
}

.btn-grad-add:hover {
  background-position: right center;
  /* change the direction of the change here */
  color: #fff;
  text-decoration: none;
}
</style>
