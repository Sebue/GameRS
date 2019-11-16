<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="pl.sebue.magisterka.grs.model.HibernateFactory" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="pl.sebue.magisterka.grs.model.data.dto.GameStatistic" %>
<html>
<head>
<title>GRS - Sebastian Jasiński</title>
    <meta charset="utf-8">
</head>
<body>
<h1>GRS - GameRecommendationSystem</h1>
<br>
<h3>Get recommendation for user by id</h3>
<FORM ACTION="/GameRecommendationSystem/???"
      METHOD="GET">
    User ID:
    <INPUT TYPE="TEXT" NAME="userId"><BR>
    <INPUT TYPE="SUBMIT" VALUE="Send">
</FORM>
<hr>
... or choose user from list: <br>
    <%
        org.hibernate.Session sessionHibernate = new HibernateFactory().getSessionFactory().openSession();
        List<GameStatistic> gameStatisticsOnDb = sessionHibernate.createQuery("from GameStatistic", GameStatistic.class).list();
        Set<Long> users = gameStatisticsOnDb.stream().map(GameStatistic::getUserId).collect(Collectors.toSet());
        for (Long userId : users) {
           out.println("<a href=\"/GameRecommendationSystem/recommendation?userId=" + userId + "\">" + userId + "</a><br>");

        }
    %>




</body>
</html>