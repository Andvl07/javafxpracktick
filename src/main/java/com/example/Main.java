package com.example;  
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.text.Text;
  
public class Main extends Application{
      
    public static void main(String[] args) {
          
        launch(args);
    }
      
    @Override
    public void start(Stage stage) {
          
        // установка надписи
        Text text = new Text("Hello METANIT.COM!");
        text.setLayoutY(320);    // установка положения надписи по оси Y
        text.setLayoutX(240);   // установка положения надписи по оси X
          
        Group group = new Group(text);
          
        Scene scene = new Scene(group);
        stage.setScene(scene);
        stage.setTitle("MyShapes with JavaFX"); // надпись в шапке окна
        stage.setWidth(640);// две величины  указывают размер открываемого окна
        stage.setHeight(480);
        stage.show();
    }
}