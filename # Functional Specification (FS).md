# Functional Specification (FS)

## 1. General Information

### 1.1 Feature Name
Gestión de Vacunación de Ganado

### 1.2 Description
Sistema que permite a un usuario gestionar la vacunación de su ganado, facilitando el registro de animales, vacunas, aplicaciones realizadas y la consulta de próximas vacunas. El objetivo es ayudar a evitar olvidos y mantener trazabilidad básica.

### 1.3 Scope
**Includes:**
- Registro de animales
- Registro de vacunas
- Registro de aplicación de vacunas
- Consulta de próximas vacunas

**Excludes:**
- Autenticación de usuarios
- Edición o eliminación de datos
- Notificaciones
- Funcionamiento offline
- Reportes avanzados

---

## 2. Objectives

- Permitir control básico de vacunación de ganado
- Evitar olvidos en aplicaciones de vacunas
- Tener visibilidad de próximas vacunas
- Construir una base para futuras funcionalidades

---

## 3. Functional Requirements

- FR-01: El sistema debe permitir registrar un animal
- FR-02: El sistema debe permitir listar animales
- FR-03: El sistema debe permitir registrar una vacuna
- FR-04: El sistema debe permitir aplicar una vacuna a un animal
- FR-05: El sistema debe calcular automáticamente la próxima fecha de vacunación
- FR-06: El sistema debe permitir consultar próximas vacunas

---

## 4. Business Rules

- BR-01: Un animal debe tener un nombre válido (no vacío)
- BR-02: La fecha de nacimiento no puede ser futura
- BR-03: Una vacuna debe tener frecuencia mayor a 0
- BR-04: La fecha de aplicación es obligatoria
- BR-05: La fecha próxima se calcula automáticamente
- BR-06: No se permite aplicar una vacuna a un animal inexistente
- BR-07: No se permite aplicar una vacuna inexistente

---

## 5. User Flows

### 5.1 Main Flow (Happy Path)
1. El usuario registra una vacuna
2. El usuario registra un animal
3. El usuario aplica una vacuna al animal
4. El sistema calcula la próxima fecha
5. El usuario consulta próximas vacunas

---

### 5.2 Alternative Flows
- Scenario 1: El usuario intenta aplicar vacuna con datos inválidos → el sistema rechaza la operación
- Scenario 2: No existen datos registrados → el sistema muestra listas vacías

---

## 6. Edge Cases

- Aplicar vacuna a animal inexistente
- Aplicar vacuna inexistente
- Frecuencia de vacuna igual o menor a cero
- Fecha de aplicación inválida
- Múltiples aplicaciones en el mismo día
- Lista de datos vacía

---

## 7. Inputs & Outputs

### Inputs

| Field | Description | Required |
|------|------------|----------|
| nombre | Nombre del animal | Sí |
| tipo | Tipo de animal | Sí |
| fechaNacimiento | Fecha de nacimiento | Sí |
| nombreVacuna | Nombre de la vacuna | Sí |
| frecuenciaDias | Frecuencia de aplicación | Sí |
| animalId | Identificador del animal | Sí |
| vacunaId | Identificador de la vacuna | Sí |
| fechaAplicacion | Fecha de aplicación | Sí |

---

### Outputs

| Field | Description |
|------|------------|
| id | Identificador |
| nombre | Nombre del animal o vacuna |
| tipo | Tipo de animal |
| fechaNacimiento | Fecha de nacimiento |
| fechaAplicacion | Fecha aplicada |
| fechaProxima | Fecha calculada |
| vacuna | Nombre de vacuna |
| animalNombre | Nombre del animal |

---

## 8. Validations

| Field | Rule | Error Message |
|------|------|--------------|
| nombre | No vacío | Nombre requerido |
| fechaNacimiento | No futura | Fecha inválida |
| frecuenciaDias | > 0 | Frecuencia inválida |
| fechaAplicacion | Obligatoria | Fecha requerida |
| animalId | Debe existir | Animal no encontrado |
| vacunaId | Debe existir | Vacuna no encontrada |

---

## 9. Error Handling

| Scenario | Message |
|---------|--------|
| Animal no existe | Animal no encontrado |
| Vacuna no existe | Vacuna no encontrada |
| Datos inválidos | Datos inválidos |
| Error interno | Error inesperado |

---

## 10. Non-Functional Considerations

- Performance: respuesta rápida para operaciones básicas
- Security: no aplica en MVP (sin autenticación)
- Usabilidad: interfaz simple y clara
- Escalabilidad: preparado para futuras mejoras

---

## 11. Acceptance Criteria (BDD)

Scenario: Aplicar vacuna correctamente  
Given existe un animal y una vacuna  
When el usuario registra la aplicación  
Then el sistema guarda la aplicación y calcula la próxima fecha  

---

## 12. Testing Considerations

- Flujo completo: crear → aplicar → consultar
- Validación de reglas de negocio
- Casos negativos (datos inválidos)
- Edge cases definidos

---

## 13. Dependencies

- Datos previamente registrados (animales y vacunas)

---

## 14. Assumptions & Constraints

- Sistema usado por un solo usuario
- Conexión a internet disponible
- No se requiere sincronización offline

---

## 15. Open Questions

- ¿Se permitirá edición de datos en el futuro?
- ¿Se incluirán notificaciones?
- ¿Se manejarán múltiples usuarios?
- ¿Se requerirá soporte offline?