<script setup>
import { ref } from 'vue'

</script>

<template>
    <nav class="navbar navbar-light">
        <div class="container-fluid">
            <a class="navbar-brand ms-3">
                <router-link to="/"><img src="/images/logo.png" alt="" width="75" height="75"></router-link>
            </a>  
            <router-link to="/ShowAllEvent" class="nav-link">Show All Event</router-link>
        </div>
    </nav>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter&display=swap');
nav{
    background-color: #D1EFFF;
    height: 95px;
    font-family: 'Inter', sans-serif;
}
.nav-link{
    color: black;
}
.nav-link:hover{
    color: #2867db;
}
</style>
