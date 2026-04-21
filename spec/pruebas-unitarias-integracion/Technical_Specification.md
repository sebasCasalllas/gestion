# Technical Specification (TS)

## 1. General Information

### 1.1 Feature Name
Cobertura de Pruebas Unitarias y de Integración con Mocks

### 1.2 Reference to Functional Spec
`Functional_Specification.md` ubicado en `spec/pruebas-unitarias-integracion/`

### 1.3 Technical Summary
Este documento describe la arquitectura y estrategia técnica para implementar una suite de pruebas automatizadas (unitarias y de integración) para el sistema de "Gestión Ganado" basado en Java y Spring Boot. Se utilizará un enfoque de simulación (Mocking) con `Mockito` para aislar la base de datos y otros servicios externos en las pruebas de integración, garantizando una ejecución rápida, determinista e independiente de la infraestructura. Adicionalmente, se implementarán pruebas parametrizadas para optimizar la cobertura de escenarios múltiples evitando la duplicación de código, con una meta estricta de alcanzar al menos el 80% de cobertura validada mediante `JaCoCo`.

---

## 2. Solution Architecture

### 2.1 High-Level Design
La arquitectura de pruebas respetará el diseño hexagonal de la aplicación base:
- **Pruebas Unitarias:** Se ejecutarán aislando completamente la capa de Dominio y Casos de Uso, sin involucrar el contexto de Spring (`ApplicationContext`). Los puertos de salida (ej. repositorios) serán mockeados mediante instanciación directa de mocks de Mockito.
- **Pruebas de Integración:** Validarán el flujo desde el adaptador de entrada (Controladores REST) hasta el adaptador de salida (Repositorios), utilizando el contexto de Spring parcial (`@WebMvcTest`) o total (`@SpringBootTest`). Para garantizar que no haya conexión a base de datos real, los beans de repositorios y clientes externos se reemplazarán en el contexto de Spring utilizando `@MockBean`.

### 2.2 Components
- **Test Runner & Framework:** JUnit 5 (Jupiter).
- **Mocking Library:** Mockito y Mockito Extension.
- **Integration Test Framework:** Spring Boot Test (`MockMvc` para endpoints, `@MockBean` para dependencias).
- **Code Coverage Tool:** JaCoCo plugin integrado en la herramienta de construcción (Maven/Gradle).
- **Assertion Library:** AssertJ y/o JUnit Assertions.

### 2.3 Design Decisions
- **Decision:** Uso estricto de Mocks (`@MockBean`) para la base de datos en las Pruebas de Integración.
  - **Justification:** Cumple con el requerimiento de aislar las pruebas de la infraestructura, previniendo latencia o caídas de DB y permitiendo simular escenarios complejos (timeouts, errores de integridad) de manera predecible.
- **Decision:** Prohibición del uso de condicionales (`if`, `switch`) dentro de los métodos de prueba.
  - **Justification:** Asegura que cada prueba posea una única responsabilidad y evalúe una única ruta de ejecución lineal, favoreciendo pruebas limpias, legibles y fáciles de depurar.
- **Decision:** Implementación de `@ParameterizedTest`.
  - **Justification:** Facilita la prueba de múltiples valores de entrada para el mismo flujo sin duplicar código, cumpliendo con la regla de negocio (BR-06).

---

## 3. API Design

*(Esta sección aplica al diseño de las pruebas sobre la API existente de la aplicación, como la gestión de animales y vacunación).*

### 3.1 Endpoints a Validar
- Todos los endpoints REST expuestos por los controladores de la aplicación serán validados a nivel de integración usando `MockMvc`.

### 3.2 Request Structure
- Las solicitudes HTTP se simularán con `MockMvcRequestBuilders` enviando los payloads requeridos en formato JSON, validados con constructores o fixtures predefinidos.

### 3.3 Response Structure
- Las aserciones sobre las respuestas del API se realizarán mediante aserciones de estado HTTP, encabezados y validaciones del cuerpo de la respuesta usando JsonPath (`MockMvcResultMatchers.jsonPath`).

### 3.4 HTTP Status Codes Validados en Pruebas
| Code | Description |
|------|------------|
| 200/201/204 | Validación de flujos exitosos (Happy Path). |
| 400 | Validación de excepciones de negocio o validación de campos. |
| 404 | Validación de respuestas cuando los Mocks de repositorios no encuentran entidades. |
| 500 | Validación de manejo global de excepciones controlando errores inesperados simulados desde los mocks. |

