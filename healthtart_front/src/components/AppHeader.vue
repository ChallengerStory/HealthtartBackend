<template>
    <header class="header">
        <div class="left-header">
            <div class="logo">
                <img src="@/assets/icons/logo.svg" alt="Healthtart Logo" class="logo-img"/>
            </div>
            <div class="nav-menu">
                <nav class="nav">
                    <button class="nav-button" :class="{active: activeTab === 'home' }" @click="setActiveTab('home')">Home</button>
                    <button class="nav-button" :class="{active: activeTab === 'gym' }" @click="setActiveTab('gym')">Gym</button>
                    <button class="nav-button" :class="{active: activeTab === 'routine' }" @click="setActiveTab('routine')">Routine</button>
                    <button class="nav-button" :class="{active: activeTab === 'history' }" @click="setActiveTab('history')">History</button>
                    <button class="nav-button" :class="{active: activeTab === 'inbody' }" @click="setActiveTab('inbody')">InBody</button>
                    <button class="nav-button" :class="{active: activeTab === 'mypage' }" @click="setActiveTab('mypage')">MyPage</button>
                </nav>
            </div>
        </div>
         <!-- 로그인 안 된 상태에는 LogIn, 로그인 된 상태에는 LogOut 글자가 나와야 한다 -->
        <!-- 배경 색도 달라야한다 -->
        <!-- <button class="login-btn" @click="login">LogIn</button> -->
         <div class="right-header">
            <div class="auth-btn">
                <button @click="handleAuthAction">
                    {{ isLoggedIn ? 'LogOut' : 'LogIn' }}  
                </button>
            </div>
        </div>
    </header>
</template>
  
<script>
import { useRoute, useRouter } from 'vue-router';
import { ref, watch, onMounted, watchEffect } from 'vue';

const route = useRoute();
const router = useRouter();

const activeTab = ref('');

function setActiveTab(tab) {
  activeTab.value = tab;

  switch (tab) {
    case 'home':
      router.push({ path: '/home'});
      break;
    case 'gym':
      router.push({ path: '/gym'});
      break;
    case 'routine':
      router.push({ path: '/routine'});
      break;
    case 'history':
      router.push({ path: '/history'});
      break;
    case 'inbody':
      router.push({ path: '/inbody'});
      break;
    case 'mypage':
        router.push({ path: '/mypage'});
        break;
  }
}

function updateActiveTabFromRoute() {
  const path = route.path;
  if (path.includes('home')) {
    activeTab.value = 'home';
  } else if (path.includes('gym')) {
    activeTab.value = 'gym';
  } else if (path.includes('routine')) {
    activeTab.value = 'routine';
  } else if (path.includes('history')) {
    activeTab.value = 'history';
  } else if (path.includes('inbody')) {
    activeTab.value = 'inbody';
  } else if (path.includes('mypage')) {
    activeTab.value = 'mypage';
  }
}

onMounted(() => {
  updateActiveTabFromRoute();
});


    export default {
    name: 'Header',
    data() {
        return {
        activeTab: 'home',
        isLoggedIn: false   // 로그인 여부를 확인할 상태
      };
    },
    // created() {
    //     // 컴포넌트가 생성될 때 localStorage를 통해 로그인 여부를 확인
    //     this.checkLoginStatus();
    // },
    methods: {
        setActiveTab(tab) {
            this.activeTab = tab;  // 탭 변경 시 상태 업데이트
        },
        handleAuthAction() {
            if (this.isLoggedIn) {
                this.logout();
            } else {
                this.$router.push('/login');
            }
        },
        logout() {
            localStorage.removeItem('authToken');
            this.isLoggedIn = false;
            this.$router.push('/login');
        }
      }
    };
</script>
  
<style scoped>
  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: black;
    padding: 10px 30px;
    height: 60px; /* Set a fixed height */
  }

  .left-header {
    padding-right: 20px;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  
  .logo {
    display: flex;
    align-items: center;
    flex: 0 0 auto;
  }
  
  .logo-img {
    width: 50px;
    /* margin-right: 10px; */
    height: 50px; /* Set a fixed height */
    object-fit: contain; /* Maintain aspect ratio */
  }
  
  .nav-menu {
    display: flex;
    justify-content: center;
    flex-grow: 0;
    margin: 0 20px;
    align-items: center;
    /* flex: 1 1 auto; */
  }
  
  .nav-button {
    color: white;
    text-decoration: none;
    font-size: 18px;
    padding: 5px 10px;
    background: none;
    border: none;
    cursor: pointer;
    white-space: nowrap; /* Prevent text wrapping */
  }
  
  .nav-button.active {
    background-color: #00E0E0;
    color: black;
    border-radius: 5px;
  }

  .auth-btn {
    flex: 0 0 auto;
  }

  .auth-btn button {
    background-color: #00E0E0;
    color: black;
    border: none;
    border-radius: 5px;
    padding: 5px 15px;
    font-size: 16px;
    cursor: pointer;
    white-space: nowrap; /* Prevent text wrapping */
  }

  .auth-btn button:hover {
    background-color: #00c0c0;
  }

  .nav {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 20px;
  }
  
  .logout-btn {
    background-color: #00E0E0;
    color: black;
    border: none;
    border-radius: 5px;
    padding: 5px 15px;
    font-size: 16px;
    cursor: pointer;
  }
  
  .logout-btn:hover {
    background-color: #00c0c0;
  }

  /* Media query for smaller screens */
    @media (max-width: 768px) {
        .header {
            flex-wrap: wrap;
            height: auto;
            padding: 10px;
        }

        .nav-menu {
            order: 3;
            width: 100%;
            margin: 10px 0 0 0;
        }

        .nav {
            justify-content: space-around;
        }

        .nav-button {
            font-size: 14px;
            padding: 5px;
        }

        .auth-btn button {
            font-size: 14px;
            padding: 5px 10px;
        }
    }

    
</style>
  