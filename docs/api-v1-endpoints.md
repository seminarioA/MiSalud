# Plataforma MiSalud · API REST v1 (Referencia para Front-End)

> Documento técnico detallado orientado a ingenieros front-end. No se asume conocimiento del back-end; cada endpoint incluye: ruta, método, cabeceras necesarias (ej. JWT), permisos/roles, cuerpo de request con validaciones, ejemplos de request/response (incluyendo PUT/DELETE), códigos HTTP esperados, errores comunes y casos borde.

Todas las rutas están versionadas bajo `/api/v1`.

---

## Convenciones globales

- Content-Type: application/json; charset=UTF-8
- Fecha: `YYYY-MM-DD` (ISO date)
- Hora: `HH:mm:ss` (24h)
- Timestamp: `YYYY-MM-DD'T'HH:mm:ss` (ISO datetime)
- Autenticación: header `Authorization: Bearer <token>` para rutas protegidas.
- Roles (constantes backend): `PACIENTE`, `DOCTOR`, `RECEPCIONISTA`, `OPERACIONES`.
- Formato de errores estándar (JSON):

  {
    "status": 400,
    "error": "Bad Request",
    "message": "Detalle legible del error",
    "timestamp": "2025-11-22T18:10:04"
  }

- Códigos HTTP principales:
  - 200 OK: request exitoso (GET/PUT)
  - 201 Created: recurso creado (POST)
  - 204 No Content: eliminación correcta (DELETE)
  - 400 Bad Request: validación fallida
  - 401 Unauthorized: token ausente/ inválido
  - 403 Forbidden: token válido pero rol insuficiente
  - 404 Not Found: recurso no existe
  - 409 Conflict: entidad duplicada (email, número documento, etc.)
  - 500 Internal Server Error: fallo inesperado

---

## JWT: cómo funciona en esta API

- Obtener token: POST `/api/v1/auth/login` (email + password) devuelve `token` y `user`.
- El token es JWT firmado HMAC con la clave en `application.properties` (`jwt.secret`).
- Claims incluidos:
  - `sub` (subject): email del usuario.
  - `iat`, `exp` estándar.
  - claim custom `rol`: string con el rol principal del usuario (ej. `PACIENTE`).
- Expiración por defecto: 86400000 ms (24 horas) salvo que se configure en propiedades.
- En cada petición protegida enviad: `Authorization: Bearer <token>`.
- El filtro `JwtAuthenticationFilter` valida token, extrae `rol` de los claims y crea autoridad `ROLE_{ROL}` en el contexto de seguridad.

Casos a tener en cuenta:
- Si el token es válido pero expirado -> 401.
- Si el token carece del claim `rol` -> el filtro usará las autoridades del UserDetails (si las tiene).

---

# Endpoints (extenso, con ejemplos concretos)

NOTA: en cada sección incluyo ejemplos explícitos para POST, PUT y DELETE (cuando aplicable), además de casos borde y respuestas de error.

---

## 1) Autenticación y usuarios del sistema

### 1.1 POST /api/v1/auth/login
- Propósito: obtener JWT
- Roles: público (sin token)
- Headers: Content-Type: application/json
- Request body (validaciones: email válido, password no vacío):

```json
{
  "email": "usuario@misalud.com",
  "password": "Secreto123"
}
```

- Response 200 (ejemplo):
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

- Errores:
  - 401: credenciales inválidas -> {status:401, error:"Unauthorized", message:"Credenciales inválidas"}
  - 400: payload inválido (email faltante / password vacía)

---

### 1.2 GET /api/v1/auth/me
- Propósito: obtener datos del usuario autenticado
- Roles: cualquier usuario autenticado
- Headers: Authorization: Bearer <token>

Response 200 (ejemplo):
```json
{
  "id": 12,
  "email": "usuario@misalud.com",
  "rol": "DOCTOR"
}
```

- Errores: 401 token inválido/expirado

---

### 1.3 CRUD Usuarios del sistema
- Rutas: `/api/v1/auth/usuarios` (GET, POST), `/api/v1/auth/usuarios/{id}` (PUT, DELETE)
- Reglas clave:
  - GET `/usuarios` requiere rol `OPERACIONES`.
  - POST `/usuarios` ahora es público: cualquiera puede registrarse. Importante: si el request intenta crear un usuario con rol `OPERACIONES`, el backend solo permitirá dicha asignación si quien hace la petición tiene rol `OPERACIONES` (ver comportamiento en service). Si no está autenticado o no tiene ese rol, el nuevo usuario será creado con rol `PACIENTE` por defecto.
  - PUT y DELETE requieren rol `OPERACIONES`.

