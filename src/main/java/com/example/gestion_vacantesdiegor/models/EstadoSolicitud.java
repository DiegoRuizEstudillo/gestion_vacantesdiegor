package com.example.gestion_vacantesdiegor.models;

public enum EstadoSolicitud {
    PENDIENTE,
    APROBADA,    // Cambié ACEPTADA por APROBADA para coincidir con la BD
    RECHAZADA
    // Eliminé REVISADA para mantenerlo simple
}