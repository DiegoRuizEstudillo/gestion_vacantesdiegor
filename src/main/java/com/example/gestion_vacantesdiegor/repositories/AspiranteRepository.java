package com.example.gestion_vacantesdiegor.repositories;

import com.example.gestion_vacantesdiegor.models.Aspirante;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AspiranteRepository extends JpaRepository<Aspirante, Long> {
    Optional<Aspirante> findByCorreo(String correo);
}
