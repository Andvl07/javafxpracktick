package com.example;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ClockApp extends Application {

    private static final double WIDTH = 800;
    private static final double HEIGHT = 600;
    private static final double CLOCK_RADIUS = 200;
    private static final double OVAL_WIDTH = 300;
    private static final double OVAL_HEIGHT = 200;
    private static final double HAND_SCALE = 0.8;
    private static final double CLOCK_RADIUS2 = 152;

    @Override
    public void start(Stage primaryStage) {
        // Создаем макет (StackPane для центрирования)
        StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);

        // Создаем фон для овала
        LinearGradient backgroundGradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.GOLD),
                new Stop(1, Color.YELLOW)
        );


        // Создаем овал
        Ellipse oval = new Ellipse(OVAL_WIDTH, OVAL_HEIGHT); // Указываем ширину и высоту, а не координаты
        oval.setFill(backgroundGradient);
        oval.setStroke(Color.BLACK);
        oval.setStrokeWidth(3);

        // Создаем круг (циферблат)
        Circle clockFace = new Circle(CLOCK_RADIUS);
        clockFace.setFill(Color.PALEGREEN);
        clockFace.setStroke(Color.BLACK);
        clockFace.setStrokeWidth(2);

        Circle clockFace2 = new Circle(CLOCK_RADIUS2);
        clockFace2.setFill(Color.TRANSPARENT);
        clockFace2.setStroke(Color.BLACK);
        clockFace2.setStrokeWidth(2);

        

        // Создаем стрелки часов
        Line hourHand = new Line();
        hourHand.setStroke(Color.BLACK);
        hourHand.setStrokeWidth(4);

        Line minuteHand = new Line();
        minuteHand.setStroke(Color.BLACK);
        minuteHand.setStrokeWidth(3);

        Line secondHand = new Line();
        secondHand.setStroke(Color.RED);
        secondHand.setStrokeWidth(2);

        // Создаем текст для отображения даты и времени
        Text dateTimeText = new Text();
        dateTimeText.setFont(Font.font(20));
        dateTimeText.setFill(Color.BLACK);

        Text yearText = new Text();
        yearText.setFont(Font.font(20));
        yearText.setFill(Color.BLACK);
        StackPane.setMargin(yearText, new Insets(0, 0, 0, -OVAL_WIDTH * 0.75)); // Положение года

        // Ножки
        Circle leftLeg = new Circle(30);
        leftLeg.setFill(Color.PALEGREEN);
        StackPane.setMargin(leftLeg, new Insets(150, 0, 0, -130));

        Circle rightLeg = new Circle(30);
        rightLeg.setFill(Color.PALEGREEN);
        StackPane.setMargin(rightLeg, new Insets(150, -130, 0, 0));

        // Добавляем элементы на сцену (В ПРАВИЛЬНОМ ПОРЯДКЕ!)
        root.getChildren().addAll(oval, clockFace, clockFace2);
        
        addRomanNumerals(root);
        root.getChildren().addAll(hourHand, minuteHand, secondHand, dateTimeText, yearText, leftLeg, rightLeg);

        StackPane.setMargin(dateTimeText, new Insets(CLOCK_RADIUS * 1.2, 0, 0, 0)); // Положение даты

        // Создаем анимацию для обновления времени
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateClock(hourHand, minuteHand, secondHand, dateTimeText, yearText);
            }
        };
        timer.start();

        // Создаем сцену
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        // Настраиваем и отображаем Stage
        primaryStage.setTitle("JavaFX Clock");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addRomanNumerals(StackPane root) {
        double radius = CLOCK_RADIUS * 1.75; // Располагаем цифры ближе к краю

        addNumeral(root, 0, radius, "XXII"); //12
        addNumeral(root, 330, radius, "I");   //1
        addNumeral(root, 300, radius, "II");  //2
        addNumeral(root, 270, radius, "III"); //3
        addNumeral(root, 240, radius, "IV"); //4
        addNumeral(root, 210, radius, "V");  //5
        addNumeral(root, 180, radius, "VI"); //6
        addNumeral(root, 150, radius, "VII");//7
        addNumeral(root, 120, radius, "VIII");//8
        addNumeral(root, 90, radius, "IX"); //9
        addNumeral(root, 60, radius, "X");  //10
        addNumeral(root, 30, radius, "XI"); //11
    }

    private void addNumeral(StackPane root, double angle, double radius, String numeral) {
        Text text = new Text(numeral);
        text.setFont(Font.font("Arial", 24));
        text.setFill(Color.BLACK);

        double radians = Math.toRadians(angle);
        double x = radius * Math.sin(radians);
        double y = -radius * Math.cos(radians);

        StackPane.setMargin(text, new Insets(y, x, 0, 0));  // Используем Insets для размещения
        root.getChildren().add(text);
    }

    private void updateClock(Line hourHand, Line minuteHand, Line secondHand, Text dateTimeText, Text yearText) {
        ZoneId moscowZone = ZoneId.of("Europe/Moscow");
        LocalDateTime now = LocalDateTime.now(moscowZone);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM", java.util.Locale.forLanguageTag("ru"));
        DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy", java.util.Locale.forLanguageTag("ru"));

        String formattedDate = now.format(dateFormatter);
        String formattedYear = now.format(yearFormatter);

        dateTimeText.setText(formattedDate);
        yearText.setText(formattedYear);

        double hour = now.getHour();
        double minute = now.getMinute();
        double second = now.getSecond();

        // Преобразуем часы в 12-часовой формат
        hour = hour % 12;

        double hourAngle = (hour * 30) + (minute * 0.5);
        double minuteAngle = minute * 6 + (second * 0.1);
        double secondAngle = second * 6;

        // Координаты стрелок относительно центра
        hourHand.setStartX(0);
        hourHand.setStartY(0);
        hourHand.setEndX(CLOCK_RADIUS * HAND_SCALE * 0.5 * Math.sin(Math.toRadians(hourAngle))); // Укоротил стрелки
        hourHand.setEndY(-CLOCK_RADIUS * HAND_SCALE * 0.5 * Math.cos(Math.toRadians(hourAngle)));

        minuteHand.setStartX(0);
        minuteHand.setStartY(0);
        minuteHand.setEndX(CLOCK_RADIUS * HAND_SCALE * 0.7 * Math.sin(Math.toRadians(minuteAngle)));
        minuteHand.setEndY(-CLOCK_RADIUS * HAND_SCALE * 0.7 * Math.cos(Math.toRadians(minuteAngle)));

        secondHand.setStartX(0);
        secondHand.setStartY(0);
        secondHand.setEndX(CLOCK_RADIUS * HAND_SCALE * 0.9 * Math.sin(Math.toRadians(secondAngle)));
        secondHand.setEndY(-CLOCK_RADIUS * HAND_SCALE * 0.9 * Math.cos(Math.toRadians(secondAngle)));

        // Теперь позиционируем стрелки в центре StackPane
        hourHand.setTranslateX(0);
        hourHand.setTranslateY(0);

        minuteHand.setTranslateX(0);
        minuteHand.setTranslateY(0);

        secondHand.setTranslateX(0);
        secondHand.setTranslateY(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}