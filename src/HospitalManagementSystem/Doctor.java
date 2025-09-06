package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Doctor {
    private Connection connection;


    public Doctor(Connection connection) {
        this.connection = connection;

    }



    public void viewDoctors(){
        String query = "SELECT * FROM doctor";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(query); //to  throw the query
            ResultSet resultSet=preparedStatement.executeQuery(); //hold or accept data from table and hold it and set pointer
            System.out.println("Doctor: ");
            System.out.println("+------------+----------------+-------------------+");
            System.out.println("| Doctor Id  | Name           | Specializaion     |");
            System.out.println("+------------+----------------+-------------------+");

            while(resultSet.next()){
                int id=resultSet.getInt("id");//hold data send to this all local varibals.
                String name=resultSet.getString("name");
                String specialization=resultSet.getString("specialization");
                System.out.printf("| %-10s | %-13s | %-16s |\n",id,name,specialization);
                System.out.println("+------------+----------------+-------------------+");            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public boolean getDoctorById(int id){
        String query="SELECT * FROM doctor WHERE id= ?";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet= preparedStatement.executeQuery();

            if(resultSet.next()){
                return true;
            }else{
                return false;
            }


        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
