import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Hotel_Reservations {

    private static final String url = "jdbc:mysql://localhost:3306/hotel";
    private static final String userName = "root";
    private static final String password = "2002";

    public static void main(String[] args) throws Exception {

        Class.forName("com.mysql.cj.jdbc.Driver");

        Connection connection = DriverManager.getConnection(url, userName, password);
        Statement statement = connection.createStatement();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println(
                    "\n Menu \n 1. New Reservations \n 2.Check Reservations \n 3.Get Room.No \n 4.update Reservation \n 5.Cancel Rreservation 6.Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    newReservation(scanner, statement);
                    break;
                case 2:
                    checkReservation(scanner, connection);
                    break;
                case 3:
                    getRoomNo(scanner, connection);
                    break;
                case 4:
                    updateReservation(scanner, connection);
                    break;
                case 5:
                    deleteReservation(scanner, connection);
                    break;
                case 6:
                    exit();
                    connection.close();
                    scanner.close();
                default:
                    System.out.println("Invalid Choice");

            }
        }
    }

    public static void newReservation(Scanner s, Statement st) throws SQLException {
        System.out.print("Enter the name: ");
        String name = s.next();
        System.out.print("Enter Room number");
        int roomNumber = s.nextInt();
        System.out.print("Enter contact number");
        String contactNumber = s.next();

        String query = " insert into reservations (guest_name, room_number, contact_number) values('" + name + "', "
                + roomNumber + ", '" + contactNumber + "')";

        int rowAffected = st.executeUpdate(query);

        if (rowAffected > 0) {
            System.out.println("Your room is reserved");
        } else {
            System.out.println("Reservation failed");
        }
    }

    private static void checkReservation(Scanner s, Connection con) throws SQLException {
        System.out.print("Enter Reservation Id: ");
        int id = s.nextInt();
        System.out.print("Enter the name: ");
        String name = s.next();

        String query = "SELECT * FROM reservations WHERE reservation_id = ? AND guest_name = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ps.setString(2, name);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            System.out.println("Your Reservation Id: " + rs.getString("reservation_id") + " : "
                    + rs.getString("guest_name") + " : " + rs.getString("room_number") + " : "
                    + rs.getString("contact_number") + " : " + rs.getDate("reservation_date"));
        } else {
            System.out.println("Not Reserved");
        }
    }

    private static void getRoomNo(Scanner s, Connection con) throws SQLException {
        System.out.print("Enter Your name: ");
        String name = s.next();
        System.out.print("Enter the Contact Number: ");
        String cNumber = s.next();

        String query = "select * from reservations where guest_name = ? and contact_number= ? ";

        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, name);
        ps.setString(2, cNumber);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            System.out.println("Your Room Number " + rs.getString("room_number"));
        } else {
            System.out.println("Your room is reserved");
        }
    }

    private static void updateReservation(Scanner s, Connection con) throws SQLException {
        System.out.print("Enter Your Reservation_id: ");
        int id = s.nextInt();

        System.out.println("Enter the details you want to change");

        System.out.print("Enter Changeing name: ");
        String name = s.next();
        System.out.print("Enter Changeing Room Number: ");
        int rNumber = s.nextInt();
        System.out.print("Enter Changeing Contact Number: ");
        String cNumber = s.next();

        String query = "update reservations set guest_name = ?, room_number = ?, contact_number = ? where reservation_id = ?";

        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, name);
        ps.setInt(2, rNumber);
        ps.setString(3, cNumber);
        ps.setInt(4, id);

        int count = ps.executeUpdate();

        if (count > 0) {
            System.out.print("Your data is updated");
        } else {
            System.out.print("Data is not updated ");
        }
    }

    private static void deleteReservation(Scanner s, Connection con) throws SQLException {
        System.out.print("Enter Your Reservation_id: ");
        int id = s.nextInt();
        System.out.print("Enter your name: ");
        String name = s.next();

        String query = "delete from reservations where reservation_id = ? and guest_name = ?";

        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ps.setString(2, name);

        int count = ps.executeUpdate();

        if (count > 0) {
            System.out.print("Your Reservation is Cancelled");
        } else {
            System.out.print("You are Entered invalid Credential ");
        }
    }

    private static void exit() throws InterruptedException {

        int i = 0;
        System.out.print("Exiting ");
        while (i < 5) {
            Thread.sleep(400);
            System.out.print(".");
            i++;
        }
        System.out.println("\n Thank you");
        System.exit(0);
    }
}
