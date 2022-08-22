router-link<script setup></script>

<template>
  <!-- <div class="homepage">
    <div class="title">
      <h1><p>OASIP-SY-6</p> Online Appointment Scheduling</h1>
    </div>
      <img src="../assets/home.png" class="main">
    <div class="caption">
      <h2>
        The website for Booking Appointment is easy to use. Just clicks 
        <router-link to="/add-event"><a>Add Event.</a></router-link>
      </h2>
    </div>
  </div> -->
  <div class="row">
    <div class="col">
      <img src="../assets/home.png" class="main" />
    </div>
    <div class="col">
      <div class="title">
        <h1 id="top">
          <p>OASIP-SY-6</p>
          Online Appointment Scheduling
        </h1><br />
        <h4>
          The website for Booking Appointment <br> is easy to use. Just clicks <br>
          <router-link to="/add-event"><button class="btn-main">Add Event</button></router-link>
        </h4>
      </div>
    </div>
  </div>
</template>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Inter&display=swap");
@import url("https://fonts.googleapis.com/css2?family=Radio+Canada:wght@600&display=swap");
@import url("https://fonts.googleapis.com/css2?family=Noto+Sans+Thai&display=swap");

.homepage {
  font-family: "Inter", sans-serif;
  text-align: center;
  justify-content: center;
}

.btn-main {
  background-image: linear-gradient(to right, #9D50BB 0%, #6E48AA 51%, #9D50BB 100%);
  margin: 10px;
  margin-top: 1em;
  padding: 15px 45px;
  text-align: center;
  text-transform: uppercase;
  transition: 0.5s;
  background-size: 200% auto;
  color: white;
  box-shadow: 0 0 20px #eee;
  border-radius: 10px;
  display: block;
  border-color: transparent;
}

.btn-main:hover {
  background-position: right center;
  /* change the direction of the change here */
  color: #fff;
  text-decoration: none;
  border-color: transparent;
}

.title {
  font-family: "Radio Canada", sans-serif;
  margin-top: 4em;
  margin-bottom: -50px;
  -webkit-transform: translate(-50%, -50%);
  transform: translate(-70%, 80%)
}

.caption {
  margin-top: 1em;
  font-family: "Noto Sans Thai", sans-serif;
}

.caption a {
  color: #e74694;
  padding: 5px;
}

.caption a:hover {
  background-color: #fae4ef;
  border-radius: 10px;
  padding: 5px;
  color: #e74694;
  cursor: pointer;
}

.main {
  position: absolute;
  top: 50%;
  left: 40%;
  -webkit-transform: translate(-50%, -50%);
  transform: translate(-7%, -50%);
  width: auto;
  height: 750px;
  animation: animate 2s infinite;
}

@keyframes animate {
0% {
            transform: translateY(-50%);
    opacity: 1;
  }
	50% {
            transform: translateY(-48%);
    opacity: 1;
  }
  100% {
            transform: translateY(-50%);
    opacity: 1;
  }
}

p {
  color: #e74694;
}

a {
  text-decoration: none;
}
</style>
