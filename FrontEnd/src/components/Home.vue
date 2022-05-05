<script setup></script>

<template>
  <div class="homepage">
    <div class="title">
      <h1><p>OASIP-SY-6</p> Online Appointment Scheduling</h1>
    </div>
      <img src="../assets/mainHome.jpeg" class="main">
    <div class="caption">
      <h2>
        The website for Booking Appointment is easy to use. Just clicks 
        <a> Add Event</a>.
      </h2>
    </div>
  </div>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Radio+Canada:wght@600&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+Thai&display=swap');
.homepage {
    font-family: 'Inter', sans-serif;
    text-align: center;
    justify-content: center;
}

.title {
    font-family: 'Radio Canada', sans-serif;
    margin-top: 4em;
    margin-bottom: 3em;
}

.caption{
    margin-top: 2em;
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
  width: 50%;
}

p {
  color: #e74694;
}
</style>
