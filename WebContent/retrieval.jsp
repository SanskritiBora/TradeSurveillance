<%@ page language="java" import="java.sql.*"%>
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
<html lang="en">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="style7.css">
<title>Trade Surveillance</title>
</head>

<body>

	<!-- <img src="image.jpg"> -->
	<div class="topnav">
		<a href="index.html">New Trade</a> <a href="retrieval.jsp">Customer
			Trade List</a> <a href="retrieval1.jsp">Firm Trade List</a> <a
			href="fraud.jsp">Frauds</a> <a href="washtrade.jsp">Wash Trades</a> <a
			href="graph.html">Graph</a>

	</div>

	<div class="table-wrapper">
		<table border="1" class="fl-table">
			<tr>
				<th>Trade Id</th>
				<th>Customer Names</th>
				<th>Timestamp</th>
				<th>Company</th>
				<th>Trade Type</th>
				<th>Security Type</th>
				<th>Quantity</th>
				<th>Price</th>
				<th>Broker Name</th>

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
con.close();
} catch (Exception e) {
e.printStackTrace();
}
%>
		</table>
	</div>

	<p class="para" id="para"></p>


</body>

</html>
