<script setup lang="ts">
import { ref, provide, computed, onMounted } from 'vue';
import { RouterLink, RouterView } from 'vue-router';
import axios from 'axios';

// 搜索框绑定的全局搜索查询
const searchQuery = ref('');
provide('searchQuery', searchQuery);

// 加载电影数据
const movies = ref([]);
const loading = ref(false);
const error = ref('');

const fetchMovies = async () => {
  loading.value = true;
  error.value = '';
  try {
    const response = await axios.get('http://localhost:8080/movies');
    movies.value = response.data;

  } catch (err) {
    console.error('Cannot load movie data:', err);
    error.value = 'Cannot load movie data, please try again later.';
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchMovies();
});

// 根据搜索框过滤电影
const filteredMovies = computed(() => {
  return movies.value.filter((movie) =>
    movie.title.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});




const refreshPage = () => {
  setTimeout(() => {
    window.location.reload(); // 强制刷新整个页面
  }, 0);
};


</script>

<template>
  <header>
    <nav class="navbar">
      <div class="navbar-left">
        <img src="@/assets/logo.jpg" alt="Logo" class="logo" />
      </div>
      <div class="navbar-center">
        <!-- 搜索框 -->
        <div class="search-container">
          <div class="search-box">
            <img
              src="@/assets/icons8-search-50.png"
              alt="Search Icon"
              class="search-icon"
            />
            <input
              type="text"
              v-model="searchQuery"
              placeholder="Search for movies"
              class="search-input"
            />
          </div>
          <!-- 实时搜索结果 -->
          <div v-if="searchQuery" class="search-results">
            <p v-if="filteredMovies.length === 0" class="no-results">
              No results found.
            </p>
            <ul>
              <li
                v-for="movie in filteredMovies"
                :key="movie.id"
                class="search-result-item"
              >
                <div class="movie-info">
                  <h3>{{ movie.title }}</h3>
                  <p>Rating: {{ movie.mpaaRating}}</p>
                </div>

                <RouterLink
                  :to="`/movie/${movie.id}`"
                  class="details-link"
                  @click.native="refreshPage"
                >
                  View Details
                </RouterLink>
                <a :href="movie.trailer" target="_blank" class="trailer-link">
                  Watch Trailer
                </a>
              </li>
            </ul>
          </div>
        </div>
      </div>
      <div class="navbar-right">
        <RouterLink to="/" class="nav-link">HOME</RouterLink>
        <RouterLink to="/movie/:id" class="nav-link">DETAILS</RouterLink>
        <RouterLink to="/login" class="nav-link">LOGIN</RouterLink>
        <RouterLink to="/logout" class="nav-link">LOGOUT</RouterLink>
        <RouterLink to="/admin" class="nav-link">
          <img
            src="@/assets/icons8-user.gif"
            alt="Profile Icon"
            class="profile-icon"
          />
        </RouterLink>
        <RouterLink to="/usermainscreen" class="nav-link">
          <img
            src="@/assets/icons8-user.gif"
            alt="Profile Icon"
            class="profile-icon"
          />
        </RouterLink>
        <!-- <RouterLink to="/cart" class="nav-link">
          <img
            src="@/assets/shopping-cart.png"
            alt="Profile Icon"
            class="profile-icon"
          />
        </RouterLink> -->
      </div>
    </nav>
  </header>

  <RouterView />
</template>



<style scoped>


.navbar {
  position: absolute;
  top: -15px;
  left: 0;
  right: 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #ffa826; /* Orange color */
  width: 100vw;
  height: 60px;
  padding: 10px 20px; /* Padding to create space on sides */
  box-sizing: border-box;
  white-space: nowrap; /* Prevent line breaks */
  overflow: visible; /* Allow elements to overflow the navbar */
  z-index: 10; /* Ensure navbar is above other elements */
}

.navbar-left {
  flex: 1; /* Take available space */
  padding: 10px 20px;
}

.navbar-center {
  flex: 2; /* Make search bar container larger */
  display: flex;
  justify-content: center; /* Center the search bar */
  align-items: center;
  position: relative; /* Enable relative positioning for dropdown */
  z-index: 15; /* Ensure search bar is above navbar */
}

.navbar-right {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

.logo {
  width: 180px; /* Adjust logo size */
  max-width: 100%;
}

.nav-link {
  margin: 0 15px;
  text-decoration: none;
  font-family: 'Roboto', sans-serif;
  color: #f8f4f4;
  font-weight: bold;
}

.nav-link:hover {
  color: #0056b3; /* Change color on hover */
  background-color: #f0f0f0; /* Add hover background color */
}

.nav-link.router-link-exact-active {
  color: #290b04d4; /* Active link color */
  font-weight: bold;
}

.profile-icon {
  width: 23px;
  cursor: pointer;
}

/* 搜索框容器 */
.search-container {
  position: relative; /* For dropdown alignment */
  max-width: 600px;
  width: 100%; /* Responsive width */
}

/* 搜索框样式 */
.search-box {
  display: flex;
  align-items: center;
  background-color: #fff;
  border: 1px solid #ccc;
  border-radius: 25px;
  padding: 10px 15px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  z-index: 15; /* Ensure search box is above the navbar */
}

.search-icon {
  width: 20px;
  height: 20px;
  margin-right: 10px;
}

.search-input {
  flex: 1;
  border: none;
  outline: none;
  font-size: 16px;
  color: #333;
  background: transparent;
}

.search-input::placeholder {
  color: #aaa;
}

/* 搜索结果样式 */
.search-results {
  position: absolute;
  top: calc(100% + 5px); /* Ensure dropdown appears below the search box */
  left: 0;
  right: 0;
  background-color: #fff;
  border: 1px solid #ccc;
  border-radius: 5px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  z-index: 20; /* Ensure dropdown is above all other elements */
  max-height: 300px;
  overflow-y: auto;
}

.search-result-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #eee;
}

.search-result-item:last-child {
  border-bottom: none;
}

.no-results {
  padding: 15px;
  color: #999;
  text-align: center;
}

.movie-info h3 {
  margin: 0;
  font-size: 16px;
  color: #333;
}

.movie-info p {
  margin: 5px 0 0;
  font-size: 14px;
  color: #666;
}

.trailer-link {
  text-decoration: none;
  color: #007bff;
  font-size: 14px;
}

.trailer-link:hover {
  color: #0056b3;
}

.login-button {
  padding: 0.5rem 1rem;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.login-button:hover {
  background-color: #0056b3;
}

@media (min-width: 1024px) {
  .navbar {
    padding: 10px 30px;
  }

  .logo {
    width: 200px;
  }

  .navbar-center {
    justify-content: center;
  }

  .search-container {
    max-width: 600px;
    margin: 0 auto;
  }
.search-results {
  margin-top: 15px;
  background: #292929;
  border-radius: 8px;
  padding: 10px 15px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
  color: #fff;
  font-family: 'Arial', sans-serif;
}

.no-results {
  font-size: 1.1em;
  color: #ff6666;
  text-align: center;
  margin-top: 10px;
}

/* Search Result Items */
.search-result-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #444;
}

.search-result-item:last-child {
  border-bottom: none;
}

.movie-info h3 {
  font-size: 1.2em;
  color: #ffcc00;
}

.movie-info p {
  font-size: 1em;
  color: #ccc;
  margin-top: 5px;
}

/* Buttons and Links */
.details-link,
.trailer-link {
  padding: 8px 15px;
  background-color: #007bff;
  color: white;
  border-radius: 5px;
  font-size: 0.9em;
  text-decoration: none;
  transition: background-color 0.3s, transform 0.3s;
  margin-left: 10px;
}

.details-link:hover,
.trailer-link:hover {
  background-color: #0056b3;
  transform: scale(1.05);
}

  
}



</style>