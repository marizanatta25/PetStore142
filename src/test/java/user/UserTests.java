package user;

import common.HttpClientUtil;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.net.HttpURLConnection;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTests {

    static final String BASE_URL = "https://petstore.swagger.io/v2/user";
    static  String USERNAME = "usuarioFake";

    @Test
    @Order(1)
    void PostUserTest() throws Exception {
        JSONObject user = new JSONObject();
        user.put("id", 1234);
        user.put("username", USERNAME);
        user.put("firstName", "Joao");
        user.put("lastName", "Teste");
        user.put("email", "fake@email.com");
        user.put("password", "123456");
        user.put("phone", "999999999");
        user.put("userStatus", 1);

        HttpURLConnection conn = HttpClientUtil.makeRequest(BASE_URL, "POST", user.toString());
        Assertions.assertEquals(200, conn.getResponseCode());
        String response = HttpClientUtil.getResponse(conn);
        Assertions.assertTrue(response.contains("\"message\":\"1234\""));

    }

    @Test
    @Order(2)
    void GETUserTest() throws Exception {
    HttpURLConnection conn = HttpClientUtil.makeRequest(BASE_URL + "/" + USERNAME, "GET", null);
    System.out.println("🔍 GET URL: " + BASE_URL + "/" + USERNAME);
    System.out.println("📥 Código da resposta: " + conn.getResponseCode());

    String response = HttpClientUtil.getResponse(conn);
    System.out.println("📥 GET response: " + response);

    Assertions.assertEquals(200, conn.getResponseCode());
    Assertions.assertTrue(response.contains("\"username\":\"" + USERNAME + "\"")); 

    JSONObject json = new JSONObject(response);
    Assertions.assertEquals("Joao", json.getString("firstName"));
    Assertions.assertEquals("Teste", json.getString("lastName"));
    Assertions.assertEquals("fake@email.com", json.getString("email")); 
}

    @Test
    @Order(3)
    void PutUserTest() throws Exception {
        JSONObject user = new JSONObject();
        user.put("id", 1234);
        user.put("username", USERNAME);
        user.put("firstName", "JoaoAtualizado");

        HttpURLConnection conn = HttpClientUtil.makeRequest(BASE_URL + "/" + USERNAME, "PUT", user.toString());
        Assertions.assertEquals(200, conn.getResponseCode());
    }

    @Test
    @Order(4)
    void DeleteUserTest() throws Exception {
        HttpURLConnection conn = HttpClientUtil.makeRequest(BASE_URL + "/" + USERNAME, "DELETE", null);
        int deleteCode = conn.getResponseCode();
        System.out.println("🗑️ Código de resposta ao deletar: " + deleteCode);
        Assertions.assertTrue(deleteCode == 200 || deleteCode == 404);

        System.out.println("🗑️ Código de resposta ao deletar: " + conn.getResponseCode());
        Assertions.assertEquals(200, conn.getResponseCode());


    // GET após DELETE (espera 404)
    HttpURLConnection getConn = HttpClientUtil.makeRequest(BASE_URL + "/" + USERNAME, "GET", null);
    Assertions.assertEquals(404, getConn.getResponseCode());
    }
}
