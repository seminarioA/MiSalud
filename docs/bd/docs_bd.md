# Especificación de Requerimientos de Software (SRS)

**Sistema de Gestión Médica – “Desarrollo Web Integrado”**
**Versión:** 1.0

---

## 1. Introducción

### 1.1 Propósito

El presente documento tiene como finalidad definir de forma estructurada los **requerimientos funcionales y no funcionales** del *Sistema de Gestión Médica*, diseñado para operar en una **única sede hospitalaria**, brindando funcionalidades de gestión de pacientes, doctores, citas médicas, facturación, procedimientos, auditoría y control de acceso jerárquico.

### 1.2 Alcance

El sistema permitirá:

* Registrar y administrar personas con múltiples roles (doctor, paciente, administrador, recepcionista).
* Gestionar citas médicas, facturación y pagos.
* Mantener trazabilidad de cambios en estados, tarifas y usuarios.
* Operar localmente sin conexión a servicios externos obligatorios.
* Garantizar seguridad, integridad transaccional y control de accesos basado en roles jerárquicos.

### 1.3 Definiciones, acrónimos y abreviaturas

| **Término** | **Definición**                                               |
| ----------- | ------------------------------------------------------------ |
| **SRS**     | Software Requirements Specification                          |
| **RBAC**    | Role-Based Access Control                                    |
| **ABAC**    | Attribute-Based Access Control                               |
| **CRUD**    | Create, Read, Update, Delete                                 |
| **Sede**    | Establecimiento médico físico donde se brindan los servicios |
| **Usuario** | Persona con acceso autenticado al sistema                    |

### 1.4 Referencias

* IEEE Std 830-1998: *IEEE Recommended Practice for Software Requirements Specifications*
* *PostgreSQL 17 Official Documentation (Render Cloud Instance)*
* *Guía de Diseño de Bases de Datos Relacionales* (C. Date)

### 1.5 Visión general del documento

El documento detalla los **requerimientos funcionales, no funcionales, restricciones del sistema, alcance y consideraciones de diseño**.
Cada requerimiento está organizado jerárquicamente por módulos.

---

## 2. Descripción General

### 2.1 Perspectiva del sistema

El sistema es una aplicación de gestión clínica local, operando sobre una única sede hospitalaria.
No depende de servicios externos obligatorios, pero puede integrarse con almacenamiento en la nube opcional.

### 2.2 Funcionalidad del sistema

El sistema cubre los siguientes módulos:

* Gestión de personas y roles
* Citas médicas
* Procedimientos y adjuntos clínicos
* Facturación y pagos
* Control de acceso y autenticación
* Auditoría modular y trazabilidad
* Administración jerárquica de permisos

### 2.3 Características del usuario

Los usuarios del sistema incluyen:

* Administradores (globales o de sede)
* Doctores (pueden ser también pacientes)
* Recepcionistas
* Pacientes
* Personal de facturación

Cada usuario puede tener uno o varios roles.

### 2.4 Restricciones

* El sistema operará en la nube sobre **PostgreSQL 17 (Render Cloud Instance)**.
* Una sola sede hospitalaria.
* No se permite la gestión de múltiples hospitales.
* No se implementan backups automáticos.
* No se registran logs de acceso a registros sensibles.

### 2.5 Suposiciones y dependencias

* Se asume disponibilidad de infraestructura local.
* Los administradores controlan manualmente la creación y desactivación de cuentas.
* No se requiere integración con sistemas externos de RRHH, contabilidad o mensajería.

---

## 3. Requerimientos Específicos

### 3.1 Requerimientos Funcionales

#### 3.1.1 Gestión de Personas y Roles

* Registro de personas con los atributos: nombres, apellidos, documento, dirección, teléfono, correo, fecha de nacimiento, género.
* Una persona puede tener múltiples roles: doctor, paciente, administrador o recepcionista.
* Un doctor o administrador también puede ser paciente.
* Registro del estado de actividad (`esta_activo`) por persona.
* Los roles se gestionan en tablas específicas:

| **Tabla**           | **Atributos adicionales**                                  |
| ------------------- | ---------------------------------------------------------- |
| `Role_Doctor`       | Licencia profesional, fecha de emisión                     |
| `Role_Patient`      | Número de historia, aseguradora, alergias, grupo sanguíneo |
| `Role_Admin`        | Nivel jerárquico (`GLOBAL`, `SEDE`)                        |
| `Role_Receptionist` | Puesto o cargo                                             |

* Asociación mediante `Person_Role` con control de vigencia (`effective_from`, `effective_to`).
* Eliminación lógica para personas y roles.
* Antes de desactivar un doctor, deben cancelarse sus citas futuras.
* Registro de historial de cambios en doctores (`Historial_Doctor`).

#### 3.1.2 Gestión de Citas Médicas

* Las citas médicas se registran en `Cita_Medica`.
* Requieren un doctor y paciente activos.
* Estados: `PROGRAMADA`, `EN_CURSO`, `CANCELADA`, `COMPLETADA`.
* No se permiten citas solapadas para un mismo doctor.
* Se permiten citas consecutivas (“back-to-back”).
* Distintos doctores pueden tener citas simultáneas.
* Cada cita puede asociarse a un consultorio único por sede.
* Confirmación de cita incluye:

  * Usuario que confirmó (`confirmado_por`).
  * Fecha y medio (`EN_PERSONA`, `TELEFONO`, `WHATSAPP`, `PORTAL_WEB`).
