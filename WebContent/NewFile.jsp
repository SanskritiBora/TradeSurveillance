<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
String trader = request.getParameter("Trader");
out.println(trader);
%>
</body>
</html>

<!DOCTYPE html>
<html>
<body>

<h1>Retrieve data from database in jsp</h1>
<table border="1">
<tr>
<td>Trade Id</td>
<td>Customer Name</td>
<td>Trader</td>
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
<td><%=rs.getInt("tradeId") %></td>
<td><%=rs.getInt("customerName") %></td>
<td><%=rs.getString("trader") %></td>
<td><%=rs.getString("timestamp") %></td>
<td><%=rs.getString("company") %></td>
<td><%=rs.getString("tradeType") %></td>
<td><%=rs.getString("securityType") %></td>
<td><%=rs.getInt("quantity") %></td>
<td><%=rs.getInt("price") %></td>
<td><%=rs.getString("brokerName") %></td>
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