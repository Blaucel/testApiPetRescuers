import java.net.*;
import java.io.*;
import org.json.*;

/**
 *
 * @author Emilio Jose Roldan Olivares
 */
public class Connections {
    static String URL_API= "http://127.0.0.1:8000/";
    //static String URL_API= "https://pet-rescuers.vercel.app/";
    
    public static void Connect () throws Exception{

        URL url = new URL(URL_API);
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

        URL url = new URL(URL_API+"token");
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
        URL url = new URL(URL_API+"login?username=" + encriptUsername + "&password=" + encriptPassword);
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
        //JSONObject json = new JSONObject(EncryptionUtils.decrypt(key, "eWduT6/s/nMB5qoeb601ZFU6lqfTbZ4GIPMS6O9BL+C2vvfQeOG+rGW09H1TdcOlabFSzVtIqwjaI21lXv8F0svFZUgCEYTV7ENT7OaPr07YaZPOsDyWxGUcBto0jwpq"));
        //System.out.println("Este es el desencriptado: " + json);
        
        return json;
    }
    
    public static void userData(String userId, String key, String requestedId){
        try {           
            URL url = new URL(URL_API+userId+"/user/" + requestedId);
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
            URL url = new URL(URL_API+"user");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            JSONObject jsonInput = new JSONObject();
            jsonInput.put("username", "Roldán");
            jsonInput.put("password", "fakepasswordX");
            jsonInput.put("email", "newuserss@example.com");
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
            System.out.println(EncryptionUtils.decrypt(key, String.valueOf(response)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static String getAllUsers(int requestId, String key) {
    try {
        URL url = new URL(URL_API + requestId + "/user");
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
    public static String getAllAssociations(int requestId, String key) {
    try {
        URL url = new URL(URL_API + requestId + "/assoc");
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
    
    public static String getAssociation(int requestId, int id, String key) {
    try {
        URL url = new URL(URL_API + requestId + "/assoc/" + id);
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
    
    public static String getAssociationByName(int requestId, String name, String key) {
    try {
        //SUPER IMPORTANTE: Todo lo que vaya en la URL debe pasar por este método
        name = URLEncoder.encode(name, "UTF-8");
        
        URL url = new URL(URL_API + requestId + "/assocname/" + name);
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
    
    public static String getUserByName(int requestId, String username, String key) {
    try {
        //SUPER IMPORTANTE: Todo lo que vaya en la URL debe pasar por este método
        username = URLEncoder.encode(username, "UTF-8");
        
        URL url = new URL(URL_API + requestId + "/username/" + username);
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
    
    public static String createPet(int requestId, String key) {
    try {
        URL url = new URL(URL_API + requestId + "/pet");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        // Crear el objeto JSON con datos de prueba
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("owner_id", 2);
        jsonInput.put("petname", "gatote test2");
        jsonInput.put("animal_type", "gato");
        jsonInput.put("description", "descripción del gato test2");
        jsonInput.put("location", "Mallorca");
        jsonInput.put("describ_location", "calle test");
        
        String originalData = jsonInput.toString();
        String encryptedData = EncryptionUtils.encrypt(key, originalData);
        String decryptedData = EncryptionUtils.decrypt(key, encryptedData);

        if (originalData.equals(decryptedData)) {
            System.out.println("Encryption and decryption were successful.");
        } else {
            System.out.println("Encryption or decryption failed.");
        }
                // Enviar el JSON
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = EncryptionUtils.encrypt(key, jsonInput.toString()).getBytes("utf-8");
            //byte[] input = EncryptionUtils.encrypt(key, new String(jsonInput.toString().getBytes("UTF-8"), "UTF-8")).getBytes("UTF-8");
            os.write(input, 0, input.length);
        }

        // Escuchar la respuesta del servidor
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
            return "POST request failed with response code: " + responseCode;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
    }

    public static void updateUser(int requestId, int id, String key) {
    try {
        URL url = new URL(URL_API + requestId + "/user/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        JSONObject jsonInput = new JSONObject();
        jsonInput.put("username", "Usuario Modificado dos");
        jsonInput.put("password", "modípass");
        jsonInput.put("email", "modimail");
        jsonInput.put("phone", "03947562");
        jsonInput.put("admin", false);
        jsonInput.put("description", "Usuario modificado felizmente");

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = EncryptionUtils.encrypt(key, jsonInput.toString()).getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int code = conn.getResponseCode();
        System.out.println(code);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
            System.out.println(EncryptionUtils.decrypt(key, String.valueOf(response)));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    public static void updateAssociation(int requestId, int id, String key) {
    try {
        URL url = new URL(URL_API + requestId + "/assoc/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        JSONObject jsonInput = new JSONObject();
        jsonInput.put("user_id", 2);
        jsonInput.put("name", "Animales de Mallorca");
        jsonInput.put("phone", "123456789");
        jsonInput.put("description", "Me lo he inventado pero seguro que existe una asociación con este nombre");
        jsonInput.put("address", "Mallorca");
        jsonInput.put("email", "e@mail.com");

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = EncryptionUtils.encrypt(key, jsonInput.toString()).getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int code = conn.getResponseCode();
        System.out.println(code);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
            System.out.println(EncryptionUtils.decrypt(key, String.valueOf(response)));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    public static void createAssociation(int requestId, String key) {
    try {
        URL url = new URL(URL_API + requestId + "/assoc");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        JSONObject jsonInput = new JSONObject();
        jsonInput.put("user_id", 5);
        jsonInput.put("name", "False Assoc");
        jsonInput.put("phone", "12421345231");
        jsonInput.put("description", "Nope");
        jsonInput.put("address", "Yep");
        jsonInput.put("email", "Nope");

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = EncryptionUtils.encrypt(key, jsonInput.toString()).getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int code = conn.getResponseCode();
        System.out.println(code);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
            System.out.println(EncryptionUtils.decrypt(key, String.valueOf(response)));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

}

