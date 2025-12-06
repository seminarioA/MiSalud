# Plataforma MiSalud · API REST v1 (Referencia para Front-End)

> **Versión Final:** 1.0.1
> Documentación técnica exhaustiva para integración Frontend.
> Todas las rutas están bajo el prefijo `/api/v1`.

---

## 0. Convenciones Técnicas

| Concepto | Regla de Implementación |
| --- | --- |
| **Autenticación** | Cabecera `Authorization: Bearer <token_jwt>`. El token se obtiene en `/auth/login`. |
| **Fecha (Date)** | Formato ISO-8601 estricto: `yyyy-MM-dd` (Ej: `2025-11-25`). |
| **Hora (Time)** | Formato ISO-8601 estricto: `HH:mm:ss` (Ej: `14:30:00`). |
| **DateTime** | Formato ISO-8601: `yyyy-MM-dd'T'HH:mm:ss` (Ej: `2025-11-25T14:30:00`). |
| **Paginación** | *No implementada en v1* (listados retornan `List<Object>`). Backend devuelve máx 50-100 items por defecto en consultas pesadas. |
| **CORS** | Configurado en `SecurityDevConfig` para permitir cualquier origen (`*`) en desarrollo. |

---

## 1. Catálogo de Enums (Glosario)

> **IMPORTANTE:** El frontend debe utilizar exactamente estos strings (Case Sensitive) para evitar errores 400.

#### 1.1 Estado de Cita (`EstadoCita`)
- `PENDIENTE`: Cita creada, aún no pagada ni confirmada.
- `CONFIRMADA`: Cita pagada/asegurada, lista para atención.
- `CANCELADA`: Anulada por paciente o doctor.
- `COMPLETADA`: Atención realizada.
- `NO_ASISTIO`: Paciente no se presentó.

#### 1.2 Tipo de Atención (`TipoAtencion`)
- `PRESENCIAL`: En consultorio físico.
- `TELEMEDICINA`: Videollamada (Link pendiente de implementación).

#### 1.3 Tipo de Documento (`TipoDocumento`)
- `DNI`: Documento Nacional de Identidad.
- `CE`: Carnet de Extranjería.
- `PASAPORTE`: Pasaporte.
- `CMP`: Colegio Médico del Perú.

#### 1.4 Estado de Tratamiento (`EstadoTratamiento`)
- `ACTIVO`: Paciente debe seguir tomándolo.
- `FINALIZADO`: Tratamiento concluido.
- `SUSPENDIDO`: Interrumpido por reacción adversa u otro motivo.

#### 1.5 Tipo de Diagnóstico (`TipoDiagnostico`)
- `PRESUNTIVO`: Sospecha inicial.
- `DEFINITIVO`: Confirmado por exámenes.

---

## 2. Autenticación (`/auth`)

### POST `/auth/login`
- **Público**
- **Body:** `{ "email": "admin@misalud.com", "password": "..." }`
- **Response 200:**
  ```json
  {
    "token": "eyJhbGciOi...",
    "user": { "id": 1, "email": "...", "rol": "OPERACIONES" }
  }
  ```

### GET `/auth/me`
- **Privado:** Requiere Token.
- **Response 200:** Datos del usuario actual.

### CRUD Usuarios (`/auth/usuarios`)
- Solo para Rol `OPERACIONES`.
- Permite crear credenciales para `RECEPCIONISTA`, `DOCTOR`, etc.

---

## 3. Gestión de Personas (`/personas`)
Recurso base para `Pacientes` y `Doctores`.

- `GET /personas`: Buscar todas.
- `GET /personas/{id}`: Detalle.
- `POST /personas`: Crear persona nueva.
- `PUT /personas/{id}`: Actualizar datos.
- `DELETE /personas/{id}`: Eliminar (si no tiene dependencias).

---

## 4. Pacientes (`/pacientes`)

### GET `/pacientes`
Lista todos los pacientes con su información de seguro.
```json
[
  {
    "id": 22,
    "persona": { ... }, // Objeto Persona completo
    "tipoSeguro": "EPS", // String libre o Enum según implementación futura
    "seguro": { "id": 5, "nombreAseguradora": "Pacifico", ... } // Objeto Seguro (si existe)
  }
]
```

### POST `/pacientes`
```json
{
  "persona": { ... },
  "tipoSeguro": "PARTICULAR"
}
```

---

