<template>
    <div class="container">
      <div class="ticket-page">
        <!-- Notification -->
        <div v-if="notification.message" :class="['notification', notification.type]">
          {{ notification.message }}
        </div>
  
        <!-- Movie Section -->
        <div v-for="movie in movies" :key="movie.id" class="movie-section">
          <div class="movie-info">
            <!-- Movie Trailer -->
            <div class="trailer-container">
              <iframe
                :src="movie.trailer"
                frameborder="0"
                allow="autoplay; encrypted-media"
                allowfullscreen
                class="movie-trailer"
              ></iframe>
            </div>
  
            <div class="movie-details">
              <h2 class="movie-title">{{ movie.title }}</h2>
              <p class="movie-description">{{ movie.synopsis }}</p>
              <div class="movie-meta">
                <p><strong>Duration:</strong> {{ movie.duration }} mins</p>
                <p><strong>Director:</strong> {{ movie.director }}</p>
                <p><strong>Producer:</strong> {{ movie.producer }}</p>
                <p><strong>Genre:</strong> {{ movie.genre }}</p>
                <p><strong>Description:</strong> {{ movie.description }}</p>
                <p><strong>MPAA Rating:</strong> {{ movie.mpaaRating }}</p>
                <p><strong>Price:</strong> ${{ movie.price }}</p>
              </div>
  
              <!-- Showtime Selection -->
              <div class="showtime-selection">
                <h3 class="showtime-title">Select Show Time</h3>
                <div class="showtime-grid">
                  <button
                    v-for="schedule in movie.schedules"
                    :key="schedule.showtime"
                    @click="openSeatSelection(movie, schedule)"
                  >
                    {{ formatDateTime(schedule.showtime) }} - {{ schedule.showroom }}
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
  
        <!-- Seat Selection Overlay -->
        <div v-if="showSeatSelection" class="seat-selection-overlay">
          <div class="seat-selection-content">
            <h3 style="color: yellow; font-size: 22px;">
              {{ selectedMovie.title }} - {{ formatDateTime(selectedShowtime) }} - {{ selectedMovie.showroom }}
            </h3>
            <h4 style="color: rgb(198, 207, 222); font-size: 18px;">Select Your Seats</h4>
            <div class="seat-grid">
              <div
                v-for="seat in selectedMovie.seats"
                :key="seat.id"
                class="seat-wrapper"
              >
                <div
                  :class="[
                    'seat',
                    seat.isSelected ? 'seat-selected' : '',
                    !seat.available ? 'seat-unavailable' : ''
                  ]"
                  @click="selectSeat(seat)"
                >
                  {{ seat.seatNumber }}
                </div>
                <div v-if="seat.isSelected && seat.available" class="age-selection">
                  <label for="age" class="age-label">Age:</label>
                  <select v-model="seat.age" class="age-dropdown">
                    <option value="">Select Age</option>
                    <option value="child">Child</option>
                    <option value="adult">Adult</option>
                    <option value="senior">Senior</option>
                  </select>
                </div>
              </div>
            </div>
  
            <!-- Action Buttons -->
            <div class="action-buttons">
              <!-- <button @click="addToCart(selectedMovie)">Add to Cart</button> -->
              <button @click="buyTicket(selectedMovie)">Buy Ticket</button>
              <button @click="closeSeatSelection">Close</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref, onMounted } from 'vue';
  import axios from 'axios';
  import { useRouter } from 'vue-router';
  import { useRoute } from 'vue-router';
  const movies = ref([]);
  const showSeatSelection = ref(false);
  const selectedMovie = ref(null);
  const selectedShowtime = ref('');
  const selectedShowRoom = ref('');
  const notification = ref({ message: '', type: '' });
  const router = useRouter();
  const route = useRoute(); // 使用路由模块

  const fetchMovies = async () => {
    try {
      const response = await axios.get('http://localhost:8080/movies');
      movies.value = response.data.map(movie => ({
        ...movie,
        schedules: movie.schedules.map(schedule => ({
          showtime: schedule.showtime,
          showroom: schedule.showroom,
          showroomId: schedule.showroomId,
        })),
      }));
      filterMoviesById(); 
      console.log('Movies:', movies.value);
    } catch (error) {
      console.error('Error fetching movies:', error);
      showNotification('Failed to load movies. Please try again later.', 'error');
    }
  };

  const filterMoviesById = () => {
  const movieId = route.params.id; // 获取 URL 中的 `id` 参数

  if (movieId != ":id") {
    movies.value = movies.value.filter((movie) => movie.id === parseInt(movieId, 10));
  } else {
    movies.value = movies.value; // 如果没有 `id` 参数，则显示所有电影
  }
};


  const openSeatSelection = async (movie, schedule) => {
    try {
      const response = await axios.get(`http://localhost:8080/schedules/movie/${movie.id}/seats`, {
        params: { showtime: schedule.showtime },
      });
      selectedMovie.value = { 
        ...movie, 
        seats: response.data, 
        showroom: schedule.showroom, 
        showtime: schedule.showtime 
      };
      selectedShowtime.value = schedule.showtime;
      selectedShowRoom.value = schedule.showroomId;
      showSeatSelection.value = true;
    } catch (error) {
      console.error('Error fetching seats:', error);
      showNotification('Failed to load seat information.', 'error');
    }
  };
  
  const closeSeatSelection = () => {
    showSeatSelection.value = false;
    selectedMovie.value = null;
    selectedShowtime.value = '';
    selectedShowRoom.value = '';
  };
  
  const showNotification = (message, type) => {
    notification.value = { message, type };
    setTimeout(() => {
      notification.value = { message: '', type: '' };
    }, 3000);
  };
  
  const addToCart = (movie) => {
    const selectedSeats = movie.seats.filter((seat) => seat.isSelected && seat.age);
    if (selectedSeats.length > 0) {
      const currentCart = JSON.parse(localStorage.getItem('cart') || '[]');
      currentCart.push({
        movie: movie.title,
        movieId: movie.id,
        showroomId: selectedShowRoom.value,
        showtime: selectedShowtime.value,
        showroom: movie.showroom,
        seats: selectedSeats.map((seat) => ({
          label: seat.seatNumber,
          price: seat.price,
          age: seat.age,
        })),
      });
      localStorage.setItem('cart', JSON.stringify(currentCart));
      movie.seats.forEach((seat) => (seat.isSelected = false));
      showNotification('Added to cart!', 'success');
    } else {
      showNotification('Please select seats, ages, and showtime.', 'error');
    }
  };
  
  const buyTicket = (movie) => {
    const selectedSeats = movie.seats.filter((seat) => seat.isSelected && seat.age);
    if (selectedSeats.length === 0) {
      alert('Please select at least one seat and assign age groups.');
      return;
    }
    const checkoutData = {
      movieTitle: movie.title,
      movieId: movie.id,
      showroomId: selectedShowRoom.value,
      showtime: selectedShowtime.value,
      showroom: movie.showroom,
      seats: selectedSeats.map((seat) => ({
        seatId: seat.id,
        seatNumber: seat.seatNumber,
        ageGroup: seat.age,
        price:
          seat.age === 'child'
            ? movie.price * 0.7
            : seat.age === 'senior'
            ? movie.price * 0.85
            : movie.price,
      })),
    };
    console.log('Checkout Data:', checkoutData);
    router.push({
      name: 'CheckOut',
      query: { data: JSON.stringify(checkoutData) },
    });
  };
  
  const selectSeat = (seat) => {
    if (!seat.available) {
      showNotification('This seat is not available.', 'error');
      return;
    }
    seat.isSelected = !seat.isSelected;
  };
  
  const formatDateTime = (dateTime) => {
    return new Date(dateTime).toLocaleString();
  };
  
  onMounted(() => {
    fetchMovies();
  });
  </script>
  

  
  <style scoped>
 /* 全局样式 */
