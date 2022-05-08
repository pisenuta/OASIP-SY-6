<script setup>
import NavBar from './components/NavBar.vue'

</script>

<template>
  <div id="main">
    <NavBar class="nav" data-spy="affix" data-offset-top="197"/>
    <router-view class="container-fluid"/>
  </div>
</template>

<style>
::-webkit-scrollbar {
  display: none;
}

html {
  scroll-behavior: smooth;
}

</style>
