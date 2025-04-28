<template>
    <div class="unauthorized-container">
      <h1 class="unauthorized-title">Access Denied</h1>
      <p class="unauthorized-message">You do not have permission to view this page.</p>
      <button @click="goToLogin" class="return-button">Return to Login</button>
      <button @click="goHome" class="home-button">Go to Homepage</button>
    </div>
  </template>
  
  <script>
  export default {
    name: "UnauthorizedScreen",
    methods: {
      goToLogin() {
        this.$router.push("/login");
      },
      goHome() {
        this.$router.push("/");
      },
    },
  };
  </script>
  
  <style scoped>
  .unauthorized-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    text-align: center;
    height: 100vh;
    background-color: #2f2e2e;
    color: #f9f9f9;
    font-family: 'Arial', sans-serif;
  }
  
  .unauthorized-title {
    font-size: 2.5em;
    margin-bottom: 20px;
    color: #ff4c4c; /* Highlight color for error */
  }
  
  .unauthorized-message {
    font-size: 1.2em;
    margin-bottom: 30px;
    color: #ddd;
  }
  
  .return-button,
  .home-button {
    padding: 10px 20px;
    font-size: 1em;
    margin: 10px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .return-button {
    background-color: #007bff;
    color: white;
  }
  
  .return-button:hover {
    background-color: #0056b3;
  }
  
  .home-button {
    background-color: #28a745;
    color: white;
  }
  
  .home-button:hover {
    background-color: #218838;
  }
  </style>
  