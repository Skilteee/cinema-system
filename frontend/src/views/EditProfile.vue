<template>
    <div class="profile-edit-container">
      <div class="profile-edit-form">
        <h2 class="form-title">Your Account</h2>
  
        <div class="form-group">
          <label>Username:</label>
          <span>{{ user.username }}</span>
          <button @click="editField('username')" class="edit-button" style=" background-color: transparent;"  disabled>
            <i class="fa fa-pencil-alt"></i> Edit
          </button>
        </div>
  
        <div class="form-group email-group">
          <label>Email:</label>
          <span class="email-text">{{ user.email }}</span>
        </div>
  
        <div class="form-group">
          <label>Phone Number:</label>
          <span>{{ user.phone }}</span>
          <button @click="editField('phone')" class="edit-button">
            <i class="fa fa-pencil-alt"></i> Edit
          </button>
        </div>
  
        <div class="form-group">
          <label>Address:</label>
          <span>{{ user.address }}</span>
          <button @click="editField('address')" class="edit-button">
            <i class="fa fa-pencil-alt"></i> Edit
          </button>
        </div>
  
        <div class="form-group">
          <label>Receive Promotions:</label>
          <input type="checkbox" v-model="user.receivePromotion" />
          <button @click="editField('receivePromotion')" class="edit-button">
            <i class="fa fa-pencil-alt"></i> Edit
          </button>
        </div>
  
        <div class="form-group">
          <label>Payment Information:</label>
          <div v-for="(payment, index) in user.payments" :key="index">
            <span>{{ payment.paymentType }} - **** {{  payment.cardNumber ? payment.cardNumber.slice(-4) : payment.accountNumber.slice(-4) }}</span>
            <button @click="removePayment(index)" class="edit-button">
              <i class="fa fa-trash-alt"></i> Remove
            </button>
          </div>
          <button v-if="user.payments.length < 4" @click="addPayment" class="edit-button">
            <i class="fa fa-plus"></i> Add Payment 
          </button>
        </div>
  
        <div class="form-group">
          <label>Password:</label>
          <button @click="editField('password')" class="edit-button">
            <i class="fa fa-key"></i> Change Password
          </button>
        </div>
      </div>
  
      <div v-if="editMode" class="modal-overlay" @click.self="closeModal">
        <div class="modal-content">
          <span class="close-button" @click="closeModal">
            <i class="fa fa-times"></i>
          </span>
          <h3>Edit {{ fieldBeingEdited }}</h3>
  
          <div v-if="fieldBeingEdited === 'payment'">
            <label for="paymentType">Select Payment Method:</label>
            <select v-model="paymentType" @change="clearPaymentDetails">
              <option value="Credit Card">Credit Card</option>
              <option value="Bank Account">Bank Account</option>
            </select>
  
            <div v-if="paymentType === 'Credit Card'">
              <label for="cardNumber">Card Number:</label>
              <input type="text" v-model="paymentDetails.cardNumber" placeholder="Enter card number" />
  
              <label for="expiry">Expiry Date:</label>
              <input type="text" v-model="paymentDetails.expiry" placeholder="MM/YY" />
  
              <label for="cvv">CVV:</label>
              <input type="text" v-model="paymentDetails.cvv" placeholder="CVV" />
            </div>
  
            <div v-if="paymentType === 'Bank Account'">
              <label for="bankName">Bank Name:</label>
              <input type="text" v-model="paymentDetails.bankName" placeholder="Enter bank name" />
  
              <label for="accountNumber">Account Number:</label>
              <input type="text" v-model="paymentDetails.accountNumber" placeholder="Enter account number" />
  
              <label for="routingNumber">Routing Number:</label>
              <input type="text" v-model="paymentDetails.routingNumber" placeholder="Enter routing number" />
            </div>
          </div>
  
          <input
            v-if="fieldBeingEdited !== 'payment' && fieldBeingEdited !== 'password'"
            type="text"
            v-model="editedValue"
            :placeholder="'Enter new ' + fieldBeingEdited"
          />
  
          <div v-if="fieldBeingEdited === 'password'">
            <label for="currentPassword">Current Password:</label>
            <input type="password" v-model="currentPassword" placeholder="Enter current password" />
            <label for="newPassword">New Password:</label>
            <input type="password" v-model="editedValue" placeholder="Enter new password" />
          </div>
  
          <button @click="saveField" class="save-button">
            <i class="fa fa-save"></i> Save
          </button>
        </div>
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
        phone: '',
        address: '',
        payments: [], // 记录用户的支付方式
        receivePromotion : false,
      },
      editMode: false,
      fieldBeingEdited: '',
      editedValue: '',
      paymentType: 'Credit Card', // 记录支付方式（Credit Card 或 Bank）
      paymentDetails: {
        cardNumber: '',
        expiry: '',
        cvv: '',
        bankName: '',
        accountNumber: '',
        routingNumber: '',
      },
      message: '',
    };
  },
  mounted() {
    this.fetchUserProfile(); // 组件挂载时调用
  },
  methods: {
    async fetchUserProfile() {
      try {
        const username = localStorage.getItem('username');
        
        const response = await axios.get('http://localhost:8080/profile/get', {
          params: { username },
        });
        this.user = response.data; // 设置用户数据
        // remove payment if payment is delete 
        console.log('this.user:', this.user);
        this.user.payments = this.user.payments.filter(payment => payment.delete !== true);
        // 确保 payments 数组存在
        if (!this.user.payments) {
          this.user.payments = [];
        }
        if (this.user.receivePromotion === undefined) {
        this.user.receivePromotion = false; // 默认值
        }
      } catch (error) {
        console.error('Error fetching user profile:', error);
      }
    },
    editField(field) {
      this.fieldBeingEdited = field;
      if (field === 'payment') {
        this.paymentType = 'Credit Card'; // 默认支付方式
        this.paymentDetails = {
          cardNumber: '',
          expiry: '',
          cvv: '',
          bankName: '',
          accountNumber: '',
          routingNumber: '',
        
        };
      } else {
        this.editedValue = this.user[field];
      }
      this.editMode = true;
    },
    addPayment() {
      if (this.user.payments.length < 4) {
        this.fieldBeingEdited = 'payment';
        this.editMode = true;
      } else {
        alert("You can only add up to 4 payment methods.");
      }
    },
    async removePayment(index) {
      this.user.payments.splice(index, 1);
      this.message = 'Payment method removed successfully!';
      try {
          const response = await axios.post('http://localhost:8080/profile/update', this.user);
          console.log('response:', response);
          if (response.status === 200) {
            this.user = response.data.user; // 更新用户数据
            // remove payment if payment is delete
            this.user.payments = this.user.payments.filter(payment => payment.delete !== true);
            this.message = `${this.fieldBeingEdited} updated successfully!`;
          } else {
            this.message = 'Error updating profile';
          }
          console.log('this.user:', this.user);
        } catch (error) {
          console.error('Error saving field:', error);
          this.message = 'Error updating profile';
        }
    },
        async validateCurrentPassword(currentPassword) {
        // Logic to validate current password against backend
        // Example: Making an API call to check if the password is correct
        const response = await axios.post('http://localhost:8080/profile/validate-password', {
        username: this.user.username,
        currentPassword: currentPassword,
        });
        return response.data.isValid; // Assuming the response contains a boolean isValid
    },
    async saveField() {
      if (this.fieldBeingEdited === 'payment') {
        const newPayment = {
          paymentType: this.paymentType === 'Credit Card' ? 'Credit Card' : 'Bank',
          cardNumber: this.paymentDetails.cardNumber, // 直接使用卡号
          expiry: this.paymentDetails.expiry,
          cvv: this.paymentDetails.cvv,
          bankName: this.paymentDetails.bankName,
          accountNumber: this.paymentDetails.accountNumber,
          routingNumber: this.paymentDetails.routingNumber,
        };
        
        // 添加新支付方法
        this.user.payments.push(newPayment);
        this.message = 'Payment information added successfully!';
        console.log('newPayment:', newPayment);
        console.log('this.user payments:', this.user.payments);
      } else if (this.fieldBeingEdited === 'password') {
      // Validate the current password before changing
        if (this.currentPassword) {
            try {
            // Replace with your validation logic, such as checking against the backend
            const isValidPassword = await this.validateCurrentPassword(this.currentPassword);
            if (!isValidPassword) {
                alert('Current password is incorrect. Please try again.');
                return; // Prevent further execution
            }
            } catch (error) {
            console.error('Error validating current password:', error);
            return;
            }

            // Update the password in the user object
            this.user.password = this.editedValue; // Make sure to handle password securely!
            console.log('this.user.password:', this.user.password);
        } else {
            alert('Please enter your current password.');
            return; // Prevent further execution
        }
    } else {
        this.user[this.fieldBeingEdited] = this.editedValue;
    }

      console.log('this.user:', this.user);
      if (this.fieldBeingEdited) {
        try {
          const response = await axios.post('http://localhost:8080/profile/update', this.user);
          console.log('response:', response);
          if (response.status === 200) {
            this.user = response.data.user; // 更新用户数据
            // remove payment if payment is delete
            this.user.payments = this.user.payments.filter(payment => payment.delete !== true);
            this.message = `${this.fieldBeingEdited} updated successfully!`;
          } else {
            this.message = 'Error updating profile';
          }
          console.log('this.user:', this.user);
        } catch (error) {
          console.error('Error saving field:', error);
          this.message = 'Error updating profile';
        }
      }
      this.closeModal();
    },
    closeModal() {
      this.editMode = false;
      this.fieldBeingEdited = '';
      this.editedValue = '';
      // 重置 paymentDetails
      this.paymentDetails = {
        cardNumber: '',
        expiry: '',
        cvv: '',
        bankName: '',
        accountNumber: '',
        routingNumber: '',
      };
    },
  },
};

  </script>
  

  <style scoped>
  .profile-edit-container {
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
  .profile-edit-form {
    max-width: 750px;
    height: 570px;
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


  .form-group {
  margin-bottom: 20px;

  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0; /* 增加上下内边距 */
  border-bottom: 1px solid #eaeaea; /* 增加底部分隔线 */
  margin-bottom: 15px; /* 增加分组之间的间距 */
}
.edit-button {
    width: 120px; /* 固定宽度 */
    padding: 8px 12px; /* 内边距控制按钮高度 */
    font-size: 14px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    text-align: center;
    transition: background-color 0.3s ease;
  }
  
  .edit-button:hover {
    background-color: #0056b3;
  }
.form-group label {
  font-weight: bold;
  font-size: 18px;
}

.form-group span {
  font-size: 17px;
  color: #333;
}

.form-label {
    font-size: 16px;
    font-weight: bold;
    flex: 1; /* 左侧标签占据 1/3 宽度 */
    color: #333;
  }
  
  /* 中间内容样式 */
  .form-content {
    font-size: 16px;
    flex: 2; /* 中间内容占据 2/3 宽度 */
    color: #555;
    text-align: right;
  }
  
.email-group {
    display: flex;
    justify-content: space-between; /* 使标签和内容分开 */
    align-items: center; /* 垂直居中 */
  }
  
  .email-text {
    text-align: center; /* 内容居中 */
    flex-grow: 1; /* 使文本占据剩余空间 */
  }
/* 编辑按钮样式 */
.edit-button {
  padding: 6px 12px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.edit-button:hover {
  background-color: #0056b3;
}

/* 弹出窗口 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  width: 400px;
  text-align: center;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  position: relative;
}

.close-button {
  position: absolute;
  top: 10px;
  right: 15px;
  font-size: 24px;
  cursor: pointer;
  color: #333;
}

.close-button:hover {
  color: #ff0000;
}

.save-button {
  padding: 8px 16px;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.save-button:hover {
  background-color: #218838;
}


.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.6);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
  }
  
  /* 模态框内容样式 */
  .modal-content {
    background-color: #ffffff;
    padding: 30px;
    border-radius: 10px;
    width: 500px;
    text-align: left;
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
    position: relative;
    animation: fadeIn 0.3s ease-in-out;
  }
  
  /* 模态框标题样式 */
  .modal-content h3 {
    font-size: 20px;
    font-weight: bold;
    margin-bottom: 20px;
    color: #333333;
  }
  
  /* 关闭按钮样式 */
  .close-button {
    position: absolute;
    top: 15px;
    right: 20px;
    font-size: 18px;
    cursor: pointer;
    color: #999999;
    transition: color 0.3s ease;
  }
  
  .close-button:hover {
    color: #ff0000;
  }
  
  /* 输入框样式 */
  input[type="text"],
  input[type="password"],
  select {
    width: 100%;
    padding: 10px;
    font-size: 16px;
    margin-bottom: 15px;
    border: 1px solid #cccccc;
    border-radius: 6px;
    box-sizing: border-box;
    outline: none;
    transition: border-color 0.3s ease;
  }
  
  input[type="text"]:focus,
  input[type="password"]:focus,
  select:focus {
    border-color: #007bff;
  }
  
  /* 标签样式 */
  label {
    font-size: 16px;
    font-weight: bold;
    margin-bottom: 5px;
    display: block;
    color: #333333;
  }
  
  /* 保存按钮样式 */
  .save-button {
    width: 100%;
    padding: 12px;
    font-size: 16px;
    background-color: #28a745;
    color: #ffffff;
    border: none;
    border-radius: 6px;
    cursor: pointer;
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 8px;
    transition: background-color 0.3s ease;
  }
  
  .save-button:hover {
    background-color: #218838;
  }
  
  /* 动画效果 */
  @keyframes fadeIn {
    from {
      opacity: 0;
      transform: scale(0.9);
    }
    to {
      opacity: 1;
      transform: scale(1);
    }
  }


  
  </style>
  

  