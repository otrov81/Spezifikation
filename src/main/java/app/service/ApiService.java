package app.service;

import app.model.WebRestApiModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ApiService {
    private static final String API_URL = "http://<server>:7010/api/firmendaten/all";

    public List<WebRestApiModel> fetchData() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(API_URL);
        CloseableHttpResponse response = httpClient.execute(request);

        String jsonResponse = EntityUtils.toString(response.getEntity());




        ObjectMapper objectMapper = new ObjectMapper();
        List<WebRestApiModel> data = objectMapper.readValue(jsonResponse, objectMapper.getTypeFactory().constructCollectionType(List.class, WebRestApiModel.class));

        response.close();
        httpClient.close();

        return data;
    }
}