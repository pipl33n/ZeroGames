# ZeroGames 🎮

Plataforma de venta de videojuegos online construida con arquitectura de microservicios independientes, comunicación REST y despliegue en Railway.

## Integrantes del equipo

| Nombre | GitHub |
|---|---|
| Josefa Roca | @jocfapaz |
| Luis San Martin | @pipl33n |
| Renato Quevedo | @Tatora09 |

---

## Microservicios implementados

| Microservicio | Puerto local | Descripción |
|---|---|---|
| `ms-juegos` | 8081 | Gestión del catálogo de videojuegos |
| `ms-usuarios` | 8082 | Gestión de usuarios registrados |
| `ms-pedidos` | 8083 | Gestión de pedidos, consume ms-juegos y ms-usuarios |
| `api-gateway` | 8080 | Enrutamiento centralizado hacia los microservicios |
| `ms-favoritos` | 8084 | Gestión de juegos favoritos por usuario |
| `ms-reseñas` | 8085 | Gestión de reseñas de juegos |
| `ms-pagos` | 8086 | Gestión de pagos |
| `ms-biblioteca` | 8087 | Gestión de biblioteca personal |
| `ms-carrito` | 8088 | Gestión de carrito de compras |
| `ms-notificaciones` | 8089 | Gestión de notificaciones |
| `ms-inventario` | 8090 | Gestión de inventario |

---

## Rutas principales del API Gateway

| Ruta Gateway | Microservicio destino | Ejemplo de uso |
|---|---|---|
| `/juegos/**` | ms-juegos (`:8081`) | `GET http://localhost:8080/juegos/api/juegos` |
| `/usuarios/**` | ms-usuarios (`:8082`) | `GET http://localhost:8080/usuarios/api/usuarios` |
| `/pedidos/**` | ms-pedidos (`:8083`) | `GET http://localhost:8080/pedidos/api/pedidos` |

---

## Documentación Swagger / OpenAPI

| Microservicio | URL local | URL Railway |
|---|---|---|
| ms-juegos | http://localhost:8081/swagger-ui/index.html | https://zerogames-production-c49d.up.railway.app/swagger-ui/index.html |
| ms-usuarios | http://localhost:8082/swagger-ui/index.html | — |
| ms-pedidos | http://localhost:8083/swagger-ui/index.html | — |

---

## Instrucciones de ejecución local

### Requisitos previos

- Java 21
- Maven 3.8+
- MySQL corriendo en `localhost:3306`

### 1. Crear las bases de datos

```sql
CREATE DATABASE db_juegos;
CREATE DATABASE db_usuarios;
CREATE DATABASE db_pedidos;
```

### 2. Levantar los microservicios (en orden)

```bash
# ms-juegos (puerto 8081)
cd ms-juegos
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# ms-usuarios (puerto 8082)
cd ms-usuarios
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# ms-pedidos (puerto 8083)
cd ms-pedidos
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# api-gateway (puerto 8080)
cd api-gateway
mvn spring-boot:run
```

### 3. Ejecutar pruebas unitarias

```bash
# ms-juegos
cd ms-juegos
mvn test

# ms-usuarios
cd ms-usuarios
mvn test

# ms-pedidos
cd ms-pedidos
mvn test
```

### 4. Verificar funcionamiento vía Gateway

```bash
# Listar juegos
GET http://localhost:8080/juegos/api/juegos

# Listar usuarios
GET http://localhost:8080/usuarios/api/usuarios

# Listar pedidos
GET http://localhost:8080/pedidos/api/pedidos
```

---

## Instrucciones de ejecución remota (Railway)

El microservicio `ms-juegos` está desplegado en Railway y accesible en:

- **Swagger:** https://zerogames-production-c49d.up.railway.app/swagger-ui/index.html
- **API:** https://zerogames-production-c49d.up.railway.app/api/juegos

Las variables de entorno configuradas en Railway son:
- `SPRING_PROFILES_ACTIVE=railway`
- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` (configuradas en el panel de Railway)
