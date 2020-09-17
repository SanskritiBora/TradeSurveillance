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

	<div class="table-wrapper">
		<table border="1" class="fl-table">
			<tr>
				<th>Company</th>
				<th>Security</th>
				<th>Broker</th>
				<th>Quantity</th>
				<th>Price</th>
				<th>Firm Time 1 </th>
					<th>Firm Type 1</th>
				<th>Firm Time 2 </th>
				<th>Firm Type 2</th>
			
				<th>Firm ID 1</th>
				<th>Firm ID 2</th>
			
			
				
				
			</tr>
			<%
			try{
				Statement stmt = con.createStatement();
				PreparedStatement pstmt, ps = null;
				ResultSet rs = null, rs2 = null;
				rs=stmt.executeQuery("select * from washtrade");
			while(rs.next()){
				long f1 = rs.getLong("firmtradeid");
				long f2 = rs.getLong("firmtradeid2");
			%>
						<tr>
							<% 
				ps = con.prepareStatement("select * from firmorders where tradeid = ?");
				ps.setLong(1, f1);
				rs2 = ps.executeQuery();				
				while(rs2.next()){
			%>
							<td><%=rs2.getString("company") %></td>
							<td><%=rs2.getString("securityType") %></td>
							<td><%=rs2.getString("brokerName") %></td>
							<td><%=rs2.getString("quantity") %></td>
							<td><%=rs2.getString("price") %></td>
							<td><%=rs2.getString("timestamp") %></td>
							<td><%=rs2.getString("tradeType") %></td>
							<%
				}
							ps = con.prepareStatement("select * from firmorders where tradeid = ?");
							ps.setLong(1, f2);
							rs2 = ps.executeQuery();
							while(rs2.next()){
			%>
							
							<td><%=rs2.getString("timestamp") %></td>
							<td><%=rs2.getString("tradeType") %></td>
							<%	
							}
							%>
							
							<td><%=f1 %></td>
							<td><%=f2 %></td>
						
							<%
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


