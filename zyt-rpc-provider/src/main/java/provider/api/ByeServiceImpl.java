package provider.api;

import method.ByeService;

/**
 * @author 祝英台炸油条
 */
public class ByeServiceImpl implements ByeService {
    @Override
    public String sayBye(String saying) {
        return "Bye," + saying;
    }
}
