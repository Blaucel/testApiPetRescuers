
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
                        
            Connections.Connect();
            
  
            // VARIABLES DE PRUEBAS
            
            String requestedId = "2";
            int requestedIdInt = 2;
            int requestedIdAssociation = 1;
            int modificarId = 79;
            int modificarIdAssoc= 1;
            String requestedNameAssociation = "PetRescuers";
            
            // OBTENER TOKEN DIARIO
            String dailyToken = Connections.dailyToken();
            //System.out.println(dailyToken);
            
            // LOGIN DE USUARIO PARA OBTENER EL TOKEN PERSONAL
            JSONObject json =Connections.login(dailyToken,username,password);
            //System.out.println(json);
            String accessToken = json.getString("access_token");
            //System.out.println("Token usuario: "+accessToken);
            int id = json.getInt("id");
            
            // COMPROBAR ESTADO DEL SERVIDOR Y LA CONEXIÓN
            //Connections.Connect();
            
            // COMPROBAR CREAR USUARIO
            //Connections.createUser (dailyToken);
            
            // COMPROBAR OBTENER DATOS DE USUARIO POR ID
            //Connections.userData(Integer.toString(id),accessToken,requestedId);
            
            // COMPROBAR OBTENER USUARIO POR NOMBRE
            //System.out.println(Connections.getUserByName(id, "Roldán", accessToken));
            
            // COMPROBAR ACTUALIZAR USUARIO
            //Connections.updateUser(requestedIdInt, modificarId, accessToken);
            
            // COMPROBAR OBTENER TODO EL LISTADO DE USUARIOS
            //System.out.println(Connections.getAllUsers(id, accessToken));
            
            // COMPROBAR CREAR MASCOTA
            //System.out.println(Connections.createPet(requestedIdInt, accessToken));
            
            // COMPROBAR OBTENER TODO EL LISTADO DE ASOCIACIONES
            //System.out.println(Connections.getAllAssociations(id, accessToken));
            
            // COMPROBAR OBTENER ASOCIACIONES POR ID
            //System.out.println(Connections.getAssociation(id,requestedIdAssociation, accessToken));
            
            // COMPROBAR OBTENER ASOCIACIONES POR NOMBRE
            //System.out.println(Connections.getAssociationByName(id,requestedNameAssociation, accessToken));
            
            // COMPROBAR ACTUALIZAR UNA ASOCIACIÓN
            //Connections.updateAssociation(id, modificarIdAssoc, accessToken);
            
            // COMPROBAR CREAR UNA ASOCIACIÓN
            //Connections.createAssociation(id, accessToken);

        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
