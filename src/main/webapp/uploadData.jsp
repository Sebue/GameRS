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
<body style="background-color:#7D5BA6;color:#32292F">
<h1>GRS - GameRecommendationSystem</h1>
<br>
<h3>Upload data from your files:</h3>
<FORM ACTION="/GameRecommendationSystem/uploadData" enctype="multipart/form-data"
      METHOD="GET">
    GameStatistics:
    <INPUT TYPE="FILE" NAME="gameStatistics"><BR>
    Games:
    <INPUT TYPE="FILE" NAME="games"><BR>
    <INPUT TYPE="SUBMIT" VALUE="Send">
</FORM>
<hr>
... or choose <a href="/GameRecommendationSystem/uploadData">predefined data.</a>
</body>
</html>