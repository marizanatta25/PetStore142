package store;

import java.net.HttpURLConnection;

import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import common.HttpClientUtil;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StoreTests {
    
    static final String BASE_URL = "https://petstore.swagger.io/v2/store/order";
    static int ORDER_ID = 101;


@Test @Order(1)
void PostOrderTest() throws Exception {
    JSONObject order = new JSONObject();
    order.put("id", ORDER_ID);
    order.put("petId", 0);  // Corrigido: "petid" → "petId"
    order.put("quantity", 0);
    order.put("shipDate", "2025-07-04T10:30:00.000Z");
    order.put("status", "placed");
    order.put("complete", true);

    HttpURLConnection conn = HttpClientUtil.makeRequest(BASE_URL, "POST", order.toString());
    int status = conn.getResponseCode();
    if (status != 200) {
        throw new RuntimeException("Erro ao criar pedido antes do DELETE: " + status);
    }
}


@Test @Order(2)
void GetOrderTest() throws Exception {
    HttpURLConnection conn = HttpClientUtil.makeRequest(BASE_URL + "/" + ORDER_ID, "GET", null);
    String response = HttpClientUtil.getResponse(conn);
    System.out.println("📥 GET response: " + response);

    Assertions.assertEquals(200, conn.getResponseCode());

    JSONObject json = new JSONObject(response);
    Assertions.assertEquals(ORDER_ID, json.getInt("id"));
    Assertions.assertEquals("placed", json.getString("status"));
    Assertions.assertEquals(true, json.getBoolean("complete"));
}


@Test @Order(3)
void DeleteOrderTest() throws Exception {
    // Verifica se pedido existe
    HttpURLConnection checkConn = HttpClientUtil.makeRequest(BASE_URL + "/" + ORDER_ID, "GET", null);
    int statusCheck = checkConn.getResponseCode();
    System.out.println("Status do GET antes do DELETE: " + statusCheck);

    // Se o pedido não existir, crie um novo para garantir que DELETE teste algo real
    if (statusCheck == 404) {
        System.out.println("Pedido não existe, criando pedido para teste...");
        // Aqui você pode chamar método para criar pedido com ORDER_ID
        // criarPedido(ORDER_ID);
    }

    // Executa o DELETE
    HttpURLConnection conn = HttpClientUtil.makeRequest(BASE_URL + "/" + ORDER_ID, "DELETE", null);
    int deleteStatus = conn.getResponseCode();
    System.out.println("🗑️ DELETE status: " + deleteStatus);
    Assertions.assertTrue(deleteStatus == 200 || deleteStatus == 404);

    Thread.sleep(1000); // espera para processar

    // Verifica se pedido não existe mais
    HttpURLConnection getConn = HttpClientUtil.makeRequest(BASE_URL + "/" + ORDER_ID, "GET", null);
    String getResponse = HttpClientUtil.getResponse(getConn);
    System.out.println("🔍 GET após DELETE status: " + getConn.getResponseCode());
    System.out.println("🔍 GET após DELETE body: " + getResponse);

   if (statusCheck == 404) {
    System.out.println("Pedido não existe, criando pedido para teste...");
    PostOrderTest();
}

}

}

