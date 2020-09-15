package com.trade;

import java.sql.*;

public class Fraud {

	public void scenario1(Connection connection) throws Exception {
		int recordsInserted = 0;
		long firmTradeId1, firmTradeId2;
		Statement statement = connection.createStatement();
		PreparedStatement ps = null;
		ResultSet customerOrders = statement.executeQuery("SELECT * FROM customerorders WHERE quantity > 500 and tradeType='BUY'");
		while(customerOrders.next()) {
			long tradeid = customerOrders.getLong("tradeid");
			////System.out.print("Customer :\t " + tradeid + "\t");
			String company = customerOrders.getString("company");
			//System.out.print(company + "\t");
			int seconds = customerOrders.getInt("seconds");
			//System.out.println(seconds);

			ps = connection.prepareStatement("SELECT * FROM firmorders WHERE company = ? AND (seconds BETWEEN ? AND ?) AND tradeType = 'BUY'", ResultSet.TYPE_SCROLL_SENSITIVE, 
					ResultSet.CONCUR_UPDATABLE); 
			ps.setString(1, company);
			ps.setInt(2, seconds - 60);
			ps.setInt(3,  seconds-1);
			ResultSet firmOrdersBefore = ps.executeQuery();  

			ps = connection.prepareStatement("SELECT * FROM firmorders WHERE company = ? AND (seconds BETWEEN ? AND ?) AND tradeType = 'SELL'", ResultSet.TYPE_SCROLL_SENSITIVE, 
					ResultSet.CONCUR_UPDATABLE); 
			ps.setString(1, company);
			ps.setInt(2, seconds+1);
			ps.setInt(3,  seconds + 60);
			ResultSet firmOrdersAfter = ps.executeQuery();  
			if((firmOrdersBefore.next()) && (firmOrdersAfter.next())) { 
				firmOrdersBefore.beforeFirst();
				firmOrdersAfter.beforeFirst();
				while(firmOrdersBefore.next()){  
					while(firmOrdersAfter.next()){  
						if(firmOrdersBefore.getInt("quantity") == firmOrdersAfter.getInt("quantity") ) {
							//System.out.println("Front Running 1 found: fraud between "+ firmOrdersBefore.getLong("tradeid") + " and " + firmOrdersAfter.getLong("tradeid") + " with customer :" + customerOrders.getLong("tradeid"));
							ps = connection.prepareStatement("insert into fraud (fraudtype, firmtradeid, firmtradeid2, customertradeid) values (?, ?, ?, ?);");  
							ps.setString(1, "Front-running 1");
							ps.setLong(4, tradeid);
							firmTradeId1 = firmOrdersBefore.getLong("tradeid");
							ps.setLong(2, firmTradeId1);
							firmTradeId2 = firmOrdersAfter.getLong("tradeid");
							ps.setLong(3, firmTradeId2);
							recordsInserted += ps.executeUpdate();  
						}
					}
				}
			}
			//System.out.println("****************************************************");
		}
		//System.out.println(recordsInserted +" Record inserted successfully");
		customerOrders.close();	
		statement.close();		
	}

	public void scenario2(Connection connection) throws SQLException {
		int recordsInserted = 0;
		long firmTradeId;
		Statement statement = connection.createStatement();
		PreparedStatement ps = null;
		ResultSet customerOrders = statement.executeQuery("SELECT * FROM customerorders WHERE quantity > 500 and tradeType='SELL'");	
		while(customerOrders.next()) {
			long tradeid = customerOrders.getLong("tradeid");
			//System.out.print("Customer :\t " + tradeid + "\t");
			String company = customerOrders.getString("company");
			//System.out.print(company + "\t");
			int quantity=customerOrders.getInt("quantity");
			//System.out.print(quantity + "\t");
			int seconds = customerOrders.getInt("seconds");
			//System.out.println(seconds);


			ps = connection.prepareStatement("SELECT * FROM firmorders WHERE company = ? AND  (seconds BETWEEN ? AND ?) and tradeType='SELL'", ResultSet.TYPE_SCROLL_SENSITIVE, 
					ResultSet.CONCUR_UPDATABLE);
			ps.setString(1, company);
			ps.setInt(2, seconds - 60);
			ps.setInt(3, seconds);
			ResultSet firmOrders = ps.executeQuery();
			if(firmOrders.next()) {
				firmOrders.beforeFirst();
				while(firmOrders.next()) {
					//System.out.println("Before : \t" + firmOrders.getLong("tradeid") + "\t" + firmOrders.getString("tradeType") + "\t" +  firmOrders.getString("seconds") + "\t" + firmOrders.getString("quantity"));
					//System.out.println("Front Running 2 found: fraud between "+ firmOrders.getLong("tradeid") + " with customer :" + customerOrders.getLong("tradeid"));
					ps = connection.prepareStatement("insert into fraud (fraudtype, firmtradeid, customertradeid) values (?, ?, ?);");  
					ps.setString(1, "Front-running 2");
					ps.setLong(3, tradeid);
					firmTradeId = firmOrders.getLong("tradeid");
					ps.setLong(2, firmTradeId);
					recordsInserted += ps.executeUpdate();  
				}
			}
			//System.out.println("****************************************************");
		}
		//System.out.println(recordsInserted +" Record inserted successfully");
		customerOrders.close();
		statement.close();	
	}

