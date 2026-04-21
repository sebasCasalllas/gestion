# Functional Specification (FS)

## 1. General Information

### 1.1 Feature Name
Cobertura de Pruebas Unitarias y de Integración con Mocks

### 1.2 Description
Definición, diseño y ejecución de un conjunto de pruebas automatizadas que abarquen tanto el nivel unitario como el de integración para el sistema actual. El propósito es garantizar que la lógica de negocio y la interacción entre componentes funcionen según lo esperado. Para aislar el entorno de las pruebas de integración y garantizar velocidad y determinismo, se simularán (mockearán) los clientes externos, específicamente la base de datos y cualquier otro servicio de terceros.

### 1.3 Scope
**Includes:**
- Definición y ejecución de pruebas unitarias para componentes aislados de lógica de negocio.
- Definición y ejecución de pruebas de integración para flujos de casos de uso completos.
- Implementación de simulación (mocking) utilizando Mockito para la base de datos y otros clientes externos en los tests de integración.
- Uso de pruebas parametrizadas para evitar duplicación de código.
- Generación de reportes de ejecución de las pruebas.

**Excludes:**
- Pruebas End-to-End (E2E) desde la interfaz de usuario.
- Pruebas de carga, estrés o rendimiento.
- Uso de bases de datos reales o en memoria para la persistencia durante las pruebas de integración (todo debe ser mockeado).
- Despliegue automático o configuración de pipelines CI/CD (solo se aborda la creación de las pruebas).

---

## 2. Objectives

- Garantizar la fiabilidad técnica del código del proyecto actual.
- Validar el correcto funcionamiento de la lógica de negocio de manera aislada (unitarias).
- Asegurar que la comunicación e integración entre las capas del sistema operen correctamente (integración).
- Eliminar la dependencia de bases de datos externas durante la ejecución de las pruebas para evitar problemas de latencia o disponibilidad.
- Prevenir la introducción de errores (regresiones) en futuros desarrollos.

---

## 3. Functional Requirements

- FR-01: El sistema de pruebas debe permitir ejecutar pruebas unitarias sin levantar el contexto completo de la aplicación.
- FR-02: El sistema de pruebas debe permitir ejecutar pruebas de integración que validen el flujo completo de los datos desde la entrada hasta la salida esperada.
- FR-03: El sistema de pruebas debe simular (mockear) la base de datos y cualquier otro servicio externo para todas las operaciones de lectura y escritura durante las pruebas de integración.
- FR-04: El sistema de pruebas debe proporcionar un reporte detallado con la cantidad de pruebas exitosas y fallidas.
- FR-05: El sistema de pruebas debe detener su ejecución de forma controlada y reportar error si se intenta conectar a una base de datos real.
- FR-06: El sistema de pruebas debe reportar la métrica de cobertura de código (Code Coverage) alcanzada.

---

## 4. Business Rules

- BR-01: Las pruebas unitarias deben ejecutarse completamente aisladas, sin depender de red, sistema de archivos o bases de datos.
- BR-02: Las pruebas de integración deben usar estrictamente dobles de prueba (usando Mockito) para representar clientes externos y bases de datos.
- BR-03: El estado de los mocks debe ser reiniciado o limpiado antes de la ejecución de cada prueba para asegurar independencia.
- BR-04: Una prueba fallida indica una no conformidad en el código y debe ser tratada como un bloqueo para el pase a producción.
- BR-05: El código del proyecto debe alcanzar y mantener un mínimo de 80% de cobertura de pruebas (Code Coverage).
- BR-06: Los escenarios de prueba con múltiples datos de entrada deben implementarse como pruebas parametrizadas para evitar la duplicación de código.
- BR-07: Está estrictamente prohibido usar condicionales (`if`, `switch`) dentro del código de las pruebas. Cada camino de ejecución debe evaluarse en un test independiente.

---

## 5. User Flows

### 5.1 Main Flow (Happy Path)
1. El desarrollador o sistema de integración continua lanza el comando de ejecución de pruebas.
2. El entorno inicializa las configuraciones de prueba y levanta los mocks de la base de datos.
3. Se ejecuta la suite de pruebas unitarias validando la lógica interna.
4. Se ejecuta la suite de pruebas de integración validando los flujos a través de las capas.
5. El sistema consolida los resultados, verificando que todas las aserciones son correctas.
6. El sistema entrega un reporte de ejecución exitoso.

---

### 5.2 Alternative Flows
- Scenario 1: Fallo en aserción → El sistema detecta que el resultado de una función no coincide con el esperado, detiene el test en curso, registra el error y marca la suite como fallida.
- Scenario 2: Error de configuración en el mock → El sistema no puede inicializar el mock de la base de datos, aborta la ejecución de las pruebas de integración y reporta un error de infraestructura de pruebas.

