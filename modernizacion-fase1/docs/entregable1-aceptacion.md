# Criterios de aceptacion - Entregable 1

## Auth Service

- [ ] `POST /api/v1/auth/login` con credenciales validas responde `200` y retorna `accessToken`.
- [ ] `POST /api/v1/auth/login` con credenciales invalidas responde `401`.
- [ ] `GET /actuator/health` responde estado `UP`.

## Product Service

- [ ] `GET /api/v1/productos` sin token responde `401`.
- [ ] `GET /api/v1/productos` con token valido responde `200`.
- [ ] `POST /api/v1/productos` con rol `USER` responde `403`.
- [ ] `POST /api/v1/productos` con rol `ADMIN` responde `201`.
- [ ] `DELETE /api/v1/productos/{id}` con rol `ADMIN` responde `204` cuando existe.
- [ ] `GET /actuator/health` responde estado `UP`.

