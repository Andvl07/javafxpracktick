
package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox; //класс для создания горизонтального бокса
import javafx.scene.layout.StackPane; //класс для объединения элементов на сцене в определенной последовательности
import javafx.scene.layout.VBox; //класс для создания верт. бокса
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon; //класс полигон для создания фигуры с заливкой по точкам
import javafx.scene.shape.Rectangle; // класс прямоугольник
import javafx.scene.text.Font; //класс фон
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text; //класс для созданя текста
import javafx.stage.Stage;

public class HouseDayNight extends Application {
    private boolean lightsOn = false; //булевское значение для хранения информации о том, что перед нажатием на кнопку свет выключен
    private Rectangle rightWindow; // строка для создания правого окна
    
    @Override
    //метод для ооздания домиков в виде верт и гор боксов
    public void start(Stage primaryStage) { 
        VBox leftHouse = createHouse("День", false);
        VBox rightHouse = createHouse("Ночь", true);
        //создание кнопки с ее функционалом
        Button switchBtn = new Button("Включить свет");
        switchBtn.setOnAction(e -> {
            lightsOn = !lightsOn;
            if (lightsOn) {
                leftHouse.setStyle("-fx-background-color: yellow;");
                rightHouse.setStyle("-fx-background-color: black;");
                rightWindow.setFill(Color.YELLOW);
                switchBtn.setText("Выключить свет");
            } else {
                leftHouse.setStyle("-fx-background-color: null;");
                rightHouse.setStyle("-fx-background-color: null;");
                rightWindow.setFill(Color.LIGHTBLUE);
                switchBtn.setText("Включить свет");
            }
        });
        //настройка расстояния между элементами в боксе по гор.
        HBox houses = new HBox(40, leftHouse, rightHouse);
        houses.setAlignment(Pos.CENTER);//расположение бокса по центру горизонтально
        //настройка расстояния между домами и кнопкой
        VBox root = new VBox(30, houses, switchBtn);
        root.setAlignment(Pos.CENTER);//расположение бокса по центру вертикально
        root.setPadding(new Insets(20));//не понятен функционал строки
        root.setStyle("-fx-background-color: #f0f0f0;");
        //создание сцены(окна приложения) с заголовком
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Домики день/ночь");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    //создание прямоугольника 
    private VBox createHouse(String label, boolean withSwitchableWindow) {
        Rectangle border = new Rectangle(200, 180);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.BLACK);
        border.setStrokeWidth(2);
        //создание тела домика 
        Rectangle body = new Rectangle(160, 120, Color.WHITE);
        body.setStroke(Color.BLACK);
        //создание крыши домика по точкам полигона
        Polygon roof = new Polygon(
            100,0,    
            0,60,     
            200,60    
        );
        roof.setFill(Color.SIENNA);//цвет полигона(крыши)
        //создание окна домика
        Rectangle window = new Rectangle(40, 40, Color.LIGHTBLUE);
        window.setStroke(Color.BLACK);//граница окошка 
        
        if (withSwitchableWindow) {
            rightWindow = window;
        }
        //объединение тела домика и окна(по центру тела)
        StackPane houseBody = new StackPane();
        houseBody.getChildren().addAll(body, window);
        StackPane.setAlignment(window, Pos.CENTER);
        
        
        Text labelText = new Text(label);//надписи под домиком с цветом и типом шрифта
        labelText.setFill(Color.BLUE);
        labelText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        //настройка расстояний в боксе между крышей телом и текстом под домиками
        VBox house = new VBox(0, roof, houseBody, labelText);
        house.setAlignment(Pos.CENTER);
        house.setPadding(new Insets(10, 0, 0, 0)); 
        
        return house;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}