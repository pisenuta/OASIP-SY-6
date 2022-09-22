<script setup>
const token = localStorage.getItem("token");

const signout = () => {
    localStorage.clear()
    window.location.href = "/"
}
</script>

<template>
    <nav class="navbar navbar-light navbar-expand-md py-3 sticky-top">
        <div class="container-fluid">
            <a class="navbar-brand d-flex align-items-center ms-3" >
                <router-link to="/" v-on:clike="page.value == 'home'">
                    <div class="logo" >
                        <h1 style="font-size: 2vw;">&lt;PP/&gt;</h1>
                        <p style="margin-bottom:0px;font-size: 0.7vw;">TEAM</p>
                    </div>
                </router-link>
            </a>
            <div class="collapse navbar-collapse" id="navcol-3">
                <ul class="navbar-nav mx-auto">
                    <li class="nav-item">
                        <a class="nav-link hover-underline-animation text-wh">
                            <router-link to="/clinic">Clinic</router-link>
                            <br>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link hover-underline-animation text-wh">
                            <router-link to="/booking">Booking</router-link>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link hover-underline-animation text-wh">
                            <router-link to="/reservation">Reservation</router-link>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link hover-underline-animation text-wh">
                            <router-link to="/user">User</router-link>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link hover-underline-animation text-wh">
                            <router-link to="/about-us">About Us</router-link>
                        </a>
                    </li>
                </ul>
                <div v-if="token === null">
                    <router-link to="/login" class="nav-link nav-btn"><button class="btn signin-btn">Sign In</button></router-link>
                </div>
                <div v-else-if="token !== null">
                    <button class="btn signin-btn" style="color: white;" @click="signout">Sign Out</button>
                </div>
                <div v-if="token === null">
                    <router-link to="/signup" class="nav-link nav-btn"><button class="btn signup-btn nav-btn">Sign Up</button></router-link>
                </div>
            </div>
        </div>
    </nav>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Radio+Canada:wght@600&display=swap');

nav {
    background-color: rgba(33, 37, 41, 1);
    height: 5vw;
    font-family: 'Radio Canada';
    color: #e74694;
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.4);
}

.signup-btn {
    background-image: linear-gradient(to right, #FF8008 0%, #FFC837  51%, #FF8008  100%);
    text-align: center;
    padding: 0.5vw 1vw;
    transition: 0.5s;
    background-size: 200% auto;
    color: white;
    font-size: 0.9vw;
    border-color: transparent;
}

.signup-btn:hover {
    background-position: right center;
    color: #fff;
    text-decoration: none;
    border-color: transparent;
}

.nav-link {
    color: #e74694;
    font-size:1vw;
    border-radius: 10px;
}

.nav-btn{
    color: #fff;
}
.nav-item {
    margin:1vw;
}

.hover-underline-animation {
    display: inline-block;
    position: relative;
    color: #e74694;
}

.hover-underline-animation:hover a {
    color: #fff;
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

.signin-btn {
    background-image: linear-gradient(to right, #f857a6 0%, #ff5858 51%, #f857a6 100%);
    text-align: center;
    padding: 0.5vw 1vw;
    transition: 0.5s;
    background-size: 200% auto;
    color: white;
    font-size: 0.9vw;
    border-color: transparent;
}

.regis-btn {
    background-image: linear-gradient(to right, #77A1D3 0%, #79CBCA 51%, #77A1D3 100%);
    text-align: center;
    transition: 0.5s;
    background-size: 200% auto;
    color: white;
    border-color: transparent;
}

.regis-btn:hover {
    background-position: right center;
    /* change the direction of the change here */
    color: #fff;
    text-decoration: none;
    border-color: transparent;
}

.signin-btn:hover {
    background-position: right center;
    color: #fff;
    text-decoration: none;
    border-color: transparent;
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
    color: #e74694;
}
</style>
