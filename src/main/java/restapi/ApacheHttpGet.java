package restapi;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ApacheHttpGet {
    private CloseableHttpClient httpClient;
    private HttpGet httpGet;
    private CloseableHttpResponse response;
    private String url;

    public ApacheHttpGet(String url) {
        this.url = url;
        httpClient = HttpClients.createDefault();
        httpGet = new HttpGet(url);
        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getResponse() {
        System.out.println(response.getStatusLine());
        HttpEntity entity = response.getEntity();

        String line;
        StringBuilder stringResponse = new StringBuilder();
        try {
            InputStream inputStream = entity.getContent();
            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = bf.readLine()) != null)
                stringResponse.append(line);

            EntityUtils.consume(entity);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringResponse.toString();
    }
}