Campos de `UsuarioSistemaRequest` (request):
```json
{
  "persona": {
    "primerNombre": "Lucía",
    "segundoNombre": "Opcional",
    "primerApellido": "Flores",
    "segundoApellido": "Opcional",
    "tipoDocumento": "DNI",            // ENUM: DNI, CE, PASAPORTE, PTP, RUC
    "numeroDocumento": "87654321",
    "fechaNacimiento": "1994-08-21",
    "genero": "FEMENINO",            // ENUM: MASCULINO, FEMENINO, OTRO
    "numeroTelefono": "999888777",
    "urlFotoPerfil": "https://..."
  },
  "email": "lucia.flores@misalud.com",
  "password": "Temporal123",
  "rol": "RECEPCIONISTA"           // OPCIONAL para llamados públicos: se ignorará y se asignará PACIENTE
}
```

- POST /api/v1/auth/usuarios (registro público)
  - Si el caller NO está autenticado o NO tiene ROLE_OPERACIONES:
    - El backend creará el usuario con `rol` = `PACIENTE` independientemente de lo que envíe el request.
  - Si el caller tiene ROLE_OPERACIONES, podrá asignar el rol especificado en el request.

- Response 201 (ejemplo cuando crea correctamente):
```json
{
  "id": 44,
  "persona": {
    "id": 91,
    "primerNombre": "Lucía",
    "segundoNombre": "Opcional",
    "primerApellido": "Flores",
    "segundoApellido": "Opcional",
    "tipoDocumento": "DNI",
    "numeroDocumento": "87654321",
    "fechaNacimiento": "1994-08-21",
    "genero": "FEMENINO",
    "numeroTelefono": "999888777",
    "urlFotoPerfil": "https://..."
  },
  "email": "lucia.flores@misalud.com",
  "rol": "PACIENTE"
}
```

- Errores frecuentes:
  - 400: validación de campos (email inválido, password vacía, faltan campos requeridos en persona)
  - 409: email ya registrado

- PUT /api/v1/auth/usuarios/{id} (solo para ROLE_OPERACIONES)
  - Request: igual que POST
  - Response 200: objeto actualizado
  - Errores: 404 id no existe, 403 si caller sin ROLE_OPERACIONES intenta acceder, 409 email duplicado

- DELETE /api/v1/auth/usuarios/{id}
  - Response 204 No Content
  - Errores: 404, 403

---

## 2) Personas

Rutas: `/api/v1/personas` y `/api/v1/personas/{id}`
Roles principales: `RECEPCIONISTA`, `OPERACIONES` (GET lista), otros endpoints protegidos según controlador.

- Ejemplo POST `/api/v1/personas` (crear persona aislada):
Request:
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

Response 201:
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

Errors:
- 400: fechaNacimiento en futuro
- 409: numeroDocumento duplicado
- 403: si recurso restringido

PUT and DELETE examples are straightforward: include full persona object for PUT; DELETE returns 204.

---

## 3) Pacientes

Rutas: `/api/v1/pacientes`, `/api/v1/pacientes/{id}`
Roles:
- GET list: PACIENTE (su propio recurso) | RECEPCIONISTA | OPERACIONES
- POST crear paciente: RECEPCIONISTA | OPERACIONES
- PUT/DELETE: OPERACIONES

Request POST `/api/v1/pacientes` (crear paciente vinculando persona):
```json
{
  "persona": { /* mismo esquema que persona */ },
  "tipoSeguro": "ESSALUD"  // ENUM: SIS, ESSALUD, EPS, PARTICULAR, SOAT, SCTR, OTRO
}
```

Response 201 (ejemplo):
```json
{
  "id": 22,
  "persona": { "id": 205, /* ...datos completos... */ },
  "tipoSeguro": "ESSALUD"
}
```

PUT `/api/v1/pacientes/{id}` ejemplo (actualizar): enviar la estructura completa; response 200 con objeto actualizado.

DELETE `/api/v1/pacientes/{id}`: 204; 409 si tiene citas activas.

---

## 4) Responsables de pacientes

Rutas: `/api/v1/pacientes/{pacienteId}/responsables`
- POST ejemplo (add responsable):
Request:
```json
{
  "pacienteId": 22,
  "personaResponsableId": 41,
  "relacion": "Madre"
}
```

