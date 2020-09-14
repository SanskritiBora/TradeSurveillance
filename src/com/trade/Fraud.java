package com.trade;
import java.sql.*;

public class Fraud {

	public void scenario1(Connection connection) throws Exception {
		int recordsInserted = 0;
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
			ps.setInt(2, seconds - 120);
			ps.setInt(3,  seconds-1);
			ResultSet firmOrdersBefore = ps.executeQuery();  

			ps = connection.prepareStatement("SELECT * FROM firmorders WHERE company = ? AND (seconds BETWEEN ? AND ?) AND tradeType = 'SELL'", ResultSet.TYPE_SCROLL_SENSITIVE, 
                    ResultSet.CONCUR_UPDATABLE); 
			ps.setString(1, company);
			ps.setInt(2, seconds+1);
			ps.setInt(3,  seconds + 120);
			ResultSet firmOrdersAfter = ps.executeQuery();  
			if((firmOrdersBefore.next()) && (firmOrdersAfter.next())) { 
				firmOrdersBefore.beforeFirst();
				firmOrdersAfter.beforeFirst();
				while(firmOrdersBefore.next()){  
					//System.out.println("Before : \t" + firmOrdersBefore.getLong("tradeid") + "\t" + firmOrdersBefore.getString("tradeType") + "\t" +  firmOrdersBefore.getString("seconds") + "\t" + firmOrdersBefore.getString("quantity"));
				} 
				while(firmOrdersAfter.next()){  
					//System.out.println("After : \t" + firmOrdersAfter.getLong("tradeid") + "\t" + firmOrdersAfter.getString("tradeType") + "\t" +  firmOrdersAfter.getString("seconds") + "\t" + firmOrdersAfter.getString("quantity"));
				}
				firmOrdersBefore.beforeFirst();
				firmOrdersAfter.beforeFirst();
				while(firmOrdersBefore.next()){  

						while(firmOrdersAfter.next()){  
								if(firmOrdersBefore.getInt("quantity") == firmOrdersAfter.getInt("quantity") ) {
									//System.out.println("Front Running 1 found: fraud between "+ firmOrdersBefore.getLong("tradeid") + " and " + firmOrdersAfter.getLong("tradeid") + " with customer :" + customerOrders.getLong("tradeid"));
									ps = connection.prepareStatement("insert into fraud (fraudtype, firmtradeid, firmtradeid2, customertradeid) values (?, ?, ?, ?);");  
									ps.setString(1, "Front-running 1");
									ps.setLong(4, tradeid);
									tradeid = firmOrdersBefore.getLong("tradeid");
									ps.setLong(2, tradeid);
									tradeid = firmOrdersAfter.getLong("tradeid");
									ps.setLong(3, tradeid);
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
			ps.setInt(2, seconds - 120);
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
					tradeid = firmOrders.getLong("tradeid");
					ps.setLong(2, tradeid);
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
			//System.out.println(quantity);

			ps = connection.prepareStatement("SELECT * FROM firmorders WHERE company = ? AND (seconds BETWEEN ? AND ?) AND tradeType = 'BUY' AND quantity= ?", ResultSet.TYPE_SCROLL_SENSITIVE, 
                    ResultSet.CONCUR_UPDATABLE); 
			ps.setString(1, company);
			ps.setInt(2, seconds - 120);
			ps.setInt(3,  seconds);
			ps.setInt(4,quantity);
			ResultSet firmOrdersBefore = ps.executeQuery();  

			ps = connection.prepareStatement("SELECT * FROM firmorders WHERE company = ? AND seconds=? AND tradeType = 'SELL' AND quantity=?", ResultSet.TYPE_SCROLL_SENSITIVE, 
                    ResultSet.CONCUR_UPDATABLE); 
			ps.setString(1, company);
			ps.setInt(2, seconds);
			ps.setInt(3,quantity);

			ResultSet firmOrdersOnTime = ps.executeQuery(); 


			if((firmOrdersBefore.next()) && (firmOrdersOnTime.next())) { 

				firmOrdersBefore.beforeFirst();
				firmOrdersOnTime.beforeFirst();

				while(firmOrdersBefore.next()){  
					//System.out.println("Before : \t" + firmOrdersBefore.getLong("tradeid") + "\t" + firmOrdersBefore.getString("tradeType") + "\t" +  firmOrdersBefore.getString("seconds") + "\t" + firmOrdersBefore.getString("quantity"));
				} 
				while(firmOrdersOnTime.next()){  
					//System.out.println("On Time : \t" + firmOrdersOnTime.getLong("tradeid") + "\t" + firmOrdersOnTime.getString("tradeType") + "\t" +  firmOrdersOnTime.getString("seconds") + "\t" + firmOrdersOnTime.getString("quantity"));
				}
				firmOrdersBefore.beforeFirst();
				firmOrdersOnTime.beforeFirst();


				while(firmOrdersBefore.next()){  

					while(firmOrdersOnTime.next()){  
							if(firmOrdersBefore.getInt("price") < firmOrdersOnTime.getInt("price")) {
								//System.out.println("Front Running 3 found: fraud between "+ firmOrdersBefore.getLong("tradeid") + " and " + firmOrdersOnTime.getLong("tradeid") + " with customer :" + customerOrders.getLong("tradeid"));
								ps = connection.prepareStatement("insert into fraud (fraudtype, firmtradeid, firmtradeid2, customertradeid) values (?, ?, ?, ?);");  
								ps.setString(1, "Front-running 3");
								ps.setLong(4, tradeid);
								tradeid = firmOrdersBefore.getLong("tradeid");
								ps.setLong(2, tradeid);
								tradeid = firmOrdersOnTime.getLong("tradeid");
								ps.setLong(3, tradeid);
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
					seconds1 = washTrades.getInt("a.tradeid");
					ps.setInt(1, seconds1);
					seconds2 = washTrades.getInt("b.tradeid");
					ps.setInt(2, seconds2);
					recordsInserted += ps.executeUpdate();  
				}
			}
			//System.out.println("****************************************************");
		}
		//System.out.println(recordsInserted +" Record inserted successfully");
		washTrades.close();
		statement.close();

	}

	public static void main(String[] args) throws Exception {
		Fraud tr = new Fraud();
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tradesurveillance?user=root&password=root&serverTimezone=UTC");
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