<%@ page language="java" import="java.sql.*" %> 
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%
	Connection con=null;
	try{
		Class.forName("com.mysql.jdbc.Driver");
		java.sql.Connection conn;
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tradesurveillance1", "root", "root");	
	}catch(Exception e){
		out.println(e);
	}
%>

<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="style3.css">

<link rel = "stylesheet"  type = "text/css" href="Styles/MyRetrieval.css">
</head>
<body>

<h1>Fraud list</h1>

        <div class="topnav">
            <a href="fraud.jsp">Front Running 1</a>
            <a href="fraud2.jsp">Front Running 2</a>
			<a href="fraud3.jsp">Front Running 3</a>
			<div class="quantity">
                        <label for="Search">Search</label>
                        <input type="integer" id="search" name="search">
                       
                    </div>
			
        </div>

<table border="1">
<tr>
<td>Fraud Type</td>
<td>Firm tradeid 1</td>
<td>Firm tradeid 2</td>
<td>Customer tradeid</td>
</tr>
<%
try{
	PreparedStatement pstmt;
	ResultSet rs = null;
	pstmt=con.prepareStatement("select * from fraud where fraudType = 'Front-running 1'");
	rs=pstmt.executeQuery();
while(rs.next()){
%>
<tr>
<td><%=rs.getString("fraudtype") %></td>
<td><%=rs.getLong("firmtradeid") %></td>
<td><%=rs.getLong("firmtradeid2") %></td>
<td><%=rs.getLong("customertradeid") %></td>
</tr>
<%
}
con.close();
} catch (Exception e) {
e.printStackTrace();
}
%>
</table>
</body>
</html>