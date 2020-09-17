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
			<a href="random.jsp">Data Generation</a>

	</div>
	
	<div class="topnav2">
		<a href="fraud.jsp">Front Running 1</a> <a href="fraud2.jsp">Front
			Running 2</a> <a href="fraud3.jsp">Front Running 3</a>
		<!-- <div class="quantity">
			<label for="Search">Search</label> <input type="integer" id="search"
				name="search">

		</div>
		 -->
	</div>

	<div class="table-wrapper">
		<table border="1" class="fl-table">
			<tr>
				<th>Fraud Type</th>
				<th>Firm tradeid 1</th>
				<th>Firm tradeid 2</th>
				<th>Customer tradeid</th>
			</tr>
			<%
try{
	PreparedStatement pstmt;
	ResultSet rs = null;
	pstmt=con.prepareStatement("select * from fraud where fraudType = 'Front-running 3'");
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
	</div>

	<p class="para" id="para"></p>


</body>

</html>