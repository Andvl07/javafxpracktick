package com.example;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MyShapes extends Application {

    private Label statusLabel; //элемент управления, предназначенный для вывода текста на графику
    private RotateTransition rotate;
    private StackPane stackPane;
    private boolean isRotating = false; // отслеживание состояния вращения

    @Override
    public void start(Stage stage) throws Exception {
        // создание объекта эллипс
        Ellipse ellipse = new Ellipse(150, 75);

        //создание текста для круга
        Text text = new Text("My Shapes");
        text.setFont(new Font("Arial Bold", 24));

        // создание статуса об остановке(нажатии паузы) вращения
        statusLabel = new Label("Вращение остановлено");
        statusLabel.setFont(new Font("Arial", 16));

        // Массив содержащий в себе цвета для эффекта градиент
        Stop[] stops = new Stop[]{
                new Stop(0, Color.DODGERBLUE),
                new Stop(0.5, Color.LIGHTBLUE),
                new Stop(1.0, Color.LIGHTGREEN)
        };

        // создание эффекта градиент с его параметрами
        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);

        // придание эллипсу цвета градиент
        ellipse.setFill(gradient);

        // добавление эффекта отбрасывания тени на фон эллипсом
        ellipse.setEffect(new DropShadow(30, 10, 10, Color.GRAY));

        // создание эффекта отражения для текста внутри эллипса
        Reflection r = new Reflection();
        r.setFraction(.8);
        r.setTopOffset(1.0);
        text.setEffect(r);

        //создание элемента компоновки, который позволяет устанавливать одни элементы поверх других
        stackPane = new StackPane();
        stackPane.getChildren().addAll(ellipse, text);
//создание анимации вращения с её параметрами в виде цвета, элемента, который будет подвержен вращению
        rotate = new RotateTransition(Duration.millis(2500), stackPane);
        rotate.setToAngle(360);
        rotate.setFromAngle(0);
        rotate.setInterpolator(Interpolator.LINEAR);
        rotate.setCycleCount(1); // Количество вращений
        rotate.setOnFinished(e -> {
            statusLabel.setText("Вращение завершено");
            isRotating = false; // сброс вращений
        });

        /*цикл if else предназначен для выявления состояния вращения эллипса
         * после первого запуска программы и первого нажатия на эллипс выводится строка о том, что эллипс вращается
         * повторное нажатие останавливает вращение и выводится строка вращение остановлено
         * rotate.plау метод для начала вращения
         */
        stackPane.setOnMouseClicked(mouseEvent -> {
            if (rotate.getStatus() == Animation.Status.RUNNING) {
                rotate.pause();
                statusLabel.setText("Вращение остановлено");
            } else {
                rotate.play();
                statusLabel.setText("Эллипс вращается");
            }
        });
    // складывает stackPane и statusLabel в вертикальный ряд
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(stackPane, statusLabel);

        //объявление сцены и придание ей свойств
        Scene scene = new Scene(vbox, 480, 320, Color.LIGHTYELLOW);

        stage.setTitle("MyShapes with JavaFX");
        stage.setScene(scene);
        stage.show();
    }
//метод main для запуска приложения
    public static void main(String[] args) {
        launch(args);
    }
}


