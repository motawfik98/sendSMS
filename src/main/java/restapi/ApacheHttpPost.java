package restapi;

import main.Employee;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ApacheHttpPost {
    private CloseableHttpClient httpClient;
    private HttpPost httpPost;
    private List<NameValuePair> nvps;
    private String url;
    private String name;
    private String[] salaries;
    private Employee[] employees;
//    private String api_ser = "5af1faf64e388";
    private String api_ser = "5af62080dfc6e";
    private String username = "motawfik";
    private String password = "mohamed";
    private String[] outputs;

    public ApacheHttpPost(String url) {
        this.url = url;
        initValues();
    }

    private void initValues() {
        httpClient = HttpClients.createDefault();
        nvps = new ArrayList<>();
    }

    public ApacheHttpPost(Employee[] employees, String[] salaries) {
        initValues();
        this.employees = employees;
        this.salaries = salaries;
    }

    public void sendMessages() {
        outputs = new String[employees.length];
        for (int i = 0; i < employees.length; i++) {
            String title = employees[i].getTitle();
            if (!(title.charAt(title.length()-1) == '.'))
                title = title + ".";
            name = employees[i].getName();
            String toNumber = employees[i].getPhoneNumber();
            String salary = salaries[i];

            getCorrectFormat(); // gets the correct format to the department and the name by putting + sign instead of spaces
            url = "http://smsbulko.com/smsportal/user_api.php?api_ser=" + api_ser + "&username=" + username + "&password=" + password +
                    "&request=5&unicode=0&country=65&to=" + toNumber + "&msg=Good+evening+" + title + "+" + name + "+we+have+added+" +
                    salary + "+LE&sender=SMBULKO&hlr=0";
            httpPost = new HttpPost(this.url);
            getResponse(toNumber, i);

        }
    }

    private void getCorrectFormat() {
        name = name.replace(" ", "+");
    }

    private void getResponse(String to, int position) {
        String line;
        StringBuilder stringResponse = new StringBuilder();
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            CloseableHttpResponse response = httpClient.execute(httpPost);

//            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();

            InputStream inputStream = entity.getContent();
            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = bf.readLine()) != null)
                stringResponse.append(line);

            EntityUtils.consume(entity);
//            int msgSuccess = Integer.parseInt(String.valueOf(stringResponse.charAt(5)));
//            success += msgSuccess;
        } catch (IOException e) {
            e.printStackTrace();
        }
//        return stringResponse.toString();
        if (stringResponse.indexOf("Success") != -1)
            outputs[position] = to + " Success";
        else
            outputs[position] = to + " " + stringResponse.toString();
    }

    public String getOutput() {
        StringBuilder sb = new StringBuilder();
        for (String output : outputs)
            sb.append(output).append("\n");
        return sb.toString();
    }

    public int getSuccessLength() {
        return outputs.length;
    }
}
