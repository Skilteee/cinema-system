<template>
    <div class="registration-container">
      <div class="registration-form">
        <h2 class="form-title">Join for free</h2>
  
        <p class="required-indicator">* Indicates a required field</p>
  
        <form @submit.prevent="registerUser">

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
            <label for="email">Email <span class="required">*</span></label>
            <input 
              type="email" 
              id="email" 
              v-model="user.email" 
              :class="{'error-border': errors.email}" 
              placeholder="Enter your email" 
            />
            <p v-if="errors.email" class="error-message">Email is required</p>
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
  
 
          <div class="form-group">
            <label for="phone">Phone Number</label>
            <input 
              type="tel" 
              id="phone" 
              v-model="user.phone" 
              placeholder="Enter your phone number" 
            />
          </div>
  
          <div class="form-group">
            <label for="address">Address</label>
            <input 
              type="text" 
              id="address" 
              v-model="user.address" 
              placeholder="Enter your address" 
            />
          </div>
          <div class="form-group promotion-option">
            <label class="promotion-label">
                <input type="checkbox" v-model="user.receivePromotion" class="large-checkbox" />
                I would like to receive promotional offers and updates.
            </label>
        </div>
        
          <button type="submit" class="submit-button">Register</button>
        </form>
  
        <div v-if="message" class="success-message">
          {{ message }}
        </div>
  
        <p class="sign-in-prompt">
          Already have an account? 
          <router-link to="/login" class="sign-in-link">Sign in</router-link>
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
          email: '',
          password: '',
          phone: '',    
          address: '',  
        },
        errors: {
          username: false,
          email: false,
          password: false,
        },
        message: '',
      };
    },
    methods: {
      validateForm() {
        this.errors.username = !this.user.username;
        this.errors.email = !this.user.email;
        this.errors.password = !this.user.password;
        return !this.errors.username && !this.errors.email && !this.errors.password;
      },
      async registerUser() {
      if (this.validateForm()) {
        try {
          // 使用 axios 发送 POST 请求到 Node.js 接口
          console.log('Sending user data:', this.user);

          const response = await axios.post('http://localhost:8080/api/register', this.user, {
            headers: {
              'Content-Type': 'application/json',
            },
          });

          this.message = `Registration successful for ${response.data.username}! Please check your email for the confirmation link.`;


          this.$router.push({ path: `/register/confirm/${this.user.username}` });


          console.log("Registering user:", this.user); 
          console.log("Response:", response.data);
          console.log("username :", response.data.username);
          console.log("username :", this.user.username);
          
          // 清空表单
          this.user.username = '';
          this.user.email = '';
          this.user.password = '';
          this.user.phone = ''; 
          this.user.address = '';
          this.errors = { username: false, email: false, password: false };
        }  catch (error) {
        // 检查是否是服务器错误响应，并根据错误信息显示在前端
        if (error.response && error.response.data) {
          this.message = error.response.data;  // 显示后端返回的错误信息
        } else {
          this.message = 'Registration failed. Please try again.';
        }
      }
    }else {
        this.message = '';
      }
    },
    },
  };
</script>
  
  <style scoped>
  .registration-container {
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
  .registration-form {
    max-width: 550px;
    width: 100%;
    padding: 25px;
    margin-top: 70px;
    margin-bottom:30px;
    background-color: #f9f9f9;
    border-radius: 8px;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  }
  
  h2 {
    text-align: center;
    margin-bottom: 10px;
  }
  
  .required-indicator {
    text-align: center;
    font-size: 14px;
    color: #555;
    margin-bottom: 5px;
  }
  
  .required {
    color: red;
    margin-left: 5px;
  }
  
  .form-group {
    margin-bottom: 15px;
  }
  .promotion-option {
    margin-bottom: 15px; /* 添加底部间距，保持与其他表单元素的间距一致 */
    }

    .promotion-label {

        align-items: center;
    }
    .large-checkbox {
        width: 10px;  /* 设置宽度 */
        height: 10px; /* 设置高度 */
        transform: scale(1.5); /* 放大复选框 */
        margin-right: 10px; /* 右侧间距 */
        cursor: pointer; /* 鼠标悬停时变为手型 */
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
    color: green;
    text-align: center;
  }
  
  .sign-in-prompt {
    text-align: center;
    margin-top: 15px;
  }
  
  .sign-in-link {
    color: #007bff;
    text-decoration: none;
  }
  
  .sign-in-link:hover {
    text-decoration: underline;
  }
  </style>
  