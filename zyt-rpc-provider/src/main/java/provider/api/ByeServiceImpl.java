package provider.api;

import api.ByeService;

public class ByeServiceImpl implements ByeService {
    @Override
    public String sayBye(String saying) {
        return "Bye,"+saying;
    }
}
