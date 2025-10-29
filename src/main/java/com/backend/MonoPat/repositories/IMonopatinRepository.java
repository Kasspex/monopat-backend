package com.backend.MonoPat.repositories;

import com.backend.MonoPat.entities.Monopatin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMonopatinRepository extends JpaRepository<Monopatin, Long> {

    List<Monopatin> findByEstado(String estado);
}
