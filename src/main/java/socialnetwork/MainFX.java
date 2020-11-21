package socialnetwork;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println("ok");
      //  initView(primaryStage);
        Parent root = FXMLLoader.load(getClass().getResource("/views/logging.fxml"));
        primaryStage.setScene(new Scene(root,300,300));
        primaryStage.setTitle("Social Network");
        primaryStage.show();
  }

    public static void main(String[] args) {
        launch(args);
    }

}
