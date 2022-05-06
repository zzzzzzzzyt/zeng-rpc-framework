package provider.api;

import method.GetNameService;
import serialization.entity.Person;

public class GetNameServiceImpl implements GetNameService {
    @Override
    public String sayGetName(Person person) {
        return person.getName();
    }
}
