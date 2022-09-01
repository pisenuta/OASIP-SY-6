<script setup>

</script>
 
<template>
    <div class="body">
        <h3 class="mx-auto mt-5" id="topic" style="font-size: 2.1vw;font-weight: bolder;">About <a>&lt;PP/&gt;</a> Team</h3>
        <div class="containerus mt-10">
            <div class="row mx-auto">
                <div class="col-6">
                    <div class="card pi" id="pa" style="width: 21vw;">
                        <img class="card-img-top" src="../assets/055.jpeg" alt="Card image cap">
                        <div class="card-body us">
                            <h3 class="card-title" style="font-size: 1.5vw;">Tarathep Siripis</h3>
                            <p class="card-text" style="font-size: 0.9vw;">63130500055</p>
                            <a href="https://github.com/pphaiaiai" target="_blank"><img src="https://api.iconify.design/bytesize/github.svg?" style="height: 2vw;"/></a>
                        </div>
                    </div>
                </div>
                <div class="col-6">
                    <div class="card pi" id="pi" style="width: 21vw;">
                        <img class="card-img-top" src="../assets/056.jpeg" alt="Card image cap">
                        <div class="card-body us">
                            <h3 class="card-title" style="font-size: 1.5vw;">Thanyakorn Thalaengdi</h3>
                            <p class="card-text" style="font-size: 0.9vw;">63130500056</p>
                            <a href="https://github.com/pisenuta" target="_blank"><img src="https://api.iconify.design/bytesize/github.svg?" style="height: 2vw;"/></a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <h3 class="mx-auto mt-5" style="font-size: 2.1vw;font-weight: bolder;">
        Our Repository : <a href="https://github.com/pisenuta/OASIP-SY-6" target="_blank"><img src="https://api.iconify.design/brandico/github-text.svg?" style="padding-right: 5px; padding-left: 4px; height: 2vw;"/>
        <img src="https://api.iconify.design/bytesize/github.svg?" style="height: 2vw;"/></a> </h3>
    </div>

</template>
 
<style>
.card-text {
    text-align: center;
}
.pi {
    text-align: center;
    justify-content: center;
    padding-left: 10;
}

.col-6 {
    padding-left: 15em;
}

#topic {
    margin-top: 2.5vw;
    margin-bottom: 3%;
}

#pa {
    margin-left: 10em;
}

#pi {
    margin-left: -10em;
}

.pi {
    border-radius: 10px;
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
    transition: all .2s ease-in-out;
}

.pi:hover {
    transform: scale(1.1);
}
</style>
