package com.example;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Shaple extends Application{
      
    public static void main(String[] args) {
          
        launch(args); // метод, запускающий приложение
    }
      
    @Override
    public void start(Stage stage) {
          
      Ellipse ellipse = new Ellipse(480, 240); //создание объекта эллипс и установка параметров(R(x,y))
      ellipse.setFill(Color.BLUE); //установка цвета фона эллипса

      Text text = new Text("MyShapes"); //создание объекта текст
      text.setFont(new Font( "Arial Bond", 24)); //выбор параметров шрифта
        
      StackPane stackPane = new StackPane(); //создание объекта stackPane который складывает эллипс и текст в единое целое
        stackPane.getChildren().addAll(ellipse, text); //взятие объектов для объекта stackpane
        Scene scene = new Scene(stackPane, 640, 480, Color.LIGHTYELLOW); // размеры окна и его цвет

        stage.setTitle("MyShapes with JavaFX"); //текст и создание сцен заголовка окна
        stage.setScene(scene);
        stage.show();
    }
}