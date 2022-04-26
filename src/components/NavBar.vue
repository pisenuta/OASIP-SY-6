<script setup>
import { ref } from 'vue'

</script>

<template>
    <nav class="navbar navbar-light">
        <div class="container-fluid">
            <a class="navbar-brand ms-3">
                <img src="/images/logo.png" alt="" width="75" height="75">
            </a>  
            <a class="nav-link" aria-current="page" href="#">Show All Event</a>
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
