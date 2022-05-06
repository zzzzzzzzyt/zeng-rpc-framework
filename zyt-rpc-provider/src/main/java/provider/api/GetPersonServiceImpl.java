package provider.api;

import method.GetPersonService;
import serialization.entity.Person;

public class GetPersonServiceImpl implements GetPersonService {
    @Override
    public Person sayGetPerson(Person person) {
        return person;
    }
}
