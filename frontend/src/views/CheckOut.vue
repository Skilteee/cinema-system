<template>
    <div class="container">
      <div class="checkout-page">
        <!-- Header -->
        <header class="checkout-header">
          <h1>Checkout</h1>
        </header>
  
        <!-- Shipping Address Section -->
        <div class="shipping-section">
          <h2>Shipping Address</h2>
          <div class="form-group">
            <label for="shippingAddress">Enter Your Shipping Address</label>
            <input
              type="text"
              id="shippingAddress"
              placeholder="1234 Main St, Apt 101"
              v-model="shippingAddress"
            />
          </div>
        </div>
  
        <!-- Content Section -->
        <div class="checkout-content">
          <!-- Payment Information -->
          <div class="payment-section">
            <h2>Payment Information</h2>
            <form>
              <div class="form-group">
                <label for="existingPayment">Select Existing Payment</label>
                <select
                  id="existingPayment"
                  v-model="selectedPaymentId"
                  @change="loadSelectedPaymentDetails"
                >
                  <option value="" disabled>Select a payment method</option>
                  <option
                    v-for="payment in userPayments"
                    :key="payment.id"
                    :value="payment.id"
                  >
                    {{ formatPaymentOption(payment) }}
                  </option>
                  <option value="new">Add New Payment Method</option>
                </select>
              </div>
  
              <!-- Existing Payment Details -->
              <div v-if="selectedPaymentId !== 'new' && selectedPaymentDetails">
                <h3>Selected Payment Details</h3>
                <p><strong>Type:</strong> {{ selectedPaymentDetails.paymentType }}</p>
                <p v-if="selectedPaymentDetails.cardNumber">
                  <strong>Card Number:</strong> **** **** ****
                  {{ selectedPaymentDetails.cardNumber.slice(-4) }}
                </p>
                <p v-if="selectedPaymentDetails.expiry">
                  <strong>Expiry:</strong> {{ selectedPaymentDetails.expiry }}
                </p>
                <p v-if="selectedPaymentDetails.bankName">
                  <strong>Bank Name:</strong> {{ selectedPaymentDetails.bankName }}
                </p>
                <p v-if="selectedPaymentDetails.accountNumber">
                  <strong>Account Number:</strong> {{ selectedPaymentDetails.accountNumber }}
                </p>
              </div>
  
              <!-- New Payment Details -->
              <div v-if="selectedPaymentId === 'new'">
                <div class="form-group">
                  <label for="paymentType">Payment Type</label>
                  <select id="paymentType" v-model="payment.paymentType">
                    <option value="" disabled>Select Payment Type</option>
                    <option value="Credit Card">Credit Card</option>
                    <option value="Bank Transfer">Bank Transfer</option>
                  </select>
                </div>
  
                <!-- Credit Card Fields -->
                <div v-if="payment.paymentType === 'Credit Card'">
                  <div class="form-group">
                    <label for="cardNumber">Card Number</label>
                    <input
                      type="text"
                      id="cardNumber"
                      placeholder="Card Number"
                      v-model="payment.cardNumber"
                    />
                  </div>
                  <div class="form-group">
                    <label for="expiry">Expiry</label>
                    <input
                      type="text"
                      id="expiry"
                      placeholder="MM/YY"
                      v-model="payment.expiry"
                    />
                  </div>
                  <div class="form-group">
                    <label for="cvv">CVV</label>
                    <input
                      type="text"
                      id="cvv"
                      placeholder="Security Code"
                      v-model="payment.cvv"
                    />
                  </div>
                </div>
  
                <!-- Bank Transfer Fields -->
                <div v-if="payment.paymentType === 'Bank Transfer'">
                  <div class="form-group">
                    <label for="bankName">Bank Name</label>
                    <input
                      type="text"
                      id="bankName"
                      placeholder="Bank Name"
                      v-model="payment.bankName"
                    />
                  </div>
                  <div class="form-group">
                    <label for="accountNumber">Account Number</label>
                    <input
                      type="text"
                      id="accountNumber"
                      placeholder="Account Number"
                      v-model="payment.accountNumber"
                    />
                  </div>
                  <div class="form-group">
                    <label for="routingNumber">Routing Number</label>
                    <input
                      type="text"
                      id="routingNumber"
                      placeholder="Routing Number"
                      v-model="payment.routingNumber"
                    />
                  </div>
                </div>
              </div>
            </form>
          </div>
  
          <!-- Order Details Section -->
          <div class="order-cost-section">
            <h2>Order Details</h2>
            <div class="order-details">
              <p><strong>Movie Title:</strong> {{ orderDetails.movieTitle }}</p>
              <p><strong>ShowRoom:</strong> {{ orderDetails.showroom }}</p>
              <p><strong>Showtime:</strong> {{ orderDetails.showtime }}</p>
              <div v-for="(seat, index) in orderDetails.seats" :key="index" class="seat-item">
                <p>
                  <strong>{{ seat.ageGroup }} Ticket:</strong>
                  Seat {{ seat.seatNumber }} - ${{ seat.price.toFixed(2) }}
                </p>
              </div>
  
              <label for="promoCode">Promo Code</label>
              <input
                type="text"
                id="promoCode"
                placeholder="Enter Promo Code"
                v-model="promoCode"
              />
              <button @click.prevent="applyPromoCode" class="btn-apply">Apply</button>
              
              <p v-if="promotionMessage" :class="['promo-message', promotionApplied ? 'success' : 'error']">
                {{ promotionMessage }}
              </p>
              <p v-if="promotionApplied">
                <strong>Promotion:</strong> {{ promotionApplied.code }} ({{ promotionApplied.discount }}% Off)
              </p>
              <p>Tax: ${{ orderDetails.salesTax.toFixed(2) }}</p>
              <hr />
              <p><strong>Total Cost:</strong> ${{ totalCost.toFixed(2) }}</p>
            </div>
          </div>
        </div>
  
        <!-- Checkout Buttons -->
        <div class="checkout-actions">
          <button @click="cancelOrder" class="btn-secondary">Cancel</button>
          <button @click="placeOrder" class="btn-primary">Place Order</button>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import axios from "axios";
  import { useRoute } from "vue-router";
  
  export default {
  data() {
    return {
      shippingAddress: "",
      payment: {
        paymentType: "",
        cardNumber: "",
        expiry: "",
        cvv: "",
        bankName: "",
        accountNumber: "",
        routingNumber: "",
      },
      selectedPaymentId: "",
      selectedPaymentDetails: null,
      userPayments: [],
      orderDetails: {
        movieTitle: "",
        showroom: "",
        showtime: "",
        movieId: "",
        showroomId: "",
        seats: [],
        salesTax: 5,
      },
      promoCode: "",
      discount: 0,
      promotionMessage: "",
      promotionApplied: null,
    };
  },
    computed: {
      totalCost() {
        const itemsCost = Array.isArray(this.orderDetails.seats)
          ? this.orderDetails.seats.reduce((sum, seat) => sum + seat.price, 0)
          : 0;
        return itemsCost - this.discount + this.orderDetails.salesTax;
      },
    },
    async mounted() {
      const route = useRoute();
      const checkoutData = JSON.parse(route.query.data);
      const userId = localStorage.getItem("id");
  
    this.orderDetails.movieTitle = checkoutData.movieTitle;
    this.orderDetails.showroom = checkoutData.showroom;
    this.orderDetails.showtime = checkoutData.showtime;
    this.orderDetails.seats = checkoutData.seats;
    this.orderDetails.movieId = checkoutData.movieId;
    this.orderDetails.showroomId = checkoutData.showroomId;

      console.log("orderDetails", this.orderDetails);
      try {
        const response = await axios.get(`http://localhost:8080/api/users/${userId}/payments`);
        this.userPayments = response.data;
        // remove payment if payment delete is true 
        this.userPayments = this.userPayments.filter((payment) => !payment.delete);
      } catch (error) {
        console.error("Failed to load user payments:", error);
      }
    },
    methods: {
      loadSelectedPaymentDetails() {
        this.selectedPaymentDetails = this.userPayments.find(
          (payment) => payment.id === this.selectedPaymentId
        );
      },
      formatPaymentOption(payment) {
        return payment.paymentType === "Credit Card"
          ? `Card: **** **** **** ${payment.cardNumber.slice(-4)}`
          : `Bank: ${payment.bankName}`;
      },
      async applyPromoCode() {
        if (!this.promoCode) {
          this.promotionMessage = "Please enter a promo code.";
          return;
        }
        try {
          const response = await axios.post(
            "http://localhost:8080/api/promotions/validate",
            null,
            {
              params: { code: this.promoCode },
            }
          );
          const { status, message, discount } = response.data;
          if (status === "success") {
            const itemsCost = this.orderDetails.seats.reduce((sum, seat) => sum + seat.price, 0);
            this.discount = (itemsCost * discount) / 100;
            this.promotionApplied = { code: this.promoCode, discount };
            this.promotionMessage = `Promo code applied! ${discount}% off.`;
          } else {
            this.promotionMessage = message;
            this.discount = 0;
          }
        } catch (error) {
          console.error("Error applying promo code:", error);
          this.promotionMessage = "Failed to validate promo code.";
          this.discount = 0;
        }
      },
      cancelOrder() {
        this.$router.push("/");
      },
      async placeOrder() {

        const userId = localStorage.getItem("id");

        if (!userId) {
            alert("User ID is missing. Please log in again.");
            return;
        }
        if (!this.shippingAddress) {
          alert("Please enter your shipping address.");
          return;
        }
        if (
          !this.payment.paymentType &&
          !this.selectedPaymentId
        ) {
          alert("Please select or add a payment method.");
          return;
        }
        const orderData = {
          userId,
          shippingAddress: this.shippingAddress,
          payment: this.selectedPaymentId !== "new" ? { id: this.selectedPaymentId } : this.payment,
          promotionCode: this.promotionApplied ? this.promotionApplied.code : null,
          promotionDiscount: this.promotionApplied ? this.promotionApplied.discount : 0,
          items: this.orderDetails.seats.map((seat) => ({
            seatId: seat.seatId,
            price: seat.price,
            ageGroup: seat.ageGroup,
            movieId: this.orderDetails.movieId,
            showroomId: this.orderDetails.showroomId,
          })),
          salesTax: this.orderDetails.salesTax,
        };
        
        console.log("Order Data:", orderData);
        try {
          const response = await axios.post("http://localhost:8080/api/orders", orderData);
          this.$router.push({
            name: "OrderConfirmation",
            query: { data: JSON.stringify(orderData) },
            });
        } catch (error) {
          console.error("Error placing order:", error);
          alert("Failed to place order.");
        }
      },
    },
  };
  </script>
  

  
