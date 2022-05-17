<script setup>


</script>

<template>
    <nav class="navbar sticky-top navbar-light">
        <div class="container-fluid">
            <a class="navbar-brand ms-3">
                <router-link to="/"><div class="logo"><h1>&lt;PP/&gt;</h1><p>TEAM</p></div></router-link>
            </a>  
            <div class="d-flex">
                <!-- <router-link to="/clinic" class="nav-link"><span class="hover-underline-animation">Clinic</span></router-link> -->
                <router-link to="/add-event" class="nav-link" style="margin-right: 7px;"><span class="hover-underline-animation">Add Event</span></router-link>
                <router-link to="/show-all-events" class="nav-link"><span class="hover-underline-animation">Show All Event</span></router-link>
                <router-link to="/about-us" class="nav-link"><span class="hover-underline-animation">About Us</span></router-link>
            </div>
        </div>
    </nav>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Radio+Canada:wght@600&display=swap');
nav{
    background-color: #212529;
    height: 95px;
    font-family: 'Radio Canada';
    color:#e74694;
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.4);
}

.nav-link{
    color:#e74694;
    font-size: 18px;
    border-radius: 10px;
}
.hover-underline-animation {
  display: inline-block;
  position: relative;
  color: #e74694;
}

.hover-underline-animation:after {
  content: '';
  position: absolute;
  width: 100%;
  transform: scaleX(0);
  height: 2px;
  bottom: 0;
  left: 0;
  background-color: white;
  transform-origin: bottom right;
  transition: transform 0.25s ease-out;
}

.hover-underline-animation:hover:after {
  transform: scaleX(1);
  transform-origin: bottom left;
}

.logo {
    margin-top: 5%;
    text-align: center;
    color: white;
    text-shadow: 0 0 15px #e74694;
    transition: all .2s ease-in-out;
}

.logo:hover {
    transform: scale(1.1);
}

p {
    margin-top: -15%;
    font-size: small;
    letter-spacing: 8px
}

a {
    text-decoration: none;
}

</style>