html, body {
    margin: 0;
    padding: 0;
    height: 100%;
    font-family: 'Open Sans', sans-serif;
    background-color: #1c1c1c;
  }
  
  body {
    display: flex;
    justify-content: center;
    align-items: center;
  }
  
  /* 容器设置 */
  .container {
    min-height: 100vh;
    width: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    padding: 20px;
    background-color: #1c1c1c;
  }
  
  /* Ticket Page Layout */
  .ticket-page {
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
    padding-top: 80px;
  }
  
  /* Movie Section Styling */
  .movie-section {
    margin-bottom: 40px;
    padding: 25px;
    background: linear-gradient(135deg, #292929, #1f1f1f);
    border-radius: 12px;
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.4);
    transition: transform 0.3s, box-shadow 0.3s;
    overflow: hidden;
  }
  
  .movie-section:hover {
    transform: scale(1.02);
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.5);
  }
  
  /* Movie Information Layout */
  .movie-info {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 20px;
  }
  
  .trailer-container {
    width: 100%;
    max-width: 800px;
    height: 450px;
    margin-bottom: 20px;
    border-radius: 10px;
    overflow: hidden;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.4);
  }
  
  .movie-trailer {
    width: 100%;
    height: 100%;
  }
  
  /* Movie Details */
  .movie-title {
    font-size: 2.5em;
    font-weight: bold;
    color: #ffcc00;
    text-align: center;
    margin-bottom: 10px;
  }
  
  .movie-description {
    font-size: 1.1em;
    color: #cccccc;
    line-height: 1.6;
    margin-bottom: 15px;
    text-align: center;
  }
  
  .movie-meta {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
    font-size: 1.2em;
    line-height: 1.8;
    color: #cccccc;
    background: #292929;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.4);
  }
  
  .movie-meta p {
    font-size: 21px;
  }
  
  .movie-meta p strong {
    color: #ffcc00;
    font-weight: bold;
    margin-right: 8px;
  }
  
  /* Showtime Selection Styling */
  .showtime-title {
    font-size: 1.7em;
    margin-top: 30px;
    margin-bottom: 30px;
    color: #ffcc00;
    text-align: center;
    font-weight: bold;
  }
  
  .showtime-grid {
    display: flex;
    flex-wrap: wrap;
    gap: 15px;
    justify-content: center;
  }
  
  .showtime-grid button {
    padding: 10px 25px;
    font-size: 1.2em;
    background: #007bff;
    color: white;
    border: none;
    border-radius: 8px;
    cursor: pointer;
    transition: background-color 0.3s ease, transform 0.3s ease;
  }
  
  .showtime-grid button:hover {
    background-color: #0056b3;
    transform: translateY(-3px);
  }
  
  /* Seat Selection Overlay */
  .seat-selection-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.8);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
  }
  
  .seat-selection-content {
    background: #2b2b2b;
    padding: 30px;
    border-radius: 15px;
    width: 80%;
    max-width: 900px;
    max-height: 90%;
    overflow-y: auto;
    text-align: center;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.6);
  }
  
  /* Seat Grid */
  .seat-grid {
    display: grid;
    grid-template-columns: repeat(5, 1fr);
    gap: 15px;
    margin: 20px 0;
  }
  
  .seat {
    padding: 15px;
    background: #444444;
    border: 2px solid #666666;
    cursor: pointer;
    text-align: center;
    border-radius: 10px;
    color: #ffffff;
    font-size: 1em;
    transition: background-color 0.3s, border-color 0.3s;
  }
  
  .seat:hover {
    background-color: #555555;
  }
  
  .seat-selected {
    background-color: #28a745;
    border-color: #28a745;
    color: white;
  }
  
  .seat-unavailable {
    background-color: #95413e;
    border-color: #d9534f;
    color: #ffffff;
    cursor: not-allowed;
  }
  
  .seat-unavailable:hover {
    background-color: #d9534f;
    border-color: #d9534f;
  }
  
  /* Age Selection */
  .age-selection {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    margin-top: 10px;
  }
  
  .age-label {
    font-size: 1em;
    color: darkgoldenrod;
    margin-bottom: 5px;
    font-weight: bold;
  }
  
  .age-dropdown {
    width: 100%;
    max-width: 130px;
    padding: 8px;
    font-size: 1em;
    background-color: #292929;
    color: #ffffff;
    border: 1px solid #666666;
    border-radius: 5px;
    transition: border-color 0.3s, box-shadow 0.3s;
  }
  
  .age-dropdown:hover,
  .age-dropdown:focus {
    border-color: #ffcc00;
    box-shadow: 0 0 5px rgba(255, 204, 0, 0.8);
  }
  
  /* Action Buttons */
  .action-buttons {
    margin-top: 20px;
  }
  
  .action-buttons button {
    padding: 12px 25px;
    margin: 0 10px;
    background: #007bff;
    color: white;
    border: none;
    border-radius: 8px;
    font-size: 1em;
    cursor: pointer;
    transition: background-color 0.3s ease, transform 0.3s ease;
  }
  
  .action-buttons button:hover {
    background-color: #0056b3;
    transform: translateY(-3px);
  }
  
  /* Notification */
  .notification {
    position: fixed;
    top: 20px;
    left: 50%;
    transform: translateX(-50%);
    padding: 15px 25px;
    border-radius: 8px;
    color: white;
    font-weight: bold;
    z-index: 1001;
    transition: opacity 0.5s ease-in-out;
    background-color: #28a745;
  }
  
  .notification.error {
    background-color: #dc3545;
  }
  
  </style>
  