	public void scenario3(Connection connection) throws Exception {
		int recordsInserted = 0;
		long firmTradeId1;
		long firmTradeId2;
		Statement statement = connection.createStatement();
		PreparedStatement ps = null;
		ResultSet customerOrders = statement.executeQuery("SELECT * FROM customerorders WHERE quantity > 500 and tradeType='BUY'");
		while(customerOrders.next()) {			
			long tradeid = customerOrders.getLong("tradeid");
			//System.out.print("Customer :\t " + tradeid + "\t");
			String company = customerOrders.getString("company");
			//System.out.print(company + "\t");
			int seconds = customerOrders.getInt("seconds");
			//System.out.print(seconds+"\t");
			int quantity = customerOrders.getInt("quantity");
			int price = customerOrders.getInt("price");
			//System.out.println(quantity);

			ps = connection.prepareStatement("SELECT * FROM firmorders WHERE company = ? AND (seconds BETWEEN ? AND ?) AND tradeType = 'BUY' AND quantity= ?", ResultSet.TYPE_SCROLL_SENSITIVE, 
					ResultSet.CONCUR_UPDATABLE); 
			ps.setString(1, company);
			ps.setInt(2, seconds - 60);
			ps.setInt(3,  seconds);
			ps.setInt(4,quantity);
			ResultSet firmOrdersBefore = ps.executeQuery();  

			ps = connection.prepareStatement("SELECT * FROM firmorders WHERE company = ? AND seconds=? AND tradeType = 'SELL' AND quantity = ? AND price = ?", ResultSet.TYPE_SCROLL_SENSITIVE, 
					ResultSet.CONCUR_UPDATABLE); 
			ps.setString(1, company);
			ps.setInt(2, seconds);
			ps.setInt(3,quantity);
			ps.setInt(4, price);

			ResultSet firmOrdersOnTime = ps.executeQuery(); 


			if((firmOrdersBefore.next()) && (firmOrdersOnTime.next())) { 

				firmOrdersBefore.beforeFirst();
				firmOrdersOnTime.beforeFirst();

				while(firmOrdersBefore.next()){  

					while(firmOrdersOnTime.next()){  
						if(firmOrdersBefore.getInt("price") < firmOrdersOnTime.getInt("price")) {
							//System.out.println("Front Running 3 found: fraud between "+ firmOrdersBefore.getLong("tradeid") + " and " + firmOrdersOnTime.getLong("tradeid") + " with customer :" + customerOrders.getLong("tradeid"));
							ps = connection.prepareStatement("insert into fraud (fraudtype, firmtradeid, firmtradeid2, customertradeid) values (?, ?, ?, ?);");  
							ps.setString(1, "Front-running 3");
							ps.setLong(4, tradeid);
							firmTradeId1 = firmOrdersBefore.getLong("tradeid");
							ps.setLong(2, firmTradeId1);
							firmTradeId2 = firmOrdersOnTime.getLong("tradeid");
							ps.setLong(3, firmTradeId2);
							recordsInserted += ps.executeUpdate();  
						}
					}
				}
			}
			//System.out.println("****************************************************");
		}
		//System.out.println(recordsInserted +" Record inserted successfully");
		customerOrders.close();
		statement.close();

	}

