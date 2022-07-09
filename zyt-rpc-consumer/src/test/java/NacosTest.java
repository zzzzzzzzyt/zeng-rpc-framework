
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;

import static constants.RpcConstants.NACOS_DISCOVERY_ADDRESS;

//进行测试获取

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class NacosTest {
    @Test
    public void nacosDiscovery() {
        String serviceName = "nacos.test.1";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpUriRequest request = RequestBuilder.get(URI.create(NACOS_DISCOVERY_ADDRESS + "serviceName=" + serviceName)).build();
        try {
            CloseableHttpResponse response = httpClient.execute(request);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

    }
}
