package heartbeat;

import annotation.HeartBeatTool;
/**
 * @author 祝英台炸油条
 * 心跳机制 工具类
 */
@HeartBeatTool(isOpenFunction = true,
        readerIdleTimeSeconds = 4,
        writerIdleTimeSeconds = 4,
        allIdleTimeSeconds = 2)
public interface HeartBeat {
}
