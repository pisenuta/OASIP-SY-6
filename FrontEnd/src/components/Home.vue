<script setup>
  const token = localStorage.getItem("token");
</script>

<template>
  <div class="homepage">
    <div class="container-home">
      <div class="row">
        <div class="col-md-6 align-self-center" style="text-align: center; margin-left: -2vw;margin-right: -2vw;">
          <div class="inside"> 
            <h1 class="home-text"><b style="color: #f857a6;">OASIP-SY-6</b><br/>Online Appointment <br/>Scheduling</h1>
            <table style="margin: auto; border-collapse: collapse;">
              <tr style="font-size: 0.95vw; font-family: Montserrat; border-left: 2px solid; border-color: #f857a6;">
                <th style="padding-left: 0.5vw;">Online Appointment Scheduling System for Integrated Project Clinics</th>
              </tr>
            </table>
            <router-link to="/login">
              <button class="btn btn-primary btn-home-login" type="button">get started</button>
            </router-link>          
            <div v-if="token !== null">
              <router-link to="/booking">
              <button class="btn btn-primary btn-home-login" type="button">Booking</button>
            </router-link>
            </div> 
          </div>
          
        </div>

        <div class="col-md-6">
          <div class="inside home-img" style="text-align: left;margin-left: -4vw;">
            <img src="../assets/home4.png" style="width:50vw;" />
          </div>
          
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Radio+Canada:wght@600&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+Thai&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Mitr&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Montserrat:wght@300&display=swap');
.homepage {
    font-family: 'Mitr', sans-serif;
    text-align: center;
    justify-content: center;
}


.home-img{
  animation: mymove infinite;
  animation-duration: 2s;
  animation-timing-function: linear;
  animation-iteration-count: infinite;
  animation-direction: alternate;
}

@keyframes mymove {
  0% {
    margin-top: -0.5vw;
  }
  100% {
    margin-top: 1vw;
  }
}
.home-text{
  font-size: 3vw;
  letter-spacing: 0.15vw;
}
.btn-home-login{
  /* display: block; */
  /* position: relative;  */
  margin-right: 9vw;
  margin-top: 1vw;
  margin-left: 17%;
  font-size: 1vw;
  background-image: linear-gradient(to right, #f857a6 0%, #ff5858 51%, #f857a6 100%);
  text-align: center;
  padding: 0.5vw 1vw;
  transition: 0.5s;
  background-size: 200% auto;
  color: white;
  border-color: transparent;
  border-radius: 1.5vw;
  letter-spacing: 0.2vw;
}

.btn-home-login:hover {
    background-position: right center;
    color: #fff;
    text-decoration: none;
    border-color: transparent;
}
div.inside {
  position: relative;
  top: 50%;
  -webkit-transform: translateY(-50%);
  -ms-transform: translateY(-50%);
  transform: translateY(-50%);
}

.col-md-6{
  height: 89.7vh;
}
.v-center {
   display: flex;
   align-items: center;
}

.title {
    font-family: 'Radio Canada', sans-serif;
    margin-top: 4em;
    margin-bottom: -50px;
}

.caption{
    margin-top: 1em;
    font-family: 'Noto Sans Thai', sans-serif;
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
  width: 40%;
}

p {
  color: #e74694;
}

a{
  text-decoration: none;
}

</style>
