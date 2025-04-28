package com.example.e_booking.services;

import com.example.e_booking.model.Showroom;
import com.example.e_booking.repositories.ShowroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowroomService {

    @Autowired
    private ShowroomRepository showroomRepository;

    public Showroom addShowroom(Showroom showroom) {
        return showroomRepository.save(showroom);
    }

    public List<Showroom> getAllShowrooms() {
        return showroomRepository.findAll();
    }

    public Showroom getShowroomById(Long id) {
        return showroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Showroom not found with id: " + id));
    }
}
