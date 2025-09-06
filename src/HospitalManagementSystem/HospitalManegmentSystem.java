package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManegmentSystem {
    private static final String url="jdbc:mysql://localhost:3306/hospital";
    private static final String username="root";
    private static final String password="tiger";

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver"); //load driver ,it is neccesery to connect with databases
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }

        //make connection with database
        Scanner scanner=new Scanner(System.in);
        try{
            Connection connection= DriverManager.getConnection(url,username,password);
            Patient patient=new Patient(connection,scanner);
            Doctor doctor=new Doctor(connection);

            while(true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM ");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patient");
                System.out.println("3. View Doctor");
                System.out.println("4. Book Appoinment");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice=scanner.nextInt();

                switch(choice){
                    case 1:
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        patient.viewPatients();
                        System.out.println();
                        break;
                    case 3:
                        doctor.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        //a
                        bookAppointment(patient,doctor,connection,scanner);
                        System.out.println();
                        break;
                    case 5:
                        System.out.println("THANK YOU FOR USING HOSPITAL MANAGEMENT SYSTEM");
                        return;
                    default:
                        System.out.println("Enter valid choice");
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patient patient,Doctor doctor, Connection connection, Scanner scanner){
        System.out.print("Enter Patient ID: ");
        int patientID=scanner.nextInt();
        System.out.print("Enter Doctor ID: ");
        int doctorID=scanner.nextInt();
        System.out.print("Enter appointment date (YYYY-MM-DD): ");
        String appointmentdate=scanner.next();

        if(patient.getPatientById(patientID)&& doctor.getDoctorById(doctorID)){
            if(checkDoctorAvailability(doctorID,appointmentdate,connection )){
                String appointmentQuery="INSERT INTO appointments(patient_id,doctor_id,appointment_date) VALUES (?,?,?)";
                try{
                    PreparedStatement preparedStatement=connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1,patientID);
                    preparedStatement.setInt(2,doctorID);
                    preparedStatement.setString(3,appointmentdate);
                    int rowsaffected=preparedStatement.executeUpdate();
                    if(rowsaffected>0){
                        System.out.println("Appointment booked");
                    }else {
                        System.out.println("Failed to booked appointment");
                    }
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }else{
                System.out.println("Doctor is not available on this date!!");
            }
        }else {
            System.out.println("Either doctor or patient dosent exist!!");
        }
    }

    public static boolean checkDoctorAvailability(int doctorID,String appointmentdate,Connection connection   ){
        String query="SELECT COUNT(*) FROM appointments WHERE doctor_id=? AND appointment_date=?";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,doctorID);
            preparedStatement.setString(2,appointmentdate);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                int count=resultSet.getInt(1);
                if(count==0){
                    return true;
                }else {
                    return false;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

}
