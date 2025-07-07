package store;

import common.HttpClientUtil;
import org.apache.commons.csv.*;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class StoreDDT {
   
    @Test
    void PostStoreTestDDT() throws Exception {
        InputStream input = getClass().getClassLoader().getResourceAsStream("store.csv");
        Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);

        for (CSVRecord record : records) {
            JSONObject order = new JSONObject();
             order.put("id",Integer.parseInt(record.get("id")));
             order.put("petid", 0);
             order.put("quantity",0);
             order.put("shipDate", "1920-01-01T10:30:00.000Z" );
             order.put("status", "placed");
             order.put("complete",true);

            HttpURLConnection conn = HttpClientUtil.makeRequest("https://petstore.swagger.io/v2/user", "POST", order.toString());
            System.out.println("Criado: " + record.get("petid") + " - Status: " + conn.getResponseCode());
        }
    }
}