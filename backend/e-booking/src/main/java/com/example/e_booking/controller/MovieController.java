package com.example.e_booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.e_booking.model.Movie;
import com.example.e_booking.model.Schedule;
import com.example.e_booking.repositories.MovieRepository;
import com.example.e_booking.repositories.ScheduleRepository;

@RestController
@RequestMapping("/movies")
@CrossOrigin(origins = "http://localhost:5173")
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;
    
    // 获取所有电影
    // @GetMapping
    // public List<Movie> getAllMovies() {
    //     return movieRepository.findAll();
    // }

    // 根据类别获取电影
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getMoviesWithShowtimes() {
        List<Movie> movies = movieRepository.findAll();
        List<Map<String, Object>> response = new ArrayList<>();
    
        for (Movie movie : movies) {
            Map<String, Object> movieData = new HashMap<>();
            movieData.put("id", movie.getId());
            movieData.put("title", movie.getTitle());
            movieData.put("trailer", movie.getTrailer());
            movieData.put("category", movie.getCategory());
            movieData.put("genre", movie.getGenre());
            movieData.put("director", movie.getDirector());
            movieData.put("producer", movie.getProducer());
            // description
            movieData.put("description", movie.getDescription());
            movieData.put("synopsis", movie.getSynopsis());
            movieData.put("cast", movie.getCast());
            movieData.put("mpaaRating", movie.getMpaaRating());
            movieData.put("duration", movie.getDuration());
            movieData.put("price", movie.getPrice());
    
            // 获取与电影关联的所有排片时间
            List<Map<String, Object>> scheduleData = scheduleRepository.findByMovieId(movie.getId())
            .stream()
            .map(schedule -> {
                Map<String, Object> scheduleInfo = new HashMap<>();
                scheduleInfo.put("showtime", schedule.getShowtime().toString());
                scheduleInfo.put("showroom", schedule.getShowroom().getName());
                scheduleInfo.put("showroomId", schedule.getShowroom().getId());
                System.out.println("showroom id: " + schedule.getShowroom().getId());
                return scheduleInfo;
            })
            .toList();
            

            movieData.put("schedules", scheduleData);
            response.add(movieData);
        }
    
        return ResponseEntity.ok(response);
    }

    // 添加新电影
    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        Movie savedMovie = movieRepository.save(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getMovieById(@PathVariable Long id) {
        return movieRepository.findById(id)
                .map(movie -> {
                    List<Schedule> schedules = scheduleRepository.findByMovieId(id);
                    Map<String, Object> response = new HashMap<>();
                    response.put("movie", movie);
                    response.put("schedules", schedules);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("error", "Movie not found");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
                });
    }
    
    

    // 更新电影信息
    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie updatedMovie) {
        return movieRepository.findById(id)
                .map(existingMovie -> {
                    // 更新字段
                    existingMovie.setTitle(updatedMovie.getTitle());
                    existingMovie.setGenre(updatedMovie.getGenre());
                    existingMovie.setDuration(updatedMovie.getDuration());
                    existingMovie.setPrice(updatedMovie.getPrice());
                    existingMovie.setCategory(updatedMovie.getCategory());
                    existingMovie.setTrailer(updatedMovie.getTrailer());

                    // 保存更新后的电影
                    Movie savedMovie = movieRepository.save(existingMovie);
                    return ResponseEntity.ok(savedMovie);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 删除电影
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