<style scoped>
/* 全局设置 */
html, body {
    margin: 0;
    padding: 0;
    height: 100%;
    font-family: 'Open Sans', sans-serif; /* 改进字体 */
    background-color: #1c1c1c;

  }
  
  body {
    display: flex;
    justify-content: center;
    align-items: center;
  }
  
  /* 容器设置 */
  .container {
    min-height: 100vh;
    width: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    padding: 20px;
    background-color: #1c1c1c;

  }
  
  /* 主页面内容样式 */
  .checkout-page {
    width: 100%;
    max-width: 900px;
    padding: 30px;
    border-radius: 15px;
    background-color: #f9f9f9; /* 保持原来的背景色 */
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3); /* 增强阴影效果 */
  }
  
  /* 页眉样式 */
  .checkout-header {
    text-align: center;
    padding-bottom: 20px;
    border-bottom: 1px solid #333333;
  }
  
  .checkout-header h1 {
    font-size: 2.5rem; /* 突出标题 */
    font-weight: bold;
    text-transform: uppercase; /* 使用全大写字母 */
    letter-spacing: 2px; /* 增加字间距 */
    color: #007bff; /* 保持配色一致 */
  }
  
  /* 配送地址部分 */
  .shipping-section {
    margin-bottom: 20px;
  }
  
  .shipping-section h2 {
    font-size: 1.8rem; /* 提高字体大小 */
    font-weight: bold;
    margin-bottom: 10px;
    color: #333; /* 提高对比度 */
  }
  
  .shipping-section .form-group {
    margin-bottom: 15px;
  }
  
  .shipping-section .form-group label {
    font-size: 1rem;
    font-weight: 600;
    margin-bottom: 5px;
    display: block;
    color: #555; /* 提升可读性 */
  }
  
  .shipping-section .form-group input {
    width: 97%;
    padding: 12px;
    border: 1px solid #ddd;
    border-radius: 8px;
    background-color: #fff; /* 保持一致 */
    color: #333; /* 提高可读性 */
    font-size: 1rem;
    font-weight: 500; /* 加粗字体以突出 */
    transition: box-shadow 0.3s ease, border-color 0.3s ease;
  }
  
  .shipping-section .form-group input:focus {
    outline: none;
    border-color: #007bff; /* 聚焦时增加颜色 */
    box-shadow: 0 0 8px rgba(0, 123, 255, 0.5); /* 添加轻微阴影 */
  }
  
  /* 页面内容布局 */
  .checkout-content {
    display: flex;
    flex-wrap: wrap;
    gap: 20px;
  }
  
  .payment-section,
  .order-cost-section {
    flex: 1;
    min-width: 300px;
    background-color: #fff; /* 保持配色 */
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2); /* 增强阴影效果 */
  }
  
  .payment-section h2,
  .order-cost-section h2 {
    font-size: 1.8rem; /* 提高字体大小 */
    margin-bottom: 15px;
    font-weight: bold;
    color: #333;
  }
  
  /* 表单组样式 */
  .form-group {
    margin-bottom: 15px;
  }
  
  .form-group label {
    font-size: 1rem;
    font-weight: 600;
    margin-bottom: 5px;
    display: block;
    color: #555; /* 提高可读性 */
  }
  
  .form-group input,
  .form-group select {
    width: 95%;
    padding: 10px;
    border-radius: 8px;
    background-color: #f9f9f9; /* 保持一致 */
    border: 1px solid #ddd;
    color: #333;
    font-size: 1rem;
    font-weight: 500;
  }
  
  .form-group input:focus,
  .form-group select:focus {
    outline: none;
    border-color: #007bff;
    box-shadow: 0 0 8px rgba(0, 123, 255, 0.5);
  }
  /* Checkout 按钮组 */
