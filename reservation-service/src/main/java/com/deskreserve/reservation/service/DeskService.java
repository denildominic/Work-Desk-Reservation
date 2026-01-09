\
package com.deskreserve.reservation.service;

import com.deskreserve.reservation.api.ApiException;
import com.deskreserve.reservation.domain.Desk;
import com.deskreserve.reservation.repo.DeskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeskService {

    private final DeskRepository deskRepository;

    public DeskService(DeskRepository deskRepository) {
        this.deskRepository = deskRepository;
    }

    @Transactional(readOnly = true)
    public List<Desk> list() {
        return deskRepository.findAll();
    }

    @Transactional
    public Desk create(String name, String location) {
        deskRepository.findByName(name).ifPresent(d -> {
            throw new ApiException(HttpStatus.CONFLICT, "Desk name already exists");
        });
        return deskRepository.save(new Desk(name, location));
    }

    @Transactional(readOnly = true)
    public Desk getRequired(Long id) {
        return deskRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Desk not found"));
    }
}
