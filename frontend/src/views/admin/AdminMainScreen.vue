<template>
    <div class="admin-container">
      <h1 class="admin-title">Admin Dashboard</h1>
      <div class="admin-options">
        <RouterLink to="/admin/movies" class="option-card">
          <h2>Manage Movies</h2>
          <p>Schedule new movies, edit existing ones, and delete movies.</p>
        </RouterLink>
        <RouterLink to="/admin/users" class="option-card">
          <h2>Manage Users</h2>
          <p>View user profiles, manage roles, and handle user accounts.</p>
        </RouterLink>
        <RouterLink to="/admin/promotions" class="option-card">
          <h2>Manage Promotions</h2>
          <p>Create and manage promotional campaigns and discounts.</p>
        </RouterLink>
      </div>

    </div>
  </template>
  
  <script>
  export default {
    name: 'AdminMainScreen',
  };
  </script>
  
  <style scoped>
  .admin-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 40px;
    background-color: #2f2e2e;
    color: #f9f9f9;
    height: 100vh;
    font-family: 'Arial', sans-serif;
  }
  
  .admin-title {
    margin-bottom: 40px;
    font-size: 2.5em;
    font-weight: bold;
    text-align: center;
    color: #ffcc00; /* Highlight color */
  }
  
  .admin-options {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 40px;
    width: 100%;
    max-width: 1000px;
  }
  
  .option-card {
    background-color: #444;
    border-radius: 10px;
    padding: 20px;
    height: 165px;
    text-align: center;
    cursor: pointer;
    transition: background-color 0.3s, transform 0.2s;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
    text-decoration: none;
    color: #f9f9f9; /* Default text color */
  }
  
  .option-card:hover {
    background-color: #555;
    transform: translateY(-2px);
  }
  
  h2 {
    margin: 0;
    font-size: 1.8em;
    color: #ffcc00; /* Highlight color for headings */
  }
  
  p {
    font-size: 17px;
    margin: 10px 0 0;
    color: #ddd; /* Slightly lighter text color for descriptions */
  }
  </style>
  