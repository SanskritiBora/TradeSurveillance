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
<body>
<link rel = "stylesheet"  type = "text/css" href="Styles/MyRetrieval.css">
<h1>Customer Orders</h1>
<table class="center" border="1">
<tr>
<td>Trade Id</td>
<td>Customer Names</td>
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
	ResultSet rs = null;
	pstmt=con.prepareStatement("select * from customerorders");
	rs=pstmt.executeQuery();
while(rs.next()){
%>
<tr>
<td><%=rs.getLong("tradeId") %></td>
<td><%=rs.getString("customerName") %></td>
<td><%=rs.getString("timestamp") %></td>
<td><%=rs.getString("company") %></td>
<td><%=rs.getString("tradeType") %></td>
<td><%=rs.getString("securityType") %></td>
<td><%=rs.getString("quantity") %></td>
<td><%=rs.getString("price") %></td>
<td><%=rs.getString("brokerName") %></td>
</tr>

<%
}
%>
</table>
<% 
con.close();
} catch (Exception e) {
e.printStackTrace();
}
%>
</table>
</body>
</html>