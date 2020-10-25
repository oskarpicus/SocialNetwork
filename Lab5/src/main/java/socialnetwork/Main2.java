package socialnetwork;

import socialnetwork.config.ApplicationContext;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.Validator;
import socialnetwork.factory.ValidatorFactory;
import socialnetwork.repository.Repository;
import socialnetwork.repository.file.UserFile;
import socialnetwork.service.UserService;
import socialnetwork.ui.ConsoleUI;
import socialnetwork.utils.ValidatorStrategy;

public class Main2 {

    public static void main(String[] args) {
        String fileName= ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.users");
        Repository<Long, User> userFileRepository = new UserFile(fileName
                , (Validator<User>) ValidatorFactory.getInstance().createValidator(ValidatorStrategy.VALIDATE_USER));
        UserService userService = new UserService(userFileRepository);

        ConsoleUI ui = new ConsoleUI(userService);
        ui.run();
    }
}
