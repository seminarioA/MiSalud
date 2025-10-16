package com.medx.beta.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entidad base para gestionar auditoría simple en las entidades del dominio.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class AuditableEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "fecha_creacion", nullable = false, updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @PrePersist
    private void beforePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.fechaCreacion = now;
        this.fechaActualizacion = now;
        onCreate();
    }

    @PreUpdate
    private void beforeUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
        onUpdate();
    }

    /**
     * Gancho para ejecutar lógica adicional en {@link PrePersist}.
     */
    protected void onCreate() {
        // Implementado por las subclases en caso necesario.
    }

    /**
     * Gancho para ejecutar lógica adicional en {@link PreUpdate}.
     */
    protected void onUpdate() {
        // Implementado por las subclases en caso necesario.
    }
}
