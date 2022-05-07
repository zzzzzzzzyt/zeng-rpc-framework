package provider.api;

import method.GetPersonService;
import entity.Person;
import entity.PersonPOJO;

public class GetPersonServiceImpl implements GetPersonService {
    @Override
    public Person sayGetPerson(Person person) {
        return person;
    }

    //protobuf
    @Override
    public PersonPOJO.Person sayGetPerson(PersonPOJO.Person person) {
        return person;
    }
}
