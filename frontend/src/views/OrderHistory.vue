<template>
    <div class="container">
      <div class="history-page">
        <!-- Header -->
        <header class="history-header">
          <h1>Your Order History</h1>
        </header>
  
        <!-- Orders List -->
        <div v-if="orders.length" class="order-list">
          <div
            v-for="(order, index) in orders"
            :key="order.orderId"
            class="order-item"
          >
            <div class="order-summary">
              <p><strong>Order Number:</strong> {{ index + 1 }}</p>
              <p><strong>Date:</strong> {{ formatDate(order.createdAt) }}</p>
              <p><strong>Total:</strong> ${{ order.totalCost.toFixed(2) }}</p>
              <button class="toggle-btn" @click="toggleOrderDetails(index)">
                {{ expandedOrders.includes(index) ? "Hide Details" : "View Details" }}
              </button>
            </div>
  
            <!-- Order Details -->
            <transition name="fade">
              <div v-if="expandedOrders.includes(index)" class="order-details">
                <div class="order-card">
                  <div class="order-card-header">
                    <span class="icon">&#128197;</span>
                    <h3>Order Summary</h3>
                  </div>
                  <div class="order-card-content">
                    <p><strong>Shipping Address:</strong> {{ order.shippingAddress }}</p>
                    <p><strong>Payment Type:</strong> {{ order.paymentType }}</p>
                    <p v-if="order.paymentDetails">
                      <strong>Payment Details:</strong> {{ order.paymentDetails }}
                    </p>
                    <p v-if="order.promotionCode">
                      <strong>Promotion Applied:</strong> {{ order.promotionCode }}
                    </p>
                    <p><strong>Sales Tax:</strong> ${{ order.salesTax.toFixed(2) }}</p>
                  </div>
                </div>
  
                <div class="order-card">
                  <div class="order-card-header">
                    <span class="icon">&#127916;</span>
                    <h3>Items Purchased</h3>
                  </div>
                  <table class="order-items-table">
                    <thead>
                      <tr>
                        <th>Movie</th>
                        <th>Showtime</th>
                        <th>Showroom</th>
                        <th>Seat</th>
                        <th>Age Group</th>
                        <th>Price</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="item in order.items" :key="item.seatNumber">
                        <td>{{ item.movieTitle }}</td>
                        <td>{{ formatDate(item.showtime) }}</td>
                        <td>{{ item.showroom }}</td>
                        <td>{{ item.seatNumber }}</td>
                        <td>{{ item.ageGroup }}</td>
                        <td>${{ item.price.toFixed(2) }}</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </transition>
          </div>
        </div>
  
        <!-- No Orders -->
        <div v-else class="no-orders">
          <p>You have no order history yet.</p>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import axios from "axios";
  
  export default {
    data() {
      return {
        orders: [], // List of orders
        expandedOrders: [], // Track which order details are expanded
      };
    },
    async mounted() {
      const userId = localStorage.getItem("id");
      if (!userId) {
        alert("User not logged in. Please log in first.");
        this.$router.push("/login");
        return;
      }
  
      try {
        const response = await axios.get(
          `http://localhost:8080/api/orders/${userId}/history`
        );
        this.orders = response.data;
        console.log("Order history loaded:", this.orders);
      } catch (error) {
        console.error("Failed to fetch order history:", error);
        alert("Unable to load order history. Please try again.");
      }
    },
    methods: {
      toggleOrderDetails(index) {
        const expandedIndex = this.expandedOrders.indexOf(index);
        if (expandedIndex > -1) {
          // Remove from expanded orders
          this.expandedOrders.splice(expandedIndex, 1);
        } else {
          // Add to expanded orders
          this.expandedOrders.push(index);
        }
      },
      formatDate(dateTime) {
        const date = new Date(dateTime);
        return date.toLocaleString();
      },
    },
  };
  </script>
  
  <style scoped>
  .container {
    width: 100%;
    padding: 20px;
    margin-top: 150px;
    background-color: #f4f4f9;
    font-family: "Arial", sans-serif;
  }
  
  .history-page {
    max-width: 900px;
    margin: 0 auto;
    background: #ffffff;
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    padding: 20px;
    animation: fadeIn 0.5s ease-in-out;
  }
  
  .history-header {
    text-align: center;
    margin-bottom: 20px;
    font-size: 28px;
    color: #333;
    font-weight: bold;
  }
  
  .order-list {
    display: flex;
    flex-direction: column;
    gap: 20px;
  }
  
  .order-item {
    background: #f9fafb;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    transition: transform 0.3s, box-shadow 0.3s;
  }
  
  .order-item:hover {
    transform: translateY(-5px);
    box-shadow: 0 6px 15px rgba(0, 0, 0, 0.15);
  }
  
  .order-summary {
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-wrap: wrap;
  }
  
  .order-summary p {
    margin: 5px 0;
  }
  
  .toggle-btn {
    padding: 10px 15px;
    border: none;
    border-radius: 5px;
    background: #007bff;
    color: white;
    cursor: pointer;
    transition: background 0.3s;
  }
  
  .toggle-btn:hover {
    background: #0056b3;
  }
  
  .order-details {
    margin-top: 15px;
  }
  
  .order-card {
    background: #f9fafb;
    border-radius: 10px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    margin-bottom: 20px;
    padding: 15px;
    transition: transform 0.3s, box-shadow 0.3s;
  }
  
  .order-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 6px 15px rgba(0, 0, 0, 0.15);
  }
  
  .order-card-header {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 10px;
  }
  
  .order-card-header .icon {
    font-size: 24px;
    color: #007bff;
  }
  
  .order-card h3 {
    margin: 0;
    font-size: 20px;
    color: #444;
  }
  
  .order-card-content p {
    margin: 5px 0;
    font-size: 14px;
    color: #666;
  }
  
  .order-items-table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 10px;
  }
  
  .order-items-table th,
  .order-items-table td {
    padding: 8px 12px;
    border: 1px solid #ddd;
    text-align: left;
  }
  
  .order-items-table th {
    background: #f4f4f9;
    font-weight: bold;
    color: #333;
  }
  
  .no-orders {
    text-align: center;
    font-size: 18px;
    color: #666;
  }
  
  @keyframes fadeIn {
    from {
      opacity: 0;
      transform: translateY(20px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }
  
  .fade-enter-active,
  .fade-leave-active {
    transition: opacity 0.3s ease-in-out;
  }
  
  .fade-enter-from,
  .fade-leave-to {
    opacity: 0;
  }
  </style>
  