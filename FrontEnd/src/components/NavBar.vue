<script setup>

</script>

<template>
    <nav class="navbar navbar-light">
        <div class="container-fluid">
            <a class="navbar-brand ms-3">
                <router-link to="/"><img src="/images/logo.png" alt="" width="75" height="75"></router-link>
            </a>  
            <div class="d-flex">
                <router-link to="/add-event" class="nav-link" style="margin-right: 7px;">Add Event</router-link>
                <router-link to="/show-all-events" class="nav-link">Show All Event</router-link>
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
}
.nav-link{
    color: #e74694;
    font-weight: bold;
    font-size: 18px;
    border-radius: 10px;
}
.nav-link:hover{
    color: white;
    background-color: #e74694;
    border-radius: 10px;
}

</style>
