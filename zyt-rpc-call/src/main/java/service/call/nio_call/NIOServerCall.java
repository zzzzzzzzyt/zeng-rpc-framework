package service.call.nio_call;

import service.nio_bootstrap.NIOServerBootStrap;

//通用启动类 将启动的逻辑藏在ServerBootStrap中

public class NIOServerCall {
    public static void main(String[] args) {
        NIOServerBootStrap.start();
    }
}
