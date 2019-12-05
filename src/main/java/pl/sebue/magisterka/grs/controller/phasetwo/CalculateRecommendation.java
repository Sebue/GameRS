package pl.sebue.magisterka.grs.controller.phasetwo;

import org.grouplens.lenskit.RecommenderBuildException;
import org.hibernate.Session;
import pl.sebue.magisterka.grs.model.HibernateFactory;
import pl.sebue.magisterka.grs.model.algorithm.LenskitContentBasedRecommendation;
import pl.sebue.magisterka.grs.model.algorithm.SlopeOneCollaborativeRecomendation;
import pl.sebue.magisterka.grs.model.data.dto.GameStatistic;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@WebServlet("/calculate")
public class CalculateRecommendation extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Runnable slopeOne = () -> {
            Session session = HibernateFactory.INSTANCE.getSessionFactory().openSession();
            List<GameStatistic> gameStatisticsOnDb = session.createQuery("from GameStatistic", GameStatistic.class).list();
            SlopeOneCollaborativeRecomendation.slopeOne(gameStatisticsOnDb, Optional.empty());
        };
        Runnable lenskit = () -> {
            try {
                LenskitContentBasedRecommendation.predictToDb();
            } catch (RecommenderBuildException e) {
                e.printStackTrace();
            }
        };

        slopeOne.run();
        lenskit.run();

        PrintWriter writer = resp.getWriter();
        resp.setContentType("text/html");
        writer.println("<h2>Calculating ongoing - check DB or logs</h2><br>");
        req.getRequestDispatcher("/index.jsp").include(req, resp);
    }
}
