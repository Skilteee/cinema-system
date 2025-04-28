

const mysql = require('mysql2/promise');


const movies = [
  {
    title: 'Kanye1',
    trailer: 'https://www.youtube.com/embed/Bm5iA4Zupek',
    category: 'currently-running',
  },
  {
    title: 'Kanye2',
    trailer: 'https://www.youtube.com/embed/afLEae9_KSA',
    category: 'coming-soon',
  },

  {
    title: 'Friends',
    trailer: 'https://www.youtube.com/embed/6-1CwFumx_M',
    category: 'currently-running',
  }
  
];


async function insertMovies() {
  
  const connection = await mysql.createConnection({
    host: 'localhost',        
    user: 'root',    
    password: '123456',
    database: 'movie_db',     
  });

  try {
    
    for (const movie of movies) {
      const [rows] = await connection.execute(
        'INSERT INTO movies (title, trailer, category) VALUES (?, ?, ?)',
        [movie.title, movie.trailer, movie.category]
      );
      console.log(`Inserted movie: ${movie.title}`);
    }
    console.log('所有电影数据已成功插入！');
  } catch (error) {
    console.error('插入数据时发生错误:', error);
  } finally {
    
    await connection.end();
  }
}


insertMovies();

