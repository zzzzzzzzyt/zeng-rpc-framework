package provider.api;

import entity.Person;
import entity.PersonPOJO;
import method.GetNameService;

/**
 * @author 祝英台炸油条
 */
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
