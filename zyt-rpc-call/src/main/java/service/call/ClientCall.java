package service.call;


import service.bootstrap.ClientBootStrap;

import java.io.IOException;

//通用启动类 将启动的逻辑藏在ClientBootStrap中
public class ClientCall {
    public static void main(String[] args) throws IOException {
        ClientBootStrap.start();
    }
}
