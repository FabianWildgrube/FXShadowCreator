package fxShadowCreator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


/**
* A small helper app that provides a graphic Interface for creating a JavaFX Dropshadow effect
* It provides controls to change the different parameters that affect the shadow.
* It also displays a preview of the effect on a sample Rectangle
* The resulting string to achieve the shadow effect in JavaFX-CSS Syntax is displayed as well and can be copied
* to the clipboard for further use
*
* @author : Fabian Wildgrube, 23/08/2017
*
 */
public class ShadowCreatorMain extends Application {

    private ShadowCreatorController shadowController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ShadowCreatorGUI.fxml"));
        Parent root = loader.load();
        shadowController = loader.getController();
        Scene scene = new Scene(root);
        primaryStage.setTitle("FXShadowCreator");
        primaryStage.setScene(scene);

        primaryStage.getIcons().add(new Image(ShadowCreatorMain.class.getResourceAsStream( "images/shadowcreator-icon500.png" )));
        primaryStage.getIcons().add(new Image(ShadowCreatorMain.class.getResourceAsStream( "images/shadowcreator-icon300.png" )));
        primaryStage.getIcons().add(new Image(ShadowCreatorMain.class.getResourceAsStream( "images/shadowcreator-icon100.png" )));

        primaryStage.show();
    }
}
