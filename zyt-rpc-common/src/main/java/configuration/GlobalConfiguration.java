package configuration;

import annotation.*;
import loadbalance.RandomLoadBalance;

/**
 * @Author ףӢ̨ը����
 * @Time : 2022/5/20 20:42
 * ȫ������  �����е����ö�������������
 * ��ѹ���� ���л��� ע������ ���ؾ��� �������Ƶ�
 **/
/*
    @RegistryChosen

    zookeeper   zkע������
    nacos       nacosʵ��ע������
    zkCurator   CuratorЭ������ע������
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
