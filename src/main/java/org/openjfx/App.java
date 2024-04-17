package org.openjfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.*;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.*;
import javafx.scene.text.*;


/**
 * JavaFX App
 */
public class App extends Application {

    //Universaaleja muuttujia, joita käyttää moni ohjelman metodi
    double brushSize;
    javafx.scene.paint.Color c = javafx.scene.paint.Color.BLACK;
    boolean isBrush = true;
    String shapeSelection = "";

    //tässä alla hiirtä seuraavia muuttujia
    double startX;
    double startY;

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

        ComboBox<Double> brushDropDown = new ComboBox<>();
        brushDropDown.getItems().addAll(
            1.0,
            2.0,
            3.0,
            4.0,
            5.0
        );
        StackPane.setMargin(brushDropDown, new javafx.geometry.Insets(50, 0, 100, 0));
        StackPane.setAlignment(brushDropDown, Pos.CENTER_LEFT);
        brushDropDown.setValue(brushSize);
        

        brushDropDown.setOnAction(event -> {
            Double selectedOption = brushDropDown.getSelectionModel().getSelectedItem();
            brushSize = selectedOption;
        });

        
        //Värin valinta
        final ColorPicker colorPicker = new ColorPicker();
        colorPicker.setValue(c);
        colorPicker.setOnAction(event -> {
            c = colorPicker.getValue();
        });
        //Värin nappi
        colorPicker.getStyleClass().add("button");
        StackPane.setMargin(colorPicker, new javafx.geometry.Insets(100, 50, 50, 0));
        


        //EVENT HANDLERS FOR MOUSE PRESSED AND DRAGGED
        //USES ARROW FUNCTION ->
        canvas.setOnMousePressed(event -> {
            if(isBrush) {
                gc.beginPath();
                gc.moveTo(event.getX(), event.getY()); //Aloittaa pathin ja siirtää sitä mihin event-muuttuja liikkuu AKA hiiri
                gc.setStroke(c); //Luo väriä Strokeksi
                gc.setLineWidth(brushSize); //BRUSH SIZE
            } else if (!isBrush) {
                startX = event.getX();
                startY = event.getY();
            }
        });
        canvas.setOnMouseDragged(event -> {
            if(isBrush) {    
                gc.lineTo(event.getX(), event.getY()); //Luo "viivaa" sinne, mihin hiiri liikkuu pisteestä toiseen.
                gc.stroke();    //Luo lopullisen "piirtämisen"
            }
        });

        canvas.setOnMouseReleased(event -> {
            if(!isBrush) {
                gc.setLineWidth(brushSize);
                if(shapeSelection=="Rectangle with Fill") {
                    gc.setFill(c);
                    gc.fillRect(startX, startY, event.getX()-startX, event.getY()-startY);
                } else if (shapeSelection=="Circle with Fill") {
                    gc.setFill(c);
                    gc.fillOval(startX, startY, event.getX()-startX, event.getY()-startY);
                } else if (shapeSelection=="Rectangle Outline") {
                    gc.setStroke(c);
                    gc.strokeRect(startX, startY, event.getX()-startX, event.getY()-startY);
                } else if (shapeSelection=="Circle Outline") {
                    gc.setStroke(c);
                    gc.strokeOval(startX, startY, event.getX()-startX, event.getY()-startY);
                }
            }
        });

        /*//Nappi muodon piirtoa varten
        Button shapeDraw = new Button("Shape");
        shapeDraw.setOnAction(event -> {
            isBrush = false;
        });*/
        ComboBox<String> shapeDropDown = new ComboBox<>();
        shapeDropDown.getItems().addAll(
            "Rectangle with Fill",
            "Circle with Fill",
            "Rectangle Outline",
            "Circle Outline"
        );
        shapeDropDown.setValue("Rectangle with Fill");
        shapeDropDown.setOnAction(event -> {
            String selectedString = shapeDropDown.getSelectionModel().getSelectedItem();
            shapeSelection = selectedString;
            isBrush = false;
        });

        //Nappi vapaapiirtoa varten
        Button freeDraw = new Button("Brush");
        freeDraw.setOnAction(event -> {
            isBrush = true;
        });

        //Teksti Brush Sizen valinnalle
        Text brushSizeText = new Text("Brush Size");
        StackPane.setMargin(brushSizeText, new javafx.geometry.Insets(0, 0, 90, 0));
        StackPane.setAlignment(brushSizeText, Pos.CENTER_LEFT);
        leftPane.getChildren().add(brushSizeText);

        //Teksti Brush Color valinnalle
        Text brushColorText = new Text("Brush Color");
        StackPane.setMargin(brushColorText, new javafx.geometry.Insets(0, 0, -10, 0));
        StackPane.setAlignment(brushColorText, Pos.CENTER_LEFT);
        leftPane.getChildren().add(brushColorText);

        leftPane.getChildren().add(brushDropDown);
        brushDropDown.setMinWidth(110);

        leftPane.getChildren().add(colorPicker);

        StackPane.setMargin(shapeDropDown, new javafx.geometry.Insets(0, 0, -150, 0));
        StackPane.setAlignment(shapeDropDown, Pos.CENTER_LEFT);

        StackPane.setMargin(freeDraw, new javafx.geometry.Insets(0, 0, -200, 0));
        StackPane.setAlignment(freeDraw, Pos.CENTER_LEFT);
        leftPane.getChildren().addAll(shapeDropDown, freeDraw);

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