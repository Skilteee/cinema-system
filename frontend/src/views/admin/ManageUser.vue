<template>
    <div class="users-container">
      <h1 class="users-title">Manage Users</h1>
  
      <button class="add-user-button" @click="showAddUserModal">Add User</button>
  
      <table class="users-table">
        <thead>
          <tr>
            <th>Username</th>
            <th>Email</th>
            <th>Role</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in paginatedUsers" :key="user.id">
            <td>{{ user.username }}</td>
            <td>{{ user.email }}</td>
            <td>{{ user.role }}</td>
            <td>
              <button @click="editUser(user)" class="edit-button">Edit</button>
              <button @click="deleteUser(user.id)" class="delete-button">Delete</button>
            </td>
          </tr>
        </tbody>
      </table>
  
      <div class="pagination">
        <button class="pagination-button" :disabled="currentPage === 1" @click="currentPage--">
          Previous
        </button>
        <span>Page {{ currentPage }} of {{ totalPages }}</span>
        <button class="pagination-button" :disabled="currentPage === totalPages" @click="currentPage++">
          Next
        </button>
      </div>
  
      <AddUserModal
        v-if="isModalVisible"
        @close="closeModal"
        @save="saveUser"
        :user="selectedUser"
      />
    </div>
  </template>
  
  <script>
  import axios from "axios";
  import AddUserModal from "./AddUserModal.vue";
  
  export default {
    components: {
      AddUserModal,
    },
    data() {
      return {
        users: [],
        isModalVisible: false,
        selectedUser: null,
        currentPage: 1,
        itemsPerPage: 5,
      };
    },
    computed: {
      totalPages() {
        return Math.ceil(this.users.length / this.itemsPerPage);
      },
      paginatedUsers() {
        const start = (this.currentPage - 1) * this.itemsPerPage;
        return this.users.slice(start, start + this.itemsPerPage);
      },
    },
    methods: {
      async fetchUsers() {
        try {
          const response = await axios.get("http://localhost:8080/api");
          this.users = response.data;
        } catch (err) {
          console.error("Error fetching users:", err);
        }
      },
      showAddUserModal() {
        this.selectedUser = null;
        this.isModalVisible = true;
      },
      editUser(user) {
        this.selectedUser = { ...user };
        this.isModalVisible = true;
      },
      async saveUser(user) {

        if (user.id) {

          try {
            const response = await axios.put(
              `http://localhost:8080/api/${user.id}`,
              user
            );
            const index = this.users.findIndex((u) => u.id === user.id);
            if (index !== -1) {
              this.users.splice(index, 1, response.data);
            }
            alert("User updated successfully!");
          } catch (err) {
            console.error("Error updating user:", err);
          }
        } else {

          try {
            const response = await axios.post("http://localhost:8080/api/add", user);
            this.users.push(response.data);
            alert("User added successfully!");
          } catch (err) {
            console.error("Error adding user:", err);
          }
        }
        this.closeModal();
      },
      async deleteUser(userId) {
        try {
          await axios.delete(`http://localhost:8080/api/${userId}`);
          this.users = this.users.filter((user) => user.id !== userId);
          alert("User deleted successfully!");
        } catch (err) {
          console.error("Error deleting user:", err);
        }
      },
      closeModal() {
        this.isModalVisible = false;
      },
    },
    mounted() {
      this.fetchUsers();
    },
  };
  </script>
  

  
  <style scoped>
  .users-container {
    padding: 20px;
    background-color: #2f2e2e;
    color: #f9f9f9;
    height: 100vh;
    overflow-y: auto;
    font-family: 'Arial', sans-serif;
  }
  
  .users-title {
    margin-top: 65px;
    margin-bottom: 20px;
    font-size: 2.4em;
    text-align: center;
    color: #ffcc00;
  }
  
  .add-user-button {
    margin-bottom: 20px;
    padding: 10px 15px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .add-user-button:hover {
    background-color: #0056b3;
  }
  
  .users-table {
    width: 100%;
    border-collapse: collapse;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
  }
  
  .users-table th,
  .users-table td {
    padding: 15px;
    text-align: left;
    border-bottom: 1px solid #444;
  }
  
  .users-table th {
    background-color: #333;
    color: #ffcc00;
    font-size: 1.2em;
  }
  
  .users-table tr:hover {
    background-color: #444;
  }
  
  .edit-button,
  .delete-button {
    padding: 5px 10px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
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
  