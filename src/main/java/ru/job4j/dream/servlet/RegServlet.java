package ru.job4j.dream.servlet;

import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RegServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = new User(
                0,
                req.getParameter("name"),
                req.getParameter("email"),
                req.getParameter("password")
        );
        User fromDB = PsqlStore.instOf().getUserOnEmail(user.getEmail());
        boolean bool = fromDB != null;
        HttpSession sc = req.getSession();
        if (user.finished() && fromDB == null) {
            PsqlStore.instOf().save(user);
            sc.setAttribute("user", user);
            sc.setAttribute("username", user.getName());
            resp.sendRedirect(req.getContextPath() + "/posts.do");
        } else if (bool && user.getEmail().equals(fromDB.getEmail())) {
            req.setAttribute("error", "Account already exist");
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
        } else {
            req.setAttribute("error", "Incorrect data entered");
            resp.sendRedirect(req.getContextPath() + "/reg.jsp");
        }
    }
}
