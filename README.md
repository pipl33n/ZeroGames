# ZeroGames 🎮

Plataforma de venta de videojuegos online construida con arquitectura de microservicios independientes, comunicación REST y despliegue en Railway.

## Integrantes del equipo

| Nombre | GitHub |
|---|---|
| Josefa Roca |  |
| Luis San Martin | @pipl33n |
| *(completar tercer integrante)* | — |

---

## Microservicios implementados

| Microservicio | Puerto local | Descripción |
|---|---|---|
| `ms-juegos` | 8081 | Gestión del catálogo de videojuegos |
| `ms-usuarios` | 8082 | Gestión de usuarios registrados |
| `ms-pedidos` | 8083 | Gestión de pedidos, consume ms-juegos y ms-usuarios |
| `api-gateway` | 8080 | Enrutamiento centralizado hacia los microservicios |
| `ms-categorias` | — | (estructura base) |
| `ms-reseñas` | — | (estructura base) |
| `ms-pagos` | — | (estructura base) |
| `ms-descuentos` | — | (estructura base) |
| `ms-carrito` | — | (estructura base) |
| `ms-notificaciones` | — | (estructura base) |

---

## Rutas principales del API Gateway

| Ruta Gateway | Microservicio destino | Ejemplo de uso |
|---|---|---|
| `GET /juegos/**` | ms-juegos (`:8081`) | `GET http://localhost:8080/juegos/api/juegos` |
| `GET /usuarios/**` | ms-usuarios (`:8082`) | `GET http://localhost:8080/usuarios/api/usuarios` |
| `GET /pedidos/**` | ms-pedidos (`:8083`) | `GET http://localhost:8080/pedidos/api/pedidos` |

---

## Documentación Swagger / OpenAPI

| Microservicio | URL local |
|---|---|
| ms-juegos | http://localhost:8081/swagger-ui/index.html |
| ms-usuarios | http://localhost:8082/swagger-ui/index.html |
| ms-pedidos | http://localhost:8083/swagger-ui/index.html |

> **Despliegue remoto (Railway):** reemplazar `localhost:8081` por la URL pública de Railway del ms-juegos.

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



# ms-usuarios (puerto 8082)


# ms-pedidos (puerto 8083)


# api-gateway (puerto 8080)

```

### 3. Verificar funcionamiento

```bash
# Listar juegos a través del Gateway
http://localhost:8080/juegos/api/juegos

# Listar usuarios a través del Gateway
http://localhost:8080/usuarios/api/usuarios
```
