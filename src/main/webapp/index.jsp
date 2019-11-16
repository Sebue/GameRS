<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
<title>SDA Servlets - zajęcia drugie</title>
    <meta charset="utf-8">
</head>
<body>
<h1>Servlets 02</h1>

<h3>Servlet 1: Configuration</h3>
<a href="/servlets02/hi">Default</a>
<FORM ACTION="/servlets02/hi"
      METHOD="GET">
    Imię:
    <INPUT TYPE="TEXT" NAME="who"><BR>
    <INPUT TYPE="SUBMIT" VALUE="Wyslij">
</FORM>
<hr>
<h3>Servlet 2: Redirects</h3>
<a href="/servlets02/clientRedirect">Po stronie klienta</a><br>
<a href="/servlets02/serverRedirect">Po stronie serwera</a><br>
<hr>
<h3>Servlet 3: Filter</h3>
<a href="/servlets02/filter">Domyślne przywitanie bez parametru</a><br>
Z parametrem<br>
<FORM ACTION="/servlets02/filter"
      METHOD="GET">
    Użytkownik:
    <INPUT TYPE="TEXT" NAME="user"><BR>
    <INPUT TYPE="SUBMIT" VALUE="Wyślij">
</FORM>
<hr>
<h3>Servlet 4: JSP</h3>
<a href="/servlets02/jsp">Przekierowanie z servletu</a><br>
<a href="/servlets02/JspHelloWorld.jsp">Strona JSP</a>
<hr>
<h3>Servlet 6: Get and Post</h3>
<a href="/servlets02/getServlet">Get</a><br>
<a href="/servlets02/getPost.html">HTML</a><br>
<a href="/servlets02/postServlet">Post</a>
<hr>
<h3>Servlet 7: Cookies</h3>
<a href="/servlets02/cookie">Cookie</a><br>
<hr>
<h3>Servlet 8: Session</h3>
<a href="/servlets02/session">Session</a><br>
<hr>

</body>
</html>