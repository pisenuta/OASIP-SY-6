<script setup></script>

<template>
  <div class="homepage">
    <div class="title">
      <h1>Online Appointment Scheduling</h1>
    </div>
      <img src="../assets/mainHome.png" />
    <div class="caption">
      <h2>
        เว็บไซต์สำหรับ Booking Appointment ใช้งานได้อย่างง่ายดาย เพียงคลิก
        <a> สร้างการนัดหมาย </a>
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
    margin-top: 10em;
    margin-bottom: 3em;
}

.caption{
    margin-top: 3em;
    font-family: 'Noto Sans Thai', sans-serif;
}
.caption a {
 color: #C95C5C;
 padding: 5px;
}

.caption a:hover {
    background-color: aliceblue;
    border-radius: 10px;
    padding: 5px;
    color: #C95C5C;
    cursor: pointer;
}
</style>
