# Technical Specification Document (TSD)
**Project:** Gestión de Vacunación de Ganado

---

## 1. Resumen Técnico
La solución consiste en la implementación de una API RESTful backend diseñada para gestionar la vacunación de ganado. Su principal objetivo técnico es proveer un sistema robusto, escalable y mantenible para el registro de animales, catálogo de vacunas y el control de aplicaciones, calculando automáticamente las próximas fechas de vencimiento.
El alcance abarca la creación y consulta de datos (operaciones Create y Read) sin incluir autenticación, roles, edición/eliminación de registros ni soporte offline, cumpliendo estrictamente con el MVP definido en el FSD.

---

## 2. Arquitectura
Se utilizará una **Arquitectura Hexagonal (Ports and Adapters)** para aislar la lógica de negocio (dominio) de las tecnologías de infraestructura (base de datos, frameworks web), facilitando así las pruebas y la evolución del sistema.

**Diagrama Lógico (Descripción):**
*   **Domain Layer (Core):** Contiene las entidades puras de negocio (`Animal`, `Vaccine`, `VaccinationRecord`) y sus reglas asociadas (cálculo de fechas). No tiene dependencias externas.
*   **Application Layer (Ports):** Orquesta la lógica de negocio mediante casos de uso (`Use Cases` como interfaces *Inbound*) y define los contratos que la infraestructura debe implementar (repositorios como interfaces *Outbound*).
*   **Infrastructure Layer (Adapters):** 
    *   *Inbound Adapters:* Controladores REST (`Controllers`) que reciben peticiones HTTP y llaman a los Use Cases.
    *   *Outbound Adapters:* Implementaciones de repositorios usando Spring Data JPA que interactúan con la base de datos, además de clientes externos o servicios de mensajería si hubiesen en el futuro.

**Decisiones Técnicas Clave y Justificación:**
*   **Java 17/21 + Spring Boot 3:** Estándar de la industria, provee un ecosistema maduro para desarrollo rápido de APIs.
*   **Arquitectura Hexagonal:** Evita el acoplamiento temprano a la base de datos y permite testear la lógica de negocio en aislamiento.
*   **PostgreSQL:** Base de datos relacional sólida, ideal para transacciones y relaciones estructuradas (Animal -> Vacuna).

**Estructura de Carpetas Sugerida:**
```text
src/main/java/com/gestionganado/
├── domain/
│   ├── model/          # Entities and Value Objects
│   ├── exception/      # Domain specific exceptions
│   └── port/
│       ├── in/         # Use Case interfaces
│       └── out/        # Repository interfaces
├── application/
│   └── service/        # Use Case implementations
└── infrastructure/
    ├── adapter/
    │   ├── in/web/          # REST Controllers, DTOs, Mappers
    │   └── out/persistence/ # JPA Entities, Spring Data Repositories
    └── config/         # Spring Beans, Error Handlers
```

---

## 3. Diseño de Componentes

### 3.1. WebAdapter (Controladores REST)
*   **Name:** `AnimalController`, `VaccineController`, `VaccinationController`
*   **Responsabilidad:** Exponer los endpoints HTTP, validar la sintaxis de las peticiones de entrada (JSR-380), y formatear la salida (DTOs).
*   **Interfaces:** Recibe HTTP Requests (JSON) y retorna HTTP Responses (JSON).
*   **Dependencias:** Depende exclusivamente de los puertos de entrada (`Use Cases` de la Application Layer).

### 3.2. ApplicationService (Casos de Uso)
*   **Name:** `AnimalService`, `VaccineService`, `VaccinationService`
*   **Responsabilidad:** Orquestar el flujo de la información. Por ejemplo, en la vacunación: buscar el animal, buscar la vacuna, invocar la lógica de dominio para calcular la fecha y finalmente persistir.
*   **Interfaces:** Implementa interfaces como `ApplyVaccineUseCase`, `GetUpcomingVaccinationsUseCase`.
*   **Dependencias:** Depende de las entidades de dominio y de los puertos de salida (`RepositoryPort`).

### 3.3. PersistenceAdapter
*   **Name:** `AnimalJpaAdapter`, `VaccineJpaAdapter`, `VaccinationRecordJpaAdapter`
*   **Responsabilidad:** Traducir las peticiones del dominio a consultas SQL. Mapear entre Entidades de Dominio y Entidades JPA.
*   **Interfaces:** Implementa los puertos de salida (`AnimalRepositoryPort`, etc.).
*   **Dependencias:** Spring Data JPA, `EntityManager`.

---

## 4. Modelo de Datos

**Consideraciones de Persistencia:** Usaremos Flyway o Liquibase para el versionamiento de la base de datos. Los IDs serán de tipo UUID para mayor seguridad e idoneidad en sistemas distribuidos.

### Esquema de Base de Datos

