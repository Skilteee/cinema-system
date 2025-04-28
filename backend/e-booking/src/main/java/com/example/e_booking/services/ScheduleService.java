package com.example.e_booking.services;

import com.example.e_booking.dto.ScheduleRequest;
import com.example.e_booking.model.Movie;
import com.example.e_booking.model.Schedule;
import com.example.e_booking.model.Showroom;
import com.example.e_booking.repositories.MovieRepository;
import com.example.e_booking.repositories.ScheduleRepository;
import com.example.e_booking.repositories.ShowroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowroomRepository showroomRepository;

    public Schedule createSchedule(ScheduleRequest request) {
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + request.getMovieId()));
        Showroom showroom = showroomRepository.findById(request.getShowroomId())
                .orElseThrow(() -> new RuntimeException("Showroom not found with id: " + request.getShowroomId()));

        Schedule schedule = new Schedule();
        schedule.setMovie(movie);
        schedule.setShowroom(showroom);
        schedule.setShowtime(request.getShowtime());
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getSchedulesByMovieId(Long movieId) {
        return scheduleRepository.findByMovieId(movieId);
    }

    public List<Schedule> getSchedulesByShowroomId(Long showroomId) {
        return scheduleRepository.findByShowroomId(showroomId);
    }
}
