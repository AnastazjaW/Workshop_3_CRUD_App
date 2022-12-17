package pl.coderslab.users;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(value = "/user/edit")
public class UserEdit extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        UserDAO userDAO = new UserDAO();
        User userRead = userDAO.read(Integer.parseInt(id));
        request.setAttribute("user", userRead);

        getServletContext().getRequestDispatcher("/users/edit.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = new User();
        user.setId(Integer.parseInt(request.getParameter("id")));
        user.setUserName(request.getParameter("userName"));
        user.setEmail(request.getParameter("email"));
        user.setPassword(request.getParameter("password"));
        UserDAO userDAO = new UserDAO();
        userDAO.update(user);
        response.sendRedirect(request.getContextPath() + "/user/list");

    }
}
