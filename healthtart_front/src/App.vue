<template>
    <div id="app">
    </div>
    <main>
    <RouterView/>  <!-- 라우트에 따라 페이지가 출력됨 -->
  </main>
  </template>
  
  <script setup>
  import './assets/css/style.css';
  import { RouterView, useRoute } from 'vue-router';

  </script>
  
  <style setup>
  </style>