**Tabla: `animals`**
| Campo | Tipo | Restricciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | UUID | PRIMARY KEY | Identificador único |
| `name` | VARCHAR(100) | NOT NULL | Nombre del animal |
| `type` | VARCHAR(50) | NOT NULL | Tipo/Raza |
| `birth_date` | DATE | NOT NULL | Fecha de nacimiento, `<= CURRENT_DATE` |
| `created_at` | TIMESTAMP | DEFAULT NOW() | Auditoría |

**Tabla: `vaccines`**
| Campo | Tipo | Restricciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | UUID | PRIMARY KEY | Identificador único |
| `name` | VARCHAR(100) | NOT NULL, UNIQUE | Nombre de la vacuna |
| `frequency_days` | INTEGER | NOT NULL, CHECK > 0 | Días hasta próxima dosis |
| `created_at` | TIMESTAMP | DEFAULT NOW() | Auditoría |

**Tabla: `vaccination_records`**
| Campo | Tipo | Restricciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | UUID | PRIMARY KEY | Identificador único |
| `animal_id` | UUID | NOT NULL, FK (animals) | Animal vacunado |
| `vaccine_id` | UUID | NOT NULL, FK (vaccines)| Vacuna aplicada |
| `application_date`| DATE | NOT NULL | Fecha de la aplicación |
| `next_due_date` | DATE | NOT NULL | Fecha calculada (`application_date` + `frequency_days`) |
| `created_at` | TIMESTAMP | DEFAULT NOW() | Auditoría |

---

## 5. APIs / Contratos

Todos los endpoints usarán el prefijo `/api/v1`.

### 5.1. Registrar Animal
*   **Method / URL:** `POST /api/v1/animals`
*   **Request payload:**
    ```json
    {
      "name": "Bessie",
      "type": "Vaca",
      "birthDate": "2020-05-15"
    }
    ```
*   **Response payload (201 Created):**
    ```json
    {
      "id": "e21b7a2d-1234-4b5c-8d9e-1a2b3c4d5e6f",
      "name": "Bessie",
      "type": "Vaca",
      "birthDate": "2020-05-15"
    }
    ```
*   **Validaciones:** `name` no vacío; `birthDate` pasada o presente.

### 5.2. Registrar Vacuna
*   **Method / URL:** `POST /api/v1/vaccines`
*   **Request payload:**
    ```json
    {
      "name": "Aftosa",
      "frequencyDays": 180
    }
    ```
*   **Response payload (201 Created):** Similar al Request pero con `id`.
*   **Validaciones:** `name` no vacío; `frequencyDays` > 0.

### 5.3. Aplicar Vacuna
*   **Method / URL:** `POST /api/v1/vaccinations`
*   **Request payload:**
    ```json
    {
      "animalId": "e21b7a2d...",
      "vaccineId": "f32c8b3e...",
      "applicationDate": "2023-10-01"
    }
    ```
*   **Response payload (201 Created):**
    ```json
    {
      "id": "a1b2c3d4...",
      "animalName": "Bessie",
      "vaccineName": "Aftosa",
      "applicationDate": "2023-10-01",
      "nextDueDate": "2024-03-29"
    }
    ```
*   **Códigos de Error:** 
    *   `404 Not Found`: Animal o Vacuna no existen.
    *   `400 Bad Request`: Validaciones fallidas.

### 5.4. Consultar Próximas Vacunas
*   **Method / URL:** `GET /api/v1/vaccinations/upcoming?days=30`
*   **Response payload (200 OK):**
    ```json
    [
      {
        "id": "a1b2c3d4...",
        "animalNombre": "Bessie",
        "vacuna": "Aftosa",
        "nextDueDate": "2024-03-29"
      }
    ]
    ```

---

## 6. Lógica de Negocio

### Flujo Principal: Aplicación de Vacuna
1.  **Validación de entrada:** El `VaccinationController` asegura que los campos requeridos estén presentes.
2.  **Verificación de existencia:** El `VaccinationService` consulta al `AnimalRepositoryPort` y `VaccineRepositoryPort`. Si alguno no existe, lanza `ResourceNotFoundException`.
3.  **Prevención de duplicidad (Edge Case):** Se verifica si ya existe un registro idéntico (`animalId`, `vaccineId`, `applicationDate`) para evitar aplicaciones múltiples accidentales el mismo día. Lanza `DuplicateVaccinationException`.
4.  **Cálculo:** El dominio (`VaccinationRecord` entity) calcula `nextDueDate` sumando `frequencyDays` de la vacuna a `applicationDate`.
5.  **Persistencia y Retorno:** Se guarda mediante `VaccinationRecordRepositoryPort` y se retorna el DTO.

### Manejo de Errores
Se implementará un `@ControllerAdvice` global siguiendo el estándar **RFC 7807 (Problem Details for HTTP APIs)**.
*   **Ejemplo de Error (Animal no encontrado - 404):**
    ```json
    {
      "type": "about:blank",
      "title": "Not Found",
      "status": 404,
      "detail": "Animal with id e21b7a2d... not found"
    }
    ```

