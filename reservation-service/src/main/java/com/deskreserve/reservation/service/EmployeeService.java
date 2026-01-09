\
package com.deskreserve.reservation.service;

import com.deskreserve.reservation.domain.Employee;
import com.deskreserve.reservation.repo.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public Employee getOrCreate(String username) {
        return employeeRepository.findByUsername(username)
                .orElseGet(() -> employeeRepository.save(new Employee(username, username)));
    }
}
