<template>
    <div class="login-container">
      <div class="login-form">
        <h2 class="form-title">User logout</h2>
             <!-- 退出按钮 -->
      <button @click="logoutUser" class="logout-button">Logout</button>
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        user: {
          username: '',
          password: '',
        },
        errors: {
          username: false,
          password: false,
        },
        message: '',
      };
    },
    methods: {
        logoutUser() {
      // 清除 localStorage 中的用户名
        localStorage.removeItem('token');
        localStorage.removeItem('username');
        localStorage.removeItem('role');
      // 可选：重定向用户到登录页面
        this.$router.push('/login');
        },
    },
  };
  </script>
    
    <style scoped>




.logout-button {
  background-color: #ff4d4d;
  color: white;
  border: none;
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
  border-radius: 5px;
}

.logout-button:hover {
  background-color: #ff3333;
}
    .login-container {
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100vh;
      background-color: #2f2e2e;
    }
    .form-title{
      font-size : 30px;
      font-weight : bold;
      font-family: "Arial Black", Gadget, sans-serif;
    }
    .login-form {
    text-align: center;
      max-width: 450px;
      width: 100%;
      padding: 25px;
      background-color: #f9f9f9;
      border-radius: 8px;
      box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
    }
    
    h2 {
      text-align: center;
      margin-bottom: 20px;
    }
    
    .required {
      color: red;
      margin-left: 5px;
    }
    
    .form-group {
      margin-bottom: 15px;
    }
    
    label {
      display: block;
      margin-bottom: 5px;
      font-weight: bold;
    }
    
    input {
      width: 100%;
      padding: 10px;
      font-size: 16px;
      border: 1px solid #ccc;
      border-radius: 4px;
    }
    
    .error-border {
      border-color: red;
    }
    
    .error-message {
      color: red;
      font-size: 14px;
      margin-top: 5px;
    }
    
    .submit-button {
      width: 100%;
      padding: 12px;
      font-size: 16px;
      background-color: #007bff;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      margin-top: 10px;
    }
    
    .submit-button:hover {
      background-color: #0056b3;
    }
    
    .success-message {
      margin-top: 20px;
      color: red;
      text-align: center;
    }
    
    .sign-up-prompt {
      text-align: center;
      margin-top: 15px;
    }
    .forgot-password {
      text-align: center;
      margin-top: 15px;
    }
    
    
    .sign-up-link {
      color: #007bff;
      text-decoration: none;
    }
  
    .forgot-password-link {
      color: #007bff;
      text-decoration: none;
    }
    
    .sign-up-link:hover {
      text-decoration: underline;
    }
    </style>
    