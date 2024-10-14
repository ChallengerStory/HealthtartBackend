<template>
    <div class="app">
      <main>
        <div class="content">
          <div class="left-content">
            <h1>
              헬린이들아,<br />
              헬스장이 두렵나?
            </h1>
            <p>
              헬스장 첫 발걸음이 막막하시다면, 걱정 마세요!<br />
              AI가 당신만을 위한 운동 루틴과 헬스 기구<br />
              사용법을 꼼꼼하게 알려주고, 신나는 맞춤<br />
              플레이리스트까지 함께합니다.
            </p>
            <div class="buttons">
              <button class="primary-btn">운동 루틴 추천받기</button>
              <button class="secondary-btn">추천 루틴 리스트 보기</button>
            </div>
          </div>
          <div class="right-content">
            <img src="@/assets/icons/mainImage.svg" alt="헬스장 내부" class="main-image" />
            <img src="@/assets/icons/blueLabelFirst.svg" alt="블루라벨" class="blue-label" />
          </div>
        </div>
      </main>
    </div>
  </template>
  
  <script setup>
  import { onMounted } from 'vue';
  import '@/assets/css/Main/MainPage.css';
  
  onMounted(() => {
    const blueWave = document.createElement('div');
    blueWave.className = 'blue-wave';
    document.querySelector('.app').appendChild(blueWave);
  });
  </script>
  