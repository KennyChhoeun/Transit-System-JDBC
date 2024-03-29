package com.company;
import java.sql.*;
import java.util.Scanner;
/* Java program that will present contents of any
 * table call and alter anything within the tables.
 * Author: Kenny Chhoeun
 * CS 4350: Database System
 */
public class GeneralTable {

    public static void main(String[] args) {
	    String ps = "password";
	    try{
	        //load mysql
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Trying connection with mySQL Server");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/lab4","root",ps);
            Scanner in = new Scanner(System.in);
            System.out.println("1. Display Trip Schedule");
            System.out.println("2. Delete a trip offering");
            System.out.println("3. Add trip offering");
            System.out.println("4. Change driver for a trip");
            System.out.println("5. Change the bus for a trip offering");
            System.out.println("6. Display trip stop information");
            System.out.println("7. Display weekly schedule based of given driver and date");
            System.out.println("8. Add a driver");
            System.out.println("9. Add a bus");
            System.out.println("10. Delete a bus");
            System.out.println("11. Insert data of trip specified by key");
            System.out.print("\n\nPICK AN OPTION: ");

            short choice = in.nextShort();
            Statement stmt = conn.createStatement();
            switch (choice) {
                case 1:
                    displaySchedule(stmt);
                    break;
                case 2:
                    deleteTripOffering(stmt);
                    break;
                case 3:
                    addTripOffering(stmt);
                    break;
                case 4:
                    changeDriver(stmt);
                    break;
                case 5:
                    changeBus(stmt);
                    break;
                case 6:
                    displayTripStopInfo(stmt);
                    break;
                case 7:
                    weeklyScheduleDrivernDate(stmt);
                    break;
                case 8:
                    addDriver(stmt);
                    break;
                case 9:
                    addBus(stmt);
                    break;
                case 10:
                    deleteBus(stmt);
                    break;
                case 11:
                    insertTrip(stmt);
                    break;
            }
        } catch(Exception e) {
	        e.printStackTrace();
        }
    }

    public static void displaySchedule(Statement stmt) throws SQLException{
        Scanner in = new Scanner(System.in);
        System.out.print("StartLocationName: ");
        String startLocation = in.nextLine().trim();
        System.out.print("DestinationName: ");
        String destinationName = in.nextLine().trim();
        System.out.print("Date: ");
        String date = in.nextLine().trim();
        //get table data

        try {
            ResultSet rs = stmt.executeQuery("SELECT t.ScheduledStartTime, t.ScheduledArrivalTime, t.DriverName, t.BusID " +
                                                "FROM TripOffering t, Trip tp " +
                                                "WHERE tp.StartLocationName = '" + startLocation + "' AND " +
                                                "tp.DestinationName = '" + destinationName + "' AND " +
                                                "t.Date = '" + date + "' AND " +
                                                "t.TripNumber = tp.TripNumber " +
                                                "ORDER BY ScheduledStartTime");

            //print column names
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();
            for(int i = 1; i <= colCount; i++)
            {
                System.out.print(rsmd.getColumnName(i) + "\t\t\t\t");
            }
            System.out.println();
            System.out.print("----------------------------------------------------------------------------------------------------");
            System.out.println("-------------------");
            //print out the data
            while(rs.next()){
                for(int i = 1; i<=colCount; i++)
                {
                    System.out.print(rs.getString(i)+"\t\t\t\t\t\t\t");
                }
                System.out.println();
            }
            rs.close();

        } catch (SQLException e){
            System.out.println("No schedule from " + startLocation + " to " + destinationName + " on " + date);
        }
    } //end displaySchedule method

    public static void deleteTripOffering(Statement stmt){
        Scanner in = new Scanner(System.in);
        System.out.print("Trip Number: ");
        String tripNo = in.nextLine().trim();
        try
        {
            //if delete returns 0 that means no rows found matching that data so output an error
            if(stmt.executeUpdate("DELETE FROM TripOffering " +
                    "WHERE TripNumber = " + tripNo) == 0)
            {
                System.out.println("No Trip Offering with Trip Number: " + tripNo);
            }
            else
                //if delete returns any other value, that means something was deleted
                System.out.println("Successfully deleted Trip Offering");
        }
        catch (SQLException e)
        {
            //if some error occurs check input
            System.out.println("No Trip Offering with Trip Number: " + tripNo);

        }

    } //end deleteTripOffering

    public static void addTripOffering(Statement stmt){
        Scanner in = new Scanner(System.in);
        System.out.print("Enter trip number: ");
        String tripNo = in.nextLine().trim();
        System.out.print("Date: ");
        String date = in.nextLine().trim();
        System.out.print("Scheduled start time: ");
        String sSt = in.nextLine().trim();
        System.out.print("Scheduled arrival time: ");
        String sAt = in.nextLine().trim();
        System.out.print("Driver name: ");
        String driverName = in.nextLine().trim();
        System.out.print("BusID: ");
        String busID = in.nextLine().trim();
        try
        {
            stmt.execute("INSERT INTO TripOffering VALUES ('" + tripNo + "', '" + date + "', '" + sSt + "', '" + sAt + "', '" + driverName + "', '" + busID + "')");
            System.out.print("Successfully added a new Trip Offering");
        }
        catch (SQLException e)
        {
            System.out.println("Check syntax formatting");
        }
    } // end addTripOffering

    public static void changeDriver(Statement stmt){
        Scanner in = new Scanner(System.in);
        System.out.print("New driver name: ");
        String driverN = in.nextLine().trim();
        System.out.print("Trip no: ");
        String tripNo = in.nextLine().trim();
        System.out.print("Date: ");
        String date = in.nextLine().trim();
        System.out.print("ScheduledStartTime: ");
        String sSt = in.nextLine().trim();

        try{
            if (stmt.executeUpdate("UPDATE TripOffering "+
                                    "SET DriverName = '" + driverN + "' " +
                                    "WHERE TripNumber = " + tripNo + " AND " +
                                    "Date = '" + date + "' AND " +
                                    "ScheduledStartTime = '" + sSt + "'") == 0)
                System.out.println("No Trip Offering with Trip Number: " + tripNo + " on " + date + " starting at " + sSt);
            else
                System.out.println("updated Driver! ");
        } catch (SQLException e)
        {
            System.out.println("No such Trip Offering or Driver in database");
        }
    } //end changeDriver

    public static void changeBus(Statement stmt){
        Scanner in = new Scanner(System.in);
        System.out.print("New BusID: ");
        String bus = in.nextLine().trim();
        System.out.print("Trip no: ");
        String tripNo = in.nextLine().trim();
        System.out.print("Date: ");
        String date = in.nextLine().trim();
        System.out.print("ScheduledStartTime: ");
        String sSt = in.nextLine().trim();
        try{
            if(stmt.executeUpdate("UPDATE TripOffering " +
                                "SET BusID = '" + bus + "' " +
                                "WHERE TripNumber = '" + tripNo + "' AND " +
                                "Date = '" + date + "' AND " +
                                "ScheduledStartTime = '" + sSt + "'") == 0)
            {
                System.out.println("Successfully updated busID");
            }
            System.out.println();
        } catch (SQLException e)
        {
            System.out.println("Could not change the bus check syntax");
        }
    }

    public static void displayTripStopInfo(Statement stmt) throws SQLException {
        Scanner in = new Scanner(System.in);
        System.out.print("Trip No: ");
        String tripNo = in.nextLine().trim();
        try{
            ResultSet rs = stmt.executeQuery("SELECT * " +
                                            "FROM TripStopInfo " +
                                            "WHERE TripNumber = '" + tripNo + "' " +
                                            "Order By SequenceNumber ");

            //column names
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();
            for(int i = 1; i <= colCount; i++)
            {
                System.out.print(rsmd.getColumnName(i) + "\t");
            }

            System.out.println();

            //print out data
            while(rs.next())
            {
                for(int i = 1; i <= colCount; i++)
                    System.out.print(rs.getString(i) + "\t\t\t\t");
                System.out.println();
            }
            rs.close();
            System.out.println("------------------------------------------------------");
        } catch (SQLException e)
        {
            System.out.println("Something went wrong");
        }
    } //end displayTripStopInfo

    public static void weeklyScheduleDrivernDate(Statement stmt) throws SQLException{ //needs column names
        Scanner in = new Scanner(System.in);
        System.out.print("Driver: ");
        String driver = in.nextLine().trim();
        System.out.print("Date: ");
        String date = in.nextLine().trim();
        try {
            ResultSet rs = stmt.executeQuery("SELECT TripNumber, Date, ScheduledStartTime, ScheduledArrivalTime, BusID " +
                    "FROM TripOffering " +
                    "WHERE DriverName = '" + driver +
                    "' AND Date = '"+ date +
                    "' ORDER BY ScheduledStartTime ");
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();
            for(int i = 1; i <= colCount; i++)
            {
                System.out.print(rsmd.getColumnName(i) + "\t\t\t\t");
            }
            System.out.println();
            System.out.print("--------------------------------");
            System.out.print("--------------------------------");
            System.out.print("--------------------------------");
            System.out.println("----------------------");

            while(rs.next())
            {
                for(int j = 1; j <= colCount; j++)
                    System.out.print("\t" + rs.getString(j) + "\t\t\t\t\t");
                System.out.println();
            }
            rs.close();

        } catch(SQLException e)
        {
            System.out.println("Something went wrong");
        }
    } // end weeklyScheduleDrivernDate

    public static void addDriver(Statement stmt) throws SQLException{
        Scanner in = new Scanner(System.in);
        System.out.print("New Driver Name: ");
        String driverName = in.nextLine();
        System.out.print("DriverPhoneNumber: ");
        String phoneN = in.nextLine();
        try{
            stmt.execute("INSERT INTO Driver VALUES('" + driverName + "' , " + phoneN +")");
            System.out.println("New driver added!");
        } catch (SQLException e)
        {
            System.out.print("An error occurred");
        }
    }

    public static void addBus(Statement stmt) throws SQLException{
        Scanner in = new Scanner(System.in);
        System.out.print("Enter the new BusID: ");
        String BusID = in.nextLine().trim();
        System.out.print("Enter the Bus Model: ");
        String Model = in.nextLine().trim();
        System.out.print("Enter the year: ");
        String Year = in.nextLine().trim();
        try{
            stmt.execute("INSERT INTO Bus VALUES (" + BusID + ", '" + Model + "', '" + Year + "')");
            System.out.println("A new bus has been added");
        } catch (SQLException e) {
            System.out.println("An error has occured");
        }
    } // end addBus method

    public static void deleteBus(Statement stmt) throws SQLException{
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the bus number that you want to delete");
        String BusID = in.nextLine();
        try{
            if(stmt.executeUpdate("DELETE FROM Bus " +
                           "WHERE BusID = " + BusID) == 0)
            {
                System.out.println("The bus was not found or could not be deleted");
            }
            else
                System.out.println("Query updated!");
        } catch (SQLException e)
        {
            System.out.println("An error occured");
        }
    } //end deleteBus

    public static void insertTrip(Statement stmt) throws SQLException{
        Scanner in = new Scanner(System.in);
        System.out.print("Trip Number: ");
        String tripNo = in.nextLine().trim();
        System.out.print("Date: ");
        String date = in.nextLine().trim();
        System.out.print("Scheduled Start Time: ");
        String startTime = in.nextLine().trim();
        System.out.print("Stop Number: ");
        String stop = in.nextLine().trim();
        System.out.print("Scheduled Arrival Time: ");
        String arrivalTime = in.nextLine().trim();
        System.out.print("Actual Start Time: ");
        String actualStartTime = in.nextLine().trim();
        System.out.print("Actual Arrival Time: ");
        String actualArrivalTime = in.nextLine().trim();
        System.out.print("Passengers in: ");
        String passengerIn = in.nextLine().trim();
        System.out.print("Passengers out: ");
        String passengerOut = in.nextLine().trim();

        try{
            stmt.execute("INSERT INTO ActualTripStopInfo VALUES (" + tripNo + ", '" + date + "', '" + startTime + "', " + stop + ", '" + arrivalTime
                    + "', '" + actualStartTime + "', '" + actualArrivalTime + "', " + passengerIn + ", " + passengerOut + ")");
            System.out.println("New Trip stop information added");
        } catch(SQLException e) {
            System.out.println("An error occurred please check the syntax");

        }
    } //end insertTrip
} //end class
