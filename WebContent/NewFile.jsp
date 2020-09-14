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
	long tradeid = Long.parseLong(request.getParameter("TradeID"));
	out.println("tradeid :" + tradeid);
	String customerName=request.getParameter("Client");
	out.println("customerName :" + customerName);
	String trader = request.getParameter("Trader");
	out.println("trader :" + trader);
	String timestamp = request.getParameter("date");
	out.println("timestamp :" + timestamp);
	String company = request.getParameter("Company");
	out.println("company :" + company);
	String sectype = request.getParameter("security");
	out.println("sectype :" + sectype);
	String tradetype = request.getParameter("buyorsell");
	out.println("tradetype :" + tradetype);
	int quantity = Integer.parseInt(request.getParameter("qty"));
	out.println("quantity :" + quantity);
	int price = Integer.parseInt(request.getParameter("Price"));
	out.println("price :" + price);
	String broker = request.getParameter("Broker");
	out.println("broker :" + broker);
	int seconds=0;
	int j[] = new int[3];
	String timeonly[] = timestamp.split("T");
    String individualTime[] = timeonly[1].split(":");
      for(int i = 0; i < 3; i++) { 
    	  j[i] = Integer.parseInt(individualTime[i]); 
      }
      seconds  = 3600 * j[0] + 60 * j[1] + j[2];
	  out.println("seconds :" + seconds);
	  if (j[0] > 12){
		j[0] -= 12;
		timeonly[1].concat(" PM");
	  }else{
		timeonly[1].concat(" AM");
	  }
	String time = timeonly[0] + " " + timeonly[1];
	out.println("time :" + time);
%>
</body>
</html>