	public void washTrades(Connection connection) throws Exception {
		int recordsInserted = 0;
		long tradeId1, tradeId2;
		Statement statement = connection.createStatement();
		PreparedStatement ps = null;
		ResultSet washTrades = statement.executeQuery("SELECT * FROM firmorders a, firmorders b WHERE a.tradeid != b.tradeid AND a.tradeType != b.tradeType AND a.brokername = b.brokername AND a.company = b.company AND a.quantity = b.quantity AND a.price = b.price");
		while(washTrades.next()) {			
			String tradeType1 = washTrades.getString("a.tradeType");
			int seconds1 = washTrades.getInt("a.seconds");
			int seconds2 = washTrades.getInt("b.seconds");
			if(seconds1 <= seconds2) {
				if(tradeType1.equals("BUY")) {
					//System.out.println("WASH TRADE:");
					//System.out.println( "Tradeid1 :\t" + washTrades.getInt("a.tradeid") + "\t" + washTrades.getString("a.tradeType") + "\t" + washTrades.getInt("a.seconds"));
					//System.out.println( "Tradeid2 :\t" + washTrades.getInt("b.tradeid") + "\t" + washTrades.getString("b.tradeType") + "\t" + washTrades.getInt("b.seconds"));
					ps = connection.prepareStatement("insert into washtrade (firmtradeid, firmtradeid2) values(?,?);");  
					tradeId1 = washTrades.getLong("a.tradeid");
					ps.setLong(1, tradeId1);
					tradeId2 = washTrades.getLong("b.tradeid");
					ps.setLong(2, tradeId2);
					recordsInserted += ps.executeUpdate();  
				}
			}
			//System.out.println("****************************************************");
		}
		//System.out.println(recordsInserted +" Record inserted successfully");
		washTrades.close();
		statement.close();

	}