Response 201:
```json
{
  "id": 11,
  "pacienteId": 22,
  "personaResponsableId": 41,
  "relacion": "Madre"
}
```

PUT `/.../{id}` ejemplo (actualizar relacion): enviar el mismo payload con nueva relacion; response 200
DELETE -> 204, 409 si es el único contacto permitido.

---

## 5) Doctores

Rutas: `/api/v1/doctores`, `/api/v1/doctores/{id}`
Roles:
- GET listado: DOCTOR (su recurso) | OPERACIONES
- POST crear: OPERACIONES
- PUT: OPERACIONES
- DELETE: OPERACIONES

POST ejemplo (crear doctor):
```json
{
  "persona": { /* persona */ },
  "numeroColegiatura": "CMP123456",
  "especialidadIds": [1, 4]
}
```

Response 201:
```json
{
  "id": 8,
  "persona": { "id": 301, /* ... */ },
  "numeroColegiatura": "CMP123456",
  "especialidadIds": [1,4]
}
```

Errores comunes:
- 400: falta de especialidades al crear
- 409: numeroColegiatura duplicado
- 409: si tiene horarios/citas al eliminar

---

## 6) Especialidades

Rutas: `/api/v1/especialidades`
- GET público: devuelve listado de especialidades

Response 200 ejemplo:
```json
[
  { "id": 1, "nombre": "Medicina General" },
  { "id": 2, "nombre": "Cardiología" }
]
```

POST/PUT/DELETE: protegidos por ROLE_OPERACIONES (crear/editar/eliminar especialidades).

---

## 7) Consultorios

Rutas: `/api/v1/consultorios`
- GET público: listar consultorios
- POST/PUT/DELETE: ROLE_OPERACIONES

POST ejemplo:
```json
{ "nombreONumero": "Consultorio 101" }
```

Response 201:
```json
{ "id": 1, "nombreONumero": "Consultorio 101" }
```

Errores: 409 si nombre duplicado.

---

## 8) Horarios médicos

Rutas: `/api/v1/horarios-medicos` (según implementación)
Roles: DOCTOR para su propio horario, OPERACIONES para administración

Request POST ejemplo:
```json
{
  "idDoctor": 8,
  "diaSemana": "LUNES",                // ENUM: LUNES..DOMINGO
  "horaInicio": "08:00:00",
  "horaFin": "12:00:00",
  "esVacaciones": false
}
```

Response 201: objeto horario con `idHorario`.

Casos borde:
- horaInicio >= horaFin -> 400
- solapamiento con otro horario del mismo doctor -> 409

---

## 9) Citas

Rutas: `/api/v1/citas`, `/api/v1/citas/{id}`
Roles:
- Crear: PACIENTE (si es su cita) | RECEPCIONISTA | OPERACIONES
- Ver: PACIENTE (solo sus citas) | DOCTOR (sus citas) | RECEPCIONISTA | OPERACIONES
- Editar/Cambiar estado: RECEPCIONISTA | OPERACIONES | DOCTOR (confirmar/actualizar estado)

POST crear cita ejemplo:
```json
{
  "idPaciente": 22,
  "idDoctor": 8,
  "idConsultorio": 1,
  "fechaCita": "2025-12-01",
  "horaCita": "09:30:00",
  "duracionMinutos": 30,
  "tipoAtencion": "PRESENCIAL",    // ENUM: PRESENCIAL, TELECONSULTA, DOMICILIARIA
  "precioBase": 100.00,
  "montoDescuento": 20.00,
  "costoNetoCita": 80.00
}
```

Response 201:
```json
{
  "idCita": 55,
  "idPaciente": 22,
  "idDoctor": 8,
  "idConsultorio": 1,
  "fechaCita": "2025-12-01",
  "horaCita": "09:30:00",
  "duracionMinutos": 30,
  "estado": "PENDIENTE",
  "tipoAtencion": "PRESENCIAL",
  "fechaImpresion": null,
  "precioBase": 100.00,
  "montoDescuento": 20.00,
  "costoNetoCita": 80.00
}
```

PUT ejemplo (modificar fecha/hora/estado): enviar objeto completo; response 200. Casos borde:
- Cita solapada con otra cita del doctor -> 409
- Fecha pasada -> 400
- Duración negativa -> 400

DELETE `/api/v1/citas/{id}`: 204; 409 si ya está completada y no puede eliminarse según reglas de negocio.

