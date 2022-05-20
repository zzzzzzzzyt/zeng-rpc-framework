package compress.diyzip;

import compress.CompressType;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

// todo  尚未完成 有一定的问题 对这块完成度还有问题

/**
 * @author 祝英台炸油条
 */
@Slf4j
//自己实现自定义解压缩工具  使用霍夫曼编码 霍夫曼解码
public class DiyZipUtils implements CompressType {

    //线程独享的霍夫曼编码表 因为害怕线程不安全导致错误 所以设置线程独享
    private static final ThreadLocal<HashMap<Byte, String>> hashMapThreadLocal = ThreadLocal.withInitial(() -> new HashMap<>());


    @Override
    public byte[] compress(byte[] bytes) {
        byte[] compressBytes = huffmanZip(bytes);
        return compressBytes;
    }


    @Override
    public byte[] deCompress(byte[] bytes) {
        byte[] deCompressBytes = huffmanUnZip(hashMapThreadLocal.get(), bytes);
        hashMapThreadLocal.remove();
        return deCompressBytes;
    }

    /*********************************************霍夫曼编码区************************************************************/

    //进行霍夫曼压缩的  //还得看看 直接拿了之前的编写的来用
    private byte[] huffmanZip(byte[] bytes) {
        //获得对应的排序 准备进行构造霍夫曼树
        List<Node1> nodes = getNodes(bytes);

        Node1 huffmanRoot = getHuffmanTree(nodes);
        getCodes(huffmanRoot);
        return zip(bytes, hashMapThreadLocal.get());
    }


    private List<Node1> getNodes(byte[] contentBytes) {
        List<Node1> nodes = new ArrayList<>();
        HashMap<Byte, Integer> nodeMap = new HashMap<>();
        for (Byte one : contentBytes) {
            Integer value = nodeMap.get(one);
            if (value == null) {
                nodeMap.put(one, 1);
            } else {
                nodeMap.put(one, value + 1);
            }
        }
        for (Map.Entry<Byte, Integer> entry : nodeMap.entrySet()) {
            Node1 node = new Node1(entry.getKey(), entry.getValue());
            nodes.add(node);
        }
        return nodes;
    }

    private class Node1 implements Comparable<Node1> {
        //数值 存放字符本身
        Byte data;
        //权重
        int weight;
        //左右
        Node1 left;
        Node1 right;

        public Node1(Byte data, int weight) {
            this.data = data;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "Node1{" +
                    "data=" + data +
                    ", weight=" + weight +
                    '}';
        }

        //进行升序排列
        @Override
        public int compareTo(Node1 o) {
            return this.weight - o.weight;
        }

        //前序遍历
        public void preList() {
            if (this.left != null) {
                this.left.preList();
            }
            if (this.right != null) {
                this.right.preList();
            }
        }
    }


    private Node1 getHuffmanTree(List<Node1> Nodes) {
        while (Nodes.size() > 1) {
            Collections.sort(Nodes);
            Node1 leftNode = Nodes.get(0);
            Node1 rightNode = Nodes.get(1);
            Node1 parentNode = new Node1(null, leftNode.weight + rightNode.weight);
            parentNode.left = leftNode;
            parentNode.right = rightNode;
            Nodes.remove(leftNode);
            Nodes.remove(rightNode);
            Nodes.add(parentNode);
        }
        //最后的结点就是哈夫曼的根节点
        return Nodes.get(0);
    }


    private static HashMap<Byte, String> getCodes(Node1 root) {
        StringBuilder stringBuilder = new StringBuilder();
        if (root == null) {
            log.info("无法进行编码 兄弟");
        } else {
            getCodes(root.left, "0", stringBuilder);
            getCodes(root.right, "1", stringBuilder);
        }
        return hashMapThreadLocal.get();
    }


    private static HashMap<Byte, String> getCodes(Node1 node, String code, StringBuilder stringBuilder) {
        StringBuilder stringBuilder1 = new StringBuilder(stringBuilder);  //必须要创建 局部的stringBuilder 不然每次相当于都会在同一个地址上工作
        stringBuilder1.append(code);
        //判断不是空
        if (node != null) {
            //判断不是叶子节点 则进行继续遍历
            if (node.data == null) {
                //向左遍历
                getCodes(node.left, "0", stringBuilder1);
                //向右遍历
                getCodes(node.right, "1", stringBuilder1);
            } else {
                hashMapThreadLocal.get().put(node.data, stringBuilder1.toString());
            }
        }
        return hashMapThreadLocal.get();
    }