---

## 7. Consideraciones Técnicas (Java)

*   **Framework:** Spring Boot 3.x.
*   **Patrones de Diseño:** 
    *   *Data Transfer Object (DTO)* para desacoplar modelos web de entidades de BD.
    *   *Mapper Pattern* (via MapStruct) para transformar entre Entidades y DTOs eficientemente.
    *   *Dependency Injection* administrado por el contenedor de Spring.
*   **Manejo de Excepciones:** Centralizado con `@RestControllerAdvice` y la clase `ProblemDetail` nativa de Spring Boot 3.
*   **Logging:** Uso de SLF4J con Logback. Los `Controllers` loguearán peticiones entrantes (nivel INFO) y el `ControllerAdvice` logueará excepciones (WARN para errores de cliente, ERROR para fallas de servidor).
*   **Seguridad:** En el MVP los endpoints estarán abiertos. Se configurará CORS globalmente mediante un `WebMvcConfigurer` para permitir peticiones desde el frontend.
*   **Timezones:** Todas las fechas se manejarán y almacenarán en formato `LocalDate` (sin timezone) ya que la precisión horaria no es relevante para el dominio.

---

## 8. Estrategia de Testing (QA)

La estrategia seguirá la pirámide de testing:

### 8.1. Pruebas Unitarias (JUnit 5 + Mockito)
*   **Casos Funcionales:** Cálculo exacto de `nextDueDate` dado distintos `frequencyDays` (ej. años bisiestos, cambios de mes).
*   **Casos Negativos:** Instanciar animales con `birthDate` futura debe lanzar excepción de dominio.
*   **Cobertura esperada:** >85% en las capas `Domain` y `Application`.

### 8.2. Pruebas de Integración (Testcontainers + @SpringBootTest)
*   **Integración DB:** Verificar que el guardado y recuperación en PostgreSQL funcione mapeando correctamente las llaves foráneas.
*   **Consultas complejas:** El método que obtiene "Próximas Vacunas en X días" se probará poblando la DB de prueba y afirmando que el query retorna estrictamente los registros dentro del rango.

### 8.3. Criterios de Aceptación (API / End-to-End)
*   **Scenario:** Aplicar vacuna exitosamente.
    *   **Given:** Un Animal válido (`id=A`) y una Vacuna válida (`id=V` con freq=30) existen en la base de datos.
    *   **When:** El cliente hace un POST a `/api/v1/vaccinations` con `animalId=A`, `vaccineId=V` y `applicationDate="2023-01-01"`.
    *   **Then:** El response HTTP es `201 Created` y el cuerpo contiene `nextDueDate="2023-01-31"`.

---

## 9. Riesgos y Supuestos

*   **Supuestos del Sistema:**
    *   Se asume que la carga será moderada-baja, por lo que no se implementará caché o paginación compleja en la primera versión.
    *   Las fechas que se envían desde los clientes son confiables en cuanto a formato y sentido de negocio.
*   **Riesgos Técnicos:**
    *   *Problemas de Zona Horaria (Timezones):* Puesto que es una aplicación que trata con fechas absolutas, una desconfiguración en el cliente vs el servidor puede alterar visualmente la fecha. **Mitigación:** Estandarizar JSON con formato `yyyy-MM-dd` ignorando la hora en la serialización.
*   **Limitaciones:** La API no soporta sincronización de datos offline. Todas las peticiones requieren conexión directa.

---

## 10. Plan de Implementación

**Metodología:** Desarrollo iterativo.

| Prioridad | Tarea | Dependencias | Responsabilidad |
| :--- | :--- | :--- | :--- |
| **Alta** | **P1: Setup del Proyecto**<br>Inicializar Spring Boot, configurar PostgreSQL (Docker Compose), estructura de paquetes y Flyway. | Ninguna | Tech Lead / Senior Dev |
| **Alta** | **P2: Catálogos Base (Animal & Vacuna)**<br>Crear entidades, repositorios, servicios y endpoints (POST/GET) para Animales y Vacunas. | P1 | Senior Dev |
| **Crítica** | **P3: Lógica Core de Vacunación**<br>Endpoint de aplicación de vacunas, validaciones cruzadas (404 si no existen), cálculo de fechas (`nextDueDate`). | P2 | Senior Dev |
| **Media** | **P4: Consultas y Dashboard**<br>Implementar el endpoint `GET /upcoming` usando queries JPA / JPQL. | P3 | Senior Dev |
| **Media** | **P5: Testing y Refinamiento**<br>Implementar Tests de Integración, configurar Global Exception Handler y QA general. | P4 | QA Engineer / Senior Dev |

*El orden sugerido garantiza que las entidades independientes (Animal/Vacuna) existan antes de intentar relacionarlas en la funcionalidad core.*
