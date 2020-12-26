package socialnetwork.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import socialnetwork.controller.pages.PageActions;
import socialnetwork.domain.User;
import socialnetwork.service.MasterService;
import socialnetwork.service.PagingService;
import socialnetwork.utils.events.user.UserEvent;
import socialnetwork.utils.observer.Observer;
import socialnetwork.utils.runners.SendFriendRequestRunner;

import java.util.List;
import java.util.stream.Collectors;

public class SearchController extends AbstractController implements Observer<UserEvent> {

    private final ObservableList<User> model = FXCollections.observableArrayList();

    @FXML
    Pagination pagination;
    @FXML
    Button buttonSendFriendRequest;
    @FXML
    TextField textFieldName;
    @FXML
    TableColumn<User,String> tableColumnFirstName;
    @FXML
    TableColumn<User,String> tableColumnLastName;
    @FXML
    TableView<User> tableViewUsers;
    @FXML
    Label labelHome;
    @FXML
    Label labelFriends;
    @FXML
    Label labelFriendRequests;

    @Override
    public void initialize(PageActions pageActions){
        super.initialize(pageActions);
        pageActions.getService().addUserObserver(this);
        initTable();
        MasterService service = pageActions.getService();
        Platform.runLater(()->pagination.setPageCount((int)Math.ceil((double)service.getAllUsers().size()/ PagingService.pageSize)));
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer param) {
                List<User> all = getAllUsers(service.getUsersPage(param));
                model.setAll(all);
                tableViewUsers.setItems(model);
                if(all.isEmpty()){
                    return null;
                }
                return tableViewUsers;
            }
        });
    }

    @Override
    public void closeWindow() {
        Stage stage = (Stage) textFieldName.getScene().getWindow();
        stage.close();
    }

    @Override
    public void update(UserEvent e){
        MasterService service = pageActions.getService();
        pagination.setPageCount((int)Math.ceil((double)service.getAllUsers().size()/ PagingService.pageSize));
        setTableViewData(service.getUsersPage(pagination.getCurrentPageIndex()));
    }

    private void setTableViewData(List<User> list){
        model.setAll(getAllUsers(list));
        tableViewUsers.setItems(model);
    }


    private List<User> getAllUsers(List<User> listOfUsers){
        return listOfUsers
                .stream()
                .filter(user -> (!user.equals(pageActions.getLoggedUser())))
                .collect(Collectors.toList());
    }

    private void initTable(){
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    }

    public void handleSendFriendRequest(ActionEvent actionEvent) {
        User selected = getSelectedUser();
        if(selected==null)
            return;
        SendFriendRequestRunner runner = new SendFriendRequestRunner(pageActions.getLoggedUser().getId(),selected.getId(), pageActions.getService());
        runner.execute();
    }

    public void handleTextFieldNameKeyTyped(KeyEvent keyEvent) {
        MasterService service = pageActions.getService();
        if(textFieldName.getText().equals(""))
            setTableViewData(service.getUsersPage(pagination.getCurrentPageIndex()));
        else {
            model.setAll(this.getAllUsers(service.filterUsers(textFieldName.getText())));
        }
    }

    private User getSelectedUser(){
        User user = tableViewUsers.getSelectionModel().getSelectedItem();
        if(user==null)
            MyAllert.showMessage(null, Alert.AlertType.WARNING,"Attention","You did not select a user");
        return user;
    }

    public void handleLabelHome(MouseEvent mouseEvent) {
        openWindow("home");
    }

    public void handleLabelFriends(MouseEvent mouseEvent) {
        openWindow("friends");
    }

    public void handleLabelFriendRequests(MouseEvent mouseEvent) {
        openWindow("friendRequests");
    }

    public void handleLabelMessages(MouseEvent mouseEvent) {
        openWindow("messages");
    }

    public void handleActivityReport(ActionEvent actionEvent) {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/reportActivity.fxml"));
            Pane pane = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(pane));
            stage.setTitle("Activity Report");

            ReportActivityController controller = loader.getController();
            controller.initialize(pageActions);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
