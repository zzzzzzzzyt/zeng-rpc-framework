package method;

import entity.Person;
import entity.PersonPOJO;

public interface GetNameService {
    String sayGetName(Person person);
    String sayGetName(PersonPOJO.Person person);
}