* Registro de motivos de cancelación y reprogramación.
* Las citas canceladas se mantienen con estado `CANCELADA`.
* Historial de estados (`Cita_Historial_Estado`).
* Campos de trazabilidad (`creado_por`, `modificado_por`).
* Observaciones del doctor y comentarios del paciente.
* Registro de inasistencias (“no-show”).
* Bloqueo automático por inasistencias consecutivas hasta reactivación manual.

#### 3.1.3 Procedimientos Médicos

* Los procedimientos pertenecen exclusivamente a una cita.
* Estados: `PENDIENTE`, `EN_CURSO`, `COMPLETADO`, `CANCELADO`.
* Adjuntos permitidos (imágenes, informes, resultados).
* Archivos locales o en la nube, con **versionado**.
* Recetas e informes se gestionan como adjuntos.
* Diagnósticos libres (no se usa CIE-10).
* Registro de descripción y costo de procedimientos.

#### 3.1.4 Alertas y Notas

* Registro de alertas médicas asociadas a pacientes.
* Tipos: `ALERGIA`, `CONDICION`, `RIESGO`, `OTRO`.
* Las alertas solo se muestran bajo consulta.
* Registro de notas internas (`Nota_Interna`) y administrativas (`Nota_Administrativa`).
* Las notas administrativas no expiran automáticamente.

#### 3.1.5 Disponibilidad y Horarios

* Los doctores deben tener horarios configurados (`Horario_Doctor`).
* Registro de ausencias o vacaciones (`Ausencia_Doctor`).
* Validación de citas contra disponibilidad y ausencias.
* Evitar solapamientos de uso de consultorios.

#### 3.1.6 Facturación y Pagos

* Cada cita puede generar una factura (`Facturacion`).
* Estados: `PENDIENTE`, `PAGADO`, `PARCIAL`, `REEMBOLSADO`.
* Registro de múltiples pagos (`Pago_Detalle`).
* Métodos: `EFECTIVO`, `TARJETA`, `TRANSFERENCIA`, `SEGURO`.
* Soporte de reembolsos.
* Historial de facturación y pagos (`Historial_Facturacion`, `Historial_Pago`).
* Registro de historial de tarifas (`Historial_Tarifa`).
* Las tarifas no se vinculan con facturas previas.

#### 3.1.7 Autenticación y Roles Jerárquicos

* Contraseñas encriptadas con **bcrypt**.
* Cambio de contraseña permitido.
* Bloqueo temporal tras N intentos fallidos.
* Estados: `ACTIVE`, `INACTIVE`, `LOCKED`.
* No hay desactivación automática por inactividad.
* Registro de trazabilidad de cuentas (creación, desactivación, motivo).
* Roles jerárquicos (`Sys_Role`) con herencia (`rol_superior`).
* Permisos personalizados (`Permiso_Usuario`), vigentes hasta revocación manual.
* Registro solo de último inicio de sesión (`last_login_at`).

#### 3.1.8 Evaluación y Retroalimentación

* Los pacientes pueden calificar citas (1–5 estrellas) y dejar comentarios.
* Calificación opcional.
* No se implementan encuestas extendidas.

#### 3.1.9 Auditoría

* Auditoría modular (no genérica).
* Registro de históricos por módulo:

| **Módulo**  | **Tabla de historial**  |
| ----------- | ----------------------- |
| Citas       | `Cita_Historial_Estado` |
| Facturación | `Historial_Facturacion` |
| Pagos       | `Historial_Pago`        |
| Doctores    | `Historial_Doctor`      |
| Tarifas     | `Historial_Tarifa`      |

* No se registra historial de accesos ni cambios en pacientes.

#### 3.1.10 Eliminación y Versionado

* Eliminación lógica en entidades principales.
* Versionado de adjuntos.
* Antes de desactivar un doctor, deben cancelarse sus citas futuras.

---

### 3.2 Requerimientos No Funcionales

#### 3.2.1 Plataforma y Persistencia

* Base de datos: **PostgreSQL 17 (Render Cloud Instance)**.
* Transacciones **ACID** en operaciones críticas.
* Índices en campos clave (`nombre`, `documento`, `email`, `fecha`).

#### 3.2.2 Seguridad

* Encriptación con **bcrypt**.
* Control de acceso por roles jerárquicos y permisos individuales.
* Desactivación y reactivación manual de usuarios.
* No se auditan accesos de lectura.

#### 3.2.3 Rendimiento

* Respuesta de consultas < 1 segundo en operaciones estándar.
* Uso de índices y caché en backend.

#### 3.2.4 Disponibilidad

* Uptime mínimo: **99.5%**.
* Sin dependencias críticas de red externas.

#### 3.2.5 Almacenamiento

* Archivos adjuntos locales o en la nube (configurable).
* No hay copias automáticas de respaldo.

---

## 5. Restricciones del Diseño

* Sistema monoinstalación local.
* Sin sincronización multi-sede.
* Sin integración con RRHH o mensajería.
* Sin auditoría de accesos sensibles.
* Backups y exportaciones se gestionan externamente.

---

## 6. Atributos de Calidad

| **Atributo**       | **Descripción**                                         |
| ------------------ | ------------------------------------------------------- |
| **Seguridad**      | Contraseñas encriptadas, control por rol                |
| **Integridad**     | Restricciones y claves foráneas en todas las relaciones |
| **Escalabilidad**  | Soporte modular sin romper compatibilidad               |
| **Mantenibilidad** | Diseño modular con auditoría separada por módulo        |
| **Usabilidad**     | Interfaz vía frontend responsivo                        |

---

## 7. Anexos

* Diagrama E-R (referencia del script base)
* Estructuras sugeridas para auditorías y tablas derivadas
* Consideraciones para exportaciones externas
