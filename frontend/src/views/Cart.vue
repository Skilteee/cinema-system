<template>
    <div class="container">
        <div class="cart-page">
          <h2>Your Cart</h2>
          <ul v-if="groupedCart.length > 0">
            <li v-for="(group, index) in groupedCart" :key="index" class="cart-item">
              <div @click="toggleDetails(index)" class="cart-header">
                <div class="cart-movie-info">
                  <strong>{{ group.movie }} - {{ group.showtime }}</strong>
                  <span class="cart-summary">
                    {{ group.seats.length }} seats - Total: {{ group.total }} USD
                  </span>
                </div>
                <div class="cart-toggle">
                  <span v-if="group.showDetails">▲</span>
                  <span v-else>▼</span>
                </div>
              </div>
      
              <transition name="slide-fade">
                <div v-if="group.showDetails" class="cart-details">
                  <ul>
                    <li v-for="(seat, seatIndex) in group.seats" :key="seatIndex" class="seat-item">
                      <div class="seat-info">
                        <template v-if="seat.isEditing">
                          Seat: <input v-model="seat.label" class="seat-input" />
                          Age: 
                          <select v-model="seat.age"  class="age-select">
                            <option value="Child">Child</option>
                            <option value="Adult">Adult</option>
                            <option value="Senior">Senior</option>
                          </select>
                          Price: {{ seat.price }} USD
                          <button @click="saveSeat(group, seatIndex)" class="save-seat-button">Save</button>
                        </template>
                        <template v-else>
                          Seat: {{ seat.label }}, Age: {{ seat.age }}, Price: {{ seat.price }} USD
                        </template>
                      </div>
                      <div class="seat-actions">
                        <button v-if="!seat.isEditing" @click="editSeat(seat)" class="edit-seat-button">Edit</button>
                        <button v-else @click="cancelEdit(seat)" class="cancel-edit-button">Cancel</button>
                        <button @click="removeSeat(group, seatIndex)" class="remove-seat-button">Remove</button>
                      </div>
                    </li>
                  </ul>
                </div>
              </transition>
      
              <button @click="orderAnother(group)" class="order-another-button">Order Another</button>
            </li>
          </ul>
      
          <div v-else>
            <p>Your cart is empty.</p>
          </div>
      
          <div v-if="groupedCart.length > 0" class="order-summary">
            <h3 class="order-total">Order Total: {{ total }} USD</h3>
            <button @click="checkout" class="checkout-button">Proceed to Checkout</button>
          </div>
        </div>
    </div>
      </template>
      
    
      <script setup lang="ts">
      import { ref, computed, onMounted } from 'vue';
      import { useRouter } from 'vue-router';
      
      // Prices based on age categories
      const priceByAge = {
        Child: 8, // Example price for children
        Adult: 12, // Example price for adults
        Senior: 10 // Example price for seniors
      };
      
      const cart = ref([]);
      const groupedCart = ref([]);
      
      onMounted(() => {
        const cartData = localStorage.getItem('cart');
        if (cartData) {
          cart.value = JSON.parse(cartData);
          groupCartItems();
        }
      });
      
      // Edit and Save Seat Functions
      const editSeat = (seat) => {
        seat.isEditing = true;
      };
      
      const cancelEdit = (seat) => {
        seat.isEditing = false;
      };
      
      const saveSeat = (group, seatIndex) => {
        const seat = group.seats[seatIndex];
        seat.isEditing = false;
        
        // Recalculate the total after editing
        group.total = group.seats.reduce((sum, seat) => sum + seat.price, 0);
        updateCartFromGrouped();
      };
    
      const removeSeat = (group, seatIndex) => {
        group.seats.splice(seatIndex, 1);
        group.total = group.seats.reduce((sum, seat) => sum + seat.price, 0);
        updateCartFromGrouped(); 
      };
      
      // Group cart items based on movie and showtime
      const groupCartItems = () => {
        const grouped = {};
        cart.value.forEach((item) => {
          item.seats.forEach(seat => {
            const key = `${item.movie}-${item.showtime}`;
            if (!grouped[key]) {
              grouped[key] = {
                movie: item.movie,
                showtime: item.showtime,
                seats: [],
                showDetails: false
              };
            }
            seat.isEditing = false; // Add an 'isEditing' flag to control edit mode
            
            // Set the price based on the age category
            if (!seat.price) {
              seat.price = priceByAge[seat.age];
            }
            
            grouped[key].seats.push(seat);
          });
        });
        groupedCart.value = Object.values(grouped).map(group => {
          group.total = group.seats.reduce((sum, seat) => sum + seat.price, 0);
          return group;
        });
      };
      
      // Toggle details view
      const toggleDetails = (index) => {
        groupedCart.value[index].showDetails = !groupedCart.value[index].showDetails;
      };
      
      // Calculate the total for the cart
      const total = computed(() => {
        return groupedCart.value.reduce((sum, group) => {
          return sum + group.total;
        }, 0);
      });
      
      // Remove the entire group
      const removeGroup = (index) => {
        groupedCart.value.splice(index, 1);
        updateCartFromGrouped();
      };
      
      // Update the cart in localStorage based on groupedCart
      const updateCartFromGrouped = () => {
        const updatedCart = [];
        groupedCart.value.forEach(group => {
          updatedCart.push({
            movie: group.movie,
            showtime: group.showtime,
            seats: group.seats,
          });
        });
        cart.value = updatedCart;
        localStorage.setItem('cart', JSON.stringify(cart.value));
      };
      
      // Navigate to the movie order page
      const orderAnother = (group) => {
        router.push('/movie/:id');
      };
      
      const router = useRouter();
      
      // Checkout button function
      const checkout = () => {
        if (groupedCart.value.length > 0) {
          router.push('/CheckOut');
        } else {
          alert('Your cart is empty!');
        }
      };
      </script>
      
    
    
      <style scoped>
      
    
      .container {
        height: 150vh;
        background-color: #2f2e2e;
        display: block;
        box-sizing: border-box; 
        padding-bottom: 100px;
        width : 100%;
    }
      /* Header Style */
      h2 {
        font-size: 32px;
        font-weight: bold;
        text-align: center;
        margin-bottom: 20px;
        color: #007bff;
        border-bottom: 3px solid #007bff;
        padding-bottom: 10px;
        padding-top: 60px;
      }
      
      /* Cart Container */
      .cart-page {
    
        max-width: 900px;
        max-height: 200vh;
        margin: 20px auto;
        padding: 20px;
        background-color: #fff;
        border-radius: 12px;
        box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
      }
      
      /* Cart Item */
      .cart-item {
        background-color: #ffffff;
        margin-bottom: 20px;
        padding: 20px;
        border-radius: 10px;
        box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
        transition: box-shadow 0.3s ease, transform 0.3s ease;
      }
      
      .cart-item:hover {
        box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);
        transform: translateY(-4px);
      }
      
      /* Cart Header */
      .cart-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        cursor: pointer;
        font-size: 20px;
        color: #333;
      }
      
      .cart-header strong {
        font-size: 22px;
      }
      
      .cart-summary {
        font-size: 16px;
        color: #888;
      }
      
      /* Toggle Arrow */
      .cart-toggle {
        font-size: 24px;
        color: #888;
        cursor: pointer;
        transition: color 0.3s ease;
      }
      
      .cart-toggle:hover {
        color: #007bff;
      }
      
      /* Cart Details */
      .cart-details {
        margin-top: 10px;
        padding: 15px;
        background-color: #f9f9f9;
        border-radius: 8px;
        border: 1px solid #eee;
      }
      
      .cart-details ul {
        list-style-type: none;
        padding-left: 0;
      }
      
      .seat-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 10px 0;
        border-bottom: 1px solid #ddd;
      }
      
      /* Seat Info */
      .seat-info {
        flex: 1;
        font-size: 16px;
        color: #555;
      }
      
      .seat-info strong {
        color: #007bff;
      }
      
      .seat-actions {
        display: flex;
        gap: 10px;
      }
      
      .seat-input, .age-select {
        padding: 8px 12px;
        border: 1px solid #ddd;
        border-radius: 6px;
        background-color: #fff;
        font-size: 14px;
        transition: border-color 0.3s ease;
      }
      
      .seat-input:focus, .age-select:focus {
        border-color: #007bff;
      }
      
      /* Buttons */
      button {
        padding: 8px 16px;
        font-size: 14px;
        border: none;
        border-radius: 6px;
        cursor: pointer;
        transition: background-color 0.3s ease;
      }
      
      .save-seat-button, .remove-seat-button, .order-another-button, .checkout-button {
        color: white;
        font-weight: bold;
      }
      .save-seat-button{
        background-color: #06b03390;
      }
      
      .remove-seat-button {
        background-color: #ff4d4d;
      }
      
      .remove-seat-button:hover {
        background-color: #e60000;
      }
      
      .order-another-button {
        background-color: #28a745;
      }
      
      .order-another-button:hover {
        background-color: #218838;
      }
      
      /* Checkout Button */
      .checkout-button {
        margin-top: 20px;
        padding: 12px 24px;
        background-color: #007bff;
        font-size: 18px;
      }
      
      .checkout-button:hover {
        background-color: #0056b3;
      }
      
      /* Order Summary */
      .order-summary {
        margin-top: 20px;
        text-align: center;
        font-size: 20px;
        color: #333;
        font-weight: bold;
      }
      
      /* Order Total */
      .order-total {
        font-size: 24px;
        font-weight: bold;
        color: #555;
      }
      
      /* Transitions for Cart Details */
      .slide-fade-enter-active, .slide-fade-leave-active {
        transition: opacity 0.5s ease, max-height 0.5s ease;
      }
      
      .slide-fade-enter, .slide-fade-leave-to {
        opacity: 0;
        max-height: 0;
      }
      </style>
      