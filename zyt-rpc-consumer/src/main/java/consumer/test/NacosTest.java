package consumer.test;

import constants.RpcConstants;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;

//进行测试获取
public class NacosTest {
    @Test
    public void nacosDiscovery() throws IOException {
        String serviceName = "nacos.test.1";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpUriRequest request = RequestBuilder.get(URI.create(RpcConstants.NACOS_DISCOVERY_ADDRESS +"serviceName="+serviceName)).build();
        CloseableHttpResponse response = httpClient.execute(request);

    }
}