	public void checkCustomerTrade(Connection connection, int quantity, String tradeType, String company, int seconds, long tradeid,int price ) throws SQLException{
		int recordsInserted = 0;
		long tradeId1, tradeId2;
		PreparedStatement ps=null;

		if(quantity>500 && tradeType.equals("BUY")) {
			ps = connection.prepareStatement("SELECT * FROM firmorders WHERE company = ? AND (seconds BETWEEN ? AND ?) AND tradeType = 'BUY'", ResultSet.TYPE_SCROLL_SENSITIVE, 
					ResultSet.CONCUR_UPDATABLE); 
			ps.setString(1, company);
			ps.setInt(2, seconds - 60);
			ps.setInt(3,  seconds);
			ResultSet firmOrdersBefore = ps.executeQuery();  

			ps = connection.prepareStatement("SELECT * FROM firmorders WHERE company = ? AND (seconds BETWEEN ? AND ?) AND tradeType = 'SELL'", ResultSet.TYPE_SCROLL_SENSITIVE, 
					ResultSet.CONCUR_UPDATABLE); 
			ps.setString(1, company);
			ps.setInt(2, seconds + 1);
			ps.setInt(3,  seconds + 60);
			ResultSet firmOrdersAfter = ps.executeQuery();  
			if((firmOrdersBefore.next()) && (firmOrdersAfter.next())) { 
				firmOrdersBefore.beforeFirst();
				firmOrdersAfter.beforeFirst();

				while(firmOrdersBefore.next()){  
					while(firmOrdersAfter.next()){  
						if(firmOrdersBefore.getInt("quantity") == firmOrdersAfter.getInt("quantity") ) {
							//System.out.println("Front Running 1 found: fraud between "+ firmOrdersBefore.getLong("tradeid") + " and " + firmOrdersAfter.getLong("tradeid") + " with customer :" + customerOrders.getLong("tradeid"));
							ps = connection.prepareStatement("insert into fraud (fraudtype, firmtradeid, firmtradeid2, customertradeid) values (?, ?, ?, ?);");  
							ps.setString(1, "Front-running 1");
							ps.setLong(4, tradeid);
							tradeId1 = firmOrdersBefore.getLong("tradeid");
							ps.setLong(2, tradeId1);
							tradeId2 = firmOrdersAfter.getLong("tradeid");
							ps.setLong(3, tradeId2);
							recordsInserted += ps.executeUpdate();  
						}
					}
				}
			}
			//System.out.println(recordsInserted +" Record inserted successfully");	
			System.out.println("****************************************************");
		}

		if(quantity>500 && tradeType.equals("SELL")) {    //2
			long firmTradeId;

			ps = connection.prepareStatement("SELECT * FROM firmorders WHERE company = ? AND  (seconds BETWEEN ? AND ?) and tradeType='SELL'", ResultSet.TYPE_SCROLL_SENSITIVE, 
					ResultSet.CONCUR_UPDATABLE);
			ps.setString(1, company);
			ps.setInt(2, seconds - 60);
			ps.setInt(3, seconds);
			ResultSet firmOrders = ps.executeQuery();
			if(firmOrders.next()) {
				firmOrders.beforeFirst();
				while(firmOrders.next()) {
					//System.out.println("Before : \t" + firmOrders.getLong("tradeid") + "\t" + firmOrders.getString("tradeType") + "\t" +  firmOrders.getString("seconds") + "\t" + firmOrders.getString("quantity"));
					//System.out.println("Front Running 2 found: fraud between "+ firmOrders.getLong("tradeid") + " with customer :" + customerOrders.getLong("tradeid"));
					ps = connection.prepareStatement("insert into fraud (fraudtype, firmtradeid, customertradeid) values (?, ?, ?);");  
					ps.setString(1, "Front-running 2");
					ps.setLong(3, tradeid);
					firmTradeId = firmOrders.getLong("tradeid");
					ps.setLong(2, firmTradeId);
					recordsInserted += ps.executeUpdate();  
				}
			}
			//System.out.println(recordsInserted +" Record inserted successfully");	
			System.out.println("****************************************************");
		}

		if(quantity>500 && tradeType.equals("BUY")) {
			long firmtradeId1, firmtradeId2;

			ps = connection.prepareStatement("SELECT * FROM firmorders WHERE company = ? AND (seconds BETWEEN ? AND ?) AND tradeType = 'BUY' AND quantity= ?", ResultSet.TYPE_SCROLL_SENSITIVE, 
					ResultSet.CONCUR_UPDATABLE); 
			ps.setString(1, company);
			ps.setInt(2, seconds - 60);
			ps.setInt(3,  seconds);
			ps.setInt(4,quantity);
			ResultSet firmOrdersBefore = ps.executeQuery();  

			ps = connection.prepareStatement("SELECT * FROM firmorders WHERE company = ? AND seconds=? AND tradeType = 'SELL' AND quantity=? and price=?", ResultSet.TYPE_SCROLL_SENSITIVE, 
					ResultSet.CONCUR_UPDATABLE); 
			ps.setString(1, company);
			ps.setInt(2, seconds);
			ps.setInt(3,quantity);
			ps.setInt(4, price);

			ResultSet firmOrdersOnTime = ps.executeQuery(); 


			if((firmOrdersBefore.next()) && (firmOrdersOnTime.next())) { 

				firmOrdersBefore.beforeFirst();
				firmOrdersOnTime.beforeFirst();
				while(firmOrdersBefore.next()){  

					while(firmOrdersOnTime.next()){  
						if(firmOrdersBefore.getInt("price") < firmOrdersOnTime.getInt("price")) {
							//System.out.println("Front Running 3 found: fraud between "+ firmOrdersBefore.getLong("tradeid") + " and " + firmOrdersOnTime.getLong("tradeid") + " with customer :" + customerOrders.getLong("tradeid"));
							ps = connection.prepareStatement("insert into fraud (fraudtype, firmtradeid, firmtradeid2, customertradeid) values (?, ?, ?, ?);");  
							ps.setString(1, "Front-running 3");
							ps.setLong(4, tradeid);
							firmtradeId1 = firmOrdersBefore.getLong("tradeid");
							ps.setLong(2, firmtradeId1);
							firmtradeId2 = firmOrdersOnTime.getLong("tradeid");
							ps.setLong(3, firmtradeId2);
							recordsInserted += ps.executeUpdate();  
						}
					}
				}


			}
			//System.out.println("****************************************************");

			//System.out.println(recordsInserted +" Record inserted successfully");	
			System.out.println("****************************************************");
		}


	}

