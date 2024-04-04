package org.openjfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.*;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.scene.canvas.*;


/**
 * JavaFX App
 */
public class App extends Application {

    double brushSize;

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane root = new BorderPane();
        StackPane leftPane = new StackPane();
        

        //Setup Canvas for drawing
        Canvas canvas = new Canvas(1280, 720);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(javafx.scene.paint.Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());


        root.setCenter(canvas);

        Button btn1 = new Button("Bigger");
        StackPane.setMargin(btn1, new javafx.geometry.Insets(100, 0, 50, 0));
        Button btn2 = new Button("Smaller");
        StackPane.setMargin(btn2, new javafx.geometry.Insets(50, 0, 100, 0));
        

        btn1.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent arg0) {
                brushSize += 1;
            }
        });
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent arg0) {
                brushSize -= 1;
            }
        });

        ComboBox<Double> brushDropDown = new ComboBox<>();
        brushDropDown.getItems().addAll(
            1.0,
            2.0,
            3.0,
            4.0,
            5.0
        );
        StackPane.setMargin(brushDropDown, new javafx.geometry.Insets(50, 0, 50, 0));
        brushDropDown.setValue(3.0);

        brushDropDown.setOnAction(event -> {
            Double selectedOption = brushDropDown.getSelectionModel().getSelectedItem();
            brushSize = selectedOption;
        });


        //EVENT HANDLERS FOR MOUSE PRESSED AND DRAGGED
        //USES ARROW FUNCTION ->
        canvas.setOnMousePressed(event -> {
            gc.beginPath();
            gc.moveTo(event.getX(), event.getY()); //Aloittaa pathin ja siirtää sitä mihin event-muuttuja liikkuu AKA hiiri
            gc.setStroke(javafx.scene.paint.Color.BLACK); //Luo mustaa väriä Strokeksi - tämä pitää muuttaa seuraamaan valittua väriä!!
            gc.setLineWidth(brushSize); //BRUSH SIZE - tämä pitää muuttaa seuraamaan valittua kokoa!!
        });
        canvas.setOnMouseDragged(event -> {
            gc.lineTo(event.getX(), event.getY()); //Luo "viivaa" sinne, mihin hiiri liikkuu pisteestä toiseen.
            gc.stroke();    //Luo lopullisen "piirtämisen"
        });



        leftPane.getChildren().add(btn1);
        leftPane.getChildren().add(btn2);
        leftPane.getChildren().addAll(brushDropDown);
        root.setLeft(leftPane);

        

        //SETUP FOR SCENE
        Scene scene = new Scene(root, 1280, 740);
        stage.setScene(scene);
        stage.setTitle("Painter");
        stage.show();
    }
    public static void Main(String[] args) {
        launch(args);
    }


}