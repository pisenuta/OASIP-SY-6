<script setup>
import { ref } from 'vue'
import NavBar from './components/NavBar.vue'

const role = ref(localStorage.getItem('role'))
if (localStorage.getItem('role') === null){
  localStorage.setItem('role','guest')
}else {
  localStorage.setItem('role',role.value)
}
</script>

<template>
  <div id="main">
    <NavBar class="nav" data-spy="affix" data-offset-top="197"/>
    <router-view class="container-fluid"/>

    
  </div>
</template>

<style>

html {
  scroll-behavior: smooth;
  /* font-size: calc(60% + 0.8vmin); */
}
</style>
