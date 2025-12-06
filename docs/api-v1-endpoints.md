# Plataforma MiSalud · API REST v1 (Referencia para Front-End)

> Documentación preparada para equipos front-end. Cada operación expone payloads reales de entrada/salida, roles permitidos, parámetros, códigos HTTP y casos borde. Todas las rutas están versionadas bajo `/api/v1`.

---

## 0. Convenciones rápidas

| Tema | Detalle |
| --- | --- |
| Autenticación | JWT Bearer en `Authorization: Bearer <token>`. Solo `/auth/login` es público. |
| Roles | `PACIENTE`, `DOCTOR`, `RECEPCIONISTA`, `OPERACIONES`. |
| Formatos | JSON UTF-8. Fecha: `yyyy-MM-dd`. Hora: `HH:mm:ss`. Timestamp: `yyyy-MM-dd'T'HH:mm:ss`. |
| Errores | Estructura estándar: `{ "status": 400, "error": "Bad Request", "message": "Detalle", "timestamp": "2025-11-22T18:10:04" }`. |
| Seguridad | `SecurityDevConfig` + `JwtAuthenticationFilter` + `@PreAuthorize`. **CORS Abierto (*)** por perfil dev. |
| Buenas prácticas | Cachear catálogos, exponer mensajes backend, limpiar sesión ante `401`, mostrar CTA ante `404`. |

---

## 0.1 Política de Seguridad y CORS (Contexto Desarrollo)

> **NOTA IMPORTANTE:** Esta configuración aplica al perfil `dev` (`SecurityDevConfig.java`).

| Pregunta | Respuesta / Política |
| --- | --- |
| **¿Quién puede acceder?** | **Cualquier Origen (`*`)**. Se permiten peticiones desde `localhost:3000`, aplicaciones móviles, Postman, etc. |
| **¿Quién NO puede acceder?** | Nadie es bloqueado por origen en este entorno. Sin embargo, usuarios **sin token válido** son rechazados en rutas privadas. |
| **¿Cuándo aplica?** | Siempre que la aplicación corra con el perfil de desarrollo activo. |
| **¿Por qué SI?** | Para facilitar la integración ágil entre equipos Frontend y Backend trabajando en puertos locales distintos sin bloqueos de navegador. |
| **¿Por qué NO?** | No se bloquea nada para evitar fricción en etapas tempranas. **En Producción se restringirá a dominios oficiales.** |

---

## 1. Autenticación (`/auth`)

