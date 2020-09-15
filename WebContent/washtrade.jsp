<%@page language="java" import="java.sql.*" %> 
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

<h1>Wash Trades</h1>
<table border="1">
<tr>
<td>Firm tradeid 1</td>
<td>Firm tradeid 2</td>
</tr>
<%
try{
	PreparedStatement pstmt;
	ResultSet rs = null;
	pstmt=con.prepareStatement("select * from washtrade");
	rs=pstmt.executeQuery();
while(rs.next()){
%>
<tr>
<td><%=rs.getLong("firmtradeid") %></td>
<td><%=rs.getLong("firmtradeid2") %></td>
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