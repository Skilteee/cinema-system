<template>
    <div class="ticket-page">
      <div 
        v-for="movie in movies" 
        :key="movie.id" 
        class="movie-section"
      >
        <div class="movie-info">
          <img :src="movie.poster" alt="Movie Poster" class="movie-poster" />
  
          <div class="movie-details">
            <h2>{{ movie.title }}</h2>
            <p>{{ movie.description }}</p>
            <p><strong>Duration:</strong> {{ movie.duration }} minutes</p>
            <p><strong>Rating:</strong> {{ movie.rating }}/10</p>
  
            <div class="seat-selection">
              <h3>Select Your Seats</h3>
              <div class="seat-grid">
                <div 
                  v-for="seat in movie.seats" 
                  :key="seat.id" 
                  :class="{ 'seat-selected': seat.isSelected, 'seat': !seat.isSelected }"
                  @click="selectSeat(movie.id, seat)"
                >
                  {{ seat.label }}
                </div>
              </div>
            </div>
  
            <div class="buy-ticket">
              <button @click="buyTicket(movie)">Buy Ticket</button>
            </div>
          </div>
        </div>
      </div>
    </div>
</template>
  
  <script setup lang="ts">
  import { ref } from 'vue';
  import { useRouter } from 'vue-router';

  import poster1 from '@/assets/poster1.jpg';
  import poster2 from '@/assets/poster2.jpg';
  import poster3 from '@/assets/poster3.jpg';
  
  const movies = ref([
    {
      id: 1,
      title: 'NEVER LET GO',
      description: 'This is the description of movie 1.',
      duration: 120,
      rating: 8.5,
      poster: poster1,
      showTimes: ['1:00 PM', '4:00 PM', '7:00 PM'], 
      seats: [
        { id: 1, label: 'A1', isSelected: false },
        { id: 2, label: 'A2', isSelected: false },
        { id: 3, label: 'A3', isSelected: false },
        { id: 3, label: 'A4', isSelected: false },
        { id: 3, label: 'A5', isSelected: false },
        { id: 3, label: 'A6', isSelected: false },
        { id: 3, label: 'A7', isSelected: false },
        { id: 3, label: 'A8', isSelected: false },
        { id: 3, label: 'A9', isSelected: false },
        { id: 4, label: 'A10', isSelected: false },
        { id: 5, label: 'A11', isSelected: false },
        { id: 6, label: 'A12', isSelected: false },
        { id: 1, label: 'B1', isSelected: false },
        { id: 2, label: 'B2', isSelected: false },
        { id: 3, label: 'B3', isSelected: false },
        { id: 3, label: 'B4', isSelected: false },
        { id: 3, label: 'B5', isSelected: false },
        { id: 3, label: 'B6', isSelected: false },
        { id: 3, label: 'B7', isSelected: false },
        { id: 3, label: 'B8', isSelected: false },
        { id: 3, label: 'B9', isSelected: false },
        { id: 4, label: 'B10', isSelected: false },
        { id: 5, label: 'B11', isSelected: false },
        { id: 6, label: 'B12', isSelected: false },
      ]
    },
    {
      id: 2,
      title: 'OPPENHEIMER',
      description: 'This is the description of movie 2.',
      duration: 140,
      rating: 9.0,
      poster: poster2,
      showTimes: ['2:00 PM', '5:00 PM', '8:00 PM'],
      seats: [
        { id: 1, label: 'A1', isSelected: false },
        { id: 2, label: 'A2', isSelected: false },
        { id: 3, label: 'A3', isSelected: false },
        { id: 3, label: 'A4', isSelected: false },
        { id: 3, label: 'A5', isSelected: false },
        { id: 3, label: 'A6', isSelected: false },
        { id: 3, label: 'A7', isSelected: false },
        { id: 3, label: 'A8', isSelected: false },
        { id: 3, label: 'A9', isSelected: false },
        { id: 4, label: 'A10', isSelected: false },
        { id: 5, label: 'A11', isSelected: false },
        { id: 6, label: 'A12', isSelected: false },
        { id: 1, label: 'B1', isSelected: false },
        { id: 2, label: 'B2', isSelected: false },
        { id: 3, label: 'B3', isSelected: false },
        { id: 3, label: 'B4', isSelected: false },
        { id: 3, label: 'B5', isSelected: false },
        { id: 3, label: 'B6', isSelected: false },
        { id: 3, label: 'B7', isSelected: false },
        { id: 3, label: 'B8', isSelected: false },
        { id: 3, label: 'B9', isSelected: false },
        { id: 4, label: 'B10', isSelected: false },
        { id: 5, label: 'B11', isSelected: false },
        { id: 6, label: 'B12', isSelected: false },
      ]
    },
    {
      id: 3,
      title: 'HOME ALONG',
      description: 'This is the description of movie 3.',
      duration: 130,
      rating: 7.8,
      poster: poster3,
      showTimes: ['12:00 PM', '3:00 PM', '6:00 PM'],
      seats: [
        { id: 1, label: 'A1', isSelected: false },
        { id: 2, label: 'A2', isSelected: false },
        { id: 3, label: 'A3', isSelected: false },
        { id: 3, label: 'A4', isSelected: false },
        { id: 3, label: 'A5', isSelected: false },
        { id: 3, label: 'A6', isSelected: false },
        { id: 3, label: 'A7', isSelected: false },
        { id: 3, label: 'A8', isSelected: false },
        { id: 3, label: 'A9', isSelected: false },
        { id: 4, label: 'A10', isSelected: false },
        { id: 5, label: 'A11', isSelected: false },
        { id: 6, label: 'A12', isSelected: false },
        { id: 1, label: 'B1', isSelected: false },
        { id: 2, label: 'B2', isSelected: false },
        { id: 3, label: 'B3', isSelected: false },
        { id: 3, label: 'B4', isSelected: false },
        { id: 3, label: 'B5', isSelected: false },
        { id: 3, label: 'B6', isSelected: false },
        { id: 3, label: 'B7', isSelected: false },
        { id: 3, label: 'B8', isSelected: false },
        { id: 3, label: 'B9', isSelected: false },
        { id: 4, label: 'B10', isSelected: false },
        { id: 5, label: 'B11', isSelected: false },
        { id: 6, label: 'B12', isSelected: false },
      ]
    }
  ]);




