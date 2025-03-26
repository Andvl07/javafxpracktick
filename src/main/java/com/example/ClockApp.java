// Пакетное объявление - указывает на принадлежность класса к пакету com.example
package com.example;

// Импорты классов для работы с датой и временем
import java.time.LocalDateTime;  // Для работы с локальной датой и временем
import java.time.ZoneId;         // Для работы с часовыми поясами
import java.time.format.DateTimeFormatter;  // Для форматирования даты и времени

import javafx.animation.AnimationTimer;  // Для создания анимации/таймера
import javafx.application.Application;  // Базовый класс JavaFX приложения
import javafx.geometry.Insets;          // Для работы с отступами
import javafx.geometry.Pos;             // Для выравнивания элементов
import javafx.scene.Group;              // Группировка элементов
import javafx.scene.Scene;             // Контейнер для сцены
import javafx.scene.layout.StackPane;   // Контейнер для компоновки элементов
import javafx.scene.paint.Color;        // Работа с цветами
import javafx.scene.paint.CycleMethod;  // Для градиентов
import javafx.scene.paint.LinearGradient;  // Линейный градиент
import javafx.scene.paint.Stop;         // Точки градиента
import javafx.scene.shape.Circle;       // Фигура круга
import javafx.scene.shape.Ellipse;      // Фигура эллипса
import javafx.scene.shape.Line;         // Линия
import javafx.scene.shape.Polygon;      // Многоугольник
import javafx.scene.text.Font;          // Шрифты текста
import javafx.scene.text.Text;          // Текстовый элемент
import javafx.stage.Stage;              // Основное окно приложения

// Главный класс приложения, наследуемый от Application
public class ClockApp extends Application {

    // Константы для размеров окна и элементов часов
    private static final double INITIAL_WIDTH = 800;       // Начальная ширина окна
    private static final double INITIAL_HEIGHT = 600;      // Начальная высота окна
    private static final double CLOCK_RADIUS = 200;        // Радиус основного циферблата
    private static final double OVAL_WIDTH = 300;          // Ширина овального корпуса часов
    private static final double OVAL_HEIGHT = 200;         // Высота овального корпуса часов
    private static final double HAND_SCALE = 0.8;          // Масштаб стрелок относительно радиуса
    private static final double CLOCK_RADIUS2 = 152;       // Радиус внутреннего декоративного круга
    private static final double LEG_WIDTH = 60;            // Ширина ножки часов
    private static final double LEG_HEIGHT = 20;           // Высота ножки часов
    private static final double LEG_ANGLE = 15;            // Угол наклона ножек

    // Главный метод запуска приложения
    @Override
    public void start(Stage primaryStage) {
        // Основной контейнер StackPane - элементы располагаются друг на друге с центрированием
        StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);  // Центрирование всех элементов

        // Группа для масштабируемого содержимого
        Group scalableContent = new Group(root);
        // Главный контейнер, содержащий scalableContent
        StackPane mainContainer = new StackPane(scalableContent);
        mainContainer.setAlignment(Pos.CENTER);

