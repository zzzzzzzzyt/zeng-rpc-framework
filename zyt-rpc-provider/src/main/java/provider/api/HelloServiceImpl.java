package provider.api;

import method.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String saying) {
        return "Hello,"+saying;
    }
}
