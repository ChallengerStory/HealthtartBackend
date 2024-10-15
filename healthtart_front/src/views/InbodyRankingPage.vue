<template>
    <div class="inbody-ranking">
      <h2>InBody Ranking</h2>
      <table>
        <thead>
          <tr>
            <th>랭킹</th>
            <th>이름</th>
            <th>성별</th>
            <th>키</th>
            <th>체중</th>
            <th>골격근량</th>
            <th>체지방률</th>
            <th>기초대사량</th>
            <th>인바디 점수</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(user, index) in inbodyUsers" :key="index">
            <td>{{ index + 1 }}</td>
            <td>{{ user.userNickname }}</td>
            <td>{{ user.userGender }}</td>
            <td>{{ user.height }}</td>
            <td>{{ user.weight }}</td>
            <td>{{ user.MuscleWeight }}</td>
            <td>{{ user.FatPercentage }}</td>
            <td>{{ user.BasalMetabolicRate }}</td>
            <td>{{ user.InbodyScore }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </template>
  
  <script>
  import { ref, onMounted } from 'vue'
  import axios from 'axios'
  
  export default {
    setup() {
      const inbodyUsers = ref([])
  
      const fetchInbodyData = async () => {
        try {
          const response = await axios.get('http://localhost:8080/inbody')
          inbodyUsers.value = response.data
        } catch (error) {
          console.error('Error fetching inbody data:', error)
        }
      }
  
      onMounted(() => {
        fetchInbodyData()
      })
  
      return {
        inbodyUsers
      }
    }
  }
  </script>
  
  <style scoped>
  .inbody-ranking {
    margin: 20px;
  }
  
  table {
    width: 100%;
    border-collapse: collapse;
  }
  
  th, td {
    border: 1px solid #ddd;
    padding: 8px;
    text-align: left;
  }
  
  th {
    background-color: #f2f2f2;
    font-weight: bold;
  }
  
  tr:nth-child(even) {
    background-color: #f9f9f9;
  }
  </style>