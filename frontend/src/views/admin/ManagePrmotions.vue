<template>
    <div class="promotions-container">
      <h1 class="promotions-title">Manage Promotions</h1>
  
      <button class="add-promotion-button" @click="showAddPromotionModal">Add Promotion</button>
  
      <table class="promotions-table">
        <thead>
          <tr>
            <th>Promotion Code</th>
            <th>Discount (%)</th>
            <th>Expiration Date</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="promotion in paginatedPromotions" :key="promotion.id">
            <td>{{ promotion.code }}</td>
            <td>{{ promotion.discount }}</td>
            <td>{{ formatDate(promotion.expirationDate) }}</td>
            <td>
              <button @click="editPromotion(promotion)" class="edit-button">Edit</button>
              <button @click="deletePromotion(promotion.id)" class="delete-button">Delete</button>
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
  
      <AddPromotionModal
        v-if="isModalVisible"
        @close="closeModal"
        @save="savePromotion"
        :promotion="selectedPromotion"
      />
    </div>
  </template>
  
  <script>
  import axios from "axios";
  import AddPromotionModal from "./AddPromotionModal.vue";
  
  export default {
    components: {
      AddPromotionModal,
    },
    data() {
      return {
        promotions: [],
        isModalVisible: false,
        selectedPromotion: null,
        currentPage: 1,
        itemsPerPage: 5,
      };
    },
    computed: {
      totalPages() {
        return Math.ceil(this.promotions.length / this.itemsPerPage);
      },
      paginatedPromotions() {
        const start = (this.currentPage - 1) * this.itemsPerPage;
        return this.promotions.slice(start, start + this.itemsPerPage);
      },
    },
    methods: {
      async fetchPromotions() {
        try {
          const response = await axios.get("http://localhost:8080/api/promotions");
          this.promotions = response.data;
        } catch (err) {
          console.error("Error fetching promotions:", err);
        }
      },
      showAddPromotionModal() {
        this.selectedPromotion = null;
        this.isModalVisible = true;
      },
      editPromotion(promotion) {
        this.selectedPromotion = { ...promotion };
        this.isModalVisible = true;
      },
      async savePromotion(promotion) {
        try {
          if (promotion.id) {
            // 更新促销
            const response = await axios.put(
              `http://localhost:8080/api/promotions/${promotion.id}`,
              promotion
            );
            const index = this.promotions.findIndex((p) => p.id === promotion.id);
            if (index !== -1) {
              this.promotions.splice(index, 1, response.data);
            }
            alert("Promotion updated successfully!");
          } else {
            // 添加促销
            const response = await axios.post("http://localhost:8080/api/promotions", promotion);
            this.promotions.push(response.data);
            alert("Promotion added successfully!");
          }
        } catch (err) {
          if (err.response && err.response.status === 409) {
            alert("Promotion code already exists!");
          } else {
            console.error("Error saving promotion:", err);
          }
        }
        this.closeModal();
      },
      async deletePromotion(id) {
        try {
          await axios.delete(`http://localhost:8080/api/promotions/${id}`);
          this.promotions = this.promotions.filter((p) => p.id !== id);
          alert("Promotion deleted successfully!");
        } catch (err) {
          console.error("Error deleting promotion:", err);
        }
      },
      closeModal() {
        this.isModalVisible = false;
      },
      formatDate(dateString) {
        return new Date(dateString).toLocaleDateString();
      },
    },
    mounted() {
      this.fetchPromotions();
    },
  };
  </script>
  

  
  <style scoped>
  .promotions-container {
    padding: 20px;
    background-color: #2f2e2e;
    color: #f9f9f9;
    height: 100vh;
    overflow-y: auto;
    font-family: 'Arial', sans-serif;
  }
  
  .promotions-title {
    margin-top: 65px;
    margin-bottom: 20px;
    font-size: 2.4em;
    text-align: center;
    color: #ffcc00; /* Highlight color */
  }
  
  .add-promotion-button {
    margin-bottom: 20px;
    padding: 10px 15px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .add-promotion-button:hover {
    background-color: #0056b3;
  }
  
  .promotions-table {
    width: 100%;
    border-collapse: collapse;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
  }
  
  .promotions-table th, .promotions-table td {
    padding: 15px;
    text-align: left;
    border-bottom: 1px solid #444;
  }
  
  .promotions-table th {
    background-color: #333;
    color: #ffcc00; /* Highlight color for headers */
    padding-top : 25px; /* Added padding to move headers down */
    padding-bottom: 25px; /* Added padding to move headers down */
    font-size: 1.2em; /* Increased font size for headers */
    font-weight: bold;
  }
  
  .promotions-table tr:hover {
    background-color: #444;
  }
  
  .edit-button, .delete-button {
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
  