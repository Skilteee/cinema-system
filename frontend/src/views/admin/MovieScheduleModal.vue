<template>
    <div class="modal-overlay" @click.self="close">
      <div class="modal-content">
        <h2>{{ movie ? 'Edit Movie' : 'Add Movie' }}</h2>
        <form @submit.prevent="save">
          <div>
            <label>Title</label>
            <input type="text" v-model="form.title" required />
          </div>
          <div>
            <label>Genre</label>
            <input type="text" v-model="form.genre" required />
          </div>
          <div>
            <label>Duration (min)</label>
            <input type="number" v-model="form.duration" required />
          </div>
          <div>
            <label>Tickets Available</label>
            <input type="number" v-model="form.tickets" required />
          </div>
          <div>
            <label>Ticket Price ($)</label>
            <input type="number" v-model="form.price" required />
          </div>
          <div>
            <label>Rating</label>
            <input type="number" v-model="form.rating" required />
          </div>
          <div>
            <label>Showtime</label>
            <input type="text" v-model="form.showtime" required />
          </div>
          <button type="submit">{{ movie ? 'Update' : 'Add' }}</button>
          <button type="button" @click="close">Cancel</button>
        </form>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    props: {
      movie: Object,
    },
    data() {
      return {
        form: {
          title: '',
          genre: '',
          duration: 0,
          tickets: 0,
          price: 0,
          showtime: '',
          rating: 0,
        },
      };
    },
    watch: {
      movie: {
        immediate: true,
        handler(newMovie) {
          if (newMovie) {
            this.form = { ...newMovie };
          }
        },
      },
    },
    methods: {
      save() {
        this.$emit('save', this.form);
      },
      close() {
        this.$emit('close');
      },
    },
  };
  </script>
  
  <style scoped>
  .modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.7);
    display: flex;
    align-items: center;
    justify-content: center;
  }
  
  .modal-content {
    background: white;
    padding: 20px;
    border-radius: 5px;
    max-width: 400px;
    width: 100%;
  }
  
  h2 {
    margin-bottom: 20px;
  }
  
  form div {
    margin-bottom: 15px;
  }
  
  label {
    display: block;
    margin-bottom: 5px;
  }
  
  input {
    width: 100%;
    padding: 8px;
    border: 1px solid #ccc;
    border-radius: 4px;
  }
  
  button {
    margin-right: 10px;
  }
  </style>
  