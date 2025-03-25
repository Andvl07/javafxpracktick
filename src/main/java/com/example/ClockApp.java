package com.example;
//сделать коэф. масштабирования
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
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ClockApp extends Application {
// различные параметры для фигурЖ( радиусы кругов, овала, размер наконечников стрелок, )
    private static final double WIDTH = 800;
    private static final double HEIGHT = 600;
    private static final double CLOCK_RADIUS = 200;
    private static final double OVAL_WIDTH = 300;
    private static final double OVAL_HEIGHT = 200;
    private static final double HAND_SCALE = 0.8;
    private static final double CLOCK_RADIUS2 = 152;
    private static final double ARROW_SIZE = 10; // Размер наконечника стрелки

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);

        // Градиентный фон
        LinearGradient backgroundGradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.GOLD),
                new Stop(1, Color.YELLOW)
        );

        // Основные элементы часов
        Ellipse oval = new Ellipse(OVAL_WIDTH, OVAL_HEIGHT);
        oval.setFill(backgroundGradient);
        oval.setStroke(Color.BLACK);
        oval.setStrokeWidth(3);

        Circle clockFace = new Circle(CLOCK_RADIUS);
        clockFace.setFill(Color.PALEGREEN);
        clockFace.setStroke(Color.BLACK);
        clockFace.setStrokeWidth(2);

        Circle clockFace2 = new Circle(CLOCK_RADIUS2);
        clockFace2.setFill(Color.TRANSPARENT);
        clockFace2.setStroke(Color.BLACK);
        clockFace2.setStrokeWidth(2);

        // Стрелки часов
        Line hourHand = new Line();
        hourHand.setStroke(Color.BLACK);
        hourHand.setStrokeWidth(6);

        Line minuteHand = new Line();
        minuteHand.setStroke(Color.BLACK);
        minuteHand.setStrokeWidth(4);

        Line secondHand = new Line();
        secondHand.setStroke(Color.RED);
        secondHand.setStrokeWidth(2);

        // Наконечники стрелок
        Polygon hourArrow = new Polygon();
        hourArrow.setFill(Color.BLACK);
        
        Polygon minuteArrow = new Polygon();
        minuteArrow.setFill(Color.BLACK);
        
        Polygon secondArrow = new Polygon();
        secondArrow.setFill(Color.RED);

        // Добавление текстовых элементов на циферблат(числомесяц и год)
        Text dateTimeText = new Text();
        dateTimeText.setFont(Font.font(20));
        dateTimeText.setFill(Color.BLACK);

        Text yearText = new Text();
        yearText.setFont(Font.font(20));
        yearText.setFill(Color.BLACK);
        StackPane.setMargin(yearText, new Insets(0, 0, 0, -OVAL_WIDTH * 0.75));

       

        // Добавление всех элементов на сцену
        root.getChildren().addAll(oval, clockFace, clockFace2);
        addRomanNumerals(root);
        root.getChildren().addAll(hourHand, minuteHand, secondHand, 
                               hourArrow, minuteArrow, secondArrow,
                               dateTimeText, yearText);

        StackPane.setMargin(dateTimeText, new Insets(CLOCK_RADIUS * 1.2, 0, 0, 0));

        // Анимация часов
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateClock(hourHand, minuteHand, secondHand, 
                          hourArrow, minuteArrow, secondArrow,
                          dateTimeText, yearText);
            }
        };
        timer.start();

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setTitle("Вариант 1 Архитектура Демидов");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // приватный метод, в котором прописываются римские цифры, радиус, на котором они располагаются от центра clockFace2 и углы, на которые они ставятся (против часовой стрелки)
    private void addRomanNumerals(StackPane root) {
        double radius = CLOCK_RADIUS * 1.75;
        addNumeral(root, 0, radius, "XII");
        addNumeral(root, 30, radius, "I");
        addNumeral(root, 60, radius, "II");
        addNumeral(root, 90, radius, "III");
        addNumeral(root, 120, radius, "IV");
        addNumeral(root, 150, radius, "V");
        addNumeral(root, 180, radius, "VI");
        addNumeral(root, 210, radius, "VII");
        addNumeral(root, 240, radius, "VIII");
        addNumeral(root, 270, radius, "IX");
        addNumeral(root, 300, radius, "X");
        addNumeral(root, 330, radius, "XI");
    }