        // Создание градиента для фона корпуса часов
        // LinearGradient: (начало X, начало Y, конец X, конец Y, пропорционально, метод цикла, точки остановки)
        LinearGradient backgroundGradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.GOLD),    // Начальный цвет - золотой
                new Stop(1, Color.YELLOW)  // Конечный цвет - желтый
        );

        // Создание основного корпуса часов в форме эллипса
        Ellipse oval = new Ellipse(OVAL_WIDTH, OVAL_HEIGHT);
        oval.setFill(backgroundGradient);  // Заливка градиентом
        oval.setStroke(Color.BLACK);       // Цвет обводки
        oval.setStrokeWidth(3);            // Толщина обводки

        // Создание внешнего круга циферблата
        Circle clockFace = new Circle(CLOCK_RADIUS);
        clockFace.setFill(Color.PALEGREEN);  // Заливка бледно-зеленым
        clockFace.setStroke(Color.BLACK);    // Обводка черным
        clockFace.setStrokeWidth(2);         // Толщина обводки

        // Создание внутреннего декоративного круга
        Circle clockFace2 = new Circle(CLOCK_RADIUS2);
        clockFace2.setFill(Color.TRANSPARENT);  // Прозрачная заливка
        clockFace2.setStroke(Color.BLACK);      // Обводка черным
        clockFace2.setStrokeWidth(2);           // Толщина обводки

        // Создание стрелок часов (возвращают Group - группу элементов)
        Group hourHand = createHourHand(Color.BLACK, 4, 0.5);    // Часовая стрелка
        Group minuteHand = createMinuteHand(Color.BLACK, 3, 0.7);// Минутная стрелка
        Group secondHand = createSecondHand(Color.RED, 2, 0.9);  // Секундная стрелка

        // Создание текстовых элементов для даты и года
        Text dateTimeText = new Text();
        dateTimeText.setFont(Font.font(20));  // Установка размера шрифта
        dateTimeText.setFill(Color.BLACK);    // Цвет текста

        Text yearText = new Text();
        yearText.setFont(Font.font(20));
        yearText.setFill(Color.BLACK);
        // Установка отступа для года (левее от центра)
        StackPane.setMargin(yearText, new Insets(0, 0, 0, -OVAL_WIDTH * 0.75));

        // Создание ножек часов (эллипсы)
        // Левая ножка
        Ellipse leftLeg = new Ellipse(LEG_WIDTH/2, LEG_HEIGHT/2);
        leftLeg.setFill(Color.BLACK);       // Заливка черным
        leftLeg.setRotate(-LEG_ANGLE);      // Поворот на отрицательный угол
        // Позиционирование левой ножки
        StackPane.setMargin(leftLeg, new Insets(OVAL_HEIGHT*1.91115, 0, 0, -OVAL_WIDTH*0.5));

        // Правая ножка
        Ellipse rightLeg = new Ellipse(LEG_WIDTH/2, LEG_HEIGHT/2);
        rightLeg.setFill(Color.BLACK);
        rightLeg.setRotate(LEG_ANGLE);      // Поворот на положительный угол
        // Позиционирование правой ножки
        StackPane.setMargin(rightLeg, new Insets(OVAL_HEIGHT*1.91115, -OVAL_WIDTH*0.5, 0, 0));

        // Добавление всех элементов в корневой контейнер
        // Порядок добавления важен - первые элементы будут внизу
        root.getChildren().addAll(leftLeg, rightLeg, oval, clockFace, clockFace2);
        addRomanNumerals(root);  // Добавление римских цифр
        root.getChildren().addAll(hourHand, minuteHand, secondHand, dateTimeText, yearText);
        // Позиционирование текста даты
        StackPane.setMargin(dateTimeText, new Insets(CLOCK_RADIUS * 1.2, 0, 0, 0));

        // Создание сцены с главным контейнером
        Scene scene = new Scene(mainContainer, INITIAL_WIDTH, INITIAL_HEIGHT);

        // Обработчики изменения размера окна для масштабирования
        scene.widthProperty().addListener((obs, oldVal, newVal) -> updateScale(scene, scalableContent));
        scene.heightProperty().addListener((obs, oldVal, newVal) -> updateScale(scene, scalableContent));

        // Создание и запуск анимационного таймера
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Обновление времени каждые 16 мс (60 FPS)
                updateClock(hourHand, minuteHand, secondHand, dateTimeText, yearText);
            }
        };
        timer.start();

        // Настройка основного окна
        primaryStage.setTitle("Antique Clock with Modified Hands");  // Заголовок окна
        primaryStage.setScene(scene);        // Установка сцены
        primaryStage.setMinWidth(400);       // Минимальная ширина
        primaryStage.setMinHeight(300);      // Минимальная высота
        primaryStage.show();                 // Показать окно
    }

    // Метод создания часовой стрелки
    private Group createHourHand(Color color, double strokeWidth, double lengthFactor) {
        // Основная линия стрелки (вертикальная, направленная вверх)
        Line line = new Line(0, 0, 0, -CLOCK_RADIUS * HAND_SCALE * lengthFactor);
        line.setStroke(color);            // Цвет линии
        line.setStrokeWidth(strokeWidth); // Толщина линии
        
        // Круглый наконечник стрелки
        Circle arrowHead = new Circle(0, -CLOCK_RADIUS * HAND_SCALE * lengthFactor, strokeWidth * 2);
        arrowHead.setFill(color);  // Заливка цветом
        
        // Группировка линии и наконечника
        return new Group(line, arrowHead);
    }

    // Метод создания минутной стрелки
    private Group createMinuteHand(Color color, double strokeWidth, double lengthFactor) {
        // Основная линия стрелки
        Line line = new Line(0, 0, 0, -CLOCK_RADIUS * HAND_SCALE * lengthFactor);
        line.setStroke(color);
        line.setStrokeWidth(strokeWidth);
        
        // Треугольный наконечник с белой заливкой и цветной обводкой
        Polygon arrowHead = new Polygon();
        double headSize = strokeWidth * 3;  // Размер наконечника
        // Координаты точек треугольника (x1,y1, x2,y2, x3,y3)
        arrowHead.getPoints().addAll(
            0.0, -CLOCK_RADIUS * HAND_SCALE * lengthFactor,  // Вершина
            -headSize, -CLOCK_RADIUS * HAND_SCALE * lengthFactor + headSize * 2,  // Левая точка
            headSize, -CLOCK_RADIUS * HAND_SCALE * lengthFactor + headSize * 2    // Правая точка
        );
        arrowHead.setFill(Color.WHITE);  // Белая заливка
        arrowHead.setStroke(color);       // Цветная обводка
        arrowHead.setStrokeWidth(1);     // Толщина обводки
        
        return new Group(line, arrowHead);
    }

    // Метод создания секундной стрелки
    private Group createSecondHand(Color color, double strokeWidth, double lengthFactor) {
        // Основная линия стрелки
        Line line = new Line(0, 0, 0, -CLOCK_RADIUS * HAND_SCALE * lengthFactor);
        line.setStroke(color);
        line.setStrokeWidth(strokeWidth);
        
        // Треугольный наконечник (меньше, чем у минутной стрелки)
        Polygon arrowHead = new Polygon();
        double headSize = strokeWidth * 2;  // Уменьшенный размер
        arrowHead.getPoints().addAll(
            0.0, -CLOCK_RADIUS * HAND_SCALE * lengthFactor,
            -headSize, -CLOCK_RADIUS * HAND_SCALE * lengthFactor + headSize * 2,
            headSize, -CLOCK_RADIUS * HAND_SCALE * lengthFactor + headSize * 2
        );
        arrowHead.setFill(color);  // Заливка цветом
        
        return new Group(line, arrowHead);
    }

    // Метод масштабирования при изменении размера окна
    private void updateScale(Scene scene, Group scalableContent) {
        double width = scene.getWidth();    // Текущая ширина сцены
        double height = scene.getHeight();  // Текущая высота сцены
        
        // Вычисление масштаба с сохранением пропорций
        double scale = Math.min(width / INITIAL_WIDTH, height / INITIAL_HEIGHT) * 0.9;
        scalableContent.setScaleX(scale);  // Масштабирование по X
        scalableContent.setScaleY(scale);  // Масштабирование по Y
        
        // Центрирование содержимого с учетом нового масштаба
        StackPane.setMargin(scalableContent, new Insets(
            (height - INITIAL_HEIGHT * scale) / 2,  // Верхний отступ
            (width - INITIAL_WIDTH * scale) / 2,    // Правый отступ
            (height - INITIAL_HEIGHT * scale) / 2,  // Нижний отступ
            (width - INITIAL_WIDTH * scale) / 2     // Левый отступ
        ));
    }

    // Метод добавления римских цифр на циферблат
    private void addRomanNumerals(StackPane root) {
        double radius = CLOCK_RADIUS * 1.75;  // Радиус расположения цифр
        // Добавление цифр для каждого часа (12 позиций)
        addNumeral(root, 0, radius, "XII");   // 12 часов
        addNumeral(root, 330, radius, "I");    // 1 час
        addNumeral(root, 300, radius, "II");   // 2 часа
        addNumeral(root, 270, radius, "III");  // 3 часа
        addNumeral(root, 240, radius, "IV");   // 4 часа
        addNumeral(root, 210, radius, "V");    // 5 часов
        addNumeral(root, 180, radius, "VI");   // 6 часов
        addNumeral(root, 150, radius, "VII");  // 7 часов
        addNumeral(root, 120, radius, "VIII"); // 8 часов
        addNumeral(root, 90, radius, "IX");    // 9 часов
        addNumeral(root, 60, radius, "X");     // 10 часов
        addNumeral(root, 30, radius, "XI");    // 11 часов
    }

    // Метод добавления одной цифры на циферблат
    private void addNumeral(StackPane root, double angle, double radius, String numeral) {
        Text text = new Text(numeral);      // Создание текстового элемента
        text.setFont(Font.font("Arial", 24));  // Шрифт и размер
        text.setFill(Color.BLACK);         // Цвет текста

        // Преобразование угла в радианы и вычисление координат
        double radians = Math.toRadians(angle);
        // x = radius * sin(angle) - смещение по горизонтали
        // y = -radius * cos(angle) - смещение по вертикали (отрицательное, так как ось Y направлена вниз)
        double x = radius * Math.sin(radians);
        double y = -radius * Math.cos(radians);

        // Установка позиции текста с помощью отступов
        StackPane.setMargin(text, new Insets(y, x, 0, 0));
        root.getChildren().add(text);  // Добавление текста в корневой контейнер
    }

    // Метод обновления времени и положения стрелок
    private void updateClock(Group hourHand, Group minuteHand, Group secondHand, Text dateTimeText, Text yearText) {
        // Получение текущего времени в часовом поясе Москвы
        ZoneId moscowZone = ZoneId.of("Europe/Moscow");
        LocalDateTime now = LocalDateTime.now(moscowZone);

        // Форматирование даты (день и месяц) и года
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM", java.util.Locale.forLanguageTag("ru"));
        DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy", java.util.Locale.forLanguageTag("ru"));

        // Установка текста для даты и года
        dateTimeText.setText(now.format(dateFormatter));
        yearText.setText(now.format(yearFormatter));

        // Вычисление углов поворота для каждой стрелки
        double hour = now.getHour() % 12;  // Текущий час (0-11)
        double minute = now.getMinute();   // Текущая минута
        double second = now.getSecond();   // Текущая секунда

        // Часовая стрелка: 30 градусов на час + 0.5 градуса на минуту
        double hourAngle = (hour * 30) + (minute * 0.5);
        // Минутная стрелка: 6 градусов на минуту + 0.1 градуса на секунду
        double minuteAngle = minute * 6 + (second * 0.1);
        // Секундная стрелка: 6 градусов на секунду
        double secondAngle = second * 6;

        // Установка углов поворота для стрелок
        hourHand.setRotate(hourAngle);
        minuteHand.setRotate(minuteAngle);
        secondHand.setRotate(secondAngle);
    }

    // Точка входа в приложение
    public static void main(String[] args) {
        launch(args);  // Запуск JavaFX приложения
    }
}