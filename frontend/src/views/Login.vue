<template>
    <div class="login-container">
      <div class="login-form">
        <h2 class="form-title">User Login</h2>
  
        <form @submit.prevent="loginUser">
          <div class="form-group">
            <label for="username">Username <span class="required">*</span></label>
            <input 
              type="text" 
              id="username" 
              v-model="user.username" 
              :class="{'error-border': errors.username}" 
              placeholder="Enter your username" 
            />
            <p v-if="errors.username" class="error-message">Username is required</p>
          </div>
  
          <div class="form-group">
            <label for="password">Password <span class="required">*</span></label>
            <input 
              type="password" 
              id="password" 
              v-model="user.password" 
              :class="{'error-border': errors.password}" 
              placeholder="Enter your password" 
            />
            <p v-if="errors.password" class="error-message">Password is required</p>
          </div>
  
          <button type="submit" class="submit-button">Login</button>
        </form>
  
        <div v-if="message" class="success-message">
          {{ message }}
        </div>
  
        <p class="sign-up-prompt">
          Don't have an account? 
          <router-link to="/register" class="sign-up-link">Sign up</router-link>
        </p>
        <p class="forgot-password">
          <router-link to="/ForgetPwd" class="forgot-password-link">Forgot Password?</router-link>
        </p>
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
    mounted() {
      // 检查用户是否已登录
      const token = localStorage.getItem('token');
      const role = localStorage.getItem('role');
      if (token) {
        if (role === 'ADMIN') {
          this.$router.push('/admin'); // 如果是管理员，跳转到管理员页面
        } else {
          this.$router.push('/'); // 如果是普通用户，跳转到首页
        }
      }
    },
    methods: {
      validateForm() {
        this.errors.username = !this.user.username;
        this.errors.password = !this.user.password;
        return !this.errors.username && !this.errors.password;
      },
      async loginUser() {
        if (this.validateForm()) {
          try {
            // 使用 axios 发送 POST 请求到后端 API
            const response = await axios.post('http://localhost:8080/api/login', this.user);
  
            // 检查后端返回的角色并存储 token 和 role
            const { token, role, id } = response.data;
  
            // 存储 token 和角色到 localStorage
            localStorage.setItem('token', token);
            localStorage.setItem('role', role);
            localStorage.setItem('username', this.user.username);
            localStorage.setItem('id', id);

            // 根据角色重定向
            if (role === 'ADMIN') {
              this.message = `Welcome back, ${this.user.username}! Redirecting to admin dashboard...`;
              await new Promise(resolve => setTimeout(resolve, 1000));
              this.$router.push('/admin');
            } else if (role === 'USER') {
              this.message = `Welcome back, ${this.user.username}! Redirecting to homepage...`;
              await new Promise(resolve => setTimeout(resolve, 1000));
              this.$router.push('/');
            } else {
              this.message = 'Unexpected role, please contact support.';
            }
          } catch (error) {
            console.error('Error during login:', error);
            // 显示后端返回的错误信息，或使用默认消息
            this.message =
              (error.response && error.response.data.message) || 'Invalid username or password.';
          }
        } else {
          this.message = 'Please fill in all required fields.';
        }
      },
    },
  };
  </script>
  

  
    <style scoped>
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
    