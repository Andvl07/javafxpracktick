// Пакет, к которому принадлежит класс
package com.example;

// Импорты необходимых классов
import java.time.LocalDateTime; // Для работы с датой и временем
import java.time.ZoneId; // Для работы с часовыми поясами
import java.time.format.DateTimeFormatter; // Для форматирования даты и времени

import javafx.animation.AnimationTimer; // Анимация для обновления часов
import javafx.application.Application; // Базовый класс JavaFX приложения
import javafx.geometry.Insets; // Для отступов
import javafx.geometry.Pos; // Для выравнивания элементов
import javafx.scene.Group; // Группировка элементов
import javafx.scene.Scene; // Сцена JavaFX
import javafx.scene.control.Menu; // Элемент меню
import javafx.scene.control.MenuBar; // Панель меню
import javafx.scene.control.MenuItem; // Пункт меню
import javafx.scene.layout.BorderPane; // Контейнер с разметкой
import javafx.scene.layout.StackPane; // Контейнер с наложением элементов
import javafx.scene.paint.Color; // Цвета
import javafx.scene.paint.CycleMethod; // Метод цикла для градиента
import javafx.scene.paint.LinearGradient; // Линейный градиент
import javafx.scene.paint.Stop; // Точки градиента
import javafx.scene.shape.Circle; // Круг
import javafx.scene.shape.Ellipse; // Эллипс
import javafx.scene.shape.Line; // Линия
import javafx.scene.shape.Polygon; // Многоугольник
import javafx.scene.text.Font; // Шрифт
import javafx.scene.text.Text; // Текст
import javafx.stage.Stage; // Окно приложения

// Главный класс приложения, наследуется от Application
public class ClockApp extends Application {

    // Константы для размеров окна
    private static final double INITIAL_WIDTH = 800; // Начальная ширина окна
    private static final double INITIAL_HEIGHT = 600; // Начальная высота окна
    
    // Константы для элементов часов
    private static final double CLOCK_RADIUS = 200; // Радиус основного циферблата
    private static final double OVAL_WIDTH = 300; // Ширина овального основания
    private static final double OVAL_HEIGHT = 200; // Высота овального основания
    private static final double HAND_SCALE = 0.8; // Масштаб стрелок
    private static final double CLOCK_RADIUS2 = 152; // Радиус внутреннего круга циферблата
    private static final double LEG_WIDTH = 60; // Ширина ножки часов
    private static final double LEG_HEIGHT = 20; // Высота ножки часов
    private static final double LEG_ANGLE = 15; // Угол наклона ножек часов

