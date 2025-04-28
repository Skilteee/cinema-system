<template>
    <div class="confirmation-container">
      <div class="confirmation-card">
        <p class="title">Account Activation Required!</p>
        <p style="font:1.2em sans-serif;font-weight: bold; " >Thank you for registering, {{ username }}!</p>
        <p style="font: 1.2em sans-serif;"> Please check your email inbox for an account activation link. You need to activate your account before logging in.</p>
  
        <div class="button-group">
          <router-link to="/login" class="button">Log In</router-link>
          <router-link to="/" class="button">Back to Home</router-link>
        </div>
      </div>
    </div>
  </template>
  
  <script>
export default {
  data() {
    return {
      username: null,
    };
  },
  created() {
    // Get the username from the route params
    this.username = this.$route.params.username;
    console.log("Username from route params:", this.username); // Debugging
  },
};
  </script>
  
  <style scoped>
  .confirmation-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background-color: #2f2e2e;
  }
  
  .confirmation-card {
    max-width: 550px;
    width: 100%;
    padding: 25px;
    background-color: #f9f9f9;
    border-radius: 8px;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
    text-align: center;
  }
  
  .title {
    font-size: 25px;
    font-weight: bold;
    margin-bottom: 15px;

    color: #007bff;
  }
  
  p {
    font-size: 16px;
    margin-bottom: 20px;
    font-family: 'Arial', sans-serif;
  }
  
  .button-group {
    display: flex;
    justify-content: space-around;
  }
  
  .button {
    padding: 12px 20px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 4px;
    text-decoration: none;
    cursor: pointer;
  }
  
  .button:hover {
    background-color: #0056b3;
  }
  </style>
  