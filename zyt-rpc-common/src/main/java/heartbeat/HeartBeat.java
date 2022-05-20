package heartbeat;

import annotation.HeartBeatTool;
/**
 * @author ףӢ̨ը����
 * �������� ������
 */
@HeartBeatTool(isOpenFunction = true,
        readerIdleTimeSeconds = 4,
        writerIdleTimeSeconds = 4,
        allIdleTimeSeconds = 2)
public interface HeartBeat {
}
