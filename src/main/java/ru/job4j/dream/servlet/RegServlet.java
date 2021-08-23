package ru.job4j.dream.servlet;

import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RegServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        User user = new User(
                0,
                req.getParameter("name"),
                req.getParameter("email"),
                req.getParameter("password")
        );
        boolean exist = PsqlStore.instOf().getUserOnEmail(user.getEmail()) != null;
        if (exist) {
            req.setAttribute("error", "Account already exist");
            req.getRequestDispatcher("reg.jsp").forward(req, resp);
        } else {
            HttpSession sc = req.getSession();
            PsqlStore.instOf().save(user);
            sc.setAttribute("user", user);
            sc.setAttribute("username", user.getName());
            resp.sendRedirect(req.getContextPath() + "/posts.do");
        }
    }
}
