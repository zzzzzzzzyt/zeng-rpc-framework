package configuration;

import annotation.*;
import loadbalance.RandomLoadBalance;

/**
 * @Author 祝英台炸油条
 * @Time : 2022/5/20 20:42
 * 全局配置  将所有的配置都配置在它上面
 * 解压缩器 序列化器 注册中心 负载均衡 心跳机制等
 **/
/*
    @RegistryChosen

    zookeeper   zk注册中心
    nacos       nacos实现注册中心
    zkCurator   Curator协助操作注册中心
*/

/*
    @CodecSelector

    ObjectCodec
    protoc
    kryo
    protostuff
    hessian
    fst
    avro
    jackson
    fastjson
    gson
 */

 /*
    @LoadBalanceMethodImpl

    RandomLoadBalance.class
    AccessLoadBalance.class
    ConsistentLoadBalance.class
  */

/*
    @CompressSelector

    BZipUtils
    DeflaterUtils
    GZipUtils
    Lz4Utils
    ZipUtils
 */


@CompressFunction(isOpenFunction = true)
@CompressSelector(CompressTool = "DeflaterUtils")
@HeartBeatTool(isOpenFunction = true,
        readerIdleTimeSeconds = 4,
        writerIdleTimeSeconds = 4,
        allIdleTimeSeconds = 2)
@LoadBalanceMethodImpl(chosenMethod = RandomLoadBalance.class)
@RegistryChosen(registryName = "zookeeper")
@RpcSerializationSelector(RpcSerialization = "fastjson")
public class GlobalConfiguration {
}