// метод добавления римских цифр на циферблат
    private void addNumeral(StackPane root, double angle, double radius, String numeral) {
        Text text = new Text(numeral);
        text.setFont(Font.font("Arial", 24));
        text.setFill(Color.BLACK);

        double radians = Math.toRadians(angle);
        double x = radius * Math.sin(radians);
        double y = -radius * Math.cos(radians);

        StackPane.setMargin(text, new Insets(y, x, 0, 0));
        root.getChildren().add(text);
    }
/*метод, который обновляет стрелки в зависимости от Московского времени, а также обновление даты, в зависимости 
от Московской даты, считывается через встроенные API java для работы с датой и временем
также присутствует перевод времени в двенадцати часовой формат*/
    private void updateClock(Line hourHand, Line minuteHand, Line secondHand,
                           Polygon hourArrow, Polygon minuteArrow, Polygon secondArrow,
                           Text dateTimeText, Text yearText) {
        ZoneId moscowZone = ZoneId.of("Europe/Moscow");
        LocalDateTime now = LocalDateTime.now(moscowZone);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM", java.util.Locale.forLanguageTag("ru"));
        DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy", java.util.Locale.forLanguageTag("ru"));

        dateTimeText.setText(now.format(dateFormatter));
        yearText.setText(now.format(yearFormatter));

        double hour = now.getHour() % 12;
        double minute = now.getMinute();
        double second = now.getSecond();

        double hourAngle = (hour * 30) + (minute * 0.5);
        double minuteAngle = minute * 6 + (second * 0.1);
        double secondAngle = second * 6;

        updateHand(hourHand, hourAngle, CLOCK_RADIUS * HAND_SCALE * 0.5);
        updateHand(minuteHand, minuteAngle, CLOCK_RADIUS * HAND_SCALE * 0.7);
        updateHand(secondHand, secondAngle, CLOCK_RADIUS * HAND_SCALE * 0.9);
        
        updateArrow(hourArrow, hourAngle, CLOCK_RADIUS * HAND_SCALE * 0.5);
        updateArrow(minuteArrow, minuteAngle, CLOCK_RADIUS * HAND_SCALE * 0.7);
        updateArrow(secondArrow, secondAngle, CLOCK_RADIUS * HAND_SCALE * 0.9);
    }

    private void updateHand(Line hand, double angle, double totalLength) {
        // Видимая часть стрелки
        double visiblePart = totalLength;
        // Невидимая часть (чтобы стрелка не начиналась строго из центра НЕ РАБОТАЕТ)
        double oppositePart = totalLength * 0.2;
        
        double radians = Math.toRadians(angle);
        double endX = visiblePart * Math.sin(radians);
        double endY = -visiblePart * Math.cos(radians);
        double startX = -oppositePart * Math.sin(radians);
        double startY = oppositePart * Math.cos(radians);
        
        hand.setStartX(startX);
        hand.setStartY(startY);
        hand.setEndX(endX);
        hand.setEndY(endY);
    }
    
    private void updateArrow(Polygon arrow, double angle, double handLength) {
        double radians = Math.toRadians(angle);
        
        // Координаты конца стрелки (куда должен указывать наконечник)
        double tipX = handLength * Math.sin(radians);
        double tipY = -handLength * Math.cos(radians);
        
        // Координаты основания наконечника (немного отступив от конца стрелки)
        double baseLength = handLength - ARROW_SIZE;
        double baseX = baseLength * Math.sin(radians);
        double baseY = -baseLength * Math.cos(radians);
        
        // Перпендикулярные точки для создания треугольного наконечника
        double perpendicularX = ARROW_SIZE * 0.6 * Math.cos(radians);
        double perpendicularY = ARROW_SIZE * 0.6 * Math.sin(radians);
        
        // Создаем треугольник наконечника
        arrow.getPoints().clear();
        arrow.getPoints().addAll(
            tipX, tipY, // Острый конец
            baseX + perpendicularX, baseY + perpendicularY, // Первая боковая точка
            baseX - perpendicularX, baseY - perpendicularY  // Вторая боковая точка
        );
    }
// метод запуска программы
    public static void main(String[] args) {
        launch(args);
    }
}