const router = useRouter();
  
const selectSeat = (movieId: number, seat: any) => {
  seat.isSelected = !seat.isSelected;
};

const buyTicket = (movie: any) => {
  const selectedSeats = movie.seats.filter((seat: any) => seat.isSelected);
  if (selectedSeats.length > 0) {
    router.push({
      name: 'CheckOutPage',
      query: {
        movieId: movie.id.toString(),
        seats: JSON.stringify(selectedSeats.map((seat: any) => seat.label)) 
      }
    });
  } else {
    alert('Please select at least one seat.');
  }
};
    
  </script>
  
  <style scoped>
  .ticket-page {
    max-width: 1000px;
    margin: 0 auto;
    padding: 20px;
    padding-top: 80px;
  }
  
  .movie-section {
    margin-bottom: 40px;
  }
  
  .movie-info {
    display: flex;
    align-items: flex-start;
  }
  
  .movie-poster {
    width: 200px;
    height: 300px;
    object-fit: cover;
    border-radius: 10px;
    margin-right: 20px;
  }
  
  .movie-details {
    flex-grow: 1;
  }
  
  .movie-details h2 {
    font-size: 1.8em;
    margin-bottom: 10px;
  }
  
  .seat-selection {
    margin-top: 20px;
  }
  
  .seat-grid {
    display: grid;
    grid-template-columns: repeat(12, 1fr);
    gap: 10px;
    max-width: 100%;
  }
  
  .seat {
    padding: 10px;
    background-color: lightgray;
    border: 2px solid #ccc;
    cursor: pointer;
    text-align: center;
    border-radius: 5px;
    color: black;
    font-weight: normal;
  }

  .seat:hover {
  background-color: #e0e0e0; 
  }
  
  .seat-selected {
    background-color: lightgreen;
    border-color: #28a745;
    color: black;
    font-weight: normal;
  }

  
  .buy-ticket {
    margin-top: 20px;
  }
  
  .buy-ticket button {
    padding: 10px 20px;
    font-size: 1.2em;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s ease;
  }
  
  .buy-ticket button:hover {
    background-color: #0056b3;
  }
  </style>