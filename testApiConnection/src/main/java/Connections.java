import java.net.*;
import java.io.*;
import org.json.*;

/**
 *
 * @author Emilio Jose Roldan Olivares
 */
public class Connections {
    
    public static void Connect () throws Exception{

        URL url = new URL("https://pet-rescuers.vercel.app");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        System.out.println(content.toString());
    }
    
    
    public static String dailyToken () throws Exception{

        URL url = new URL("https://pet-rescuers.vercel.app/token");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        //Hay que limpiar el string del token, ya que vuelve con comillas incluidas XD
        String dailyToken = content.toString();
        dailyToken = dailyToken.substring(1, dailyToken.length() - 1);
        return dailyToken;
    }
    
    
    public static JSONObject login (String key, String username,String password) throws Exception{
        //Para garantizar la seguridad encriptamos username y password
        String encriptUsername = EncryptionUtils.encrypt(key, username);
        String encriptPassword = EncryptionUtils.encrypt(key, password);
        //Es necesario emplear URLEncoder para evitar símbolos prohibidos en la cadena URL de la petición
        encriptUsername = URLEncoder.encode(encriptUsername, "UTF-8");
        encriptPassword = URLEncoder.encode(encriptPassword, "UTF-8");
        URL url = new URL("https://pet-rescuers.vercel.app/login?username=" + encriptUsername + "&password=" + encriptPassword);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        //Recuerda que el resultado viene encriptado por el DailyToken
        JSONObject json = new JSONObject(EncryptionUtils.decrypt(key, content.toString()));
        return json;
    }
    
    public static void userData(String userId, String key, String requestedId){
        try {           
            URL url = new URL("https://pet-rescuers.vercel.app/"+userId+"/user/" + requestedId);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            System.out.println(EncryptionUtils.decrypt(key, String.valueOf(content)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void createUser (String key){
        //Crear el JSON con los datos
        try {
            URL url = new URL("https://pet-rescuers.vercel.app/user");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            JSONObject jsonInput = new JSONObject();
            jsonInput.put("username", "test User 28");
            jsonInput.put("password", "password123");
            jsonInput.put("email", "newuser@example.com");
            jsonInput.put("admin", false);
            jsonInput.put("description", "New user description");
            
        //Enviar el JSON
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = EncryptionUtils.encrypt(key, jsonInput.toString()).getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            
        // Escuchar la respuesta del servidor
            int code = conn.getResponseCode();
            System.out.println(code);
           try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static String getAllUsers(int requestId, String key) {
    try {
        URL url = new URL("https://pet-rescuers.vercel.app/" + requestId + "/user");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return EncryptionUtils.decrypt(key,response.toString());
        } else {
            return "GET request failed with response code: " + responseCode;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
}

