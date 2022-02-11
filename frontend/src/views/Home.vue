<script setup>
import KotlinSVG from "../components/KotlinSVG.vue"
import TrophySVG from "../components/TrophySVG.vue"
</script>

<template>
    <div class="home">
        <div class="hero-wrap">
            <div class="svg">
                <KotlinSVG class="kotlin-svg" />
                <TrophySVG class="trophy-svg" />
            </div>
            <div class="text">
                <h3 class="text-gray-2">Kotlin Knowledge Contest</h3>
                <p class="text-gray-6">Kotlin 编程知识竞赛</p>
            </div>
        </div>
    </div>
    </div>
</template>

<style scoped lang="scss">
.hero-wrap {
    height: 500px;
    // background-image: linear-gradient(150deg, #ed485a 0%, #963f8a 60%, #6c49e3 100%);
    background-color: #27282c;
    display: flex;
    justify-content: center;
    align-items: center;
    position: relative;
    flex-direction: column;
    .svg {
        position: relative;
        top: -50px;
        right: 10px;

        .kotlin-svg {
            height: 150px;
            width: 150px;
        }

        .trophy-svg {
            position: absolute;
            height: 70px;
            width: 70px;
            transform: rotate(30deg);
            right: 5px;
            bottom: 5px;
        }
    }

    .text {
        text-align: center;
        position: relative;
        top: -30px;
        p {
            font-size: 16px;
            font-weight: 200;
        }
    }
}

</style>