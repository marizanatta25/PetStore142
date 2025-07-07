package user;

import common.HttpClientUtil;
import org.apache.commons.csv.*;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class UserDataDriven {

    @Test
    void PostUserTestDDT() throws Exception {
        InputStream input = getClass().getClassLoader().getResourceAsStream("users.csv");
        Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);

        for (CSVRecord record : records) {
            JSONObject user = new JSONObject();
            user.put("id", Integer.parseInt(record.get("id")));
            user.put("username", record.get("username"));
            user.put("firstName", record.get("firstName"));
            user.put("lastName", record.get("lastName"));
            user.put("email", record.get("email"));
            user.put("password", record.get("password"));
            user.put("phone", record.get("phone"));

            HttpURLConnection conn = HttpClientUtil.makeRequest("https://petstore.swagger.io/v2/user", "POST", user.toString());
            System.out.println("Criado: " + record.get("username") + " - Status: " + conn.getResponseCode());
        }
    }
}