    // Главный метод запуска приложения
    @Override
    public void start(Stage primaryStage) {
        // Создаем главный контейнер с разметкой BorderPane
        BorderPane rootPane = new BorderPane();
        
        // Создаем панель меню
        MenuBar menuBar = new MenuBar();
        
        // Создаем меню "Файл" с пунктами
        Menu fileMenu = new Menu("Файл");
        MenuItem saveItem = new MenuItem("Сохранение"); // Пункт "Сохранение"
        MenuItem loadItem = new MenuItem("Загрузка"); // Пункт "Загрузка"
        fileMenu.getItems().addAll(saveItem, loadItem); // Добавляем пункты в меню
        
        // Создаем меню "Данные" с пунктами
        Menu dataMenu = new Menu("Данные");
        MenuItem groupItem = new MenuItem("Группа"); // Пункт "Группа"
        MenuItem disciplinesItem = new MenuItem("Дисциплины"); // Пункт "Дисциплины"
        MenuItem resultsItem = new MenuItem("Данные итогов"); // Пункт "Данные итогов"
        dataMenu.getItems().addAll(groupItem, disciplinesItem, resultsItem);
        
        // Создаем меню "Редактирование" с пунктами
        Menu editMenu = new Menu("Редактирование");
        MenuItem editItem = new MenuItem("Редактирование"); // Пункт "Редактирование"
        MenuItem viewItem = new MenuItem("Просмотр"); // Пункт "Просмотр"
        MenuItem clearItem = new MenuItem("Очистка"); // Пункт "Очистка"
        editMenu.getItems().addAll(editItem, viewItem, clearItem);
        
        // Создаем меню "Выход"
        Menu exitMenu = new Menu("Выход");
        
        // Добавляем все меню в панель меню
        menuBar.getMenus().addAll(fileMenu, dataMenu, editMenu, exitMenu);
        // Размещаем панель меню в верхней части BorderPane
        rootPane.setTop(menuBar);

        // Создаем панель с часами
        StackPane clockPane = createClockPane();
        
        // Размещаем часы в центре BorderPane
        rootPane.setCenter(clockPane);
        
        // Создаем текст с информацией о сессии
        Text sessionInfo = new Text("ИТОГИ СЕССИИ\n\nГруппа:\nКлассный руководитель:");
        sessionInfo.setFont(Font.font(20)); // Устанавливаем шрифт
        // Создаем панель для нижней части
        StackPane bottomPane = new StackPane(sessionInfo);
        bottomPane.setPadding(new Insets(10)); // Устанавливаем отступы
        // Размещаем в нижней части BorderPane
        rootPane.setBottom(bottomPane);

        // Создаем сцену с корневым контейнером
        Scene scene = new Scene(rootPane, INITIAL_WIDTH, INITIAL_HEIGHT);
        // Устанавливаем заголовок окна
        primaryStage.setTitle("Часы с заготовкой меню");
        // Устанавливаем сцену для окна
        primaryStage.setScene(scene);
        // Устанавливаем минимальные размеры окна
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(300);
        // Показываем окно
        primaryStage.show();
    }

    // Метод для создания панели с часами
    private StackPane createClockPane() {
        // Основной контейнер для часов
        StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER); // Выравнивание по центру

        // Группа для масштабируемого содержимого
        Group scalableContent = new Group(root);
        // Главный контейнер
        StackPane mainContainer = new StackPane(scalableContent);
        mainContainer.setAlignment(Pos.CENTER);

