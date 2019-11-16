package pl.sebue.magisterka.grs.controller.phaseone;

import pl.sebue.magisterka.grs.model.data.loader.CsvReader;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/uploadData")
public class UploadDataServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        File gamesCsv = new File(getClass().getResource("games.csv").getFile());
        File gamesStatisticCsv = new File(getClass().getResource("players.csv").getFile());
        CsvReader csvReader = new CsvReader(gamesCsv.getPath(), gamesStatisticCsv.getPath());
        PrintWriter writer = response.getWriter();
        response.setContentType("text/html");
        writer.println("<h2>Hello, World</h2><br>");
    }
}
