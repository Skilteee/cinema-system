<template>
    <div class="modal-background">
      <div class="modal-content">
        <h2>{{ promotion ? 'Edit Promotion' : 'Add Promotion' }}</h2>
        <form @submit.prevent="submitForm">
          <label for="code">Promotion Code:</label>
          <input type="text" v-model="promotionData.code" required />
  
          <label for="discount">Discount (%):</label>
          <input type="number" v-model="promotionData.discount" required min="0" max="100" />
  
          <label for="expirationDate">Expiration Date:</label>
          <input type="date" v-model="promotionData.expirationDate" required />
  
          <div class="modal-actions">
            <button type="submit" class="save-button">Save</button>
            <button type="button" class="cancel-button" @click="$emit('close')">Cancel</button>
          </div>
        </form>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    props: {
      promotion: {
        type: Object,
        default: () => null,
      },
    },
    data() {
      return {
        promotionData: this.promotion ? { ...this.promotion } : { code: '', discount: '', expirationDate: '' },
      };
    },
    methods: {
      submitForm() {
        this.$emit('save', this.promotionData);
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
  
  .save-button, .cancel-button {
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
  