---

## 6. Edge Cases

- Simulación de tiempos de espera (timeouts) desde la base de datos mockeada para validar la resiliencia del sistema.
- Simulación de excepciones de negocio o de datos (e.g., violación de restricciones de unicidad) provenientes del mock de la base de datos.
- Ejecución concurrente o en paralelo de las pruebas sin que los mocks compartan estado y generen colisiones.
- Respuestas vacías o nulas desde el cliente externo mockeado.

---

## 7. Inputs & Outputs

### Inputs

| Field | Description | Required |
|------|------------|----------|
| Comando de ejecución | Instrucción para iniciar las pruebas | Sí |
| Casos de prueba | Archivos con la definición de los tests | Sí |
| Datos simulados (Mocks) | Respuestas preparadas para simular la base de datos | Sí |
| Parámetros de entorno | Configuración que indica que el entorno es de prueba | Sí |

---

### Outputs

| Field | Description |
|------|------------|
| Estado general | Aprobado o Reprobado (Pass/Fail) |
| Reporte de ejecución | Lista de pruebas ejecutadas con su tiempo de duración |
| Trazas de error | Detalle de las aserciones fallidas o excepciones lanzadas (si aplica) |

---

## 8. Validations

| Field | Rule | Error Message |
|------|------|--------------|
| Conexión a DB | No debe existir conexión real | "Intento de conexión a base de datos real bloqueado" |
| Entorno | Debe ser estrictamente entorno de test | "Entorno de ejecución inválido para pruebas" |
| Definición de Mock | El mock debe coincidir con el contrato real | "El mock no implementa el contrato esperado" |

---

## 9. Error Handling

| Scenario | Message |
|---------|--------|
| Aserción fallida | "El resultado obtenido no coincide con la expectativa" |
| Mock mal configurado | "Error al inicializar el cliente externo simulado" |
| Timeout en prueba | "La prueba excedió el tiempo límite permitido" |
| Excepción no controlada en el código | "Error inesperado durante la ejecución del flujo" |

---

## 10. Non-Functional Considerations

- **Performance**: Las pruebas deben ejecutarse rápidamente (idealmente en segundos) al no depender de I/O de red ni disco.
- **Reliability**: Las pruebas deben ser deterministas; el mismo código debe producir siempre el mismo resultado (no debe haber pruebas *flaky*).
- **Maintainability**: El código de las pruebas y la configuración de los mocks deben ser limpios, modulares y fáciles de mantener al evolucionar la aplicación.

---

## 11. Acceptance Criteria (BDD)

Scenario: Ejecución exitosa de pruebas de integración con base de datos mockeada  
Given que el sistema cuenta con pruebas de integración definidas  
And el entorno está configurado para usar mocks en lugar de una base de datos real  
When se lanza la ejecución de las pruebas  
Then el sistema simula exitosamente las lecturas y escrituras en la base de datos  
And se validan todas las aserciones de los flujos de integración  
And el sistema retorna un estado de aprobación global  

---

## 12. Testing Considerations

- El desarrollo de estas pruebas debe verificar que se cubran los "Happy Paths" y los flujos alternativos definidos en especificaciones anteriores.
- Es crucial revisar que el comportamiento del mock represente fielmente el comportamiento de la base de datos o servicio externo real (por ejemplo, retornando los mismos tipos de error).
- Se debe validar el correcto funcionamiento de las inyecciones de dependencia para reemplazar los repositorios reales por los mocks.
- Asegurar la correcta utilización de las anotaciones de pruebas parametrizadas según el framework empleado.

---

## 13. Dependencies

- Código fuente base del proyecto (interfaces, servicios, controladores).
- Contratos de los repositorios y clientes externos claramente definidos.
- Framework base de pruebas (ej. JUnit) y Mockito como framework de simulación (mocking) integrados en el ecosistema del proyecto.

---

## 14. Assumptions & Constraints

- La arquitectura del proyecto actual permite la inyección de dependencias y la separación de responsabilidades necesaria para aplicar mocks (por ejemplo, arquitectura hexagonal o de n-capas).
- No hay lógica de negocio acoplada directamente dentro de consultas SQL o procedimientos almacenados que no pueda ser mockeada.
- Todos los clientes externos relevantes interactúan mediante interfaces bien definidas.

---

## 15. Open Questions

- Ninguna. Todas las dudas iniciales (cobertura del 80%, uso de Mockito, simulación de todos los servicios externos, pruebas parametrizadas y sin condicionales) han sido resueltas e incorporadas en el diseño.
