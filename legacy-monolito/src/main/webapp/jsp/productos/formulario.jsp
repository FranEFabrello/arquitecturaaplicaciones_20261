<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${not empty producto && producto.id != 0 ? 'Editar' : 'Nuevo'} Producto - Sistema Legacy</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<!-- Navbar -->
<nav class="navbar">
    <div class="navbar-brand">&#128274; Sistema Legacy</div>
    <div class="navbar-menu">
        <span class="navbar-user">
            &#128100; ${sessionScope.usuarioLogueado.nombre}
            <span class="badge badge-${sessionScope.usuarioLogueado.rol == 'ADMIN' ? 'danger' : 'info'}">
                ${sessionScope.usuarioLogueado.rol}
            </span>
        </span>
        <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-light btn-sm">
            Cerrar Sesión
        </a>
    </div>
</nav>

<div class="main-container">
    <div class="page-header">
        <div>
            <h2>${not empty producto && producto.id != 0 ? '&#9998; Editar Producto' : '&#43; Nuevo Producto'}</h2>
            <p class="text-muted">
                ${not empty producto && producto.id != 0 ? 'Modifique los datos del producto' : 'Complete los datos para registrar un nuevo producto'}
            </p>
        </div>
        <a href="${pageContext.request.contextPath}/productos" class="btn btn-secondary">
            &#8592; Volver a la lista
        </a>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-error">&#9888; ${error}</div>
    </c:if>

    <div class="card form-card">
        <form action="${pageContext.request.contextPath}/productos" method="post">

            <c:choose>
                <c:when test="${not empty producto && producto.id != 0}">
                    <input type="hidden" name="accion" value="actualizar">
                    <input type="hidden" name="id" value="${producto.id}">
                </c:when>
                <c:otherwise>
                    <input type="hidden" name="accion" value="insertar">
                </c:otherwise>
            </c:choose>

            <div class="form-row">
                <div class="form-group">
                    <label for="nombre">Nombre <span class="required">*</span></label>
                    <input type="text" id="nombre" name="nombre"
                           value="${producto.nombre}" placeholder="Nombre del producto" required>
                </div>
                <div class="form-group">
                    <label for="categoria">Categoría</label>
                    <select id="categoria" name="categoria">
                        <option value="">-- Seleccionar --</option>
                        <option value="Electrónica"  ${producto.categoria == 'Electrónica'  ? 'selected' : ''}>Electrónica</option>
                        <option value="Periféricos"  ${producto.categoria == 'Periféricos'  ? 'selected' : ''}>Periféricos</option>
                        <option value="Ropa"         ${producto.categoria == 'Ropa'         ? 'selected' : ''}>Ropa</option>
                        <option value="Alimentos"    ${producto.categoria == 'Alimentos'    ? 'selected' : ''}>Alimentos</option>
                        <option value="Hogar"        ${producto.categoria == 'Hogar'        ? 'selected' : ''}>Hogar</option>
                        <option value="Deportes"     ${producto.categoria == 'Deportes'     ? 'selected' : ''}>Deportes</option>
                        <option value="Otros"        ${producto.categoria == 'Otros'        ? 'selected' : ''}>Otros</option>
                    </select>
                </div>
            </div>

            <div class="form-group">
                <label for="descripcion">Descripción</label>
                <textarea id="descripcion" name="descripcion" rows="3"
                          placeholder="Descripción del producto">${producto.descripcion}</textarea>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="precio">Precio ($) <span class="required">*</span></label>
                    <input type="number" id="precio" name="precio"
                           value="${producto.precio}" placeholder="0.00"
                           min="0" step="0.01" required>
                </div>
                <div class="form-group">
                    <label for="stock">Stock <span class="required">*</span></label>
                    <input type="number" id="stock" name="stock"
                           value="${producto.stock}" placeholder="0"
                           min="0" required>
                </div>
            </div>

            <div class="form-actions">
                <a href="${pageContext.request.contextPath}/productos" class="btn btn-secondary">
                    Cancelar
                </a>
                <button type="submit" class="btn btn-primary">
                    ${not empty producto && producto.id != 0 ? 'Actualizar Producto' : 'Guardar Producto'}
                </button>
            </div>
        </form>
    </div>
</div>

</body>
</html>