    private static byte[] zip(byte[] contentBytes, HashMap<Byte, String> huffmanMap) {
        StringBuilder stringBuilder = new StringBuilder();
        //进行取值 生成编码的字符串 然后进行压缩
        for (byte b : contentBytes) {
            String code = huffmanMap.get(b);
            stringBuilder.append(code);
        }

        //最后的生成的stringBuilder就是所需要的编码字符串 紧接着进行压缩
        int len;
        //因为要把二进制转化为字节 一个字节为8位  所以看要创建的接收数组得多长
        if (stringBuilder.length() % 8 == 0) {
            len = stringBuilder.length() / 8;
        } else {
            len = stringBuilder.length() / 8 + 1;
        }
        //创建byte数组 用于接收解压后的编码
        byte[] huffmanZipCode = new byte[len];
        int index = 0; //用于记录 目前数组的索引值
        //因为是八位一次
        for (int i = 0; i < stringBuilder.length(); i += 8) {
            Byte code = null;
            if (i + 8 > stringBuilder.length()) {
                //变形了 经过这个  不能忘记radix 要改为2 因为我们是二进制进行转换
                code = (byte) Integer.parseInt(stringBuilder.substring(i), 2);
            } else {
                code = (byte) Integer.parseInt(stringBuilder.substring(i, i + 8), 2);
            }
            huffmanZipCode[index] = code;
            //千万不能忘记索引要+1
            index++;
        }

        return huffmanZipCode;
    }

    /*********************************************霍夫曼解码区************************************************************/

    //进行霍夫曼编码解压
    private byte[] huffmanUnZip(Map<Byte, String> huffmanCodes, byte[] huffmanBytes) {
        //首先是要将生成的赫夫曼编码组转换回二进制的字符串 根据i是否到了数据的最后一位 进行添加
        //这里的stringBuilder是用来接收转换后的二进制字符串的  之前的是1010100010111111110010001011111111001000101111111100100101001101110001110000011011101000111100101000101111111100110001001010011011100
        //这里出了点bug  得到的编码不是先前的二进制编码 很奇怪  找到了 就是书签的位置
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < huffmanBytes.length; i++) {
            boolean flag = !(i == huffmanBytes.length - 1);
            byte b = huffmanBytes[i];
            stringBuilder.append(byteToBitString(flag, b));
        }
        //返回的二进制字符串
        //log.info(stringBuilder);
        //因为要根据字符串返回编码 所以 要把它原来生成的编码对应的编码表 变回 字符对应的 byte 表
        HashMap<String, Byte> deHuffmanCodes = new HashMap<>();
        for (Map.Entry<Byte, String> byteStringEntry : huffmanCodes.entrySet()) {
            deHuffmanCodes.put(byteStringEntry.getValue(), byteStringEntry.getKey());
        }
        //用于接收 解码后的byte字符串
        List<String> stringList = new ArrayList<>();
        //生成了 返回的对照表 现在开始遍历 字符串
        for (int i = 0; i < stringBuilder.length(); )  //不用i++的判断条件了 因为完成了i+count 之后开始读取的时候 又是i+i 因为初始的count=1;
        {
            //stringbuilder 这里的是接收转换回来的byte的
            StringBuilder byteStringBuilder = new StringBuilder();
            boolean flag = true;
            int count = 1;
            while (flag) {
                String huffKey = stringBuilder.substring(i, i + count); //遍历查看是不是 数组中的key 是的话 则 添加 不是的话 count+1 因为赫夫曼编码 它没有多义性 一个对应一个
                if (deHuffmanCodes.get(huffKey) == null) {
                    count++;
                } else {
                    flag = false;
                    byteStringBuilder.append(deHuffmanCodes.get(huffKey));
                }
            }
            stringList.add(byteStringBuilder.toString());
            i += count;
        }

        byte[] bytes = new byte[stringList.size()];
        for (int i = 0; i < stringList.size(); i++) {
            bytes[i] = Byte.valueOf(stringList.get(i));
        }
        //log.info(Arrays.toString(bytes));

        return bytes;
    }


    private static String byteToBitString(boolean flag, byte b) {
        //log.info(b);
        //首先 byte没有转换为二进制的方法 所以把它变为int  因为int有
        int temp = b;
        //补齐高位  如果是数组的最后一位则不需要  然后无论正负数都可以或上256  因为怕他不满8位 就要让他满八位 不
        if (flag) {
            temp |= 256;
        }
        //转换为二进制字符串
        String str = Integer.toBinaryString(temp); //返回的temp对应的补码
        if (flag) {
            //如果不是最后一位的话 就取8位
            return str.substring(str.length() - 8);
        }
        //因为最后一位也没有补高位 可以直接返回

        return str;
    }

}





