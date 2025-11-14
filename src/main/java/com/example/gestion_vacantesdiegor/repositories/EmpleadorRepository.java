package com.example.gestion_vacantesdiegor.repositories;

import com.example.gestion_vacantesdiegor.models.Empleador;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmpleadorRepository extends JpaRepository<Empleador, Long> {
    Optional<Empleador> findByCorreo(String correo);
}
