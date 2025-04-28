package com.example.e_booking.repositories;


import com.example.e_booking.model.Movie;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// 使用 @Repository 注解来标识这是一个数据访问组件
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    // JpaRepository 提供了基本的 CRUD 方法，因此我们可以不需要额外实现
    // 如果需要的话，可以添加自定义查询方法
    // 例如：根据类别查找电影
    List<Movie> findByCategory(String category);

    
}
