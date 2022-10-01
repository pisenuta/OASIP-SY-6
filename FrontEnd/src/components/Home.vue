<script setup></script>

<template>
  <div class="homepage">
    <!-- <div class="title">
      <h1><p>OASIP-SY-6</p> Online Appointment Scheduling</h1>
    </div>
      <img src="../assets/home.png" class="main">
    <div class="caption">
      <h2>
        The website for Booking Appointment is easy to use. Just clicks 
        <router-link to="/add-event"><a>Add Event.</a></router-link>
      </h2>
    </div> -->
    <div class="container-home">
      <div class="row">
        <div class="col-md-6 align-self-center" style="text-align: center">
          <div class="inside"> 
            <h1><b style="color: #f857a6;">OASIP-SY-6</b><br/>Online Appointment Scheduling</h1>
            <router-link to="/login">
              <button class="btn btn-primary btn-home-login" type="button">LogIn</button>
            </router-link>
          </div>
          
        </div>

        <div class="col-md-6">
          <div class="inside">
            <img src="../assets/home-background.png" style="width:30vw;" />
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
.homepage {
    font-family: 'Mitr', sans-serif;
    text-align: center;
    justify-content: center;
}

.btn-home-login{
  /* display: block; */
  /* position: relative;  */
  margin-right: 9vw;
  margin-left: 17%;
  font-size: 1vw;
  background-image: linear-gradient(to right, #f857a6 0%, #ff5858 51%, #f857a6 100%);
  text-align: center;
  padding: 0.5vw 1vw;
  transition: 0.5s;
  background-size: 200% auto;
  color: white;
  border-color: transparent;
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
