<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Productos - Sistema Legacy</title>
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

<!-- Contenido Principal -->
<div class="main-container">
    <div class="page-header">
        <div>
            <h2>&#128230; Gestión de Productos</h2>
            <p class="text-muted">Administre el catálogo de productos del sistema</p>
        </div>
        <a href="${pageContext.request.contextPath}/productos?accion=nuevo" class="btn btn-primary">
            + Nuevo Producto
        </a>
    </div>

    <!-- Alertas -->
    <c:if test="${not empty sessionScope.mensaje}">
        <div class="alert alert-success">
            &#10003; ${sessionScope.mensaje}
        </div>
        <c:remove var="mensaje" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-error">
            &#9888; ${sessionScope.error}
        </div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <!-- Tabla de Productos -->
    <div class="card">
        <c:choose>
            <c:when test="${empty productos}">
                <div class="empty-state">
                    <div class="empty-icon">&#128230;</div>
                    <h3>No hay productos registrados</h3>
                    <p>Comience agregando el primer producto al sistema.</p>
                    <a href="${pageContext.request.contextPath}/productos?accion=nuevo" class="btn btn-primary">
                        + Agregar Producto
                    </a>
                </div>
            </c:when>
            <c:otherwise>
                <table class="table">
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Nombre</th>
                            <th>Categoría</th>
                            <th>Descripción</th>
                            <th>Precio</th>
                            <th>Stock</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="p" items="${productos}">
                            <tr>
                                <td>${p.id}</td>
                                <td><strong>${p.nombre}</strong></td>
                                <td>
                                    <span class="badge badge-info">${not empty p.categoria ? p.categoria : 'Sin categoría'}</span>
                                </td>
                                <td class="text-truncate">${not empty p.descripcion ? p.descripcion : '-'}</td>
                                <td class="precio">
                                    <fmt:formatNumber value="${p.precio}" type="currency" currencySymbol="$"/>
                                </td>
                                <td>
                                    <span class="badge ${p.stock > 10 ? 'badge-success' : p.stock > 0 ? 'badge-warning' : 'badge-danger'}">
                                        ${p.stock}
                                    </span>
                                </td>
                                <td class="acciones">
                                    <a href="${pageContext.request.contextPath}/productos?accion=editar&id=${p.id}"
                                       class="btn btn-warning btn-sm">Editar</a>
                                    <c:if test="${sessionScope.usuarioLogueado.rol == 'ADMIN'}">
                                        <a href="${pageContext.request.contextPath}/productos?accion=eliminar&id=${p.id}"
                                           class="btn btn-danger btn-sm"
                                           onclick="return confirm('¿Está seguro de eliminar el producto: ${p.nombre}?')">
                                            Eliminar
                                        </a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <div class="table-footer">
                    Total: <strong>${productos.size()} producto(s)</strong>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

</body>
</html>
