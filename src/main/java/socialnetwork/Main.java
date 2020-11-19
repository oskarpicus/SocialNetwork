package socialnetwork;

import socialnetwork.config.ApplicationContext;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.file.UserFile;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String fileName=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.users");
        //String fileName="data/users.csv";
//        Repository0<Long,Utilizator> userFileRepository = new UtilizatorFile0(fileName
//                , new UtilizatorValidator());

//        Repository<Long,Utilizator> userFileRepository = new UtilizatorFile(fileName
//                , new UtilizatorValidator());

        Repository<Long, User> userFileRepository = new UserFile(fileName
                , new UserValidator());

//
//        Repository<Long,Utilizator> userFileRepository2 = new UtilizatorFile(fileName
//                , new UtilizatorValidator());
//
        userFileRepository.findAll().forEach(System.out::println);
        System.out.println("okish");

}}


