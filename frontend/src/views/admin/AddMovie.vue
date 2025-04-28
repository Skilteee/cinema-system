<template>
    <div class="modal-background" @click.self="close">
      <div class="modal-content">
        <h2>Add New Movie</h2>
        <form @submit.prevent="addMovie">
          <label for="title">Title:</label>
          <input id="title" v-model="movie.title" required />
  
          <label for="genre">Genre:</label>
          <input id="genre" v-model="movie.genre" required />
  
          <label for="duration">Duration (min):</label>
          <input id="duration" type="number" v-model="movie.duration" required />
  
          <label for="price">Price ($):</label>
          <input id="price" type="number" v-model="movie.price" step="0.01" required />
  
          <label for="category">Category:</label>
          <input id="category" v-model="movie.category" required />

          <label for="rating">Rating:</label>
          <input id="rating" type="number" v-model="movie.mpaarating" required />
  
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
    data() {
      return {
        movie: {
          title: "",
          genre: "",
          duration: 0,
          price: 0.0,
          category: "",
          trailer: "",
          rating: 0,
        },
      };
    },
    methods: {
      addMovie() {
        this.movie.trailer = this.convertYouTubeLink(this.movie.trailer);
        this.$emit("save", this.movie);
        this.close();
      },

      convertYouTubeLink(url) {
        const regExp = /^.*(?:youtu\.be\/|v=|\/v\/|u\/\w\/|embed\/|watch\?v=&?)([^#&?]*).*/;
        const match = url.match(regExp);
        const videoId = match && match[1].length === 11 ? match[1] : null;

        // 如果匹配成功，返回嵌入链接，否则返回原始输入
        return videoId ? `https://www.youtube.com/embed/${videoId}` : url;
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
  