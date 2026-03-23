<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Sistema Legacy</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="login-body">

<div class="login-container">
    <div class="login-card">
        <div class="login-header">
            <div class="login-logo">&#128274;</div>
            <h1>Sistema Legacy</h1>
            <p>Ingrese sus credenciales</p>
        </div>

        <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-error">
            &#9888; ${error}
        </div>
        <% } %>

        <form action="${pageContext.request.contextPath}/login" method="post" class="login-form">
            <div class="form-group">
                <label for="username">Usuario</label>
                <input type="text" id="username" name="username"
                       value="${username}" placeholder="Ingrese su usuario"
                       required autofocus>
            </div>
            <div class="form-group">
                <label for="password">Contraseña</label>
                <input type="password" id="password" name="password"
                       placeholder="Ingrese su contraseña" required>
            </div>
            <button type="submit" class="btn btn-primary btn-block">
                Ingresar
            </button>
        </form>

        <div class="login-footer">
            <p><strong>Usuarios de prueba:</strong></p>
            <p>admin / admin123 &nbsp;|&nbsp; usuario / user123</p>
        </div>
    </div>
</div>

</body>
</html>