### POST `/auth/login`
- **Roles:** Público
- **Request**
```json
{
  "email": "usuario@misalud.com",
  "password": "Secreto123"
}
```
- **Response 200**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 12,
    "email": "usuario@misalud.com",
    "rol": "DOCTOR"
  }
}
```
- **Errores:** `401` credenciales inválidas, `423` cuenta bloqueada.

### GET `/auth/me`
- **Roles:** Cualquiera autenticado
- **Response 200**
```json
{
  "id": 12,
  "email": "usuario@misalud.com",
  "rol": "DOCTOR"
}
```
- **Errores:** `401` token inválido/expirado.

### CRUD de usuarios del sistema (`ROL OPERACIONES`)

#### GET `/auth/usuarios`
```json
[
  {
    "id": 44,
    "email": "lucia.flores@misalud.com",
    "rol": "RECEPCIONISTA"
  }
]
```

#### POST `/auth/usuarios`
- **Request**
```json
{
  "persona": {
    "primerNombre": "Lucía",
    "primerApellido": "Flores",
    "tipoDocumento": "DNI",
    "numeroDocumento": "87654321",
    "fechaNacimiento": "1994-08-21",
    "genero": "FEMENINO"
  },
  "email": "lucia.flores@misalud.com",
  "password": "Temporal123",
  "rol": "RECEPCIONISTA"
}
```
- **Response 201**
```json
{
  "id": 44,
  "persona": {
    "id": 91,
    "primerNombre": "Lucía",
    "primerApellido": "Flores",
    "tipoDocumento": "DNI",
    "numeroDocumento": "87654321",
    "fechaNacimiento": "1994-08-21",
    "genero": "FEMENINO"
  },
  "email": "lucia.flores@misalud.com",
  "rol": "RECEPCIONISTA"
}
```
- **Errores:** `400` password corta, `409` email duplicado.

#### PUT `/auth/usuarios/{id}`
```json
{
  "persona": {
    "primerNombre": "Lucía",
    "primerApellido": "Flores",
    "tipoDocumento": "DNI",
    "numeroDocumento": "87654321",
    "fechaNacimiento": "1994-08-21",
    "genero": "FEMENINO"
  },
  "email": "lucia.flores@misalud.com",
  "password": "NuevaClave456",
  "rol": "OPERACIONES"
}
```
Response 200: igual al POST con los nuevos valores. `404` id inexistente, `409` email usado por otro usuario.

#### DELETE `/auth/usuarios/{id}`
- **Response 204** sin cuerpo.
- **Caso borde:** si el usuario estaba logueado, cualquier request posterior devolverá `401`.

---

## 2. Personas (`/personas`)

### GET `/personas`
- **Roles:** `RECEPCIONISTA`, `OPERACIONES`
```json
[
  {
    "id": 105,
    "primerNombre": "Marcos",
    "segundoNombre": "Andrés",
    "primerApellido": "Soto",
    "segundoApellido": "Pérez",
    "tipoDocumento": "CE",
    "numeroDocumento": "CE998877",
    "fechaNacimiento": "1988-02-14",
    "genero": "MASCULINO",
    "numeroTelefono": "998887766",
    "urlFotoPerfil": "https://cdn.misalud.com/perfiles/marcos.jpg"
  }
]
```

### GET `/personas/{id}`
```json
{
  "id": 105,
  "primerNombre": "Marcos",
  "segundoNombre": "Andrés",
  "primerApellido": "Soto",
  "segundoApellido": "Pérez",
  "tipoDocumento": "CE",
  "numeroDocumento": "CE998877",
  "fechaNacimiento": "1988-02-14",
  "genero": "MASCULINO",
  "numeroTelefono": "998887766",
  "urlFotoPerfil": "https://cdn.misalud.com/perfiles/marcos.jpg"
}
```
Errores: `404` id inexistente.

### POST `/personas`
```json
{
  "primerNombre": "Marcos",
  "segundoNombre": "Andrés",
  "primerApellido": "Soto",
  "segundoApellido": "Pérez",
  "tipoDocumento": "CE",
  "numeroDocumento": "CE998877",
  "fechaNacimiento": "1988-02-14",
  "genero": "MASCULINO",
  "numeroTelefono": "998887766",
  "urlFotoPerfil": "https://cdn.misalud.com/perfiles/marcos.jpg"
}
```
Response 201: mismo objeto + `id`. Errores: `400` documento inválido, `409` duplicado.

### PUT `/personas/{id}`
```json
{
  "primerNombre": "Marcos",
  "segundoNombre": "A.",
  "primerApellido": "Soto",
  "segundoApellido": "Pérez",
  "tipoDocumento": "CE",
  "numeroDocumento": "CE998877",
  "fechaNacimiento": "1988-02-14",
  "genero": "MASCULINO",
  "numeroTelefono": "900111222",
  "urlFotoPerfil": null
}
```
Response 200: registro actualizado. `422` si fecha futura.

### DELETE `/personas/{id}`
`204 No Content`. `409` si la persona está enlazada a un paciente/doctor.

---

## 3. Pacientes (`/pacientes`)

### GET `/pacientes`
- **Roles:** `PACIENTE`, `RECEPCIONISTA`, `OPERACIONES`
```json
[
  {
    "id": 22,
    "persona": {
      "id": 205,
      "primerNombre": "Ana",
      "segundoNombre": "Lucía",
      "primerApellido": "Rojas",
      "segundoApellido": "García",
      "tipoDocumento": "DNI",
      "numeroDocumento": "12345678",
      "fechaNacimiento": "1992-04-15",
      "genero": "FEMENINO",
      "numeroTelefono": "987654321"
    },
    "tipoSeguro": "ESSALUD"
  }
]
```

### GET `/pacientes/{id}`
```json
{
  "id": 22,
  "persona": {
    "id": 205,
    "primerNombre": "Ana",
    "segundoNombre": "Lucía",
    "primerApellido": "Rojas",
    "segundoApellido": "García",
    "tipoDocumento": "DNI",
    "numeroDocumento": "12345678",
    "fechaNacimiento": "1992-04-15",
    "genero": "FEMENINO",
    "numeroTelefono": "987654321"
  },
  "tipoSeguro": "ESSALUD"
}
```

### POST `/pacientes`
```json
{
  "persona": {
    "primerNombre": "Ana",
    "segundoNombre": "Lucía",
    "primerApellido": "Rojas",
    "segundoApellido": "García",
    "tipoDocumento": "DNI",
    "numeroDocumento": "12345678",
    "fechaNacimiento": "1992-04-15",
    "genero": "FEMENINO",
    "numeroTelefono": "987654321"
  },
  "tipoSeguro": "ESSALUD"
}
```
Response 201: paciente con `id`. `409` si documento ya existe.

### PUT `/pacientes/{id}`
```json
{
  "persona": {
    "primerNombre": "Ana",
    "segundoNombre": "María",
    "primerApellido": "Rojas",
    "segundoApellido": "García",
    "tipoDocumento": "DNI",
    "numeroDocumento": "12345678",
    "fechaNacimiento": "1992-04-15",
    "genero": "FEMENINO",
    "numeroTelefono": "987000111"
  },
  "tipoSeguro": "EPS"
}
```
Response 200: paciente actualizado.

### DELETE `/pacientes/{id}`
`204`. `409` si tiene citas activas.

---

## 4. Responsables (`/pacientes/{pacienteId}/responsables`)

### GET
```json
[
  {
    "id": 11,
    "pacienteId": 22,
    "personaResponsableId": 41,
    "relacion": "Madre"
  }
]
```

### POST
```json
{
  "pacienteId": 22,
  "personaResponsableId": 41,
  "relacion": "Madre"
}
```
Response 201: responsable creado.

### PUT `/pacientes/{pacienteId}/responsables/{id}`
```json
{
  "pacienteId": 22,
  "personaResponsableId": 41,
  "relacion": "Hermana"
}
```
Response 200: responsable actualizado. `404` si id inválido.

### DELETE
`204`. `409` si se intenta eliminar el único contacto permitido.

---

## 5. Doctores (`/doctores`)

### GET `/doctores`
```json
[
  {
    "id": 8,
    "persona": {
      "id": 301,
      "primerNombre": "Julio",
      "primerApellido": "García",
      "tipoDocumento": "CMP",
      "numeroDocumento": "CMP123456",
      "fechaNacimiento": "1980-10-10",
      "genero": "MASCULINO"
    },
    "numeroColegiatura": "CMP123456",
    "especialidadIds": [1, 4]
  }
]
```

### GET `/doctores/{id}`
```json
{
  "id": 8,
  "persona": {
    "id": 301,
    "primerNombre": "Julio",
    "primerApellido": "García",
    "tipoDocumento": "CMP",
    "numeroDocumento": "CMP123456",
    "fechaNacimiento": "1980-10-10",
    "genero": "MASCULINO"
  },
  "numeroColegiatura": "CMP123456",
  "especialidadIds": [1, 4]
}
```

### POST / PUT
```json
{
  "persona": {
    "primerNombre": "Julio",
    "primerApellido": "García",
    "tipoDocumento": "CMP",
    "numeroDocumento": "CMP123456",
    "fechaNacimiento": "1980-10-10",
    "genero": "MASCULINO"
  },
  "numeroColegiatura": "CMP123456",
  "especialidadIds": [1, 4]
}
```
Response 201/200 según corresponda. `400` si sin especialidades, `409` colegiatura duplicada.

### DELETE `/doctores/{id}`
`204`. `409` si tiene horarios o citas vigentes.

---

## 6. Especialidades (`/especialidades`)

### GET (público)
```json
[
  { "id": 1, "nombre": "Medicina General" }
]
```

### POST / PUT
```json
{ "nombre": "Gastroenterología" }
```
Response 201/200: `{ "id": 3, "nombre": "Gastroenterología" }`. `409` si nombre repetido.

### DELETE `/especialidades/{id}`
`204`. `409` si se usa en doctores.

---

## 7. Consultorios (`/consultorios`)

### GET (público)
```json
[
  { "id": 7, "nombreONumero": "Consultorio 201" }
]
```

### POST / PUT
```json
{ "nombreONumero": "Consultorio 301" }
```
Response 201/200: `{ "id": 9, "nombreONumero": "Consultorio 301" }`. `409` si duplicado.

### DELETE `/consultorios/{id}`
`204`. `409` si hay citas asociadas.

---

## 8. Horarios médicos (`/horarios-medicos`)

### GET `/horarios-medicos`
```json
[
  {
    "id": 3,
    "doctorId": 8,
    "diaSemana": "LUNES",
    "horaInicio": "08:00:00",
    "horaFin": "12:00:00",
    "esVacaciones": false
  }
]
```

### POST / PUT
```json
{
  "doctorId": 8,
  "diaSemana": "MARTES",
  "horaInicio": "14:00:00",
  "horaFin": "18:00:00",
  "esVacaciones": false
}
```
Response 201/200: horario con `id`. `400` si hora fin <= inicio, `409` por solapamiento.

### DELETE `/horarios-medicos/{id}`
`204`. `409` si existen citas para ese bloque de tiempo.

---

## 9. Historias clínicas (`/historias-clinicas`)

### GET `/historias-clinicas`
```json
[
  {
    "id": 15,
    "pacienteId": 22,
    "fechaApertura": "2024-01-05T10:00:00"
  }
]
```

### GET `/historias-clinicas/{id}`
```json
{
  "id": 15,
  "pacienteId": 22,
  "fechaApertura": "2024-01-05T10:00:00"
}
```

### GET `/historias-clinicas/paciente/{pacienteId}`
```json
{
  "id": 15,
  "pacienteId": 22,
  "fechaApertura": "2024-01-05T10:00:00"
}
```

### POST
```json
{ "pacienteId": 22 }
```
Response 201: historia creada.

### PUT `/historias-clinicas/{id}`
```json
{
  "pacienteId": 22,
  "fechaApertura": "2024-01-05T10:00:00"
}
```
Response 200: historia actualizada. `409` si se intenta cambiar el paciente.

### DELETE
`204`. `409` si existen registros clínicos dependientes.

---

## 10. Citas (`/citas`)

### GET `/citas`
```json
[
  {
    "id": 31,
    "pacienteId": 22,
    "doctorId": 8,
    "consultorioId": 7,
    "fechaCita": "2025-12-01",
    "horaCita": "09:30:00",
    "duracionMinutos": 30,
    "estado": "PENDIENTE",
    "tipoAtencion": "PRESENCIAL",
    "precioBase": 150.0,
    "montoDescuento": 20.0,
    "costoNetoCita": 130.0
  }
]
```

### GET `/citas/{id}`
```json
{
  "id": 31,
  "pacienteId": 22,
  "doctorId": 8,
  "consultorioId": 7,
  "fechaCita": "2025-12-01",
  "horaCita": "09:30:00",
  "duracionMinutos": 30,
  "estado": "PENDIENTE",
  "tipoAtencion": "PRESENCIAL",
  "precioBase": 150.0,
  "montoDescuento": 20.0,
  "costoNetoCita": 130.0
}
```

### GET `/citas/doctor/{doctorId}?fecha=2025-12-01`
```json
[
  {
    "id": 31,
    "pacienteId": 22,
    "doctorId": 8,
    "consultorioId": 7,
    "fechaCita": "2025-12-01",
    "horaCita": "09:30:00",
    "duracionMinutos": 30,
    "estado": "PENDIENTE",
    "tipoAtencion": "PRESENCIAL",
    "precioBase": 150.0,
    "montoDescuento": 20.0,
    "costoNetoCita": 130.0
  }
]
```

### GET `/citas/paciente/{pacienteId}`
```json
[
  {
    "id": 31,
    "pacienteId": 22,
    "doctorId": 8,
    "consultorioId": 7,
    "fechaCita": "2025-12-01",
    "horaCita": "09:30:00",
    "duracionMinutos": 30,
    "estado": "PENDIENTE",
    "tipoAtencion": "PRESENCIAL",
    "precioBase": 150.0,
    "montoDescuento": 20.0,
    "costoNetoCita": 130.0
  }
]
```

### POST / PUT
```json
{
  "pacienteId": 22,
  "doctorId": 8,
  "consultorioId": 7,
  "fechaCita": "2025-12-01",
  "horaCita": "09:30:00",
  "duracionMinutos": 30,
  "estado": "PENDIENTE",
  "tipoAtencion": "PRESENCIAL",
  "precioBase": 150.0,
  "montoDescuento": 20.0,
  "costoNetoCita": 130.0
}
```
Response 201/200: cita con `id`. `409` si ya existe una cita en la misma franja.

### DELETE `/citas/{id}`
`204`. `409` si la cita tiene pagos aplicados.

### PATCH `/citas/{id}/reprogramacion`
Permite cambiar fecha y hora de una cita existente (si no está finalizada).
- **Roles:** `RECEPCIONISTA`, `OPERACIONES`
- **Query Params:**
  - `fecha` (Date, ISO): Nueva fecha `yyyy-MM-dd`.
  - `hora` (Time, ISO): Nueva hora `HH:mm:ss`.
- **Response 200**: Objeto Cita actualizado.
- **Errores**: `400` si hay conflicto de horario (Double Booking) o fecha pasada. `409` si estado no permite cambios.

### PATCH `/citas/{id}/estado`
Cambio de ciclo de vida (confirmar, cancelar, completar).
- **Roles:** `DOCTOR`, `RECEPCIONISTA`, `OPERACIONES`
- **Query Params:**
  - `nuevoEstado`: Enum (`PENDIENTE`, `CONFIRMADA`, `CANCELADA`, `COMPLETADA`, `NO_ASISTIO`).
- **Response 204**: Sin cuerpo.
- **Errores**: `409` transición de estado inválida.

---

## 11. Pagos (`/pagos`)

### GET `/pagos`
```json
[
  {
    "id": 1,
    "citaId": 31,
    "monto": 130.0,
    "tipoTransaccion": "INGRESO",
    "estadoPago": "COMPLETADO",
    "metodoPago": "YAPE",
    "codigoOperacion": "YAPE-20240511-9988",
    "fechaPago": "2025-11-22T12:01:00"
  }
]
```

### GET `/pagos/{id}`
```json
{
  "id": 1,
  "citaId": 31,
  "monto": 130.0,
  "tipoTransaccion": "INGRESO",
  "estadoPago": "COMPLETADO",
  "metodoPago": "YAPE",
  "codigoOperacion": "YAPE-20240511-9988",
  "fechaPago": "2025-11-22T12:01:00"
}
```

### GET `/pagos/cita/{citaId}`
```json
[
  {
    "id": 1,
    "citaId": 31,
    "monto": 130.0,
    "tipoTransaccion": "INGRESO",
    "estadoPago": "COMPLETADO",
    "metodoPago": "YAPE",
    "codigoOperacion": "YAPE-20240511-9988",
    "fechaPago": "2025-11-22T12:01:00"
  }
]
```

### POST / PUT
```json
{
  "citaId": 31,
  "monto": 130.0,
  "tipoTransaccion": "INGRESO",
  "estadoPago": "COMPLETADO",
  "metodoPago": "YAPE",
  "codigoOperacion": "YAPE-20240511-9988",
  "fechaPago": "2025-11-22T12:01:00"
}
```
Response 201/200: pago con `id`. `409` si ya existe un pago con el mismo código de operación.

### DELETE `/pagos/{id}`
`204`. `409` si existe documento fiscal asociado (eliminar primero el documento).

---

## 12. Documentos fiscales (`/documentos-fiscales`)

### GET `/documentos-fiscales`
```json
[
  {
    "id": 1,
    "pagoId": 45,
    "tipoComprobante": "FACTURA",
    "serie": "F001",
    "correlativo": 1234,
    "rucCliente": "20123456789",
    "nombreClienteFiscal": "ACME SAC"
  }
]
```

### GET `/documentos-fiscales/{id}`
```json
{
  "id": 1,
  "pagoId": 45,
  "tipoComprobante": "FACTURA",
  "serie": "F001",
  "correlativo": 1234,
  "rucCliente": "20123456789",
  "nombreClienteFiscal": "ACME SAC"
}
```

### GET `/documentos-fiscales/pago/{pagoId}`
```json
[
  {
    "id": 1,
    "pagoId": 45,
    "tipoComprobante": "FACTURA",
    "serie": "F001",
    "correlativo": 1234,
    "rucCliente": "20123456789",
    "nombreClienteFiscal": "ACME SAC"
  }
]
```

### POST / PUT
```json
{
  "pagoId": 45,
  "tipoComprobante": "FACTURA",
  "serie": "F001",
  "correlativo": 1234,
  "rucCliente": "20123456789",
  "nombreClienteFiscal": "ACME SAC"
}
```
Response 201/200: documento con `id`. `409` si la serie/correlativo ya existe.

### DELETE `/documentos-fiscales/{id}`
`204`. `404` si el id no existe.

---

## 13. Matriz de roles

| Recurso | Lectura | Escritura |
| --- | --- | --- |
| Auth/login | Público | – |
| Auth/me | Autenticado | – |
| Usuarios sistema | `OPERACIONES` | `OPERACIONES` |
| Personas | `RECEPCIONISTA`, `OPERACIONES` | `OPERACIONES` |
| Pacientes | `PACIENTE`, `RECEPCIONISTA`, `OPERACIONES` | `RECEPCIONISTA`, `OPERACIONES` (DELETE solo `OPERACIONES`) |
| Responsables | `PACIENTE`, `RECEPCIONISTA`, `OPERACIONES` | `RECEPCIONISTA`, `OPERACIONES` (DELETE solo `OPERACIONES`) |
| Doctores | `DOCTOR`, `PACIENTE`, `RECEPCIONISTA`, `OPERACIONES` | `OPERACIONES` |
| Especialidades | Público | `OPERACIONES` |
| Consultorios | Público | `OPERACIONES` |
| Horarios | `DOCTOR`, `PACIENTE`, `RECEPCIONISTA`, `OPERACIONES` | `DOCTOR`, `OPERACIONES` |
| Historias clínicas | `DOCTOR`, `RECEPCIONISTA`, `OPERACIONES` (paciente con endpoint dedicado) | `DOCTOR`, `OPERACIONES` |
| Citas | `DOCTOR`, `PACIENTE`, `RECEPCIONISTA`, `OPERACIONES` | `DOCTOR` (Estado), `RECEPCIONISTA`, `OPERACIONES` |
| Pagos | `DOCTOR`, `RECEPCIONISTA`, `OPERACIONES` | `RECEPCIONISTA`, `OPERACIONES` |
| Documentos fiscales | `RECEPCIONISTA`, `OPERACIONES` | `OPERACIONES` |

---

## 14. Errores comunes

| Código | Situación | Acción sugerida |
| --- | --- | --- |
| 400 | Validaciones, datos faltantes, formatos incorrectos | Mostrar mensaje textual y resaltar campos. |
| 401 | Token ausente/expirado | Forzar logout y redirigir a login. |
| 403 | Rol insuficiente | Mostrar aviso "No tienes permisos". |
| 404 | Recurso no encontrado | Mostrar CTA para volver al listado. |
| 409 | Conflicto (duplicados, dependencias) | Mostrar detalle y sugerir acción previa. |
| 500 | Error inesperado | Mostrar mensaje genérico y registrar en monitoring. |

---

## 15. Roadmap

- Próximos módulos (diagnósticos detallados, registro de documentos adjuntos) llegarán como `/api/v2`.
- Tests legacy con `Hospital`, `SedeHospital`, `CitaMedica` aún no reflejan esta versión.
- Notificar al equipo backend ante inconsistencias enviando request/response al correo `backend@misalud.com`.

> Última actualización: 22 Nov 2025. Mantener copia sincronizada en Confluence (Integraciones / MiSalud API).
