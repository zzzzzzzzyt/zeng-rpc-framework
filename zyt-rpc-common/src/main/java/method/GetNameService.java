package method;

import entity.Person;
import entity.PersonPOJO;

/**
 * @author ×£Ó¢Ì¨Õ¨ÓÍÌõ
 */
public interface GetNameService {
    String sayGetName(Person person);
    String sayGetName(PersonPOJO.Person person);
}
