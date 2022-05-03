<script setup>

</script>

<template>
    <nav class="navbar navbar-light">
        <div class="container-fluid">
            <a class="navbar-brand ms-3">
                <router-link to="/"><img src="/images/logo.png" alt="" width="75" height="75"></router-link>
            </a>  
            <div class="d-flex">
                <router-link to="/add-event" class="nav-link">Add Event</router-link>
                <router-link to="/show-all-events" class="nav-link">Show All Event</router-link>
            </div>
        </div>
    </nav>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter&family=Noto+Sans+Thai&display=swap');
nav{
    background-color: #D1EFFF;
    height: 95px;
    font-family: 'Inter';
}
.nav-link{
    color: black;
}
.nav-link:hover{
    color: #2867db;
}
</style>
