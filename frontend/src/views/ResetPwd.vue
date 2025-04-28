<template>
   <div class="container">
  <div class="reset-password-container">
    <h2>Reset Password</h2>

    <p>Your email: {{ email }}</p>

    <form @submit.prevent="resetPassword">
      <div class="form-group">
        <label for="newPassword">New Password</label>
        <input v-model="newPassword" type="password" id="newPassword" placeholder="Enter your new password" required />
      </div>

      <div class="form-group">
        <label for="confirmPassword">Confirm Password</label>
        <input v-model="confirmPassword" type="password" id="confirmPassword" placeholder="Confirm your new password" required />
      </div>

      <button type="submit">Reset Password</button>
    </form>

    <p v-if="message" :class="{ 'error-message': !isResetSuccessful, 'success-message': isResetSuccessful }">{{ message }}</p>
  </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      email: '', // 将传递过来的邮箱保存在这里
      newPassword: '',
      confirmPassword: '',
      message: '',
      isResetSuccessful: false
    };
  },
  mounted() {
    // 在组件加载时，获取传递过来的邮箱参数
    this.email = this.$route.query.email || '';
  },
  methods: {
    async resetPassword() {
      if (this.newPassword === this.confirmPassword) {
        try {
          // 使用 axios 向后端发送新密码和邮箱
          const response = await axios.post('http://localhost:8080/api/reset-password', {
            email: this.email, // 将邮箱和新密码一起发送
            newPassword: this.newPassword,
          });

          this.isResetSuccessful = true;
          this.message = response.data.message;
        } catch (error) {
          console.error('Error during password reset:', error);
          this.isResetSuccessful = false;
          this.message = 'An error occurred while resetting your password.';
        }
      } else {
        this.isResetSuccessful = false;
        this.message = 'Passwords do not match. Please try again.';
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

.reset-password-container {
    max-width: 400px;
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
  width: 90%;
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