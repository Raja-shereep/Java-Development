import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        EmployeeCrud emp=new EmployeeCrud();
        System.out.println("WELCOME TO EMPLOYEE MANAGEMENT SYSTEM");
        do{
            System.out.println("1.Get Employee");
            System.out.println("2.Search Employee by Name");
            System.out.println("3.Add Employee");
            System.out.println("4.Update Employee");
            System.out.println("5.Delete Employee");
            System.out.println("6.Transaction Amount");
            System.out.println("7.Batch Insert");
            System.out.println("8.Database Metadata");
            System.out.println("9.Table Metadata");
            System.out.println("10.Exit");
            int choice=sc.nextInt();
            switch (choice){
                case 1:
                    emp.readData();
                    break;
                case 2:
                    emp.searchEmployeeByName();
                    break;
                case 3:
                    emp.addData();
                    break;
                case 4:
                    emp.updateData();
                    break;
                case 5:
                    emp.deleteData();
                    break;
                case 6:
                    emp.transaction();
                    break;
                case 7:
                    emp.batchUpdate();
                    break;
                case 8:
                    emp.showDatabaseMetadata();
                    break;
                case 9:
                    emp.showResultSetMetaData();
                    break;
                case 10:
                    System.out.println("See you Later!");
                    return;
            }
        }while(true);
    }
}