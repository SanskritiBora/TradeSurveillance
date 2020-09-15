<%@ page language="java" import="java.sql.*" %> 
<%@ page language="java" import="com.trade.Fraud" %> 
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="style7.css">
    <title>Trade Surveillance</title>
</head>

<body>
    <div class="topnav">
       	<a href="index.html">New Trade</a>
        <a href="retrieval.jsp">Customer Trade List</a>
        <a href="retrieval1.jsp">Firm Trade List</a>
        <a href="fraud.jsp">Frauds</a>
        <a href="washtrade.jsp">Wash Trades</a>
        <a href="graph.html">Graph</a>
    </div>
    <div class="login">
<%
	Connection con=null;
	PreparedStatement ps=null;
	long tradeid = Long.parseLong(request.getParameter("TradeID"));
	String customerName=request.getParameter("clientID");
	String trader = request.getParameter("Trader");
	String timestamp = request.getParameter("date");
	String company = request.getParameter("Company");
	String sectype = request.getParameter("security");
	String tradetype = request.getParameter("TradeType");
	int quantity = Integer.parseInt(request.getParameter("qty"));
	float price = Float.parseFloat(request.getParameter("Price"));
	String broker = request.getParameter("Broker");
	int seconds=0;
	int j[] = new int[3];
	String timeonly[] = timestamp.split("T");
    String individualTime[] = timeonly[1].split(":");
      for(int i = 0; i < 3; i++) { 
    	  j[i] = Integer.parseInt(individualTime[i]); 
      }
      seconds  = 3600 * j[0] + 60 * j[1] + j[2];
	String time = timeonly[0] + " " + timeonly[1];
	try{
		Class.forName("com.mysql.jdbc.Driver");
		
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tradesurveillance1", "root", "root");	
		
		if(trader.equals("Customer")){
			ps = con.prepareStatement("insert into customerorders values (?, ?, ?, ?, ?, ?, ?, ?, ?,?)");
			ps.setLong(1, tradeid);
			ps.setString(2,customerName);
			ps.setString(3, time);
			ps.setString(4,company);
			ps.setString(5, sectype);
			ps.setString(6, tradetype);
			ps.setInt(7, quantity);
			ps.setFloat(8, price);
			ps.setString(9,broker);
			ps.setInt(10,seconds);
			
		}
		else{
			ps = con.prepareStatement("insert into firmorders values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setLong(1, tradeid);
			
			ps.setString(2, time);
			ps.setString(3,company);
			ps.setString(4, sectype);
			ps.setString(5, tradetype);
			ps.setInt(6, quantity);
			ps.setFloat(7, price);
			ps.setString(8,broker);
			ps.setInt(9,seconds);
		}
		
		int i = ps.executeUpdate();
		if(i == 1){
			
%>
      <h1 class="head">Trade inserted sucessfully!</h1>
<%
		}else{
%>
      <h1 class="head">Error occurred while entering trade</h1>
<%
		}
		
		Fraud tr=new Fraud();
		if(trader.equals("Customer")){
			int num=tr.checkCustomerTrade(con, quantity, tradetype, company, seconds, tradeid, price);
			if(num>0){
				%>
			      <h1 class="head">New Frauds Detected!</h1>
			<%
					}else{
			%>
			      <h1 class="head">No Frauds Found!</h1>
			<%
			}
			
			
		}
		else{
			int num1=tr.checkFirmTrade(con, quantity, tradetype, company, seconds, tradeid, price);
			if(num1>0){
				%>
			      <h1 class="head">New Frauds Detected!</h1>
			<%
					}else{
			%>
			      <h1 class="head">No Frauds Found!</h1>
			<%
			}
			int num=tr.checkWashTrade(con, tradeid, tradetype, broker, company, quantity, price, seconds);
			if(num>0){
				%>
			      <h1 class="head">Wash Trade Detected!</h1>
			<%
					}else{
			%>
			      <h1 class="head">No WashTrade Found!</h1>
			<%
			}
			
		}
		
		
		
		
	}catch(Exception e){
		out.println(e);
	}
%> 
    </div>

    <p class="para" id="para"></p>
</body>
</html>
