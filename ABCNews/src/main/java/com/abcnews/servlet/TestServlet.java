package com.abcnews.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/test-servlet-post") // URL hoàn toàn mới
public class TestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(">>> TestServlet doPost WAS CALLED! <<<"); // Log Console
        resp.setContentType("text/plain; charset=UTF-8");
        resp.getWriter().println("Test POST OK!"); // Trả về trình duyệt
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
         System.out.println(">>> TestServlet doGet WAS CALLED! <<<"); // Log Console
         resp.setContentType("text/plain; charset=UTF-8");
         resp.getWriter().println("Test GET OK!"); // Trả về trình duyệt
    }
}