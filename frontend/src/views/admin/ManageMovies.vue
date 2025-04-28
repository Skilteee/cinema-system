<template>
    <div class="movies-container">
      <h1 class="movies-title">Manage Movies</h1>
  
      <button class="add-movie-button" @click="showAddMovieModal">Add Movie</button>
  
      <table class="movies-table">
        <thead>
          <tr>
            <th>Title</th>
            <th>Genre</th>
            <th>Duration</th>
            <th>Price ($)</th>
            <th>Category</th>
            <th>Trailer</th>
            <th>Rating</th>
            <th>Showtimes</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="movie in paginatedMovies" :key="movie.id">
            <td>{{ movie.title }}</td>
            <td>{{ movie.genre }}</td>
            <td>{{ movie.duration }}</td>
            <td>{{ movie.price }}</td>
            <td>{{ movie.category }}</td>
            <td>
              <a :href="movie.trailer" target="_blank" style="color:white">Watch Trailer</a>
            </td>
            <td>{{ movie.mpaaRating }}</td>
            <td>
              <ul>
                <li v-for="showtime in movie.showtimes" :key="showtime.id">
                  {{ formatDateTime(showtime.showtime) }} - {{ showtime.showroom.name }}
                </li>
              </ul>
            </td>
            <td>
              <button @click="editMovie(movie)" class="edit-button">Edit</button>
              <button @click="scheduleMovie(movie)" class="schedule-button">Schedule</button>
              <button @click="deleteMovie(movie.id)" class="delete-button">Delete</button>
            </td>
          </tr>
        </tbody>
      </table>
  
      <div class="pagination">
        <button
          class="pagination-button"
          :disabled="currentPage === 1"
          @click="currentPage--"
        >
          Previous
        </button>
        <span>Page {{ currentPage }} of {{ totalPages }}</span>
        <button
          class="pagination-button"
          :disabled="currentPage === totalPages"
          @click="currentPage++"
        >
          Next
        </button>
      </div>
  
      <!-- Modals -->
      <AddMovie
        v-if="isAddModalVisible"
        @close="closeAddModal"
        @save="addMovie"
      />
      <EditMovie
        v-if="isEditModalVisible"
        @close="closeEditModal"
        @save="updateMovie"
        :movie="selectedMovie"
      />
      <ScheduleMovie
        v-if="isScheduleModalVisible"
        @close="closeScheduleModal"
        @save="addSchedule"
        :movie="selectedMovie"
        :showrooms="showrooms"
      />
    </div>
  </template>
  
  <script>
  import axios from "axios";
  import AddMovie from "./AddMovie.vue";
  import EditMovie from "./EditMovie.vue";
  import ScheduleMovie from "./ScheduleMovie.vue";
  
  export default {
    components: {
      AddMovie,
      EditMovie,
      ScheduleMovie,
    },
    data() {
      return {
        movies: [],
        showrooms: [],
        isAddModalVisible: false,
        isEditModalVisible: false,
        isScheduleModalVisible: false,
        selectedMovie: null,
        currentPage: 1,
        itemsPerPage: 5,
      };
    },
    computed: {
      totalPages() {
        return Math.ceil(this.movies.length / this.itemsPerPage);
      },
      paginatedMovies() {
        const start = (this.currentPage - 1) * this.itemsPerPage;
        return this.movies.slice(start, start + this.itemsPerPage);
      },
    },
    methods: {
      async fetchMovies() {
        try {
          const response = await axios.get("http://localhost:8080/movies");
          this.movies = response.data.map((movie) => ({
            ...movie,
            showtimes: [],
          }));
          await Promise.all(
            this.movies.map(async (movie) => {
              const scheduleResponse = await axios.get(
                `http://localhost:8080/schedules/movie/${movie.id}`
              );
              movie.showtimes = scheduleResponse.data;
            })
          );
        } catch (err) {
          console.error("Error fetching movies:", err);
        }
      },
      async fetchShowrooms() {
        try {
          const response = await axios.get("http://localhost:8080/showrooms");
          this.showrooms = response.data;
        } catch (err) {
          console.error("Error fetching showrooms:", err);
        }
      },
      async addMovie(movie) {
        try {
          const response = await axios.post("http://localhost:8080/movies", movie);
          this.movies.push(response.data);
          alert("Movie added successfully!");
        } catch (err) {
          console.error("Error adding movie:", err);
        }
      },
      async updateMovie(movie) {
        try {
          const response = await axios.put(
            `http://localhost:8080/movies/${movie.id}`,
            movie
          );
          const index = this.movies.findIndex((m) => m.id === movie.id);
          if (index !== -1) {
            this.movies.splice(index, 1, response.data);
          }
          alert("Movie updated successfully!");
        } catch (err) {
          console.error("Error updating movie:", err);
        }
      },
      async deleteMovie(movieId) {
        try {
          await axios.delete(`http://localhost:8080/movies/${movieId}`);
          this.movies = this.movies.filter((movie) => movie.id !== movieId);
          alert("Movie deleted successfully!");
        } catch (err) {
          console.error("Error deleting movie:", err);
        }
      },
      async addSchedule(scheduleDetails) {
        try {
          await axios.post("http://localhost:8080/schedules", scheduleDetails);
          alert("Schedule added successfully!");
          this.closeScheduleModal();
          this.fetchMovies();
        } catch (err) {
          console.error("Error adding schedule:", err);
        }
      },
      editMovie(movie) {
        this.selectedMovie = { ...movie };
        this.isEditModalVisible = true;
      },
      showAddMovieModal() {
        this.isAddModalVisible = true;
      },
      scheduleMovie(movie) {
        this.selectedMovie = movie;
        this.isScheduleModalVisible = true;
      },
      closeAddModal() {
        this.isAddModalVisible = false;
      },
      closeEditModal() {
        this.isEditModalVisible = false;
      },
      closeScheduleModal() {
        this.isScheduleModalVisible = false;
      },
      formatDateTime(dateTimeString) {
        return new Date(dateTimeString).toLocaleString();
      },
    },
    mounted() {
      this.fetchMovies();
      this.fetchShowrooms();
    },
  };
  </script>
  

  <style scoped>
  .movies-container {
    padding: 20px;
    background-color: #2f2e2e;
    color: #f9f9f9;
    height: 100vh;
    overflow-y: auto;
    font-family: Arial, sans-serif;
  }
  
  .movies-title {
    margin-top: 65px;
    margin-bottom: 20px;
    font-size: 2.4em;
    text-align: center;
    color: #ffcc00;
  }
  
  .add-movie-button {
    margin-bottom: 20px;
    padding: 10px 15px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .add-movie-button:hover {
    background-color: #0056b3;
  }
  
  .movies-table {
    width: 100%;
    border-collapse: collapse;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
  }
  
  .movies-table th,
  .movies-table td {
    padding: 15px;
    text-align: left;
    border-bottom: 1px solid #444;
  }
  
  .movies-table th {
    background-color: #333;
    color: #ffcc00;
    font-size: 1.2em;
  }
  
  .movies-table tr:hover {
    background-color: #444;
  }
  
  .edit-button,
  .schedule-button,
  .delete-button {
    padding: 5px 10px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .edit-button {
    background-color: #ffc107;
    color: #212529;
  }
  
  .edit-button:hover {
    background-color: #e0a800;
  }
  
  .schedule-button {
    background-color: #17a2b8;
    color: white;
    margin-left: 5px;
  }
  
  .schedule-button:hover {
    background-color: #138496;
  }
  
  .delete-button {
    background-color: #dc3545;
    color: white;
    margin-left: 5px;
  }
  
  .delete-button:hover {
    background-color: #c82333;
  }
  
  .pagination {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 20px;
  }
  
  .pagination-button {
    padding: 10px 15px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s;
    margin: 0 10px;
  }
  
  .pagination-button:disabled {
    background-color: #6c757d;
    cursor: not-allowed;
  }
  
  .pagination-button:hover:not(:disabled) {
    background-color: #0056b3;
  }
  </style>
  