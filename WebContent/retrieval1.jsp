<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
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
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tradesurveillance", "root", "root");	
	}catch(Exception e){
		out.println(e);
	}
%>

<!DOCTYPE html>
<html>
<head>
<link rel = "stylesheet"  type = "text/css" href="Styles/MyRetrieval.css">
</head>
<body>

<h1>Firm Orders</h1>
<table class="center" border="1">
<tr>
<td>Trade Id</td>
<td>Timestamp</td>
<td>Company</td>
<td>Trade Type</td>
<td>Security Type</td>
<td>Quantity</td>
<td>Price</td>
<td>Broker Name</td>

</tr>

<%
try{
	PreparedStatement pstmt;
	ResultSet rs1 = null;
	pstmt=con.prepareStatement("select * from firmorders");
	rs1=pstmt.executeQuery();
while(rs1.next()){
%>
<tr>
<td><%=rs1.getLong("tradeId") %></td>
<td><%=rs1.getString("timestamp") %></td>
<td><%=rs1.getString("company") %></td>
<td><%=rs1.getString("tradeType") %></td>
<td><%=rs1.getString("securityType") %></td>
<td><%=rs1.getString("quantity") %></td>
<td><%=rs1.getString("price") %></td>
<td><%=rs1.getString("brokerName") %></td>
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
