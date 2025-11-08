import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.*;
public class EmployeeCrud {
    public static Connection getConnection() throws Exception{
        String URL="jdbc:mysql://localhost:3306/testdb";
        String USER_NAME="root";
        String PASS="root";
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL,USER_NAME,PASS);
    }
    public void readData() {
        try(Connection conn = getConnection()) {
            String sql = "SELECT * FROM employee";
            PreparedStatement prep= conn.prepareStatement(sql);
            ResultSet rs = prep.executeQuery();
            System.out.println("\n---------------------------------------------");
            System.out.println("ID | Name        | Salary    | Balance   | Photo");
            System.out.println("---------------------------------------------");

            while (rs.next()) {
                InputStream is = rs.getBinaryStream("photo");
                String photo=is!=null ? "Yes" : "No";
                System.out.printf("%-2d | %-10s | %-9.2f | %-9.2f | %-5s%n",
                        rs.getInt("id"), rs.getString("name"), rs.getDouble("salary"), rs.getDouble("balance"), photo);            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("---------------------------------------------");
    }
    public void searchEmployeeByName() {
        try (Connection conn = getConnection()) {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter employee name to search: ");
            String name = sc.nextLine();

            String sql = "SELECT * FROM employee WHERE name=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("\nEmployee Found:");
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Salary: " + rs.getDouble("salary"));
                System.out.println("Balance: " + rs.getDouble("balance"));
                System.out.println("Photo Exists: " + (rs.getBinaryStream("photo") != null ? "Yes" : "No"));
            } else {
                System.out.println("No employee found with name: " + name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addData(){
        try(Connection conn=getConnection()){
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter the name :");
            String name=sc.nextLine();
            System.out.println("Enter the salary :");
            double salary=sc.nextDouble();
            System.out.println("Enter the balance :");
            double balance=sc.nextDouble();
            sc.nextLine();
            System.out.println("Do you want to upload a photo? (yes/no):");
            String choice = sc.nextLine();
            String sql="INSERT INTO employee (name,salary,balance,photo) VALUES(?,?,?,?)";
            PreparedStatement prep= conn.prepareStatement(sql);
            prep.setString(1,name);
            prep.setDouble(2,salary);
            prep.setDouble(3,balance);
            if(choice.equalsIgnoreCase("yes")){
                System.out.println("Enter the path of your picture :");
                String path=sc.nextLine();
                File file=new File(path);
                if(file.exists()){
                    FileInputStream fis=new FileInputStream(file);
                    prep.setBinaryStream(4,fis,(int)file.length());
                }
                else{
                    System.out.println("FIle not found ! Skipping photo upload.");
                    prep.setNull(4,Types.BLOB);
                }
            }
            else{
                prep.setNull(4,Types.BLOB);
            }
            int res=prep.executeUpdate();
            if(res!=0)
                System.out.println("Employee added successfully");
            else
                System.out.println("Employee is not added ");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void updateData(){
        try(Connection conn = getConnection()) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter the name and new salary of Employee to update salary :");
            String name=sc.nextLine();
            double salary=sc.nextDouble();
            String sql = "UPDATE employee SET salary=? WHERE name=?";
            PreparedStatement prep= conn.prepareStatement(sql);
            prep.setDouble(1,salary);
            prep.setString(2,name);
            int res = prep.executeUpdate();
            if(res!=0)
                System.out.println("Employee updated successfully");
            else
                System.out.println("Employee is not updated");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void deleteData(){
        try(Connection conn = getConnection()) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter Employee name :");
            String name=sc.nextLine();
            String sql = "DELETE FROM employee WHERE name=?";
            PreparedStatement prep= conn.prepareStatement(sql);
            prep.setString(1,name);
            int res = prep.executeUpdate();
            if(res!=0)
                System.out.println("Employee removed successfully");
            else
                System.out.println("Employee is not in the DB");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void transaction(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the sender name :");
        String send=sc.nextLine();
        System.out.println("Enter the receiver name :");
        String receive=sc.nextLine();
        System.out.println("Enter the amount to transfer :");
        double amount=sc.nextDouble();
        try(Connection conn=getConnection()){
            conn.setAutoCommit(false);

            String checkBalanceSQL = "SELECT balance FROM employee WHERE name=?";
            PreparedStatement checkStmt = conn.prepareStatement(checkBalanceSQL);
            checkStmt.setString(1, send);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Sender not found!");
                conn.rollback();
                return;
            }


            double currentBalance = rs.getDouble("balance");
            if (currentBalance < amount) {
                System.out.println("Insufficient balance! Transaction canceled.");
                conn.rollback();
                return;
            }

            //sender
            String sqlSend="UPDATE employee SET balance=balance-? WHERE name=?";
            PreparedStatement prep1= conn.prepareStatement(sqlSend);
            prep1.setDouble(1,amount);
            prep1.setString(2,send);
            int r1=prep1.executeUpdate();

            //check receiver
            String checkReceiverSQL = "SELECT COUNT(*) FROM employee WHERE name=?";
            PreparedStatement checkReceiver = conn.prepareStatement(checkReceiverSQL);
            checkReceiver.setString(1, receive);
            ResultSet rsReceiver = checkReceiver.executeQuery();
            rsReceiver.next();
            if (rsReceiver.getInt(1) == 0) {
                System.out.println("Receiver not found! Transaction canceled.");
                conn.rollback();
                return;
            }

            //reciever
            String sqlReceive="UPDATE employee SET balance=balance+? WHERE name=?";
            PreparedStatement prep2= conn.prepareStatement(sqlReceive);
            prep2.setDouble(1,amount);
            prep2.setString(2,receive);
            int r2=prep2.executeUpdate();

            if(r1>0 && r2>0){
                conn.commit();
                System.out.println("Transaction succeed.");
            }
            else{
                conn.rollback();
                System.out.println("Transaction Denied!!!");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void batchUpdate(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of Employees to insert");
        int n=sc.nextInt();
        try(Connection conn =getConnection()){
            conn.setAutoCommit(false);
            String sql="INSERT INTO employee(name,salary,balance) VALUES(?,?,?)";
            PreparedStatement prep= conn.prepareStatement(sql);
            while(n-- >0){
                sc.nextLine();
                System.out.println("Enter employee name:");
                String name=sc.next();
                System.out.println("Enter employee salary:");
                double salary=sc.nextDouble();
                System.out.println("Enter employee balance:");
                double balance=sc.nextDouble();

                prep.setString(1,name);
                prep.setDouble(2,salary);
                prep.setDouble(3,balance);

                prep.addBatch();
            }
            int rs[]=prep.executeBatch();
            conn.commit();
            System.out.println(rs.length+" Employees are added.");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void showDatabaseMetadata(){
        try(Connection conn=getConnection()){
            DatabaseMetaData dbmd=conn.getMetaData();
            System.out.println("Database Name: " + dbmd.getDatabaseProductName());
            System.out.println("Database Version: " + dbmd.getDatabaseProductVersion());
            System.out.println("Driver Name: " + dbmd.getDriverName());
            System.out.println("User: " + dbmd.getUserName());
            System.out.println("URL: " + dbmd.getURL());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showResultSetMetaData() {
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM employee";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();

            int columnCount = rsmd.getColumnCount();
            System.out.println("Total Columns: " + columnCount);
            for (int i = 1; i <= columnCount; i++) {
                System.out.println("Column " + i + ": " + rsmd.getColumnName(i) +
                        " (" + rsmd.getColumnTypeName(i) + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
