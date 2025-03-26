package com.example;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
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

    // Константы для размеров окна и элементов часов
    private static final double INITIAL_WIDTH = 800;
    private static final double INITIAL_HEIGHT = 600;
    private static final double CLOCK_RADIUS = 200;      // Радиус основного циферблата
    private static final double OVAL_WIDTH = 300;        // Ширина овального корпуса
    private static final double OVAL_HEIGHT = 200;       // Высота овального корпуса
    private static final double HAND_SCALE = 0.8;        // Масштаб стрелок
    private static final double CLOCK_RADIUS2 = 152;     // Радиус внутреннего круга циферблата
    private static final double LEG_WIDTH = 60;          // Ширина ножки (большая ось)
    private static final double LEG_HEIGHT = 20;         // Высота ножки (малая ось)
    private static final double LEG_ANGLE = 15;          // Угол наклона ножек

    @Override
    public void start(Stage primaryStage) {
        // Основной контейнер для всех элементов
        StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);

        // Группа для масштабируемого содержимого
        Group scalableContent = new Group(root);
        StackPane mainContainer = new StackPane(scalableContent);
        mainContainer.setAlignment(Pos.CENTER);

        // Градиент для фона корпуса часов
        LinearGradient backgroundGradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.GOLD),
                new Stop(1, Color.YELLOW)
        );

        // Основной корпус часов (овал)
        Ellipse oval = new Ellipse(OVAL_WIDTH, OVAL_HEIGHT);
        oval.setFill(backgroundGradient);
        oval.setStroke(Color.BLACK);
        oval.setStrokeWidth(3);

        // Внешний круг циферблата
        Circle clockFace = new Circle(CLOCK_RADIUS);
        clockFace.setFill(Color.PALEGREEN);
        clockFace.setStroke(Color.BLACK);
        clockFace.setStrokeWidth(2);

        // Внутренний круг циферблата (декоративный)
        Circle clockFace2 = new Circle(CLOCK_RADIUS2);
        clockFace2.setFill(Color.TRANSPARENT);
        clockFace2.setStroke(Color.BLACK);
        clockFace2.setStrokeWidth(2);

        // Создаем стрелки часов с модифицированными наконечниками
        Group hourHand = createHourHand(Color.BLACK, 4, 0.5);      // Часовая стрелка с круглым наконечником
        Group minuteHand = createMinuteHand(Color.BLACK, 3, 0.7);  // Минутная стрелка с контурным наконечником
        Group secondHand = createSecondHand(Color.RED, 2, 0.9);    // Секундная стрелка с уменьшенным наконечником

        // Текстовые элементы для отображения даты и года
        Text dateTimeText = new Text();
        dateTimeText.setFont(Font.font(20));
        dateTimeText.setFill(Color.BLACK);

        Text yearText = new Text();
        yearText.setFont(Font.font(20));
        yearText.setFill(Color.BLACK);
        StackPane.setMargin(yearText, new Insets(0, 0, 0, -OVAL_WIDTH * 0.75));

        // Создаем две овальные ножки черного цвета
        // Левая ножка (наклонена влево)
        Ellipse leftLeg = new Ellipse(LEG_WIDTH/2, LEG_HEIGHT/2);
        leftLeg.setFill(Color.BLACK);
        leftLeg.setRotate(-LEG_ANGLE);
        StackPane.setMargin(leftLeg, new Insets(OVAL_HEIGHT*0.85, 0, 0, -OVAL_WIDTH*0.5));

        // Правая ножка (наклонена вправо)
        Ellipse rightLeg = new Ellipse(LEG_WIDTH/2, LEG_HEIGHT/2);
        rightLeg.setFill(Color.BLACK);
        rightLeg.setRotate(LEG_ANGLE);
        StackPane.setMargin(rightLeg, new Insets(OVAL_HEIGHT*0.85, -OVAL_WIDTH*0.5, 0, 0));

        // Добавляем все элементы в корневой контейнер в правильном порядке
        root.getChildren().addAll(oval, clockFace, clockFace2);
        addRomanNumerals(root);
        root.getChildren().addAll(hourHand, minuteHand, secondHand, 
                               dateTimeText, yearText, leftLeg, rightLeg);
        StackPane.setMargin(dateTimeText, new Insets(CLOCK_RADIUS * 1.2, 0, 0, 0));

        // Настройка сцены
        Scene scene = new Scene(mainContainer, INITIAL_WIDTH, INITIAL_HEIGHT);

        // Настройка масштабирования при изменении размера окна
        scene.widthProperty().addListener((obs, oldVal, newVal) -> updateScale(scene, scalableContent));
        scene.heightProperty().addListener((obs, oldVal, newVal) -> updateScale(scene, scalableContent));

        // Таймер для обновления времени
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateClock(hourHand, minuteHand, secondHand, dateTimeText, yearText);
            }
        };
        timer.start();

        // Настройка и отображение основного окна
        primaryStage.setTitle("Antique Clock with Modified Hands");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(300);
        primaryStage.show();
    }

    // Создание часовой стрелки с круглым наконечником
    private Group createHourHand(Color color, double strokeWidth, double lengthFactor) {
        // Основная линия стрелки
        Line line = new Line(0, 0, 0, -CLOCK_RADIUS * HAND_SCALE * lengthFactor);
        line.setStroke(color);
        line.setStrokeWidth(strokeWidth);
        
        // Круглый наконечник (вместо треугольного)
        Circle arrowHead = new Circle(0, -CLOCK_RADIUS * HAND_SCALE * lengthFactor, strokeWidth * 2);
        arrowHead.setFill(color);
        
        return new Group(line, arrowHead);
    }

    // Создание минутной стрелки с контурным наконечником
    private Group createMinuteHand(Color color, double strokeWidth, double lengthFactor) {
        // Основная линия стрелки
        Line line = new Line(0, 0, 0, -CLOCK_RADIUS * HAND_SCALE * lengthFactor);
        line.setStroke(color);
        line.setStrokeWidth(strokeWidth);
        
        // Контурный наконечник (только обводка, заливка белым)
        Polygon arrowHead = new Polygon();
        double headSize = strokeWidth * 3;
        arrowHead.getPoints().addAll(
            0.0, -CLOCK_RADIUS * HAND_SCALE * lengthFactor,
            -headSize, -CLOCK_RADIUS * HAND_SCALE * lengthFactor + headSize * 2,
            headSize, -CLOCK_RADIUS * HAND_SCALE * lengthFactor + headSize * 2
        );
        arrowHead.setFill(Color.WHITE);  // Внутренность белая
        arrowHead.setStroke(color);      // Контур цветной
        arrowHead.setStrokeWidth(1);
        
        return new Group(line, arrowHead);
    }

    // Создание секундной стрелки с уменьшенным наконечником
    private Group createSecondHand(Color color, double strokeWidth, double lengthFactor) {
        // Основная линия стрелки
        Line line = new Line(0, 0, 0, -CLOCK_RADIUS * HAND_SCALE * lengthFactor);
        line.setStroke(color);
        line.setStrokeWidth(strokeWidth);
        
        // Уменьшенный треугольный наконечник
        Polygon arrowHead = new Polygon();
        double headSize = strokeWidth * 2;  // Уменьшенный размер
        arrowHead.getPoints().addAll(
            0.0, -CLOCK_RADIUS * HAND_SCALE * lengthFactor,
            -headSize, -CLOCK_RADIUS * HAND_SCALE * lengthFactor + headSize * 2,
            headSize, -CLOCK_RADIUS * HAND_SCALE * lengthFactor + headSize * 2
        );
        arrowHead.setFill(color);
        
        return new Group(line, arrowHead);
    }

    // Масштабирование содержимого при изменении размера окна
    private void updateScale(Scene scene, Group scalableContent) {
        double width = scene.getWidth();
        double height = scene.getHeight();
        
        // Вычисляем масштаб, сохраняя пропорции
        double scale = Math.min(width / INITIAL_WIDTH, height / INITIAL_HEIGHT) * 0.9;
        scalableContent.setScaleX(scale);
        scalableContent.setScaleY(scale);
        
        // Центрируем содержимое
        StackPane.setMargin(scalableContent, new Insets(
            (height - INITIAL_HEIGHT * scale) / 2,
            (width - INITIAL_WIDTH * scale) / 2,
            (height - INITIAL_HEIGHT * scale) / 2,
            (width - INITIAL_WIDTH * scale) / 2
        ));
    }

    // Добавление римских цифр на циферблат
    private void addRomanNumerals(StackPane root) {
        double radius = CLOCK_RADIUS * 1.75;
        addNumeral(root, 0, radius, "XII");
        addNumeral(root, 330, radius, "I");
        addNumeral(root, 300, radius, "II");
        addNumeral(root, 270, radius, "III");
        addNumeral(root, 240, radius, "IV");
        addNumeral(root, 210, radius, "V");
        addNumeral(root, 180, radius, "VI");
        addNumeral(root, 150, radius, "VII");
        addNumeral(root, 120, radius, "VIII");
        addNumeral(root, 90, radius, "IX");
        addNumeral(root, 60, radius, "X");
        addNumeral(root, 30, radius, "XI");
    }

    // Добавление одной цифры на циферблат
    private void addNumeral(StackPane root, double angle, double radius, String numeral) {
        Text text = new Text(numeral);
        text.setFont(Font.font("Arial", 24));
        text.setFill(Color.BLACK);

        // Вычисление позиции цифры по углу и радиусу
        double radians = Math.toRadians(angle);
        double x = radius * Math.sin(radians);
        double y = -radius * Math.cos(radians);

        StackPane.setMargin(text, new Insets(y, x, 0, 0));
        root.getChildren().add(text);
    }

    // Обновление положения стрелок и отображения даты/времени
    private void updateClock(Group hourHand, Group minuteHand, Group secondHand, Text dateTimeText, Text yearText) {
        // Получаем текущее время в Москве
        ZoneId moscowZone = ZoneId.of("Europe/Moscow");
        LocalDateTime now = LocalDateTime.now(moscowZone);

        // Форматирование даты и года
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM", java.util.Locale.forLanguageTag("ru"));
        DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy", java.util.Locale.forLanguageTag("ru"));

        dateTimeText.setText(now.format(dateFormatter));
        yearText.setText(now.format(yearFormatter));

        // Вычисление углов для стрелок
        double hour = now.getHour() % 12;
        double minute = now.getMinute();
        double second = now.getSecond();

        double hourAngle = (hour * 30) + (minute * 0.5);  // Часовая стрелка двигается плавно
        double minuteAngle = minute * 6 + (second * 0.1);  // Минутная стрелка двигается плавно
        double secondAngle = second * 6;                   // Секундная стрелка

        // Установка углов поворота для стрелок
        hourHand.setRotate(hourAngle);
        minuteHand.setRotate(minuteAngle);
        secondHand.setRotate(secondAngle);
    }

    public static void main(String[] args) {
        launch(args);
    }
}