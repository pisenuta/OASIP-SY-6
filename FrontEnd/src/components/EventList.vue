<script setup>
import { ref } from 'vue';
    defineProps({
        eventList:{
            type: Array,
            require: true
        }
    })
    let DetailBtn = ref(false)
    const openDetail = () => {
        DetailBtn.value = true
    }
    const closeDetail = () =>{
        DetailBtn.value = false
    }

    const showIndex = ref(null)

</script>

<template>
    <div>
        <!-- <ul>
            <li v-for="(event,index) in eventList" :key="index">
                <p><b>นัดหมายที่ {{index+1}}</b></p>
            <div class="card-body-main">
                <div class="card" style="width: 45rem;">
                    <div class="card-body">
                        <b>วันที่ - เวลานัดหมาย :</b> {{event.eventStartTime.slice(0,10)}} {{event.eventStartTime.slice(11,16)}}<br>
                        <b>ระยะเวลา :</b> {{event.eventDuration}} นาที <br>
                        <b>คลีนิก :</b> {{event.eventCategory.eventCategoryName}}<br>
                        <b>ชื่อผู้นัดหมาย :</b> {{event.bookingName}}<br>
                        <div class="card-detail" v-if="DetailBtn == true" >
                            <div v-if="showIndex === index"> 
                                <b>Email :</b> {{event.bookingEmail}}<br>
                                <b>Note : </b>{{event.eventNotes}}<br>
                                <button type="button" class="btn btn-danger" @click="close" v-on:click="showIndex = null"> ซ่อน </button>
                            </div>      
                        </div> 
                        <button type="button" class="btn btn-primary" v-on:click="showIndex = index" @click="openDetail" v-if="showIndex !== index"> รายละเอียด </button>     
                        <button type="button" class="btn btn-danger">ยกเลิกนัดหมาย</button>  
                    </div>
                </div>    
            </div>         
            </li>
        </ul> -->
        <table class="table table-hover mx-auto align-middle" id="list-table" >
            <thead class="table-dark align-middle">
                <tr>
                    <th scope="col" class="mx-auto">#</th>
                    <th scope="col">DATE</th>
                    <th scope="col">TIME</th>
                    <th scope="col">CLINIC</th>
                    <th scope="col">DURATION<br> <span style="font-size:12px">(MINUTES)</span></th>
                    <th scope="col">BOOKER</th>
                    <th scope="col"></th>
                </tr>
            </thead>
            <tbody>
                    <tr v-for="(event,index) in eventList" :key="index">
                    <td scope="row">{{index+1}}</td>
                    <td>{{event.eventStartTime.slice(0,10)}}</td>
                    <td>{{event.eventStartTime.slice(11,16)}}</td>
                    <td>{{event.eventCategory.eventCategoryName}}</td>
                    <td>{{event.eventDuration}}</td>
                    <td>{{event.bookingName}}</td>
                    <td><span class="detail-Btn" v-on:click="showIndex = index" @click="openDetail">Details</span></td>
                    </tr>   
            </tbody>
        </table>

        <ul>
            <li v-for="(event,index) in eventList" :key="index">
                <div class="card-body-main" v-if="DetailBtn == true">
                    <div class="card" style="width: 45rem;">
                        <div class="card-body" v-if="showIndex === index">
                            {{event.bookingName}}<br>
                            {{event.bookingEmail}}<br>
                            Clinic<br>
                            {{event.eventCategory.eventCategoryName}}<br>
                            {{event.eventStartTime.slice(0,10)}} at {{event.eventStartTime.slice(11,16)}}<br>
                            
                            <p v-if="event.eventNotes == null">No Msg</p>
                            <p v-else>note<br>{{event.eventNotes}}</p>
                        </div>
                    </div>    
                </div>    
            </li>
        </ul>
              
        
    </div>

</template>

<style scoped>
.card-body-main{
    display: flex;
    justify-content: center;
    margin-bottom: 30px;
}
.detail-Btn{
    color: #e74694;
    cursor: pointer;
}
.detail-Btn:hover{
    text-decoration: underline;
}
ul{
    list-style-type: none;
}

td{
    height: 60px;
}
thead{
    height: 75px;
    color: white;
}
#list-table{
    width: 60%;
    border-radius: 5px 5px 5px 5px;
    overflow: hidden;
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
    position: relative;
    text-align: center;
}
</style>