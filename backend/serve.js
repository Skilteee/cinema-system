// server.js
const axios = require('axios');



const express = require('express');
const mysql = require('mysql2/promise');
const cors = require('cors');

const app = express();
const PORT = 3000;

app.use(cors());


const pool = mysql.createPool({
  host: 'localhost',     
  user: 'root',  
  password: '123456',
  database: 'movie_db',    
  waitForConnections: true,
  connectionLimit: 10,
  queueLimit: 0
});

app.get('/api/movies', async (req, res) => {
  try {
    const [rows] = await pool.query('SELECT * FROM movies');
    res.json(rows);
  } catch (error) {
    console.error('server when getting the movie data:', error);
    res.status(500).json({ error: 'server error' });
  }
});


app.get('/api/movies/:category', async (req, res) => {
  const { category } = req.params;
  try {
    const [rows] = await pool.query('SELECT * FROM movies WHERE category = ?', [category]);
    res.json(rows);
  } catch (error) {
    console.error('server when getting the movie data:', error);
    res.status(500).json({ error: 'server error' });
  }
});

app.listen(PORT, () => {
  console.log(`the server is running on  http://localhost:${PORT}`);
});

// // server.js
// const express = require('express');
// const axios = require('axios');
// const cors = require('cors');

// const app = express();
// const PORT = 3000;

// // 使用 CORS 中间件
// app.use(cors());

// // Middleware to parse JSON request bodies
// app.use(express.json());

// // 获取所有电影
// app.get('/api/movies', async (req, res) => {
//   try {
//     // 调用 Spring Boot API
//     const response = await axios.get('http://localhost:8080/movies');
//     // 将 Spring Boot 返回的数据发送到前端
//     res.json(response.data);
//   } catch (error) {
//     console.error('Error fetching movie data from Spring Boot:', error);
//     res.status(500).json({ error: 'Server error while fetching movies' });
//   }
// });

// // 根据类别获取电影
// app.get('/api/movies/:category', async (req, res) => {
//   const { category } = req.params;
//   try {
//     // 调用 Spring Boot API
//     const response = await axios.get(`http://localhost:8080/movies/${category}`);
//     // 将 Spring Boot 返回的数据发送到前端
//     res.json(response.data);
//   } catch (error) {
//     console.error('Error fetching movie data by category from Spring Boot:', error);
//     res.status(500).json({ error: 'Server error while fetching movies by category' });
//   }
// });

// // 用户注册
// app.post('/api/register', async (req, res) => {
//   const userData = req.body;

//   try {
//     // 调用 Spring Boot API，转发用户数据
//     const response = await axios.post('http://localhost:8080/api/users/register/regular', userData);
//     // 返回 Spring Boot 的响应给前端
//     res.status(200).json(response.data);
//   } catch (error) {
//     // 检查是否有来自 Spring Boot 的响应错误
//     if (error.response && error.response.data) {
//       res.status(error.response.status).json(error.response.data);
//     } else {
//       res.status(500).json({ message: 'Failed to register user' });
//     }
//   }
// });

// // 启动服务器
// app.listen(PORT, () => {
//   console.log(`Node.js server is running on http://localhost:${PORT}`);
// });