---

## 10) Registro de citas médicas, historias clínicas y diagnósticos

- POST registro médico (vinculado a `registro_citas_medicas`): crear notas médicas vinculadas a la `historia_clinica`.
- POST diagnóstico: asociado a registro (campo `codigo_icd10` requiere formato alfanumérico corto).

Ejemplos:
POST /api/v1/registro-citas
```json
{
  "idHistoriaClinica": 12,
  "idCita": 55,
  "notasMedicas": "Paciente con dolor torácico..."
}
```

Response 201: registro creado con id.

POST /api/v1/diagnosticos
```json
{
  "idRegistro": 33,
  "codigoIcd10": "I20",
  "descripcion": "Angina de pecho"
}
```

Response 201: diagnóstico creado.

Errores:
- 400 código inválido, 404 historia/registros no encontrados.

---

## 11) Pagos y documentos fiscales

Rutas: `/api/v1/pagos`, `/api/v1/documentos-fiscales`

- POST pago (ejemplo):
```json
{
  "idCita": 55,
  "monto": 80.00,
  "tipoTransaccion": "INGRESO",  // ENUM: INGRESO, EGRESO
  "estadoPago": "COMPLETADO",    // DEFAULT: COMPLETADO
  "metodoPago": "YAPE",          // ENUM con valores listados en DB
  "codigoOperacion": "YAPE-TRX-123"
}
```

Response 201:
```json
{
  "idPago": 101,
  "idCita": 55,
  "monto": 80.00,
  "tipoTransaccion": "INGRESO",
  "estadoPago": "COMPLETADO",
  "metodoPago": "YAPE",
  "codigoOperacion": "YAPE-TRX-123",
  "fechaPago": "2025-11-22T18:10:04"
}
```

- POST documento fiscal (emitir boleta/factura):
```json
{
  "idPago": 101,
  "tipoComprobante": "BOLETA",  // ENUM: BOLETA, FACTURA
  "serie": "F001",
  "correlativo": 12345,
  "rucCliente": "20123456789",
  "nombreClienteFiscal": "Juan Pérez"
}
```

Response 201: documento fiscal creado.

Errores:
- 409 serie+correlativo duplicado
- 404 pago no encontrado

---

## 12) Mensajes comunes / Respuestas HTTP ejemplos

- 401 Unauthorized: {
  "status":401, "error":"Unauthorized", "message":"Token inválido o expirado", "timestamp":"..."
}
- 403 Forbidden: {
  "status":403, "error":"Forbidden", "message":"No tiene permisos para acceder a este recurso", "timestamp":"..."
}
- 404 Not Found: {
  "status":404, "error":"Not Found", "message":"Recurso no encontrado", "timestamp":"..."
}

---

## Ejemplos end-to-end (flujo típico front-end)

1) Registro de usuario desde app móvil (sin rol OPERACIONES):
- POST /api/v1/auth/usuarios con body (email,password,persona,rol: RECEPT) pero backend creará rol PACIENTE si el caller no es OPERACIONES.
- Respuesta 201 con datos del usuario.
- Cliente hace POST /api/v1/auth/login con email/password y obtiene token.
- Cliente guarda token y lo envía en `Authorization` para llamadas siguientes.

2) Recepcionista crea paciente y agenda cita:
- Recepcionista (con token ROLE_RECEPCIONISTA) POST /api/v1/personas -> 201
- POST /api/v1/pacientes con personaId o persona en body -> 201
- POST /api/v1/citas -> 201

---

## Notas técnicas y advertencias para front-end engineers

- Siempre validar mensajes de error del servidor (409) y mostrarlos de forma legible.
- No dependen del orden de campos en respuestas JSON.
- Para PUT requests siempre enviar el objeto completo según contrato (no partial PATCH en este API salvo endpoints explícitos).
- El claim `rol` en JWT determina las autorizaciones; si el front-end necesita mostrar o esconder opciones, confíen en el token pero tengan en cuenta que el servidor es la fuente de verdad.
- En subida/edición de `urlFotoPerfil` el backend espera una URL (no envíes archivos binarios a ese campo); subir imágenes se hace por un endpoint de archivos (no documentado aquí) o por CDN externo.

---

Si queréis, puedo generar un archivo Postman/Insomnia collection con ejemplos y variables de entorno (baseUrl, token) o un OpenAPI/Swagger básico con todos los contratos aquí descritos.
