package sit.int221.eventsservice.advice.UniqueValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sit.int221.eventsservice.repositories.UserRepository;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    UserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if(userRepository.findByEmail(email) != null) {
            return userRepository.findByEmail(email).getEmail() == email;
        }
        return true;
    }


}
