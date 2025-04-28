<template>
    <div class="modal-background" @click.self="close">
      <div class="modal-content">
        <h2>Schedule Movie: {{ movie.title }}</h2>
  
        <form @submit.prevent="schedule">
          <!-- Date Input -->
          <label for="date">Specific Date:</label>
          <input
            type="date"
            v-model="scheduleDate"
            @change="resetShowrooms"
            required
          />
  
          <!-- Time Input -->
          <label for="showtime">Showtime:</label>
          <input
            type="time"
            v-model="scheduleTime"
            @change="resetShowrooms"
            required
          />
  
          <!-- Showroom Selection -->
          <label for="showroom">Select Showroom:</label>
          <select
            id="showroom"
            v-model="selectedShowroomId"
            :disabled="!availableShowrooms.length"
            required
          >
            <option value="" disabled>Select a showroom</option>
            <option
              v-for="showroom in availableShowrooms"
              :key="showroom.id"
              :value="showroom.id"
            >
              {{ showroom.name }} (Capacity: {{ showroom.capacity }})
            </option>
          </select>
  
          <button
            type="button"
            class="fetch-button"
            @click="fetchAvailableShowrooms"
            :disabled="!scheduleDate || !scheduleTime"
          >
            Fetch Available Showrooms
          </button>
  
          <!-- Actions -->
          <div class="modal-actions">
            <button type="submit" class="save-button">Schedule</button>
            <button type="button" class="cancel-button" @click="close">
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  </template>
  
  <script>
  import axios from "axios";
  
  export default {
    props: {
      movie: Object, // 当前选中的电影对象
    },
    data() {
      return {
        scheduleDate: "", // 用户选择的日期
        scheduleTime: "", // 用户选择的时间
        availableShowrooms: [], // 可用的放映室列表
        selectedShowroomId: "", // 用户选择的放映室 ID
      };
    },
    methods: {
      async fetchAvailableShowrooms() {
        if (!this.scheduleDate || !this.scheduleTime) {
          alert("Please select both date and time before fetching showrooms!");
          return;
        }
  
        try {
          const response = await axios.get(
            "http://localhost:8080/showrooms/available",
            {
              params: {
                date: this.scheduleDate,
                time: this.scheduleTime,
              },
            }
          );
          this.availableShowrooms = response.data;
  
          if (!this.availableShowrooms.length) {
            alert("No available showrooms found for the selected time.");
          }
        } catch (err) {
          console.error("Error fetching available showrooms:", err);
          this.availableShowrooms = [];
        }
      },
      resetShowrooms() {
        this.availableShowrooms = []; // 清空可用放映室
        this.selectedShowroomId = ""; // 重置用户选择
      },
      async schedule() {
        if (
          !this.selectedShowroomId ||
          !this.scheduleDate ||
          !this.scheduleTime
        ) {
          alert("Please complete all fields before scheduling.");
          return;
        }
  
        // 组合排片数据
        const scheduleDetails = {
          movie: { id: this.movie.id },
          showroom: { id: this.selectedShowroomId },
          showtime: `${this.scheduleDate}T${this.scheduleTime}`, // 合成 ISO 格式的日期时间
        };
  
        try {
          // 调用后端 API 创建排片
          await axios.post("http://localhost:8080/schedules", scheduleDetails);
          alert("Schedule created successfully!");
          

          // 调用座位初始化接口
          const seatInitResponse = await axios.post("http://localhost:8080/seats/initialize", null, {
            params: { showroomId: this.selectedShowroomId },
          });

        //   if (seatInitResponse.status === 200) {
        //     alert("Seats initialized successfully!");
        //   } 

          this.$emit("schedule-success");
          this.close();
        } catch (err) {
          console.error("Error creating schedule:", err);
          alert("Failed to create schedule. Please try again.");
        }
      },
      close() {
        this.$emit("close"); // 通知父组件关闭模态框
      },
    },
  };
  </script>

  <style scoped>
  .modal-background {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.7);
    display: flex;
    justify-content: center;
    align-items: center;
  }
  
  .modal-content {
    background: #333;
    padding: 20px;
    border-radius: 8px;
    width: 400px;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.5);
  }
  
  h2 {
    margin-bottom: 15px;
    color: #ffcc00; /* Highlight color */
  }
  
  label {
    display: block;
    margin-bottom: 5px;
    color: #f9f9f9;
  }
  
  input,
  select {
    width: 100%;
    padding: 8px;
    margin-bottom: 15px;
    border: none;
    border-radius: 4px;
    background-color: #444;
    color: #f9f9f9;
  }
  
  input:focus,
  select:focus {
    outline: none;
    background-color: #555;
  }
  
  button.fetch-button {
    background-color: #ffc107;
    color: #212529;
    padding: 10px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    margin-bottom: 15px;
  }
  
  button.fetch-button:hover {
    background-color: #e0a800;
  }
  
  .modal-actions {
    display: flex;
    justify-content: space-between;
  }
  
  .save-button,
  .cancel-button {
    padding: 10px 15px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .save-button {
    background-color: #007bff;
    color: white;
  }
  
  .save-button:hover {
    background-color: #0056b3;
  }
  
  .cancel-button {
    background-color: #dc3545;
    color: white;
  }
  
  .cancel-button:hover {
    background-color: #c82333;
  }
  </style>
  