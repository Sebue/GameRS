package pl.sebue.magisterka.grs.controller.recommendation;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/recommendation")
public class RecommendationServlet extends HttpServlet {
    private static final String PARAMETER = "userId";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter writer = response.getWriter();
        response.setContentType("text/html");
        writer.println("<h2>Hello, World</h2><br>");
    }
}
