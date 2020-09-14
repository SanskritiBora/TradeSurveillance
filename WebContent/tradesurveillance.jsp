<%@ page language="java" import="java.sql.*" %> 
<%@ page language="java" import="com.trade.Fraud" %> 

<%
	Connection con=null;
	PreparedStatement ps=null;
	long tradeid = Long.parseLong(request.getParameter("TradeID"));
	String customerName=request.getParameter("Client");
	String trader = request.getParameter("Trader");
	String timestamp = request.getParameter("date");
	String company = request.getParameter("Company");
	String sectype = request.getParameter("security");
	String tradetype = request.getParameter("buyorsell");
	int quantity = Integer.parseInt(request.getParameter("qty"));
	int price = Integer.parseInt(request.getParameter("Price"));
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
		
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tradesurveillance", "root", "root");	
		
		if(trader.equals("Client")){
			ps = con.prepareStatement("insert into customerorders values (?, ?, ?, ?, ?, ?, ?, ?, ?,?)");
			ps.setLong(1, tradeid);
			ps.setString(2,customerName);
			ps.setString(3, time);
			ps.setString(4,company);
			ps.setString(5, sectype);
			ps.setString(6, tradetype);
			ps.setInt(7, quantity);
			ps.setInt(8, price);
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
			ps.setInt(7, price);
			ps.setString(8,broker);
			ps.setInt(9,seconds);
		}
		
		int i = ps.executeUpdate();
		if(i == 1){
			out.println("Trade inserted successfully");
		}else{
			out.println("Trade is not inserted properly");
		}
		
		Fraud tr=new Fraud();
		ps.executeUpdate("TRUNCATE fraud");
		ps.executeUpdate("TRUNCATE washtrade");
		//out.println("\n-------------------------------------------------------------------------------\nScenario 1\n-------------------------------------------------------------------------------");
		tr.scenario1(con);
		//out.println("\n-------------------------------------------------------------------------------\nScenario 2\n-------------------------------------------------------------------------------");
		tr.scenario2(con);
		//out.println("\n-------------------------------------------------------------------------------\nScenario 3\n-------------------------------------------------------------------------------");
		tr.scenario3(con);
		//System.out.println("\n-------------------------------------------------------------------------------\nWash Trade\n-------------------------------------------------------------------------------");
		tr.washTrades(con);
		
		
		
	}catch(Exception e){
		out.println(e);
	}
%>

