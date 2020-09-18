<%@ page language="java" import="java.sql.*" %>
<%@ page language="java" import="com.trade.Fraud" %>
<%@ page language="java" import = "java.util.Random" %>


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
		<a href="random.jsp">Data Generation</a>
	</div>
	<div class="login">
	<%
	Connection con=null;
	PreparedStatement ps=null;
	String trader="";
	int quantity=0;
	String company="";
	String sectype="";
	String tradetype="";
	int price=0;
	int seconds=0;
	String broker="";
	int tradeid=0;
	String customerName="";
	int count_Firm=0;
	int count_Customer=0;
	
	try{
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tradesurveillance1", "root", "root");	
		
		Statement statement = con.createStatement();
		statement.executeUpdate("TRUNCATE customerorders");
		statement.executeUpdate("TRUNCATE firmorders");
		statement.executeUpdate("TRUNCATE fraud");
		statement.executeUpdate("TRUNCATE washtrade");
		
		
		
		Random CustomerNameNum = new Random();
		Random T_typeNum = new Random();
		Random CompanyNum = new Random();
		
		Random TradetypeNum = new Random();
		//Random QuantityNum = new Random();
		Random PriceNum = new Random();
		Random BrokerNum = new Random();
		
		Random Index = new Random();
		int[] qty = new int[]{ 100,150,200,250,300,350,400,450,500,550,600,650,700,750,800,850,900 ,950,1000}; 

		


		for (int i=1;i<1000;i++){		
			
			tradeid+=1;
			int customerNameNum = CustomerNameNum.nextInt(5)+1;
			int trader_typeNum = T_typeNum.nextInt(2)+1;
			int companyNum = CompanyNum.nextInt(3)+1;
			
			int index = Index.nextInt(19);
			quantity = qty[index];
			
			int tradetypeNum = TradetypeNum.nextInt(2)+1;
			//int quantityNum = (int)(Math.random() * (520 - 510 + 1) + 510);
			int priceNum =  (int)(Math.random() * (100 - 90 + 1) + 90);
			int brokerNum = BrokerNum.nextInt(3)+1;
			
			
			
			

			int hh_num = (int)(Math.random() * (13 - 10 + 1) + 10);
			int mm_num = (int)(Math.random() * (59 - 56 + 1) + 56);
			int ss_num = (int)(Math.random() * (59 - 56 + 1) + 56);
			
			
			
			String sb="";
			sb="08-09-2020 "+hh_num+":"+(mm_num)+":"+(ss_num);
			//out.println(sb);
			double seconds1 = (hh_num*3600+mm_num*60+ss_num);
			
			Double newData = new Double(seconds1);
			seconds = newData.intValue();

			
			if(customerNameNum==1)
				customerName = "Himanshu Upadhyay";
			else if(customerNameNum==2)
				customerName = "Sanskriti Bora";
			else if(customerNameNum==3)
				customerName = "Shravani Shah";
			else if(customerNameNum==4)
				customerName = "Nilesh Kavhale";
			else if(customerNameNum==5)
				customerName = "Krutika Padeer";
			

			trader = ( trader_typeNum==1)? "Firm": "Customer";
			//out.println(trader);
			
			if(companyNum==1)
				company = "Facebook";
			else if(companyNum==2)
				company = "Walmart";
			else if(companyNum==3)
				company = "Apple";

			
			sectype = "Shares";
			
			 tradetype = (tradetypeNum==1)? "BUY": "SELL";
			
			//quantity = quantityNum;
			price = priceNum;
			
			
			if(brokerNum==1)
				broker = "Interactive Brokers";
			else if(brokerNum == 2)
				broker = "Fidelity Investments";
			else if(brokerNum == 3)
				broker = "Merrill Edge";
			

			
			if(trader.equals("Customer")){

			ps = con.prepareStatement("insert into customerorders  values ( ?, ?, ?, ?, ?, ?, ?,?,?,?)");
			ps.setInt(1,i);
			ps.setString(2,customerName);
			ps.setString(3,sb);
			ps.setString(4,company);
			ps.setString(5, sectype);
			ps.setString(6, tradetype);
			ps.setInt(7, quantity);
			ps.setFloat(8, price);
			ps.setString(9,broker);
			ps.setInt(10,seconds);
			
			count_Customer+=ps.executeUpdate();
			
			
			}
			else{
				ps = con.prepareStatement("insert into firmorders  values ( ?, ?, ?, ?, ?, ?, ?,?,?)");
				ps.setInt(1,i);
				ps.setString(2,sb);
				ps.setString(3,company);
				ps.setString(4, sectype);
				ps.setString(5, tradetype);
				ps.setInt(6, quantity);
				ps.setFloat(7, price);
				ps.setString(8,broker);
				ps.setInt(9,seconds);
				
				
				count_Firm+=ps.executeUpdate();
				
				
				
			
			}
		
		}
		%>
	      <h1 class="head">Records inserted in Customer Orders :<%=count_Customer %></h1>
	      <h1 class="head">Records inserted in Firm Orders :<%=count_Firm %></h1>
	<%

		Fraud tr=new Fraud();
		tr.scenario1(con);
		//System.out.println("\n-------------------------------------------------------------------------------\nScenario 2\n-------------------------------------------------------------------------------");
		tr.scenario2(con);
		//System.out.println("\n-------------------------------------------------------------------------------\nScenario 3\n-------------------------------------------------------------------------------");
		tr.scenario3(con);
		//System.out.println("\n-------------------------------------------------------------------------------\nWash Trade\n-------------------------------------------------------------------------------");
		tr.washTrades(con);
		statement.close();
		con.close();
		
		
		
		
	}catch(Exception e){
		out.println(e);
	}
%>
	</div>

	<p class="para" id="para"></p>
</body>

</html>