## 5. Doctores (`/doctores`)

### GET `/doctores`
Devuelve lista de doctores con sus especialidades.
```json
[
  {
    "id": 8,
    "persona": { ... },
    "numeroColegiatura": "CMP123",
    "especialidadIds": [1, 2] // IDs de especialidades
  }
]
```

---

## 6. Recursos Auxiliares
- **Especialidades** (`/especialidades`): CRUD simple `{id, nombre}`.
- **Consultorios** (`/consultorios`): CRUD simple `{id, nombreONumero}`.

---

## 7. Gestión de Agenda

### 7.1 Horarios Base (`/horarios-medicos`)
Define el horario semanal recurrente (Lunes a Domingo).
- `GET /horarios-medicos?doctorId=X`: Ver horario de un doctor.
- `POST`: `{ "doctorId": 8, "diaSemana": "LUNES", "horaInicio": "08:00", "horaFin": "12:00" }`

### 7.2 Ausencias / Bloqueos (`/ausencias`) **[NUEVO]**
Excepciones al horario base (Vacaciones, Feriados). sobrescriben la disponibilidad.

- `POST /ausencias`:
```json
{
  "doctorId": 8,
  "fechaInicio": "2025-12-25",
  "fechaFin": "2025-12-25",
  "motivo": "Navidad"
}
```

- `GET /ausencias/doctor/{doctorId}`: Lista historial de ausencias.

---

## 8. Citas Médicas (`/citas`)

### GET `/citas`
Lista general (Roles administrativos).

### GET `/citas/doctor/{doctorId}`
- Query Param: `?fecha=yyyy-MM-dd` (Opcional, filtra por día específico).

### GET `/citas/paciente/{pacienteId}`
Historial completo del paciente.

### POST `/citas` (Agendar)
```json
{
  "pacienteId": 22,
  "doctorId": 8,
  "consultorioId": 1,
  "fechaCita": "2025-12-01",
  "horaCita": "10:00:00",
  "duracionMinutos": 30,
  "estado": "PENDIENTE",
  "tipoAtencion": "PRESENCIAL",
  "precioBase": 100.00,
  "seguroId": 5
}
```
> **Validaciones de Backend:**
> 1. El Doctor no debe tener otra cita a esa hora.
> 2. El Consultorio no debe estar ocupado.
> 3. El Doctor no debe tener una Ausencia (Vacaciones) registrada.

### PATCH `/citas/{id}/reprogramacion`
- Params: `fecha` (Date), `hora` (Time).
- Valida disponibilidad nuevamente.

### PATCH `/citas/{id}/estado`
- Param: `nuevoEstado` (`CONFIRMADA`, `CANCELADA`, etc).

---

## 9. Historia Clínica (`/historias-clinicas`)

### GET `/historias-clinicas/paciente/{pacienteId}`
Obtiene la carpeta médica del paciente. Si no existe, devuelve 404 o vacía.

### 9.1 Diagnósticos (`/diagnosticos`)
Asociados a una Cita y a la Historia Clínica.

- `GET /diagnosticos/cita/{citaId}`: Diagnósticos de una sesión específica.
- `GET /diagnosticos/historia/{historiaId}`: Todo el historial de diagnósticos del paciente.
- `POST`:
```json
{
  "citaId": 31,
  "descripcion": "Gripe Estacional",
  "cie10": "J10",
  "tipo": "DEFINITIVO"
}
```

### 9.2 Tratamientos (`/tratamientos`)
Asociados a un Diagnóstico específico.

- `GET /tratamientos/diagnostico/{diagnosticoId}`
- `POST`:
```json
{
  "diagnosticoId": 50,
  "descripcion": "Ibuprofeno 400mg",
  "fechaInicio": "2025-12-01",
  "fechaFin": "2025-12-05",
  "estado": "ACTIVO"
}
```

---

## 10. Pagos y Finanzas (`/pagos`)
- `GET /pagos`: Listado general.
- `POST /pagos`: Registrar un pago (Yape, Tarjeta).
- `GET /documentos-fiscales`: Facturas/Boletas generadas.

---

> **Nota para Frontend:**
> Si reciben un error `401 Unauthorized`, redirigir al Login.
> Si reciben `403 Forbidden`, mostrar mensaje "No tiene permisos para esta acción".
> Errores de validación (`400`) suelen venir con un mensaje descriptivo en el campo `message`.
