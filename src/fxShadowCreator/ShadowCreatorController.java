package fxShadowCreator;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class ShadowCreatorController implements Initializable {

    private static final String BLURTYPE_GAUSSIAN = "gaussian";
    private static final String BLURTYPE_ONE_PASS_BOX = "one-pass-box";
    private static final String BLURTYPE_TWO_PASS_BOX = "two-pass-box";
    private static final String BLURTYPE_THREE_PASS_BOX = "three-pass-box";

    //Rectangle, the shadow is applied to
    @FXML Rectangle sampleRectangle;

    //Background on which the Rectangle is displayed
    @FXML VBox sampleBackground;

    //Textfield with de.zeiss.fxHelpers.css Syntax-String for created Shadow
    @FXML TextField shadowStringOutputTextField;

    //Button to copy String to clipboard
    @FXML Button btn_copyToClipboard;

    //StackPane containing the controls -> Needed for z-Ordering
    @FXML StackPane controlsContainerPane;

    //String for created de.zeiss.fxHelpers.css effect
    private String out_shadowString;

    //Inputs:
    @FXML ComboBox<String> in_blurtype;

    @FXML TextField in_blurradius;
    @FXML Slider slider_blurradius;
    @FXML TextField in_spread;
    @FXML Slider slider_spread;
    @FXML TextField in_offsetX;
    @FXML Slider slider_offsetX;
    @FXML TextField in_offsetY;
    @FXML Slider slider_offsetY;

    @FXML ColorPicker in_shadowcolor;
    @FXML ColorPicker in_objectcolor;
    @FXML ColorPicker in_backgroundcolor;

    //Intermediate Variables for Inputvalues
    private String out_blurtype;
    private Color out_shadowcolor;
    private int out_blurradius;
    private float out_spread;
    private int out_offsetX;
    private int out_offsetY;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Bring Controls to front (otherwise shadow would fall onto them if displaced too far)
        sampleBackground.setClip(new Rectangle(1, 0, 489, 455));

        //Setting Rectangle
        in_objectcolor.setValue(Color.ORANGE);
        sampleRectangle.setFill(in_objectcolor.getValue());
        sampleRectangle.setStroke(Color.TRANSPARENT);
        in_objectcolor.setOnAction(e -> {
            sampleRectangle.setFill(in_objectcolor.getValue());
        });

        //Setting Background
        in_backgroundcolor.setValue(Color.WHITE);
        sampleBackground.setStyle("-fx-background-color: " + colorToRGBA(in_backgroundcolor.getValue()) + ";");
        in_backgroundcolor.setOnAction(e -> {
            sampleBackground.setStyle("-fx-background-color: " + colorToRGBA(in_backgroundcolor.getValue()) + ";");
        });


        //Setting initial values for effect parameters and binding parameters/listeners to each other
        //Blurtype
        ObservableList<String> blurtypes = FXCollections.observableArrayList(BLURTYPE_GAUSSIAN, BLURTYPE_ONE_PASS_BOX, BLURTYPE_TWO_PASS_BOX, BLURTYPE_THREE_PASS_BOX);
        in_blurtype.setItems(blurtypes);
        in_blurtype.setValue(BLURTYPE_GAUSSIAN);
        in_blurtype.setOnAction(e -> blurtypeChanged());
        out_blurtype = BLURTYPE_GAUSSIAN;

        //Color
        out_shadowcolor = Color.BLACK;
        in_shadowcolor.setValue(out_shadowcolor);
        in_shadowcolor.setOnAction(e -> shadowcolorChanged());


        //Radius
        out_blurradius = 10;
        slider_blurradius.setValue(out_blurradius);
        in_blurradius.textProperty().bind(
                Bindings.format(
                        "%.0f",
                        slider_blurradius.valueProperty()
                )
        );
        slider_blurradius.valueProperty().addListener(o -> blurradiusChanged() );

        //Spread
        out_spread = 0.5f;
        slider_spread.setValue(out_spread);
        in_spread.textProperty().bind(
                Bindings.format(
                        "%.2f",
                        slider_spread.valueProperty()
                )
        );
        slider_spread.valueProperty().addListener(o -> spreadChanged() );

        //Offset X
        out_offsetX = 5;
        slider_offsetX.setValue(out_offsetX);
        in_offsetX.textProperty().bind(
                Bindings.format(
                        "%.0f",
                        slider_offsetX.valueProperty()
                )
        );
        slider_offsetX.valueProperty().addListener(o -> offsetXChanged() );

        //Offset Y
        out_offsetY = 5;
        slider_offsetY.setValue(out_offsetY);
        in_offsetY.textProperty().bind(
                Bindings.format(
                        "%.0f",
                        slider_offsetY.valueProperty()
                )
        );
        slider_offsetY.valueProperty().addListener(o -> offsetYChanged() );


        updateShadowString();
        btn_copyToClipboard.setOnAction(e ->{
            //Copy shadowstring to the System Clipboard
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(out_shadowString);
            clipboard.setContent(content);

            //Display Info Dialouge as feedback for successfull copying
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Copied to Clipboard");
            alert.setHeaderText(null);
            alert.setContentText("The effect was copied to your clipboard.");

            alert.showAndWait();
        });
    }


    private void updateShadowString(){
        String spreadformatted = String.format(java.util.Locale.US,"%.2f", out_spread);
        out_shadowString = "dropshadow(" + out_blurtype + ", "
                                            + colorToRGBA(out_shadowcolor) + ", "
                                            + out_blurradius + ", "
                                            + spreadformatted + ", "
                                            + out_offsetX + ", "
                                            + out_offsetY + ");";

        shadowStringOutputTextField.setText(out_shadowString);
        sampleRectangle.setStyle("-fx-effect: " + out_shadowString);
    }


    /** Converts a Colorvalue to a String representation of the form rgba(red, green, blue, alpha)
        @param: Color color   The input Colorvalue
        @return: String output  Has the format "rgba(r, g, b, a)"
     */
    private String colorToRGBA(Color color){
        String output = "";
        int red, green, blue;
        double alpha;

        red = (int) Math.round(color.getRed() * 255);
        green = (int) Math.round(color.getGreen() * 255);
        blue = (int) Math.round(color.getBlue() * 255);
        alpha = color.getOpacity();

        output = "rgba(" + red + ", " + green + ", " + blue + ", " + alpha + ")";
        return output;
    }

    private void blurtypeChanged(){
        out_blurtype = in_blurtype.getValue();
        System.out.println("Blurtype Changed to: " + out_blurtype);
        updateShadowString();
    }

    private void shadowcolorChanged(){
        out_shadowcolor = in_shadowcolor.getValue();
        System.out.println("Shadowcolor changed to: " + out_shadowcolor);
        updateShadowString();
    }

    private void blurradiusChanged(){
        out_blurradius = (int) Math.round(slider_blurradius.getValue());
        System.out.println("Blurradius changed to: " + out_blurradius);
        updateShadowString();
    }

    private void spreadChanged(){
        out_spread = (float) slider_spread.getValue();
        System.out.print("Spread changed to: ");
        System.out.printf("%.2f\n", out_spread);
        updateShadowString();
    }

    private void offsetXChanged(){
        out_offsetX = (int) Math.round(slider_offsetX.getValue());
        System.out.println("Offset X changed to: " + out_offsetX);
        updateShadowString();
    }

    private void offsetYChanged(){
        out_offsetY = (int) Math.round(slider_offsetY.getValue());
        System.out.println("Offset Y changed to: " + out_offsetY);
        updateShadowString();
    }

    @FXML
    private void increaseShadowRadius(){
        double current = slider_blurradius.getValue();
        slider_blurradius.setValue(current+1.0);
    }

    @FXML
    private void decreaseShadowRadius(){
        double current = slider_blurradius.getValue();
        slider_blurradius.setValue(current-1.0);
    }

    @FXML
    private void increaseSpread(){
        double current = slider_spread.getValue();
        slider_spread.setValue(current + 0.02);
    }

    @FXML
    private void decreaseSpread(){
        double current = slider_spread.getValue();
        slider_spread.setValue(current - 0.02);
    }

    @FXML
    private void increaseOffsetX(){
        double current = slider_offsetX.getValue();
        slider_offsetX.setValue(current + 1);
    }

    @FXML
    private void decreaseOffsetX(){
        double current = slider_offsetX.getValue();
        slider_offsetX.setValue(current - 1);
    }

    @FXML
    private void increaseOffsetY(){
        double current = slider_offsetY.getValue();
        slider_offsetY.setValue(current + 1);
    }

    @FXML
    private void decreaseOffsetY(){
        double current = slider_offsetY.getValue();
        slider_offsetY.setValue(current - 1);
    }
}
