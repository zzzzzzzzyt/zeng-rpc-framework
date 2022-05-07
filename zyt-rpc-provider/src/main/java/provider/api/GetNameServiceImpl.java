package provider.api;

import method.GetNameService;
import entity.Person;
import entity.PersonPOJO;

public class GetNameServiceImpl implements GetNameService {
    @Override
    public String sayGetName(Person person) {
        return person.getName();
    }

    //protobuf
    @Override
    public String sayGetName(PersonPOJO.Person person) {
        return person.getName();
    }
}
