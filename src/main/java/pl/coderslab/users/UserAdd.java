package pl.coderslab.users;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(value = "/user/add")
public class UserAdd extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getServletContext().getRequestDispatcher("/users/add.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = new User();
        user.setUserName(request.getParameter("userName"));
        user.setEmail(request.getParameter("email"));
        user.setPassword(request.getParameter("password"));
        UserDAO userDAO = new UserDAO();
        userDAO.create(user);
        response.sendRedirect(request.getContextPath() + "/user/list");
    }
}
