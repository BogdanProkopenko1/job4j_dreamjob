package ru.job4j.dream.servlet;

import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = PsqlStore.instOf().getUserOnEmail(req.getParameter("email"));
        HttpSession sc = req.getSession();
        if (user != null && user.getPassword().equals(req.getParameter("password"))) {
            sc.setAttribute("user", user);
            sc.setAttribute("username", user.getName());
            resp.sendRedirect(req.getContextPath() + "/posts.do");
        } else {
            req.setAttribute("error", "Illegal email or password");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}
