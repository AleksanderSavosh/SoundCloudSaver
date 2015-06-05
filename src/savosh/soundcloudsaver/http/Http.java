package savosh.soundcloudsaver.http;

import com.google.common.collect.Maps;
import com.turbomanage.httpclient.BasicHttpClient;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;

import java.util.AbstractMap;
import java.util.Map;

public class Http {

    public static String get(String domain, String urn, Map.Entry<String, String>... params){
        BasicHttpClient basicHttpClient = new BasicHttpClient("http://" + domain);

        ParameterMap parameterMap = basicHttpClient.newParams();
        for(Map.Entry<String, String> entry : params){
            parameterMap.add(entry.getKey(), entry.getValue());
        }

        basicHttpClient.addHeader("Accept", "application/json");
        basicHttpClient.setConnectionTimeout(2000);

        HttpResponse httpResponse = basicHttpClient.get(urn, parameterMap);

        return httpResponse.getBodyAsString();
    }

    public static void main(String[] args) {
        System.out.println(Http.get(
                "api.soundcloud.com",
                "/tracks.json",
                new AbstractMap.SimpleEntry<String, String>("q", "text"),
                new AbstractMap.SimpleEntry<String, String>("limit", "5"),
                new AbstractMap.SimpleEntry<String, String>("client_id", "b45b1aa10f1ac2941910a7f0d10f8e28")));

    }

}