---

## 4. Domain Model

### 4.1 Entities a Testear
- Las entidades del dominio (e.g., `Animal`, `Vacuna`, `RegistroVacunacion`) serán instanciadas en las pruebas para comprobar sus métodos y reglas intrínsecas (validaciones de dominio).

### 4.2 Relationships
- En las pruebas unitarias, se crearán objetos "Dummy" o Mocks de objetos relacionados para garantizar que el foco de la prueba permanezca exclusivamente sobre la entidad o servicio que se está evaluando.

---

## 5. Data Model

### 5.1 Database Schema
*(No aplica interacción real con el esquema. Todo el acceso a datos será simulado).*

### 5.2 Indexes
- N/A

### 5.3 Migrations
- **Tool:** N/A (Liquibase o Flyway estarán desactivados o eludidos en el perfil de test).
- **Strategy:** Como se utilizan Mocks en lugar de una base de datos real o en memoria, las migraciones de esquemas no se ejecutarán durante la fase de testing, acelerando el inicio del contexto de Spring.

---

## 6. Validations
- Se validará que las pruebas cumplan con la métrica de **Code Coverage del 80%**. Esto será configurado en las reglas de ejecución de `JaCoCo` para que el "build" falle si no se alcanza el umbral.
- Se implementarán validaciones estáticas para garantizar que ninguna prueba unitaria cargue el `@SpringBootTest`.

---

## 7. Error Handling

### 7.1 Error Structure
En caso de que el entorno intente conectar a una base de datos, el sistema de configuración lanzará una excepción preventiva:
```json
{
  "errorCode": "TEST_ENV_VIOLATION",
  "message": "Intento de conexión a base de datos real bloqueado en entorno de pruebas."
}
```

### 7.2 Error Catalog (Fallos de Prueba)
| Code | Description |
|------|------------|
| ASSERTION_FAILED | Los datos retornados por la aplicación no coinciden con las expectativas de la prueba. |
| MOCK_CONFIG_ERR | Configuración de Mockito incorrecta o interacciones no declaradas (Strict Stubbing). |
| UNEXPECTED_EXCEPTION | El código bajo prueba lanzó una excepción que el test no esperaba capturar. |

---

## 8. Technical Flow

El flujo típico de ejecución de un caso de prueba será (Patrón AAA - Arrange, Act, Assert):

1. **Setup de Entorno (`@BeforeEach`)**: Limpieza del estado de los mocks o reseteo del contenedor de dependencias simulado.
2. **Arrange (Preparación)**: Creación de datos de entrada (fixtures) y configuración del comportamiento esperado de los mocks (`when(...).thenReturn(...)` o `given(...).willReturn(...)`).
3. **Act (Ejecución)**: Invocación del método del caso de uso o la llamada `MockMvc.perform(...)`.
4. **Assert (Aserción)**: Verificación de los resultados contra los valores esperados (`assertEquals`, `assertThrows`).
5. **Verify (Verificación de interacciones)**: Comprobación de que el código invocó los métodos correctos en las dependencias mockeadas (`verify(mock).metodo(...)`).

---

## 9. Non-Functional Requirements

### 9.1 Performance
- Al estar exentas de I/O contra bases de datos reales y usar contextos ligeros (o nulos en unitarias), la ejecución de la suite de pruebas al completo debe tardar únicamente unos pocos segundos, permitiendo una rápida retroalimentación para el desarrollador (Shift-Left Testing).

### 9.2 Scalability
- **Ejecución Paralela**: Las pruebas se configurarán para ejecutarse en paralelo mediante JUnit 5 properties (`junit.jupiter.execution.parallel.enabled = true`), asegurando que las inyecciones de mocks por método prevengan colisiones entre hilos.

### 9.3 Security
- **Mocks de Seguridad**: Si existe un framework de seguridad (ej. Spring Security), este será integrado con la librería nativa de test (`@WithMockUser` o `SecurityMockMvcRequestPostProcessors`) para simular la autenticación y autorización en pruebas de integración, sin validar credenciales contra bases de datos.

### 9.4 Concurrency
- Configuración de los mocks y contexto de test para evitar la mutación de estado compartido global estático que pudiera causar pruebas "flaky".

---

