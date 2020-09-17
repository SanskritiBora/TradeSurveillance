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
				<th>Company</th>
				<th>Security</th>
				<th>Firm Time </th>
				<th>Client Time</th>
				<th>Firm Time</th>
				<th>Firm Type</th>
				<th>Client Type</th>
				<th>Firm Type</th>
				
				<th>Firm Quantity</th>
				<th>Client Quantity</th>
				<th>Firm Quantity</th>
				<th>Firm ID</th>
				<th>Client ID</th>
				<th>Firm ID</th>
			
				
				
			</tr>
			<%
try{
	Statement stmt = con.createStatement();
	PreparedStatement pstmt, ps = null;
	ResultSet rs = null, rs2 = null;
	rs=stmt.executeQuery("select * from fraud where fraudType = 'Front-running 3'");
while(rs.next()){
	long f1 = rs.getLong("firmtradeid");
	long c = rs.getLong("customertradeid");
	long f2 = rs.getLong("firmtradeid2");
	int q1=0,q2=0,q3=0;
	String tt1="",tt2="",tt3="";
	
%>
			<tr>
				<td><%=rs.getString("fraudtype") %></td>
				<% 
	ps = con.prepareStatement("select * from firmorders where tradeid = ?");
	ps.setLong(1, f1);
	rs2 = ps.executeQuery();				
	while(rs2.next()){
		q1=rs2.getInt("quantity");
		tt1=rs2.getString("tradeType");
%>
				<td><%=rs2.getString("company") %></td>
				<td><%=rs2.getString("securityType") %></td>
				<td><%=rs2.getString("timestamp") %></td>
				<%
	}
				ps = con.prepareStatement("select * from customerorders where tradeid = ?");
				ps.setLong(1, c);
				rs2 = ps.executeQuery();
				while(rs2.next()){
					q2=rs2.getInt("quantity");
					tt2=rs2.getString("tradeType");
%>
				
				<td><%=rs2.getString("timestamp") %></td>

				<%	
				}
				ps = con.prepareStatement("select * from firmorders where tradeid = ?");
				ps.setLong(1, f2);
				rs2 = ps.executeQuery();
				while(rs2.next()){
					q3=rs2.getInt("quantity");
					tt3=rs2.getString("tradeType");
%>
				
				<td><%=rs2.getString("timestamp") %></td>
				<td><%=tt1 %></td>
				<td><%=tt2 %></td>
				<td><%=tt3 %></td>
				<td><%=q1 %></td>
				<td><%=q2 %></td>
				<td><%=q3 %></td>
				<td><%=f1 %></td>
				<td><%=c %></td>
				<td><%=f2 %></td>
				<%
				}
				
}
	stmt.close();
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