.checkout-actions {
    display: flex;
    justify-content: center;
    align-items: center; /* 确保垂直对齐 */
    margin-top: 20px;
    gap: 10px; /* 使用 gap 替代 margin-right 提供一致的间距 */
  }
  
  /* Primary 和 Secondary 按钮通用样式 */
  .checkout-actions .btn-primary,
  .checkout-actions .btn-secondary {
    padding: 12px 24px;
    border: none;
    border-radius: 8px;
    font-size: 1rem;
    font-weight: bold; /* 统一按钮字体 */
    cursor: pointer;
    display: inline-flex; /* 使用 flex 确保内容居中 */
    align-items: center; /* 内容垂直居中 */
    justify-content: center; /* 内容水平居中 */
    transition: background-color 0.3s ease, transform 0.3s ease;
    box-sizing: border-box; /* 确保内外边距计算一致 */
  }
  
  /* Primary 按钮样式 */
  .checkout-actions .btn-primary {
    background-color: #28a745;
    color: white;
  }
  
  .checkout-actions .btn-primary:hover {
    background-color: #218838;
  }
  
  /* Secondary 按钮样式 */
  .checkout-actions .btn-secondary {
    background-color: #6c757d;
    color: white;
  }
  
  .checkout-actions .btn-secondary:hover {
    background-color: #5a6268;
  }
  
  
  /* 订单详情 */
  .order-details p {
    font-size: 1rem;
    margin: 10px 0;
    font-weight: 500; /* 加粗文字 */
  }
  
  .order-details hr {
    margin: 15px 0;
    border-color: #ddd;
  }
  
  .order-details strong {
    color: #007bff; /* 保持一致的强调色 */
    font-weight: bold;
  }
  
  /* 响应式适配 */
  @media screen and (max-width: 768px) {
    .checkout-content {
      flex-direction: column;
      gap: 10px;
    }
  
    .payment-section,
    .order-cost-section {
      width: 100%;
    }
  }

  /* Promo Code 输入框样式 */
