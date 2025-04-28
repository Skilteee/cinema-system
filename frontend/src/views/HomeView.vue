<script setup lang="ts">
import { ref, computed,onMounted,inject } from 'vue';


import axios from 'axios';


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
    console.error('cant not load movie data:', err);
    error.value = 'cant not load movie data, please try again later.';
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchMovies();
});



const searchQuery = inject('searchQuery', '');



const filteredMovies = computed(() => {
  return movies.value.filter(movie => 
    movie.title.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});


const currentlyRunningMovies = computed(() => 
  filteredMovies.value.filter(movie => movie.category === 'currently-running')
);

const comingSoonMovies = computed(() => 
  filteredMovies.value.filter(movie => movie.category === 'coming-soon')
);


import image3 from '@/assets/preview1.jpg';
import image2 from '@/assets/preview2.jpg';
import image1 from '@/assets/preview3.jpg';
const images: string[] = [
    image3,
    image2,
    image1,

];

const currentIndex = ref<number>(0);

const nextImage = () => {
  currentIndex.value = (currentIndex.value + 1) % images.length;
};

const prevImage = () => {
  currentIndex.value = (currentIndex.value - 1 + images.length) % images.length;
};


const refreshPage = () => {
  setTimeout(() => {
    window.location.reload(); // 强制刷新整个页面
  }, 0);
};
</script>


<template>

  <div class="container">

    <section class="movies-preview">
        <div class="image-carousel">
            <div class="arrow left-arrow" @click="prevImage">
              &lt;
            </div>
    
            <div class="image-wrapper">
              <img :src="images[currentIndex]" alt="Image" />
            </div>
            <div class="arrow right-arrow" @click="nextImage">
              &gt;
            </div>
          </div>
    </section>

    <section class="movies-sec">
      <div class="section-now">
        <h2 class="section-title">Currently Running</h2>
      </div>
      <div class="movies-sec-now">
      <div class="movie-grid">
        <div 
          v-for="movie in currentlyRunningMovies" 
          :key="movie.title" 
          class="movie-card"
        >
          <div class="trailer-container">
            <iframe 
              :src="movie.trailer" 
              frameborder="0" 
              allow="autoplay; encrypted-media" 
              allowfullscreen
              class="movie-trailer"
            ></iframe>
          </div>
          <div class="movie-header">
            <h3 class="movie-title">{{ movie.title }}</h3>
          </div>
          <RouterLink
                  :to="`/movie/${movie.id}`"
                  class="book-button"
                  @click.native="refreshPage"
                >
                 Booking
                </RouterLink>
        </div>

      </div>
    </div>
    </section>

    <section class="movies-section">
     <div class="section-soon">
        <h2 class="section-title" >Coming Soon </h2>
     </div>
     <div class="movies-sec-soon">
      <div class="movie-grid">
        <div 
          v-for="movie in comingSoonMovies" 
          :key="movie.title" 
          class="movie-card"
        >
          <div class="trailer-container">
            <iframe 
              :src="movie.trailer" 
              frameborder="0" 
              allow="autoplay; encrypted-media" 
              allowfullscreen
              class="movie-trailer"
            ></iframe>
          </div>
          <div class="movie-header">
            <h3 class="movie-title">{{ movie.title }}</h3>
          </div>
          <RouterLink
                  :to="`/movie/${movie.id}`"
                  class="book-button"
                  @click.native="refreshPage"
                >
                 Booking
                </RouterLink>
        </div>
      </div>
      </div>
    </section>
  </div>
</template>

<style scoped>

.container {
 background-image: url('@/assets/backgroup.png');
 width : 100%;
 margin-top: 40px;

 background-size: cover; /* This makes the image cover the entire container */
}



.image-carousel {
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;
    width: 1150px;
    height: 590px;
    margin: 0 auto;
    background-color: #ffffff;
    border-radius: 10px;
    border: 5px solid black; 
    overflow: hidden;
    top: 330px;
    left: -90px;
  }
  
  .image-wrapper img {
    max-width: 1200px;
    max-height: 600px;

    object-fit: cover;
  }
  
  .arrow {
    position: absolute;
    top: 50%;
    transform: translateY(-50%);
    font-size: 2rem;
    font-weight: bold;
    color: #333;
    background-color: rgba(255, 255, 255, 0.7);
    padding: 10px;
    cursor: pointer;
    user-select: none;
  }
  
  .left-arrow {
    left: 10px;
  }
  
  .right-arrow {
    right: 10px;
  }
  
  .arrow:hover {
    background-color: rgba(255, 255, 255, 1);
  }

.search-section {
  justify-content: center;
  margin-bottom: 40px;
}

.search-input {
  width: 100%;
  max-width: 500px;
  padding: 12px 20px;
  border: 1px solid #ccc;
  border-radius: 25px;
  font-size: 16px;
  transition: border-color 0.3s;
}

.search-input:focus {
  border-color: #007BFF;
  outline: none;
}

.section-now{
    margin-top: 350px;
    margin-left: 150px;

}
.movies-sec-now{
    margin-left: 150px;
}
.movies-sec-soon{
    margin-left: 150px;
}

.section-soon{
    margin-top: 20px;
    margin-left: 150px;
}


.section-title {
  font-size: 28px;
  margin-bottom: 20px;
  color: #f4f0f0;
  text-transform: uppercase;
  font-family: 'Arial', sans-serif;
  font-weight: bold;
}


.movie-grid {
  width: 1200px;
  display: flex;
  gap: 30px; 
  overflow-x: auto; 
  padding-bottom: 20px; 
}


.movie-card {
    flex: 0 0 335px; 
    background: #000000;
    border-radius: 10px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    overflow: hidden;
    display: flex;
    flex-direction: column;
    transition: transform 0.3s;
  }

.trailer-container:hover {
  transform: translateY(-5px);
}


.movie-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  background-color: #010101;
}

.movie-title {
  font-size: 1.2em;
  color: #ffffff; 
  font-family: 'Arial', sans-serif;
  display: center;
  text-align: center;
  flex-grow: 1;

}
.movie-title:hover {
  color: #007BFF;
}

.book-button {
  padding: 8px 16px;
  width: 100%;
  background-color: #417dbe;
  border: none;
  border-radius: 20px;
  color: #fff;
  cursor: pointer;
  font-size: 1.1em;
  transition: background-color 0.3s;
}

.book-button:hover {
  background-color: #0056b3;
}


.trailer-container {
  position: relative;
  padding-bottom: 56.25%; 
  padding-top: 25px;
  height: 0;
}

.movie-trailer {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border: none;
  border-bottom-left-radius: 10px;
  border-bottom-right-radius: 10px;
}


@media (max-width: 768px) {
  .search-input {
    max-width: 100%;
  }

  .section-title {
    font-size: 1.5em;
  }

  .movie-title {
    font-size: 1em;
  }

  .book-button {
    padding: 6px 12px;
    font-size: 0.8em;
  }
}
</style>
