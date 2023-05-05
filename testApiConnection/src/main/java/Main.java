
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


/**
 *
 * @author Emilio Jose Roldan Olivares
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            String username= "Emilio";
            String password= "2222";
            
            //JSONObject json =Connections.login(username,password);
//            String accessToken = json.getString("access_token");
//            boolean admin = json.getBoolean("admin");
//            int id = json.getInt("id");
//            boolean login = json.getBoolean("login");
            
            Connections.Connect();
            
            //Connections.userData(Integer.toString(id),accessToken);
            //String accessToken = "123456789asdfghj";
            //Connections.createUser (accessToken);
            
            String dailyToken = Connections.dailyToken();
            //System.out.println(dailyToken);
            //Connections.createUser (dailyToken);
            System.out.println(dailyToken);
            
            JSONObject json =Connections.login(dailyToken,username,password);
            String accessToken = json.getString("access_token");
            int id = json.getInt("id");
            System.out.println(json);
            String requestedId = "2";
            Connections.userData(Integer.toString(id),accessToken,requestedId);
            System.out.println(Connections.getAllUsers(id, accessToken));
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
