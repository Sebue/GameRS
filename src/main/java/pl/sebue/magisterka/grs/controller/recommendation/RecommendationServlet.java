package pl.sebue.magisterka.grs.controller.recommendation;

import org.hibernate.Session;
import pl.sebue.magisterka.grs.model.HibernateFactory;
import pl.sebue.magisterka.grs.model.data.dto.GameStatistic;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/recommendation")
public class RecommendationServlet extends HttpServlet {
    private static final String PARAMETER = "userId";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter writer = response.getWriter();
        response.setContentType("text/html");
        writer.println("<h2>Not implemented yet! but ...</h2><br>");

        String userId = request.getParameter(PARAMETER);
        long parsedUserId = Long.parseLong(userId);

        Session hibernateSession = HibernateFactory.INSTANCE.getSessionFactory().openSession();
        List<GameStatistic> gameStatisticList = hibernateSession.createQuery("from GameStatistic", GameStatistic.class).list();
        gameStatisticList.stream().filter(gs -> gs.getUserId().equals(parsedUserId)).forEach(gs -> writer.println("Game: " + gs.getGame().getName() + "; Played hours: " + gs.getPlayedHours() + "<br>"));
    }
}
