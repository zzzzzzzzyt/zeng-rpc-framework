package method;

import entity.Person;
import entity.PersonPOJO;

/**
 * @author ףӢ̨ը����
 */
public interface GetNameService {
    String sayGetName(Person person);

    String sayGetName(PersonPOJO.Person person);
}