## 10. Integrations
- **Bases de Datos PostgreSQL**: Totalmente mockeadas en los repositorios o adaptadores.
- **Servicios Externos**: Cualquier cliente REST/HTTP saliente (e.g. `RestTemplate`, `WebClient`) será mockeado.

---

## 11. Testing Strategy

### 11.1 Unit Tests
- Aislamiento absoluto usando `@ExtendWith(MockitoExtension.class)`.
- Foco en Servicios de Dominio, Utils y Mappers.
- Aserciones granulares de la lógica y cálculos de negocio.

### 11.2 Integration Tests
- Uso de `@WebMvcTest` para validar los controladores web aislando la capa de servicio con `@MockBean`.
- Uso de `@SpringBootTest` (con un perfil `test-mock`) para testear flujos completos desde el controlador hasta la frontera de persistencia (que será un `@MockBean`).

### 11.3 Contract Tests
- Fuera de alcance, los contratos con el frontend se garantizan implícitamente simulando respuestas de la API en integración.

### 11.4 End-to-End Tests
- Fuera de alcance según la especificación funcional.

### 11.5 Estructura de Carpetas
Para mantener el orden y alineación con la arquitectura hexagonal, la estructura de paquetes dentro de `src/test/java` y `src/test/resources` será la siguiente:

```text
src/
└── test/
    └── java/
        └── com/
            └── gestionganado/
                ├── unit/                   # Pruebas Unitarias puras
                │   ├── domain/             # Pruebas para entidades y servicios de dominio
                │   ├── application/        # Pruebas para casos de uso / puertos de entrada
                │   └── infrastructure/     # Pruebas para mappers o utilidades puras
                └── integration/            # Pruebas de Integración (Spring Context)
    │                   ├── controllers/        # Pruebas WebMvcTest para adaptadores REST
    │                   └── adapters/           # Pruebas para otros adaptadores (simulando DB)
    └── resources/
        ├── application-test.yml            # Configuración para el perfil de pruebas
        ├── data/                           # (Opcional) JSONs / fixtures para tests
        └── logback-test.xml                # Configuración de logs limpia para testing
```

---

## 12. Observability

- **Logging:** Configuración de loggers (`logback-test.xml`) a nivel de DEBUG temporalmente para registrar transacciones y payloads simulados en caso de falla.
- **Metrics (Coverage):** `JaCoCo` se encargará de reportar el índice de cobertura de líneas y ramas (*branches*), garantizando el objetivo de >80%.
- **Reportes:** Generación automática de reportes de test de Maven/Gradle (HTML y XML) compatibles con visores de integración continua.

---

## 13. Deployment

- **CI/CD:** Las pruebas se ejecutarán automáticamente en cada "build" de Pull Request o commit en ramas principales como parte del comando `mvn test` o `gradle test`.
- **Environment Profiles:** Se usará el perfil `test` en Spring Boot, el cual tendrá configuraciones anuladas (`application-test.yml`) para asegurar que no existan URLs de bases de datos productivas.

---

## 14. Risks

- **Falsos Positivos de Integración:** Un mock mal configurado puede aprobar una prueba aunque el servicio real falle (e.g. restricciones en DB que el mock ignora).
  - *Mitigación:* Pruebas unitarias de repositorios no son parte de este scope (puesto que no usamos bases de datos reales), pero las firmas y reglas de los Mocks deben ser minuciosamente validadas por los desarrolladores en revisión de código (PR).

---

## 15. Task

1. Adicionar dependencias en `pom.xml`/`build.gradle`: `spring-boot-starter-test`, `mockito-core`, `mockito-junit-jupiter`, `jacoco-maven-plugin`.
2. Crear perfil de pruebas (`application-test.yml`) desactivando configuraciones innecesarias o riesgosas (como migraciones de Flyway/Liquibase y DataSources).
3. Escribir pruebas unitarias base para las clases principales de la capa de Dominio.
4. Desarrollar las pruebas de integración base para los Controladores utilizando `MockMvc`.
5. Implementar Casos Parametrizados (`@ParameterizedTest`) para los flujos que requieran variedad de datos.
6. Ejecutar reporte JaCoCo local y verificar que el porcentaje de cobertura supera el 80%.

---
## 16. Open Questions
- Ninguna. Todos los puntos han sido abordados de acuerdo a la especificación funcional.