#promoCode {
    width: 30%;
    padding: 10px;
    border-radius: 8px;
    border: 1px solid #ddd;
    background-color: #f9f9f9;
    color: #333;
    font-size: 15px;
    font-weight: 400;
    transition: border-color 0.3s ease, box-shadow 0.3s ease;
    margin-top: 10px;
    margin-left: 10px;
    margin-right: 10px;
  }
  
  #promoCode:focus {
    outline: none;
    border-color: #007bff;
    box-shadow: 0 0 8px rgba(0, 123, 255, 0.5);
  }
  
  /* Apply 按钮样式 */
  .btn-apply {
    background-color: #007bff;
    color: white;
    padding: 10px 20px;
    border: none;
    border-radius: 8px;
    font-size: 1rem;
    font-weight: bold;
    margin-top: 10px;
    cursor: pointer;
    transition: background-color 0.3s ease, transform 0.3s ease;
  }
  
  .btn-apply:hover {
    background-color: #0056b3;
    transform: translateY(-3px);
  }
  
  /* Promo 提示信息样式 */
  .promo-message {
    font-size: 0.9rem;
    margin-top: 10px;
    padding: 10px;
    border-radius: 8px;
    text-align: center;
    font-weight: 600;
  }
  
  /* 成功消息 */
  .promo-message.success {
    color: #28a745;
    background-color: #eaf8f1;
    border: 1px solid #28a745;
  }
  
  /* 错误消息 */
  .promo-message.error {
    color: #dc3545;
    background-color: #f8eae9;
    border: 1px solid #dc3545;
  }
  
    
</style>

