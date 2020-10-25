package socialnetwork.factory;

import socialnetwork.domain.validators.UserValidator;
import socialnetwork.domain.validators.Validator;
import socialnetwork.utils.ValidatorStrategy;

public class ValidatorFactory {

    private static ValidatorFactory instance = null;

    private ValidatorFactory(){}

    /**
     * Method for getting an instance of a ValidatorFactory
     * @return instance : ValidatorFactory
     */
    public static ValidatorFactory getInstance(){
        if(instance==null)
            instance=new ValidatorFactory();
        return instance;
    }

    /**
     * Method for creating a Validator based on a strategy
     * @param strategy : ValidatorStrategy
     * @return Validator<?>, according to strategy
     */
    public Validator<?> createValidator(ValidatorStrategy strategy){
        switch (strategy){
            case VALIDATE_USER -> {
                return new UserValidator();
            }
            default -> {
                return null;
            }
        }
    }

}
