
package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HouseDayNight extends Application {
    private boolean lightsOn = false;
    private Rectangle rightWindow; 
    
    @Override
    public void start(Stage primaryStage) {
        VBox leftHouse = createHouse("День", false);
        VBox rightHouse = createHouse("Ночь", true);
        
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
        
        HBox houses = new HBox(40, leftHouse, rightHouse);
        houses.setAlignment(Pos.CENTER);
        
        VBox root = new VBox(30, houses, switchBtn);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f0f0f0;");
        
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Домики день/ночь");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private VBox createHouse(String label, boolean withSwitchableWindow) {
        Rectangle border = new Rectangle(200, 180);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.BLACK);
        border.setStrokeWidth(2);
        
        Rectangle body = new Rectangle(160, 120, Color.WHITE);
        body.setStroke(Color.BLACK);
        
        Polygon roof = new Polygon(
            100,0,    
            0,60,     
            200,60    
        );
        roof.setFill(Color.SIENNA);
        
        Rectangle window = new Rectangle(40, 40, Color.LIGHTBLUE);
        window.setStroke(Color.BLACK);
        
        if (withSwitchableWindow) {
            rightWindow = window;
        }
        
        StackPane houseBody = new StackPane();
        houseBody.getChildren().addAll(body, window);
        StackPane.setAlignment(window, Pos.CENTER);
        
        
        Text labelText = new Text(label);
        labelText.setFill(Color.GREEN);
        labelText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        VBox house = new VBox(0, roof, houseBody, labelText);
        house.setAlignment(Pos.CENTER);
        house.setPadding(new Insets(10, 0, 0, 0)); // Добавляем отступ сверху для надписи
        
        return house;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}