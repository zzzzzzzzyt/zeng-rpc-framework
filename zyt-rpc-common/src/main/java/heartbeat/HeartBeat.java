package heartbeat;

import annotation.HeartBeatTool;

/**
 * @author ףӢ̨ը����
 * �������� ������   �������ʱ�� ͳһ������ͳһ��������
 */
@Deprecated
@HeartBeatTool(isOpenFunction = true,
        readerIdleTimeSeconds = 4,
        writerIdleTimeSeconds = 4,
        allIdleTimeSeconds = 2)
public interface HeartBeat {
}
