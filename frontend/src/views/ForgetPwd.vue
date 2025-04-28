<template>
    <div class="container">
    <div class="forgot-password-container">
      <h2>Forgot Password</h2>
      <p>Please enter your email and the verification code we sent to your email.</p>
  
      <form @submit.prevent="verifyCode">
        <div class="form-group">
          <label for="email">Email Address</label>
          <input v-model="email" type="email" id="email" placeholder="Enter your email" required />
        </div>
  
        <div class="form-group">
          <label for="verificationCode">Verification Code</label>
          <input v-model="verificationCode" type="text" id="verificationCode" placeholder="Enter the verification code" required />
        </div>
  
        <button type="submit">Submit</button>
      </form>
  
      <p v-if="message" :class="{ 'error-message': !isVerified, 'success-message': isVerified }">{{ message }}</p>
    </div>
   </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        email: '',
        verificationCode: '',
        correctCode: '123456', // 假设写死的验证码
        message: '',
        isVerified: false
      };
    },
    methods: {
        verifyCode() {
  if (this.verificationCode === this.correctCode) {
    this.isVerified = true;
    this.message = 'Verification successful! Redirecting to reset password...';

    // 等待一会再跳转到重置密码界面，并传递邮箱参数
    setTimeout(() => {
      this.$router.push({ path: '/ResetPwd', query: { email: this.email } });
    }, 1500);
  } else {
    this.isVerified = false;
    this.message = 'Incorrect verification code. Please try again.';
  }
}
    }
  };
  </script>
  
  <style scoped>
  .container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background-color: #2f2e2e;
  }
  .forgot-password-container {
    max-width: 450px;
    margin: 50px auto;
    padding: 20px;
    background-color: #f9f9f9;
    border-radius: 8px;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  }
  
  h2 {
    text-align: center;
    margin-bottom: 20px;
  }
  
  .form-group {
    margin-bottom: 15px;
  }
  
  label {
    display: block;
    margin-bottom: 5px;
  }
  
  input {
    width: 95%;
    padding: 10px;
    font-size: 16px;
    border: 1px solid #ccc;
    border-radius: 4px;
  }
  
  button {
    width: 100%;
    padding: 12px;
    font-size: 16px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
  
  button:hover {
    background-color: #0056b3;
  }
  
  p {
    text-align: center;
    margin-top: 15px;
  }
  
  .error-message {
    color: red;
  }
  
  .success-message {
    color: green;
  }
  </style>
  