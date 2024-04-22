package org.openjfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.*;
import javafx.scene.text.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.control.TextInputDialog;
import java.lang.Object;
import javafx.scene.control.Label;
import java.io.File;
import java.io.IOException;
import java.awt.desktop.*;
import java.awt.image.RenderedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import javafx.embed.swing.SwingFXUtils;






/**
 * JavaFX App
 */
public class App extends Application {

    //Universaaleja muuttujia, joita käyttää moni ohjelman metodi
    double brushSize; //self explanatory
    double eraserSize;
    javafx.scene.paint.Color c = javafx.scene.paint.Color.BLACK; //määrittää brushin värin, alussa musta.
    boolean isBrush = true; //määrittää, onko kyseessä sivellinpiirto vai muotopiirto
    boolean isEraser = false;
    boolean isText = false;
    boolean textIsFill = true;
    String shapeSelection = ""; //määrittää, mitä muotoa aletaan piirtää muotopiirrossa.

    //tässä alla hiiren sijaintia painohetkellä seuraavia muuttujia
    double startX;
    double startY;

    //vitun github
    @Override
    public void start(Stage stage) throws Exception {
        //Borderpane on
        BorderPane root = new BorderPane();
        StackPane leftPane = new StackPane();
        

        //Setup Canvas for drawing
        Canvas canvas = new Canvas(1280, 720);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        

        //NÄMÄ PIIRTÄVÄT VALKOISEN SUORAKULMION, JOS EI OLE TAUSTAKUVAA, EI KUITENKAAN PAKOLLINEN.
        //gc.setFill(javafx.scene.paint.Color.WHITE);
        //gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        //TÄSSÄ ESIMERKKI MITEN SAA "RESOURCES"-KANSIOSTA KUVAN TAUSTAKSI
        //TÄTÄ VOI KÄYTTÄÄ HYÖDYKSI (EHKÄ) OMAN KUVAN LATAAMISEKSI TAUSTAKUVAKSI
        //Image wizCat = new Image(getClass().getResourceAsStream("/wizCat.png"));
        //gc.drawImage(wizCat, 5, 5, canvas.getWidth()-5, canvas.getHeight()-5);

        root.setCenter(canvas);

        //BrushSizea varten dropdown-menu, jonka arvot vastaavat siveltimen kokoa.
        ComboBox<Double> brushDropDown = new ComboBox<>();
        brushDropDown.getItems().addAll(
            1.0,
            2.0,
            3.0,
            4.0,
            5.0,
            6.0,
            7.0,
            8.0,
            9.0,
            10.0,
            100.0
        );
        ComboBox<String> eraserDropDown = new ComboBox<>();
        eraserDropDown.getItems().addAll(
            "Small",
            "Medium",
            "Large"
            
        );

        
        
        //Brushsize-valikon toiminto tässä.
        brushDropDown.setOnAction(event -> {
            Double selectedOption = brushDropDown.getSelectionModel().getSelectedItem();
            brushSize = selectedOption;
        });

        //Eraser koon toiminto.
        eraserDropDown.setOnAction(event -> {
            String selectedOption = eraserDropDown.getSelectionModel().getSelectedItem();
            if(selectedOption == "Small"){
                eraserSize = 10;
            }else if (selectedOption == "Medium"){
                eraserSize = 25;
            }else {
                eraserSize = 50;
        }

        }
        );

        
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
            //isBrush = sivellin käytössä ja pitää seurata hiiren liikettä painettaessa
            if(isBrush && !isEraser && !isText) {
                gc.beginPath();
                gc.moveTo(event.getX(), event.getY()); //Aloittaa pathin ja siirtää sitä mihin event-muuttuja liikkuu AKA hiiri
                gc.setStroke(c); //Luo väriä Strokeksi
                gc.setLineWidth(brushSize); //BRUSH SIZE


            //!isBrush = sivellin poiskäytöstä, jolloin tallennetaan painohetken x- ja y-koordinaatit. Käytetään tallentamaan kuviopiirron alkupisteet.
            }
            else if (!isBrush && !isEraser && isText){
                gc.setLineWidth(1.0);
                startX =event.getX();
                startY = event.getY();
                
            }
            
            else if (!isBrush) {
                
                startX = event.getX();
                startY = event.getY();
            } 
        });
        
        //Tämä seuraa hiirtä ja piirtää viivaa kun sitä vedetään painettuna.
        canvas.setOnMouseDragged(event -> {
            if(isBrush && !isEraser && !isText) {    
                gc.lineTo(event.getX(), event.getY()); //Luo "viivaa" sinne, mihin hiiri liikkuu pisteestä toiseen.
                gc.stroke();    //Luo lopullisen "piirtämisen"
            }else if(!isBrush && isEraser && !isText){
                double dx = event.getX() - startX;
                double dy = event.getY() - startY;
                double distance = Math.hypot(dx, dy);
                double step = Math.min(distance, 5); // Limit step size to 5 pixels or distance, whichever is smaller

                double angle = Math.atan2(dy, dx);
                double stepX = step * Math.cos(angle);
                double stepY = step * Math.sin(angle);

                for (double r = 0; r < distance; r += step) {
                    gc.clearRect(startX - eraserSize / 2, startY - eraserSize / 2, eraserSize, eraserSize);
                    startX += stepX;
                    startY += stepY;
    }
            }

                //gc.clearRect(event.getX()-size/2, event.getY()-size/2, size,size);
                //startX=event.getX();
                //startY=event.getY();
                
                
                
            
        });
        //Mitä käyttäjä haluaa kirjoittaa
        TextField textField1 = new TextField();
        TextField textField2 = new TextField();
        //StackPane click = new StackPane();

        //Tämä koko härdelli piirtää lopullisen muodon siihen pisteeseen, jossa hiiren painike päästetään.
        //Kunnon spagettikoodi, voisi yrittää tehdä hardcoded -> modulaarinen mutta en keksinyt miten niin ihan sama.
        canvas.setOnMouseReleased(event -> {
            if(!isBrush && !isEraser && !isText) {
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
            //Käyttäjällä on teksi työkalu päällä, piirretään tekstin hiiren klikkaamaan paikkaan
            else if (!isBrush && !isEraser && isText){
                String text = textField1.getText();
                if(textField2.getText() == "" && textIsFill){
                    gc.setFont(new Font("Verdina", 20));
                    gc.fillText(text, startX, startY);
                }else if (textField2.getText() == "" && !textIsFill) {
                    gc.setFont(new Font("Verdina", 20));
                    gc.fillText(text, startX, startY);
                }else if (textIsFill){

                Double fontSize = Double.parseDouble(textField2.getText());
                gc.setFont(new Font("Verdina", fontSize));
                gc.fillText(text, startX, startY);

                }else if (!textIsFill){
                    Double fontSize = Double.parseDouble(textField2.getText());
                    gc.setFont(new Font("Verdina", fontSize));
                    gc.strokeText(text, startX, startY);

                }

            }
            
        });

        




        //TÄMÄ ON DROPDOWN-MENU MUODON VALINTAA VARTEN.
        ComboBox<String> shapeDropDown = new ComboBox<>();
        shapeDropDown.getItems().addAll(
            "Rectangle with Fill",
            "Circle with Fill",
            "Rectangle Outline",
            "Circle Outline"
        );
        shapeDropDown.setValue("Rectangle with Fill");

        //TÄSSÄ YLLÄ OLEVAN VALIKON TOIMINTO. Tekee isBrushista falsen eli muotopiirto on käytössä ja määrittää mitä muotoa halutaan piirtää.
        shapeDropDown.setOnAction(event -> {
            String selectedString = shapeDropDown.getSelectionModel().getSelectedItem();
            shapeSelection = selectedString;
            isBrush = false;
            isEraser = false;
            isText = false;
        });

        

        

        //Nappi kumia varten
        Button eraserButton = new Button("Eraser");
        eraserButton.setOnAction(event -> {
            isBrush = false;
            isEraser = true;
            isText = false;
        });

        //Nappi vapaapiirtoa varten
        Button freeDraw = new Button("Brush");
        freeDraw.setOnAction(event -> {
            isBrush = true;
            isEraser = false;
            isText = false;
        });

        //Nappi tyhjentämään canvas
        Button eraseAll = new Button("Reset canvas");
        eraseAll.setOnAction(event -> {
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        }
        );

        //Nappi täytölle
        Button fillAll = new Button ("Fill canvas");
        fillAll.setOnAction(event ->{
            gc.setFill(c);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        });
       
        //Nappi teksti työkalun valitsemiselle
        Button enterText = new Button("Enter Text");
        enterText.setOnAction(event -> {
            isBrush = false;
            isEraser =false;
            isText = true;
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Enter Text");
            dialog.showAndWait().ifPresent(string -> textField1.setText(string));

            
        });

        //Nappi Fonttikoolle

        Button enterFontSize = new Button("Enter Font Size");
        enterFontSize.setOnAction(event ->{
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Enter Font Size");
            dialog.showAndWait().ifPresent(string ->textField2.setText(string));

            
        });
        //Nappi taustakuvan valitsemista
        Button chooseFile = new Button ("Choose a background image");
        chooseFile.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose a background picture");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("png (*.png)","*.png");
            fileChooser.getExtensionFilters().add(extFilter);
            File pic = fileChooser.showOpenDialog(stage); 
            Image image = new Image(pic.toURI().toString());
            //Image wizCat = new Image(getClass().getResourceAsStream("/wizCat.png"));
            gc.drawImage(image, 5, 5, canvas.getWidth()-5, canvas.getHeight()-5);
        });

 

        //Nappi työn tallentamista varten
        Button saveFile = new Button("Save File");
        saveFile.setOnAction(event -> {
            FileChooser savedFile = new FileChooser();
            savedFile.setTitle("Save file");

            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files", "*.PNG");
            savedFile.getExtensionFilters().add(extFilter);

            File file = savedFile.showSaveDialog(stage);
            if (file != null){
                try {
                    WritableImage writableImage = new WritableImage(1280,720);
                    canvas.snapshot(null, writableImage);
                    RenderedImage rImage = SwingFXUtils.fromFXImage(writableImage,null);
                    ImageIO.write(rImage, "png", file);
                    
                    

                }catch (IOException ex){
                    ex.printStackTrace();
                    System.out.println("ERROR");
                }
            }


            

            
        });

        //Nappi tekstin tyylin valitsemista varten 

        Button textFill = new Button("on");
        textFill.setOnAction(event -> {
            if (textFill.getText() == "on"){
                textFill.setText("off");
                textIsFill = false;

            } else{
                textFill.setText("on");
                textIsFill = true;
            }
            
        });
        

        

        
        
        
     //Nämä muuttavat brushsize-valikon napin paikkaa vasemmassa reunassa.
     StackPane.setMargin(brushDropDown, new javafx.geometry.Insets(50, 0, 100, 0));
     StackPane.setAlignment(brushDropDown, Pos.CENTER_LEFT);
     //Tämä asettaa alkuperäisarvon brushsizelle ohjelman launchatessa
     brushDropDown.setValue(brushSize);
   
     //Menu eraser koolle
     StackPane.setMargin(eraserDropDown, new javafx.geometry.Insets(50,0,210,0));
     StackPane.setAlignment(eraserDropDown, Pos.CENTER_LEFT);
     eraserDropDown.setValue("Small");




        //ComboBox<String> eraser = new ComboBox<>();
        //eraser;

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

        //Teksti Eraser size valinnalle

        Text eraserSizeText = new Text("Eraser size");
        StackPane.setMargin(eraserSizeText, new javafx.geometry.Insets(0,0, 200,0));
        StackPane.setAlignment(eraserSizeText,Pos.CENTER_LEFT);
        leftPane.getChildren().add(eraserSizeText);

        //Teksti textFill on/off
        Text textFillOnOff = new Text("Text fill on/off");
        StackPane.setMargin(textFillOnOff, new javafx.geometry.Insets(0,0, 290,0));
        StackPane.setAlignment(textFillOnOff,Pos.CENTER_LEFT);
        leftPane.getChildren().add(textFillOnOff);

        


        //About kaikki tässä alla joko lisäävät elementtejä vasempaan
        //reunaan tai siirtävät niiden paikkaa marginaalien ja alignmentin avulla.
        leftPane.getChildren().add(brushDropDown);
        brushDropDown.setMinWidth(110);

        leftPane.getChildren().add(eraserDropDown);
        eraserDropDown.setMinWidth(110);

        leftPane.getChildren().add(colorPicker);
        

        StackPane.setMargin(shapeDropDown, new javafx.geometry.Insets(0, 0, -150, 0));
        StackPane.setAlignment(shapeDropDown, Pos.CENTER_LEFT);

        StackPane.setMargin(freeDraw, new javafx.geometry.Insets(0, 0, -200, 0));
        StackPane.setAlignment(freeDraw, Pos.CENTER_LEFT);
        
        StackPane.setMargin(eraserButton, new javafx.geometry.Insets(0, 0, -200, 45));
        StackPane.setAlignment(eraserButton, Pos.CENTER_LEFT);

        StackPane.setMargin(eraseAll, new javafx.geometry.Insets(0,0,-250,0));
        StackPane.setAlignment(eraseAll, Pos.CENTER_LEFT);
        StackPane.setMargin(fillAll, new javafx.geometry.Insets(0,0,-250,84));
        StackPane.setAlignment(fillAll, Pos.CENTER_LEFT);

        StackPane.setMargin(enterText,new javafx.geometry.Insets(0,0,-300,95));
        StackPane.setAlignment(enterText, Pos.CENTER_LEFT);


        StackPane.setMargin(enterFontSize, new javafx.geometry.Insets(0,0,-300,0));
        StackPane.setAlignment(enterFontSize,Pos.CENTER_LEFT);

        StackPane.setMargin(chooseFile, new javafx.geometry.Insets(0,0,-350,0));
        StackPane.setAlignment(chooseFile,Pos.CENTER_LEFT);

        StackPane.setMargin(saveFile, new javafx.geometry.Insets(0,0,-200,92));
        StackPane.setAlignment(saveFile,Pos.CENTER_LEFT);
       
        StackPane.setMargin(textFill, new javafx.geometry.Insets(50,0,300,0));
        StackPane.setAlignment(textFill,Pos.CENTER_LEFT);

        leftPane.getChildren().addAll(shapeDropDown, freeDraw,eraseAll,enterFontSize,chooseFile,saveFile,textFill);
        leftPane.getChildren().add(enterText);
        leftPane.getChildren().add(eraserButton);
        leftPane.getChildren().add(fillAll);

        root.setLeft(leftPane);
        

        //  Sovellukselle kuvake, voi tietty vaihtaa mutta täsä ny eka.
        Image logo = new Image (getClass().getClassLoader().getResourceAsStream("icon.png"));  

        //SETUP FOR SCENE
        Scene scene = new Scene(root, 1280, 740);
        stage.setScene(scene);
        stage.setTitle("Painter");
        stage.getIcons().add(logo);
        stage.show();

        

    }
    

    
    

    //Tämä starttaa kun ohjelma starttaa.
    public static void Main(String[] args) {
        launch(args);
    }


}