	public void checkFirmTrade(Connection connection, int quantity, String tradeType, String company, int seconds, long tradeid, int price) throws SQLException{
		int recordsInserted=0;
		long tradeId1;
		long tradeId2;
		PreparedStatement ps=null;

		//scenario 1
		if(tradeType.equals("BUY")) {
			ps = connection.prepareStatement("SELECT * FROM customerorders WHERE company = ? AND (seconds BETWEEN ? AND ?) AND tradeType = 'BUY' and quantity > 500", ResultSet.TYPE_SCROLL_SENSITIVE, 
					ResultSet.CONCUR_UPDATABLE); 
			ps.setString(1, company);
			ps.setInt(2, seconds+1);
			ps.setInt(3,  seconds+60);
			ResultSet customerOrders1 = ps.executeQuery();  

			ps = connection.prepareStatement("SELECT * FROM firmorders WHERE company = ? AND (seconds BETWEEN ? AND ?) AND tradeType = 'SELL' and quantity = ?", ResultSet.TYPE_SCROLL_SENSITIVE, 
					ResultSet.CONCUR_UPDATABLE); 
			ps.setString(1, company);
			ps.setInt(2, seconds+61);
			ps.setInt(3,  seconds + 120);
			ps.setInt(4,  quantity);
			ResultSet firmOrdersAfter = ps.executeQuery();  
			System.out.println("Entering");
			if((customerOrders1.next()) && (firmOrdersAfter.next())) {
				System.out.println("in");
				customerOrders1.beforeFirst();
				firmOrdersAfter.beforeFirst();

				while(customerOrders1.next()){  
					while(firmOrdersAfter.next()){  
						//System.out.println("Front Running 1 found: fraud between "+ customerOrders1.getLong("tradeid") + " and " + firmOrdersAfter.getLong("tradeid") + " with customer :" + customerOrders.getLong("tradeid"));
						ps = connection.prepareStatement("insert into fraud (fraudtype, firmtradeid, firmtradeid2, customertradeid) values (?, ?, ?, ?);");  
						ps.setString(1, "Front-running 1");
						ps.setLong(2, tradeid);
						tradeId1 = customerOrders1.getLong("tradeid");
						ps.setLong(4, tradeId1);
						tradeId2 = firmOrdersAfter.getLong("tradeid");
						ps.setLong(3, tradeId2);
						recordsInserted += ps.executeUpdate();  
					}
				}
			}
			//System.out.println(recordsInserted +" Record inserted successfully");	
			System.out.println("****************************************************");
		}
		else {
			ps = connection.prepareStatement("SELECT * FROM customerorders WHERE company = ? AND (seconds BETWEEN ? AND ?) AND tradeType = 'BUY' and quantity > 500", ResultSet.TYPE_SCROLL_SENSITIVE, 
					ResultSet.CONCUR_UPDATABLE); 
			ps.setString(1, company);
			ps.setInt(2, seconds - 60);
			ps.setInt(3,  seconds);
			ResultSet customerOrders1 = ps.executeQuery();  

			ps = connection.prepareStatement("SELECT * FROM firmorders WHERE company = ? AND (seconds BETWEEN ? AND ?) AND tradeType = 'BUY' and quantity = ?", ResultSet.TYPE_SCROLL_SENSITIVE, 
					ResultSet.CONCUR_UPDATABLE); 
			ps.setString(1, company);
			ps.setInt(2, seconds - 120);
			ps.setInt(3,  seconds - 60);
			ps.setInt(4, quantity);
			ResultSet firmOrdersAfter = ps.executeQuery();  
			if((customerOrders1.next()) && (firmOrdersAfter.next())) { 
				customerOrders1.beforeFirst();
				firmOrdersAfter.beforeFirst();
				while(customerOrders1.next()){  
					while(firmOrdersAfter.next()){  
						//System.out.println("Front Running 1 found: fraud between "+ customerOrders1.getLong("tradeid") + " and " + firmOrdersAfter.getLong("tradeid") + " with customer :" + customerOrders.getLong("tradeid"));
						ps = connection.prepareStatement("insert into fraud (fraudtype, firmtradeid, firmtradeid2, customertradeid) values (?, ?, ?, ?);");  
						ps.setString(1, "Front-running 1");
						ps.setLong(2, tradeid);
						tradeId1 = customerOrders1.getLong("tradeid");
						ps.setLong(4, tradeId1);
						tradeId2 = firmOrdersAfter.getLong("tradeid");
						ps.setLong(3, tradeId2);
						recordsInserted += ps.executeUpdate();  
					}
				}
			}
			//System.out.println(recordsInserted +" Record inserted successfully");	
			System.out.println("****************************************************");

		}

		//scenario 2
		if(tradeType.equals("SELL")) {    //2
			long customerTradeId;
			int time; 
			ps = connection.prepareStatement("SELECT * FROM customerorders WHERE company = ? AND  (seconds BETWEEN ? AND ?) and tradeType='SELL' and quantity > 500", ResultSet.TYPE_SCROLL_SENSITIVE, 
					ResultSet.CONCUR_UPDATABLE);
			ps.setString(1, company);
			ps.setInt(2, seconds+1);
			ps.setInt(3, seconds+60);
			ResultSet customerOrders = ps.executeQuery();
			if(customerOrders.next()) {
				customerOrders.beforeFirst();
				while(customerOrders.next()) {
					//System.out.println("Before : \t" + customerOrders.getLong("tradeid") + "\t" + customerOrders.getString("tradeType") + "\t" +  customerOrders.getString("seconds") + "\t" + customerOrders.getString("quantity"));
					//System.out.println("Front Running 2 found: fraud between "+ customerOrders.getLong("tradeid") + " with customer :" + customerOrders.getLong("tradeid"));
					ps = connection.prepareStatement("insert into fraud (fraudtype, firmtradeid, customertradeid) values (?, ?, ?);");  
					ps.setString(1, "Front-running 2");
					ps.setLong(2, tradeid);
					customerTradeId = customerOrders.getLong("tradeid");
					ps.setLong(3, customerTradeId);
					recordsInserted += ps.executeUpdate();  
				}
			}
			//System.out.println(recordsInserted +" Record inserted successfully");	
			System.out.println("****************************************************");
		}
		
		//scenerio3
		if(tradeType.equals("BUY") && quantity > 500) {
			ps = connection.prepareStatement("SELECT * FROM customerorders WHERE company = ? AND (seconds BETWEEN ? AND ?) AND tradeType = 'BUY' AND quantity = ? and price  > ? ", ResultSet.TYPE_SCROLL_SENSITIVE, 
					ResultSet.CONCUR_UPDATABLE); 
			ps.setString(1, company);
			ps.setInt(2, seconds + 1);
			ps.setInt(3,  seconds + 60);
			ps.setInt(4, quantity);
			ps.setInt(5, price);
			ResultSet customerOrders = ps.executeQuery();  

			ps = connection.prepareStatement("SELECT * FROM firmorders WHERE company = ? AND (seconds BETWEEN ? AND ?) AND tradeType = 'SELL' AND quantity = ? and price > ?", ResultSet.TYPE_SCROLL_SENSITIVE, 
					ResultSet.CONCUR_UPDATABLE); 
			ps.setString(1, company);
			ps.setInt(2, seconds + 1);
			ps.setInt(3,  seconds + 60);
			ps.setInt(4,quantity);
			ps.setInt(5, price);

			ResultSet firmOrdersOnTime = ps.executeQuery(); 
			if((customerOrders.next()) && (firmOrdersOnTime.next())) { 
				customerOrders.beforeFirst();
				firmOrdersOnTime.beforeFirst();
				while(customerOrders.next()){  
					while(firmOrdersOnTime.next()){  
						if(customerOrders.getInt("seconds") == firmOrdersOnTime.getInt("seconds") && customerOrders.getInt("price") == firmOrdersOnTime.getInt("price")) {
							//System.out.println("Front Running 3 found: fraud between "+ customerOrders.getLong("tradeid") + " and " + firmOrdersOnTime.getLong("tradeid") + " with customer :" + customerOrders.getLong("tradeid"));
							ps = connection.prepareStatement("insert into fraud (fraudtype, firmtradeid, firmtradeid2, customertradeid) values (?, ?, ?, ?);");  
							ps.setString(1, "Front-running 3");
							ps.setLong(4, tradeid);
							long firmTradeId1 = customerOrders.getLong("tradeid");
							ps.setLong(2, firmTradeId1);
							long firmTradeId2 = firmOrdersOnTime.getLong("tradeid");
							ps.setLong(3, firmTradeId2);
							recordsInserted += ps.executeUpdate();  
						}
					}
				}
			}
			//System.out.println("****************************************************");
		}else if(tradeType.equals("SELL") && quantity > 500){
			ps = connection.prepareStatement("SELECT * FROM customerorders WHERE company = ? AND seconds = ? AND tradeType = 'BUY' AND quantity = ? and price = ?", ResultSet.TYPE_SCROLL_SENSITIVE, 
					ResultSet.CONCUR_UPDATABLE); 
			ps.setString(1, company);
			ps.setInt(2, seconds);
			ps.setInt(3, quantity);
			ps.setInt(4, price);
			ResultSet customerOrders = ps.executeQuery();  

			ps = connection.prepareStatement("SELECT * FROM firmorders WHERE company = ? AND (seconds BETWEEN ? AND ?) AND tradeType = 'BUY' AND quantity = ? and price < ?", ResultSet.TYPE_SCROLL_SENSITIVE, 
					ResultSet.CONCUR_UPDATABLE); 
			ps.setString(1, company);
			ps.setInt(2, seconds - 60);
			ps.setInt(3,  seconds );
			ps.setInt(4,quantity);
			ps.setInt(5, price);

			ResultSet firmOrdersOnTime = ps.executeQuery(); 
			if((customerOrders.next()) && (firmOrdersOnTime.next())) { 
				customerOrders.beforeFirst();
				firmOrdersOnTime.beforeFirst();
				while(customerOrders.next()){  
					while(firmOrdersOnTime.next()){  
							//System.out.println("Front Running 3 found: fraud between "+ customerOrders.getLong("tradeid") + " and " + firmOrdersOnTime.getLong("tradeid") + " with customer :" + customerOrders.getLong("tradeid"));
							ps = connection.prepareStatement("insert into fraud (fraudtype, firmtradeid, firmtradeid2, customertradeid) values (?, ?, ?, ?);");  
							ps.setString(1, "Front-running 3");
							ps.setLong(4, tradeid);
							long firmTradeId1 = customerOrders.getLong("tradeid");
							ps.setLong(2, firmTradeId1);
							long firmTradeId2 = firmOrdersOnTime.getLong("tradeid");
							ps.setLong(3, firmTradeId2);
							recordsInserted += ps.executeUpdate();  
					}
				}
			}
			//System.out.println("****************************************************");
			
		}
		//System.out.println(recordsInserted +" Record inserted successfully");
	}
	