        // Создаем градиент для фона
        LinearGradient backgroundGradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.GOLD), // Начальный цвет
                new Stop(1, Color.YELLOW) // Конечный цвет
        );

        // Создаем овальное основание часов
        Ellipse oval = new Ellipse(OVAL_WIDTH, OVAL_HEIGHT);
        oval.setFill(backgroundGradient); // Заливка градиентом
        oval.setStroke(Color.BLACK); // Цвет обводки
        oval.setStrokeWidth(3); // Толщина обводки

        // Создаем основной циферблат
        Circle clockFace = new Circle(CLOCK_RADIUS);
        clockFace.setFill(Color.PALEGREEN); // Заливка цветом
        clockFace.setStroke(Color.BLACK); // Обводка
        clockFace.setStrokeWidth(2); // Толщина обводки

        // Создаем внутренний круг циферблата
        Circle clockFace2 = new Circle(CLOCK_RADIUS2);
        clockFace2.setFill(Color.TRANSPARENT); // Прозрачная заливка
        clockFace2.setStroke(Color.BLACK); // Обводка
        clockFace2.setStrokeWidth(2); // Толщина обводки

        // Создаем стрелки часов
        Group hourHand = createHourHand(Color.BLACK, 4, 0.5); // Часовая
        Group minuteHand = createMinuteHand(Color.BLACK, 3, 0.7); // Минутная
        Group secondHand = createSecondHand(Color.RED, 2, 0.9); // Секундная

        // Текст для отображения даты
        Text dateTimeText = new Text();
        dateTimeText.setFont(Font.font(20)); // Шрифт
        dateTimeText.setFill(Color.BLACK); // Цвет текста

        // Текст для отображения года
        Text yearText = new Text();
        yearText.setFont(Font.font(20)); // Шрифт
        yearText.setFill(Color.BLACK); // Цвет текста
        // Устанавливаем отступ для года
        StackPane.setMargin(yearText, new Insets(0, 0, 0, -OVAL_WIDTH * 0.75));

        // Создаем левую ножку часов
        Ellipse leftLeg = new Ellipse(LEG_WIDTH/2, LEG_HEIGHT/2);
        leftLeg.setFill(Color.BLACK); // Заливка черным
        leftLeg.setRotate(-LEG_ANGLE); // Поворот на отрицательный угол
        // Устанавливаем положение
        StackPane.setMargin(leftLeg, new Insets(OVAL_HEIGHT*1.91115, 0, 0, -OVAL_WIDTH*0.5));

        // Создаем правую ножку часов
        Ellipse rightLeg = new Ellipse(LEG_WIDTH/2, LEG_HEIGHT/2);
        rightLeg.setFill(Color.BLACK); // Заливка черным
        rightLeg.setRotate(LEG_ANGLE); // Поворот на положительный угол
        // Устанавливаем положение
        StackPane.setMargin(rightLeg, new Insets(OVAL_HEIGHT*1.91115, -OVAL_WIDTH*0.5, 0, 0));

        // Добавляем все элементы в корневой контейнер
        root.getChildren().addAll(leftLeg, rightLeg, oval, clockFace, clockFace2);
        // Добавляем римские цифры
        addRomanNumerals(root);
        // Добавляем стрелки и тексты
        root.getChildren().addAll(hourHand, minuteHand, secondHand, dateTimeText, yearText);
        // Устанавливаем отступ для текста даты
        StackPane.setMargin(dateTimeText, new Insets(CLOCK_RADIUS * 1.2, 0, 0, 0));

        // Создаем таймер для обновления часов
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Обновляем положение стрелок и тексты
                updateClock(hourHand, minuteHand, secondHand, dateTimeText, yearText);
            }
        };
        timer.start(); // Запускаем таймер

        return mainContainer; // Возвращаем готовую панель с часами
    }

    // Метод для создания часовой стрелки
    private Group createHourHand(Color color, double strokeWidth, double lengthFactor) {
        // Линия стрелки
        Line line = new Line(0, 0, 0, -CLOCK_RADIUS * HAND_SCALE * lengthFactor);
        line.setStroke(color); // Цвет
        line.setStrokeWidth(strokeWidth); // Толщина
        
        // Наконечник стрелки (круг)
        Circle arrowHead = new Circle(0, -CLOCK_RADIUS * HAND_SCALE * lengthFactor, strokeWidth * 2);
        arrowHead.setFill(color); // Заливка
        
        // Группируем линию и наконечник
        return new Group(line, arrowHead);
    }

    // Метод для создания минутной стрелки
    private Group createMinuteHand(Color color, double strokeWidth, double lengthFactor) {
        // Линия стрелки
        Line line = new Line(0, 0, 0, -CLOCK_RADIUS * HAND_SCALE * lengthFactor);
        line.setStroke(color); // Цвет
        line.setStrokeWidth(strokeWidth); // Толщина
        
        // Наконечник стрелки (треугольник)
        Polygon arrowHead = new Polygon();
        double headSize = strokeWidth * 3; // Размер наконечника
        // Координаты точек треугольника
        arrowHead.getPoints().addAll(
            0.0, -CLOCK_RADIUS * HAND_SCALE * lengthFactor,
            -headSize, -CLOCK_RADIUS * HAND_SCALE * lengthFactor + headSize * 2,
            headSize, -CLOCK_RADIUS * HAND_SCALE * lengthFactor + headSize * 2
        );
        arrowHead.setFill(Color.WHITE); // Заливка белым
        arrowHead.setStroke(color); // Обводка
        arrowHead.setStrokeWidth(1); // Толщина обводки
        
        // Группируем линию и наконечник
        return new Group(line, arrowHead);
    }

    // Метод для создания секундной стрелки
    private Group createSecondHand(Color color, double strokeWidth, double lengthFactor) {
        // Линия стрелки
        Line line = new Line(0, 0, 0, -CLOCK_RADIUS * HAND_SCALE * lengthFactor);
        line.setStroke(color); // Цвет
        line.setStrokeWidth(strokeWidth); // Толщина
        
        // Наконечник стрелки (треугольник)
        Polygon arrowHead = new Polygon();
        double headSize = strokeWidth * 2; // Размер наконечника
        // Координаты точек треугольника
        arrowHead.getPoints().addAll(
            0.0, -CLOCK_RADIUS * HAND_SCALE * lengthFactor,
            -headSize, -CLOCK_RADIUS * HAND_SCALE * lengthFactor + headSize * 2,
            headSize, -CLOCK_RADIUS * HAND_SCALE * lengthFactor + headSize * 2
        );
        arrowHead.setFill(color); // Заливка
        
        // Группируем линию и наконечник
        return new Group(line, arrowHead);
    }

    // Метод для добавления римских цифр на циферблат
    private void addRomanNumerals(StackPane root) {
        double radius = CLOCK_RADIUS * 1.75; // Радиус расположения цифр
        // Добавляем все цифры по кругу
        addNumeral(root, 0, radius, "XII"); // 12 часов
        addNumeral(root, 330, radius, "I"); // 1 час
        addNumeral(root, 300, radius, "II"); // 2 часа
        addNumeral(root, 270, radius, "III"); // 3 часа
        addNumeral(root, 240, radius, "IV"); // 4 часа
        addNumeral(root, 210, radius, "V"); // 5 часов
        addNumeral(root, 180, radius, "VI"); // 6 часов
        addNumeral(root, 150, radius, "VII"); // 7 часов
        addNumeral(root, 120, radius, "VIII"); // 8 часов
        addNumeral(root, 90, radius, "IX"); // 9 часов
        addNumeral(root, 60, radius, "X"); // 10 часов
        addNumeral(root, 30, radius, "XI"); // 11 часов
    }

    // Метод для добавления одной цифры на циферблат
    private void addNumeral(StackPane root, double angle, double radius, String numeral) {
        // Создаем текстовый элемент
        Text text = new Text(numeral);
        text.setFont(Font.font("Arial", 24)); // Шрифт
        text.setFill(Color.BLACK); // Цвет текста

        // Преобразуем угол в радианы
        double radians = Math.toRadians(angle);
        // Рассчитываем координаты по углу и радиусу
        double x = radius * Math.sin(radians);
        double y = -radius * Math.cos(radians);

        // Устанавливаем отступы для позиционирования текста
        StackPane.setMargin(text, new Insets(y, x, 0, 0));
        // Добавляем текст в контейнер
        root.getChildren().add(text);
    }

    // Метод для обновления положения стрелок и даты
    private void updateClock(Group hourHand, Group minuteHand, Group secondHand, Text dateTimeText, Text yearText) {
        // Устанавливаем московский часовой пояс
        ZoneId moscowZone = ZoneId.of("Europe/Moscow");
        // Получаем текущее время
        LocalDateTime now = LocalDateTime.now(moscowZone);

        // Форматтеры для даты и года
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM", java.util.Locale.forLanguageTag("ru"));
        DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy", java.util.Locale.forLanguageTag("ru"));

        // Устанавливаем тексты даты и года
        dateTimeText.setText(now.format(dateFormatter));
        yearText.setText(now.format(yearFormatter));

        // Получаем текущие часы, минуты и секунды
        double hour = now.getHour() % 12; // Часы в 12-часовом формате
        double minute = now.getMinute(); // Минуты
        double second = now.getSecond(); // Секунды

        // Рассчитываем углы поворота для стрелок
        double hourAngle = (hour * 30) + (minute * 0.5); // 30 градусов на час + 0.5 градуса на минуту
        double minuteAngle = minute * 6 + (second * 0.1); // 6 градусов на минуту + 0.1 градуса на секунду
        double secondAngle = second * 6; // 6 градусов на секунду

        // Устанавливаем углы поворота для стрелок
        hourHand.setRotate(hourAngle);
        minuteHand.setRotate(minuteAngle);
        secondHand.setRotate(secondAngle);
    }

    // Точка входа в приложение
    public static void main(String[] args) {
        launch(args); // Запуск JavaFX приложения
    }
}