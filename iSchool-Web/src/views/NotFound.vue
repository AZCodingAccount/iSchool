<script setup>
</script>

<template>
    <div class="background">
        <el-image src="/img/notFound.jpg"></el-image>
    </div>
    <div class="box">
        <div class="notFound">
            404 not found
        </div>
    </div>
</template>
<style scoped>
.box {
    position: fixed;
    left: 33%;
    bottom: 51%;
    color: white;
}

.notFound {
    font-size: 100px;
    font-weight: 1000;
}

.background {
    position: fixed;
    left: 0;
    top: -90px;
    width: 100%;
    z-index: -1;
}
</style>