	public void checkWashTrade(Connection connection, long tradeid, String tradeType, String broker, String company, int quantity, int price, int seconds) throws SQLException {
		int recordsInserted = 0;
		if(tradeType.equals("SELL")) {
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM firmorders WHERE tradeid != ? AND tradeType = 'BUY' AND brokername = ? AND company = ? AND quantity = ? AND price = ?");
			ps.setLong(1, tradeid);
			ps.setString(2, broker);
			ps.setString(3, company);
			ps.setInt(4, quantity);
			ps.setInt(5, price);
			ResultSet washTrades = ps.executeQuery();

			while(washTrades.next()) {			
				int seconds2 = washTrades.getInt("seconds");
				if(seconds2 <= seconds) {
						//System.out.println("WASH TRADE:");
						//System.out.println( "Tradeid1 :\t" + washTrades.getInt("a.tradeid") + "\t" + washTrades.getString("a.tradeType") + "\t" + washTrades.getInt("a.seconds"));
						//System.out.println( "Tradeid2 :\t" + washTrades.getInt("b.tradeid") + "\t" + washTrades.getString("b.tradeType") + "\t" + washTrades.getInt("b.seconds"));
						ps = connection.prepareStatement("insert into washtrade (firmtradeid, firmtradeid2) values(?,?);");  
						ps.setLong(1, tradeid);
						long tradeId2 = washTrades.getLong("tradeid");
						ps.setLong(2, tradeId2);
						recordsInserted += ps.executeUpdate();  
					}
				}
				//System.out.println("****************************************************");
			}
			//System.out.println(recordsInserted +" Record inserted successfully");
	}

	public static void main(String[] args) throws Exception {
		Fraud tr = new Fraud();
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tradesurveillance1?user=root&password=root&serverTimezone=UTC");
		Statement statement = connection.createStatement();
		statement.executeUpdate("TRUNCATE fraud");
		statement.executeUpdate("TRUNCATE washtrade");
		//System.out.println("\n-------------------------------------------------------------------------------\nScenario 1\n-------------------------------------------------------------------------------");
		tr.scenario1(connection);
		//System.out.println("\n-------------------------------------------------------------------------------\nScenario 2\n-------------------------------------------------------------------------------");
		tr.scenario2(connection);
		//System.out.println("\n-------------------------------------------------------------------------------\nScenario 3\n-------------------------------------------------------------------------------");
		tr.scenario3(connection);
		//System.out.println("\n-------------------------------------------------------------------------------\nWash Trade\n-------------------------------------------------------------------------------");
		tr.washTrades(connection);
		connection.close();
	}

}