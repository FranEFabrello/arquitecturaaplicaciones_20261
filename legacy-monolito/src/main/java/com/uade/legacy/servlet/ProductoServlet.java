package com.uade.legacy.servlet;

import com.uade.legacy.dao.ProductoDAO;
import com.uade.legacy.model.Producto;
import com.uade.legacy.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/productos")
public class ProductoServlet extends HttpServlet {

    private final ProductoDAO productoDAO = new ProductoDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String accion = req.getParameter("accion");
        if (accion == null) accion = "listar";

        switch (accion) {
            case "nuevo":
                req.getRequestDispatcher("/jsp/productos/formulario.jsp").forward(req, resp);
                break;

            case "editar":
                mostrarFormularioEdicion(req, resp);
                break;

            case "eliminar":
                eliminar(req, resp);
                break;

            default:
                listar(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String accion = req.getParameter("accion");
        if ("insertar".equals(accion)) {
            insertar(req, resp);
        } else if ("actualizar".equals(accion)) {
            actualizar(req, resp);
        } else {
            listar(req, resp);
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Producto> productos = productoDAO.listarTodos();
        req.setAttribute("productos", productos);
        req.getRequestDispatcher("/jsp/productos/lista.jsp").forward(req, resp);
    }

    private void mostrarFormularioEdicion(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            Producto producto = productoDAO.buscarPorId(id);
            if (producto == null) {
                req.setAttribute("error", "Producto no encontrado.");
                listar(req, resp);
                return;
            }
            req.setAttribute("producto", producto);
            req.getRequestDispatcher("/jsp/productos/formulario.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/productos");
        }
    }

    private void insertar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Producto p = extraerProducto(req);
        String validacion = validar(p);

        if (validacion != null) {
            req.setAttribute("error", validacion);
            req.setAttribute("producto", p);
            req.getRequestDispatcher("/jsp/productos/formulario.jsp").forward(req, resp);
            return;
        }

        if (productoDAO.insertar(p)) {
            req.getSession().setAttribute("mensaje", "Producto creado correctamente.");
        } else {
            req.getSession().setAttribute("error", "Error al crear el producto.");
        }
        resp.sendRedirect(req.getContextPath() + "/productos");
    }

    private void actualizar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Producto p = extraerProducto(req);
        try {
            p.setId(Integer.parseInt(req.getParameter("id")));
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/productos");
            return;
        }

        String validacion = validar(p);
        if (validacion != null) {
            req.setAttribute("error", validacion);
            req.setAttribute("producto", p);
            req.getRequestDispatcher("/jsp/productos/formulario.jsp").forward(req, resp);
            return;
        }

        if (productoDAO.actualizar(p)) {
            req.getSession().setAttribute("mensaje", "Producto actualizado correctamente.");
        } else {
            req.getSession().setAttribute("error", "Error al actualizar el producto.");
        }
        resp.sendRedirect(req.getContextPath() + "/productos");
    }

    private void eliminar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Usuario usuario = (Usuario) req.getSession().getAttribute("usuarioLogueado");
        if (!"ADMIN".equals(usuario.getRol())) {
            req.getSession().setAttribute("error", "No tiene permisos para eliminar productos.");
            resp.sendRedirect(req.getContextPath() + "/productos");
            return;
        }

        try {
            int id = Integer.parseInt(req.getParameter("id"));
            if (productoDAO.eliminar(id)) {
                req.getSession().setAttribute("mensaje", "Producto eliminado correctamente.");
            } else {
                req.getSession().setAttribute("error", "No se pudo eliminar el producto.");
            }
        } catch (NumberFormatException e) {
            req.getSession().setAttribute("error", "ID de producto inválido.");
        }
        resp.sendRedirect(req.getContextPath() + "/productos");
    }

    private Producto extraerProducto(HttpServletRequest req) {
        Producto p = new Producto();
        p.setNombre(req.getParameter("nombre"));
        p.setDescripcion(req.getParameter("descripcion"));
        p.setCategoria(req.getParameter("categoria"));
        try { p.setPrecio(Double.parseDouble(req.getParameter("precio"))); }
        catch (NumberFormatException e) { p.setPrecio(0); }
        try { p.setStock(Integer.parseInt(req.getParameter("stock"))); }
        catch (NumberFormatException e) { p.setStock(0); }
        return p;
    }

    private String validar(Producto p) {
        if (p.getNombre() == null || p.getNombre().trim().isEmpty())
            return "El nombre del producto es obligatorio.";
        if (p.getPrecio() < 0)
            return "El precio no puede ser negativo.";
        if (p.getStock() < 0)
            return "El stock no puede ser negativo.";
        return null;
    }
}
