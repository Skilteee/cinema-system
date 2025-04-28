<template>
    <div class="modal-background" @click.self="close">
      <div class="modal-content">
        <h2>Edit Movie: {{ movie.title }}</h2>
        <form @submit.prevent="editMovie">
          <label for="title">Title:</label>
          <input id="title" v-model="movie.title" required />
  
          <label for="genre">Genre:</label>
          <input id="genre" v-model="movie.genre" required />
  
          <label for="duration">Duration (min):</label>
          <input id="duration"  v-model="movie.duration" required />
  
          <label for="price">Price ($):</label>
          <input id="price" type="number" v-model="movie.price" step="0.01" required />
  
          <label for="category">Category:</label>
          <input id="category" v-model="movie.category" required />


          <label for="description">Rating:</label>
          <input id="rating" v-model="movie.mpaaRating" required />
          
          <label for="trailer">Trailer URL:</label>
          <input id="trailer" v-model="movie.trailer" required />
  
          <div class="modal-actions">
            <button type="submit" class="save-button">Save</button>
            <button type="button" class="cancel-button" @click="close">Cancel</button>
          </div>
        </form>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    props: {
      movie: Object,
    },
    methods: {
      editMovie() {
        console.log(this.movie);
        this.$emit("save", this.movie);
        this.close();
      },
      close() {
        this.$emit("close");
      },
    },
  };
  </script>
  


  <style scoped>
  .modal-background {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.7);
    display: flex;
    justify-content: center;
    align-items: center;
  }
  
  .modal-content {
    background: #333;
    padding: 20px;
    border-radius: 8px;
    width: 400px;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.5);
  }
  
  h2 {
    margin-bottom: 15px;
    color: #ffcc00; /* Highlight color */
  }
  
  label {
    display: block;
    margin-bottom: 5px;
    color: #f9f9f9;
  }
  
  input {
    width: 100%;
    padding: 8px;
    margin-bottom: 15px;
    border: none;
    border-radius: 4px;
    background-color: #444;
    color: #f9f9f9;
  }
  
  input:focus {
    outline: none;
    background-color: #555;
  }
  
  .modal-actions {
    display: flex;
    justify-content: space-between;
  }
  
  .save-button,
  .cancel-button {
    padding: 10px 15px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .save-button {
    background-color: #007bff;
    color: white;
  }
  
  .save-button:hover {
    background-color: #0056b3;
  }
  
  .cancel-button {
    background-color: #dc3545;
    color: white;
  }
  
  .cancel-button:hover {
    background-color: #c82333;
  }
  </style>
  