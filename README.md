<div align="center">
  <h1>📦 Sistema de Inventario y Ventas API</h1>
  <p><i>API RESTful robusta y escalable para la gestión integral de inventarios y ventas.</i></p>

  <!-- Badges -->
  <p>
    <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21" />
    <img src="https://img.shields.io/badge/Spring_Boot-3.3.3-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot" />
    <img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL" />
    <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white" alt="JWT" />
    <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=Swagger&logoColor=black" alt="Swagger" />
    <img src="https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge" alt="License MIT" />
  </p>
</div>

---

## 📖 Descripción

Este proyecto es una aplicación backend construida con **Spring Boot 3** y **Java 21**, diseñada para gestionar un sistema de inventario y ventas de manera eficiente y segura. Implementa buenas prácticas de desarrollo, una arquitectura en capas escalable y proporciona todo lo necesario para interactuar con las entidades clave de un negocio: **Productos, Categorías, Clientes y Ventas**. 

Es un proyecto ideal para demostrar la creación de APIs listas para entornos de producción, centralizando la lógica del negocio con herramientas modernas y seguras.

## ✨ Características Principales

*   **Seguridad Robusta:** Autenticación y autorización protegidas mediante JSON Web Tokens (JWT) integrados con Spring Security.
*   **Gestión de Inventario:** Control total (CRUD) sobre el ciclo de vida de productos y categorías.
*   **Gestión de Ventas:** Registro detallado de ventas, cálculo de montos y trazabilidad.
*   **Manejo de Clientes:** Información centralizada e histórico de los clientes.
*   **Documentación Interactiva:** Endpoints documentados y testeables directamente mediante Swagger UI / OpenAPI 3.
*   **Mapeo de Datos Eficiente:** Transformación limpia de entidades a DTOs utilizando MapStruct.
*   **Resiliencia y Datos:** Soporte local en memoria con base de datos H2 para desarrollo/pruebas, fácilmente escalable a PostgreSQL para entornos de producción.

## 🛠️ Tecnologías Empleadas

El proyecto está desarrollado utilizando un *stack* tecnológico orientado al rendimiento empresarial y buenas prácticas del ecosistema Spring:

*   **Lenguaje:** Java 21
*   **Framework Principal:** Spring Boot 3.3.3 (Web, Data JPA, Security, Validation)
*   **Bases de Datos:** PostgreSQL & H2 Database
*   **Seguridad:** io.jsonwebtoken (JWT)
*   **Herramientas & Utilidades:** Lombok, MapStruct
*   **Documentación:** SpringDoc OpenAPI (Swagger UI)
*   **Gestor de Proyectos:** Maven

## 🚀 Inicio Rápido

### Prerrequisitos

Asegúrate de tener instalados los siguientes componentes en tu entorno local:

*   [Java 21 JDK](https://adoptium.net/)
*   [Apache Maven 3.8+](https://maven.apache.org/)

### Instalación y Ejecución

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/argenischacon/inventory-sales-system.git
   cd inventory-sales-system
   ```

2. **Ejecutar la aplicación (con H2 en memoria por defecto):**
   ```bash
   ./mvnw spring-boot:run
   ```
   *La aplicación descargará sus dependencias, se iniciará y estará disponible en el puerto `8080`.*

> **Nota:** Para entornos de producción, simplemente ajusta tus credenciales de PostgreSQL en el archivo `application.properties` correspondiente a tu perfil.

## 📚 Documentación de la API

La API cuenta con una interfaz visual y documentación dinámica. Una vez ejecutada la aplicación, puedes acceder a:

*   **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
*   **OpenAPI JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

Desde la interfaz de Swagger UI podrás visualizar cada endpoint expuesto, comprender los esquemas de petición requeridos, inspeccionar los modelos de datos (DTOs) y realizar pruebas HTTP de manera intuitiva.

## 📂 Estructura del Proyecto

El proyecto está organizado siguiendo una limpieza estructural orientada a dominios y en capas:

```text
src/
├── main/
│   ├── java/com/argenischacon/
│   │   ├── config/       # Configuraciones globales (Security, Cors, OpenAPI)
│   │   ├── controller/   # Controladores REST exponiendo los endpoints
│   │   ├── dto/          # Data Transfer Objects (Peticiones/Respuestas)
│   │   ├── exception/    # Interceptores y manejo unificado de errores (Advice)
│   │   ├── mapper/       # Interfaces MapStruct para conversión Entity-DTO
│   │   ├── model/        # Entidades persistentes de la base de datos (JPA)
│   │   ├── repository/   # Abstracciones de acceso a datos (Spring Data)
│   │   ├── security/     # Lógica centralizada de validación de tokens
│   │   └── service/      # Servicios encapsulando la lógica de negocio
│   └── resources/
│       └── application.properties # Variables de conexión y entorno
└── test/                 # Pruebas automatizadas del sistema
```

## 🤝 Contribución

Cualquier contribución o sugerencia técnica para mejorar el proyecto es bienvenida y se agradece profundamente:

1. Haz un *Fork* de este repositorio.
2. Crea una rama para tu característica o mejora (`git checkout -b feature/CaracteristicaIncreible`).
3. Confirma los cambios realizados asegurándote de ser descriptivo (`git commit -m 'feat: Añade nueva CaracteristicaIncreible'`).
4. Sube tu rama al servidor (`git push origin feature/CaracteristicaIncreible`).
5. Abre y solicita un *Pull Request* hacia la rama principal.

## 📄 Licencia

Este software se encuentra disponible bajo los términos de la licencia MIT. Consulta el archivo [LICENSE](LICENSE) para información legal más detallada.

## 👨‍💻 Autor

**Argenis Chacón**
*   GitHub: [@argenischacon](https://github.com/argenischacon)
*   ¡No dudes en dejar tu **estrella ⭐** si este proyecto te ha resultado inspirador o de utilidad en tu camino de aprendizaje!