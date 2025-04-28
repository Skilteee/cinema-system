<template>
    <div class="container">
      <div class="confirmation-page">
        <!-- Header -->
        <header class="confirmation-header">
          <h1>Thank You!</h1>
        </header>
  
        <!-- Order Details -->
        <div v-if="orderData" class="order-details">
          <h2>Order Details</h2>
          <p><strong>Shipping Address:</strong> {{ orderData.shippingAddress }}</p>
          <p v-if="orderData.promotionCode">
            <strong>Promotion Applied:</strong> {{ orderData.promotionCode }}
          </p>
          <p v-if="orderData.promotionCode">
            <strong>Promotion Discount:</strong> {{ orderData.promotionDiscount }} %
          </p>
        </div>
        <div v-else>
          <p>No order details available. Please try again later.</p>
        </div>
  
        <!-- Payment Information -->
        <div v-if="orderData && orderData.payment" class="payment-information">
          <h2>Payment Information</h2>
          <p v-if="orderData.payment.paymentType === 'Credit Card'">
            <strong>Payment Type:</strong> Credit Card ending in
            {{ orderData.payment.cardNumber ? orderData.payment.cardNumber.slice(-4) : '****' }}
          </p>
          <p v-if="orderData.payment.paymentType === 'Bank Transfer'">
            <strong>Payment Type:</strong> Bank Transfer from
            {{ orderData.payment.bankName || 'N/A' }}
          </p>
        </div>
  
        <!-- Ticket Information -->
        <div v-if="orderData && orderData.items" class="ticket-information">
            <h2>Ticket Information</h2>
            <div v-for="(item, index) in orderData.items" :key="index">
              <p>
                <strong>{{ item.ageGroup }} Ticket:</strong>
                Seat {{ item.seatId }} - ${{ item.price ? item.price.toFixed(2) : '0.00' }}
              </p>
            </div>
            <p><strong>Sales Tax:</strong> ${{ orderData.salesTax ? orderData.salesTax.toFixed(2) : '0.00' }}</p>
            <p v-if="orderData.promotionDiscount">
              <strong>Discount ({{ orderData.promotionDiscount }}%):</strong> -$
              {{
                (
                  orderData.items.reduce((sum, item) => sum + (item.price || 0), 0) *
                  (orderData.promotionDiscount / 100)
                ).toFixed(2)
              }}
            </p>
            <hr />
            <p><strong>Total Cost:</strong> ${{ calculateTotalCost() }}</p>
          </div>
  
        <!-- Email Confirmation -->
        <div class="email-confirmation" v-if="orderData">
          <p>A confirmation email has been sent to your registered email address.</p>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import { useRoute } from "vue-router";
  
  export default {
    data() {
      return {
        orderData: null, // Holds the passed order data
      };
    },
    mounted() {
      const route = useRoute();
      const queryData = route.query.data;
  
      if (!queryData) {
        console.warn("No order data found in query parameters.");
        return;
      }
  
      try {
        this.orderData = JSON.parse(queryData);
        console.log("Order Data received:", this.orderData);
      } catch (error) {
        console.error("Failed to parse order data:", error);
      }
    },
    methods: {
    calculateTotalCost() {
        if (!this.orderData || !this.orderData.items) return "0.00";

        const itemsTotal = this.orderData.items.reduce(
        (sum, item) => sum + (item.price || 0),
        0
        );

        // Calculate discount if promotionDiscount exists
        let discount = 0;
        if (this.orderData.promotionDiscount) {
        discount = (itemsTotal * this.orderData.promotionDiscount) / 100;
        }

        // Add sales tax and subtract discount
        const totalCost = itemsTotal - discount + (this.orderData.salesTax || 0);
        return totalCost.toFixed(2);
    },
},
  };
  </script>
  
  <style scoped>
  .container {
    width: 100vw;
    height: 100vh;
    background-color: #2f2e2e; /* Lighter background for better contrast */
    display: flex;
    justify-content: center;
    align-items: center;
  }
  
  .confirmation-page {
    font-family: "Arial", sans-serif;
    padding: 20px;
    text-align: center;
    background: #ffffff; /* Card background */
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1); /* Shadow effect */
    border-radius: 10px;
    max-width: 600px; /* Limit width for better readability */
    margin: 20px;
  }
  
  .confirmation-header {
    background-color: #007bff;
    color: white;
    padding: 10px;
    border-radius: 5px;
    margin-bottom: 20px; /* Spacing below header */
  }
  
  .order-details,
  .ticket-information {
    background-color: #e0f7fa; /* Light blue background for sections */
    margin: 20px 0; /* Vertical spacing between sections */
    padding: 20px;
    border-radius: 10px;
    text-align: left;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05); /* Subtle shadow */
  }
  
  h2 {
    border-bottom: 1px solid #ccc;
    padding-bottom: 10px;
    margin-bottom: 10px; /* Spacing below heading */
    color: #333; /* Darker heading color */
  }
  
  p {
    margin: 10px 0;
    color: #555; /* Slightly darker text color */
  }
  
  hr {
    border: 0;
    height: 1px;
    background: #ccc;
  }
  
  .email-confirmation {
    margin-top: 20px;
    font-style: italic;
    color: #777; /* Softer color for confirmation text */
  }
  </style>
  