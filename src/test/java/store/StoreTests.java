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


    @Test
    @Order(1)
    void PostOrderTest() throws Exception {
        JSONObject order = new JSONObject();
        order.put("id", ORDER_ID);
        order.put("petid", 0);
        order.put("quantity",0);
        order.put("shipDate", "2025-07-04T10:30:00.000Z" );
        order.put("status", "placed");
        order.put("complete",true);
        

        HttpURLConnection conn = HttpClientUtil.makeRequest(BASE_URL, "POST", order.toString());
        Assertions.assertEquals(200, conn.getResponseCode());
    }

    @Test
    @Order(2)
    void GetOrderTest() throws Exception {
        HttpURLConnection conn = HttpClientUtil.makeRequest(BASE_URL + "/" + ORDER_ID, "GET", null);
        String response = HttpClientUtil.getResponse(conn);
        JSONObject json = new JSONObject(response);
        ORDER_ID = json.getInt("id");
        Assertions.assertTrue(response.contains("\"id\":" + ORDER_ID));
    }

    @Test
    @Order(3)
    void DeleteOrderTest() throws Exception {
        HttpURLConnection conn = HttpClientUtil.makeRequest(BASE_URL + "/"+ ORDER_ID, "DELETE", null);
        Assertions.assertEquals(200, conn.getResponseCode());
    }
}

