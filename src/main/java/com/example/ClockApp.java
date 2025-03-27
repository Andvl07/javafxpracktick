package com.example;

// Импорты для работы с датой и временем
import java.time.LocalDateTime;  // Для получения текущего времени
import java.time.ZoneId;        // Для работы с часовыми поясами
import java.time.format.DateTimeFormatter;  // Для форматирования даты

// Импорты JavaFX
import javafx.animation.AnimationTimer;  // Для анимации стрелок
import javafx.application.Application;   // Базовый класс приложения JavaFX
import javafx.geometry.Insets;          // Для задания отступов
import javafx.geometry.Pos;             // Для выравнивания элементов
import javafx.scene.Group;              // Для группировки элементов
import javafx.scene.Scene;              // Сцена - контейнер для элементов интерфейса
import javafx.scene.control.Menu;       // Элемент меню
import javafx.scene.control.MenuBar;    // Панель меню
import javafx.scene.control.MenuItem;   // Пункт меню
import javafx.scene.layout.BorderPane;  // Контейнер с разметкой (верх, центр, низ)
import javafx.scene.layout.StackPane;   // Контейнер с наложением элементов
import javafx.scene.paint.Color;        // Цвета
import javafx.scene.paint.CycleMethod;  // Метод цикла для градиента
import javafx.scene.paint.LinearGradient;  // Линейный градиент
import javafx.scene.paint.Stop;         // Точки градиента
import javafx.scene.shape.Circle;       // Круг
import javafx.scene.shape.Ellipse;      // Эллипс
import javafx.scene.shape.Line;         // Линия
import javafx.scene.shape.Polygon;      // Многоугольник
import javafx.scene.text.Font;          // Шрифт
import javafx.scene.text.Text;          // Текст
import javafx.stage.Stage;              // Окно приложения

public class ClockApp extends Application {

    // Размеры окна
    private static final double INITIAL_WIDTH = 800;  // Начальная ширина окна
    private static final double INITIAL_HEIGHT = 600; // Начальная высота окна
    
    // Параметры часов
    private static final double CLOCK_RADIUS = 200;   // Радиус основного циферблата
    private static final double OVAL_WIDTH = 300;     // Ширина овального основания
    private static final double OVAL_HEIGHT = 200;    // Высота овального основания
    private static final double HAND_SCALE = 0.8;     // Масштаб стрелок
    private static final double CLOCK_RADIUS2 = 152;   // Радиус внутреннего циферблата
    private static final double LEG_WIDTH = 60;       // Ширина ножки часов
    private static final double LEG_HEIGHT = 20;      // Высота ножки часов
    private static final double LEG_ANGLE = 15;       // Угол наклона ножек
    
    // Параметры разметки циферблата
    private static final double LONG_TICK_LENGTH = 10;  // Длина основных черточек (на цифрах)
    private static final double SHORT_TICK_LENGTH = 5;  // Длина промежуточных черточек
    private static final double TICK_WIDTH = 2;         // Толщина черточек
    private static final int TICKS_COUNT = 60;          // Всего делений (12 часов × 5)

    // Главный метод запуска приложения
    @Override
    public void start(Stage primaryStage) {
        // Основной контейнер с разметкой (верх, центр, низ)
        BorderPane rootPane = new BorderPane();
        
        // Создаем панель меню
        MenuBar menuBar = new MenuBar();
        
        // Меню "Файл"
        Menu fileMenu = new Menu("Файл");
        MenuItem saveItem = new MenuItem("Сохранение");  // Пункт "Сохранение"
        MenuItem loadItem = new MenuItem("Загрузка");    // Пункт "Загрузка"
        fileMenu.getItems().addAll(saveItem, loadItem);  // Добавляем пункты в меню
        
        // Меню "Данные"
        Menu dataMenu = new Menu("Данные");
        MenuItem groupItem = new MenuItem("Группа");         // Пункт "Группа"
        MenuItem disciplinesItem = new MenuItem("Дисциплины"); // Пункт "Дисциплины"
        MenuItem resultsItem = new MenuItem("Данные итогов");  // Пункт "Данные итогов"
        dataMenu.getItems().addAll(groupItem, disciplinesItem, resultsItem);
        
        // Меню "Редактирование"
        Menu editMenu = new Menu("Редактирование");
        MenuItem editItem = new MenuItem("Редактирование"); // Пункт "Редактирование"
        MenuItem viewItem = new MenuItem("Просмотр");       // Пункт "Просмотр"
        MenuItem clearItem = new MenuItem("Очистка");       // Пункт "Очистка"
        editMenu.getItems().addAll(editItem, viewItem, clearItem);
        
        // Меню "Выход"
        Menu exitMenu = new Menu("Выход");
        
        // Добавляем все меню в панель
        menuBar.getMenus().addAll(fileMenu, dataMenu, editMenu, exitMenu);
        rootPane.setTop(menuBar);  // Размещаем меню вверху

        // Создаем панель с часами
        StackPane clockPane = createClockPane();
        rootPane.setCenter(clockPane);  // Часы по центру
        
        // Текст с информацией о сессии
        Text sessionInfo = new Text("ИТОГИ СЕССИИ\n\nГруппа:\nКлассный руководитель:");
        sessionInfo.setFont(Font.font(20));  // Устанавливаем шрифт
        
        // Панель внизу с информацией
        StackPane bottomPane = new StackPane(sessionInfo);
        bottomPane.setPadding(new Insets(10));  // Отступы 10 пикселей
        rootPane.setBottom(bottomPane);  // Размещаем внизу

        // Создаем сцену с корневым контейнером
        Scene scene = new Scene(rootPane, INITIAL_WIDTH, INITIAL_HEIGHT);
        primaryStage.setTitle("Часы с заготовкой меню");  // Заголовок окна
        primaryStage.setScene(scene);        // Устанавливаем сцену
        primaryStage.setMinWidth(400);       // Минимальная ширина
        primaryStage.setMinHeight(300);      // Минимальная высота
        primaryStage.show();                 // Показываем окно
    }

    // Создает панель с часами
    private StackPane createClockPane() {
        // Основной контейнер для часов
        StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);  // Выравнивание по центру

        // Группа для масштабируемого содержимого
        Group scalableContent = new Group(root);
        StackPane mainContainer = new StackPane(scalableContent);
        mainContainer.setAlignment(Pos.CENTER);

        // Градиент для фона
        LinearGradient backgroundGradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.GOLD),    // Начальный цвет
                new Stop(1, Color.YELLOW)   // Конечный цвет
        );

        // Овальное основание часов
        Ellipse oval = new Ellipse(OVAL_WIDTH, OVAL_HEIGHT);
        oval.setFill(backgroundGradient);  // Заливка градиентом
        oval.setStroke(Color.BLACK);       // Цвет обводки
        oval.setStrokeWidth(3);            // Толщина обводки

        // Основной циферблат
        Circle clockFace = new Circle(CLOCK_RADIUS);
        clockFace.setFill(Color.PALEGREEN);  // Заливка
        clockFace.setStroke(Color.BLACK);    // Обводка
        clockFace.setStrokeWidth(2);         // Толщина обводки

        // Внутренний циферблат (для черточек)
        Circle clockFace2 = new Circle(CLOCK_RADIUS2);
        clockFace2.setFill(Color.TRANSPARENT);  // Прозрачная заливка
        clockFace2.setStroke(Color.BLACK);       // Обводка
        clockFace2.setStrokeWidth(2);            // Толщина обводки

        // Создаем стрелки
        Group hourHand = createHourHand(Color.BLACK, 4, 0.5);    // Часовая
        Group minuteHand = createMinuteHand(Color.BLACK, 3, 0.7); // Минутная
        Group secondHand = createSecondHand(Color.RED, 2, 0.9);   // Секундная

        // Текст с датой
        Text dateTimeText = new Text();
        dateTimeText.setFont(Font.font(20));  // Шрифт
        dateTimeText.setFill(Color.BLACK);    // Цвет

        // Текст с годом
        Text yearText = new Text();
        yearText.setFont(Font.font(20));      // Шрифт
        yearText.setFill(Color.BLACK);        // Цвет
        StackPane.setMargin(yearText, new Insets(0, 0, 0, -OVAL_WIDTH * 0.75));  // Позиция

        // Левая ножка часов
        Ellipse leftLeg = new Ellipse(LEG_WIDTH/2, LEG_HEIGHT/2);
        leftLeg.setFill(Color.BLACK);         // Заливка
        leftLeg.setRotate(-LEG_ANGLE);        // Поворот
        StackPane.setMargin(leftLeg, new Insets(OVAL_HEIGHT*1.91115, 0, 0, -OVAL_WIDTH*0.5));  // Позиция

        // Правая ножка часов
        Ellipse rightLeg = new Ellipse(LEG_WIDTH/2, LEG_HEIGHT/2);
        rightLeg.setFill(Color.BLACK);        // Заливка
        rightLeg.setRotate(LEG_ANGLE);        // Поворот
        StackPane.setMargin(rightLeg, new Insets(OVAL_HEIGHT*1.91115, -OVAL_WIDTH*0.5, 0, 0)); // Позиция

        // Добавляем элементы на сцену
        root.getChildren().addAll(leftLeg, rightLeg, oval, clockFace, clockFace2);
        addRomanNumerals(root);       // Добавляем римские цифры
        addClockTicks(root);          // Добавляем черточки
        root.getChildren().addAll(hourHand, minuteHand, secondHand, dateTimeText, yearText);
        StackPane.setMargin(dateTimeText, new Insets(CLOCK_RADIUS * 1.2, 0, 0, 0));  // Позиция даты

        // Таймер для обновления времени
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateClock(hourHand, minuteHand, secondHand, dateTimeText, yearText);  // Обновляем время
            }
        };
        timer.start();  // Запускаем таймер

        return mainContainer;
    }

    // Добавляет черточки на внутренний циферблат
    private void addClockTicks(StackPane root) {
        // Группа для всех черточек
        Group ticksGroup = new Group();
        
        // Радиус внутреннего циферблата
        double radius = CLOCK_RADIUS2;
        
        // Создаем 60 делений (12 основных × 5 промежуточных)
        for (int i = 0; i < TICKS_COUNT; i++) {
            double angleDeg = i * 6;  // 6° между делениями
            double angleRad = Math.toRadians(angleDeg);  // Переводим в радианы
            boolean isHourTick = (i % 5 == 0);  // Основные черточки на цифрах
            
            // Длина черточки
            double length = isHourTick ? LONG_TICK_LENGTH : SHORT_TICK_LENGTH;
            
            // Координаты внешней точки (на окружности)
            double outerX = radius * Math.sin(angleRad);
            double outerY = -radius * Math.cos(angleRad);  // Ось Y вниз
            
            // Координаты внутренней точки (смещены внутрь)
            double innerX = (radius - length) * Math.sin(angleRad);
            double innerY = -(radius - length) * Math.cos(angleRad);
            
            // Создаем черточку
            Line tick = new Line(outerX, outerY, innerX, innerY);
            tick.setStroke(Color.BLACK);  // Цвет
            tick.setStrokeWidth(TICK_WIDTH);  // Толщина
            
            ticksGroup.getChildren().add(tick);  // Добавляем в группу
        }
        
        root.getChildren().add(ticksGroup);  // Добавляем группу черточек на сцену
    }

    // Создает часовую стрелку
    private Group createHourHand(Color color, double strokeWidth, double lengthFactor) {
        // Линия стрелки
        Line line = new Line(0, 0, 0, -CLOCK_RADIUS * HAND_SCALE * lengthFactor);
        line.setStroke(color);             // Цвет
        line.setStrokeWidth(strokeWidth);  // Толщина
        
        // Наконечник стрелки (круг)
        Circle arrowHead = new Circle(0, -CLOCK_RADIUS * HAND_SCALE * lengthFactor, strokeWidth * 2);
        arrowHead.setFill(color);  // Заливка
        
        return new Group(line, arrowHead);  // Группа из линии и наконечника
    }

    // Создает минутную стрелку
    private Group createMinuteHand(Color color, double strokeWidth, double lengthFactor) {
        // Линия стрелки
        Line line = new Line(0, 0, 0, -CLOCK_RADIUS * HAND_SCALE * lengthFactor);
        line.setStroke(color);             // Цвет
        line.setStrokeWidth(strokeWidth);  // Толщина
        
        // Наконечник (треугольник)
        Polygon arrowHead = new Polygon();
        double headSize = strokeWidth * 3;  // Размер наконечника
        arrowHead.getPoints().addAll(
            0.0, -CLOCK_RADIUS * HAND_SCALE * lengthFactor,  // Вершина
            -headSize, -CLOCK_RADIUS * HAND_SCALE * lengthFactor + headSize * 2,  // Левая точка
            headSize, -CLOCK_RADIUS * HAND_SCALE * lengthFactor + headSize * 2    // Правая точка
        );
        arrowHead.setFill(Color.WHITE);  // Заливка белым
        arrowHead.setStroke(color);      // Обводка
        arrowHead.setStrokeWidth(1);     // Толщина обводки
        
        return new Group(line, arrowHead);  // Группа из линии и наконечника
    }

    // Создает секундную стрелку
    private Group createSecondHand(Color color, double strokeWidth, double lengthFactor) {
        // Линия стрелки
        Line line = new Line(0, 0, 0, -CLOCK_RADIUS * HAND_SCALE * lengthFactor);
        line.setStroke(color);             // Цвет
        line.setStrokeWidth(strokeWidth);  // Толщина
        
        // Наконечник (треугольник)
        Polygon arrowHead = new Polygon();
        double headSize = strokeWidth * 2;  // Размер наконечника
        arrowHead.getPoints().addAll(
            0.0, -CLOCK_RADIUS * HAND_SCALE * lengthFactor,  // Вершина
            -headSize, -CLOCK_RADIUS * HAND_SCALE * lengthFactor + headSize * 2,  // Левая точка
            headSize, -CLOCK_RADIUS * HAND_SCALE * lengthFactor + headSize * 2    // Правая точка
        );
        arrowHead.setFill(color);  // Заливка
        
        return new Group(line, arrowHead);  // Группа из линии и наконечника
    }

    // Добавляет римские цифры на циферблат
    private void addRomanNumerals(StackPane root) {
        double radius = CLOCK_RADIUS * 1.75;  // Радиус расположения цифр
        
        // Добавляем все цифры по кругу
        addNumeral(root, 0, radius, "XII");    // 12 часов
        addNumeral(root, 330, radius, "I");    // 1 час
        addNumeral(root, 300, radius, "II");   // 2 часа
        addNumeral(root, 270, radius, "III");  // 3 часа
        addNumeral(root, 240, radius, "IV");   // 4 часа
        addNumeral(root, 210, radius, "V");    // 5 часов
        addNumeral(root, 180, radius, "VI");   // 6 часов
        addNumeral(root, 150, radius, "VII");  // 7 часов
        addNumeral(root, 120, radius, "VIII"); // 8 часов
        addNumeral(root, 90, radius, "IX");     // 9 часов
        addNumeral(root, 60, radius, "X");      // 10 часов
        addNumeral(root, 30, radius, "XI");     // 11 часов
    }

    // Добавляет одну цифру на циферблат
    private void addNumeral(StackPane root, double angle, double radius, String numeral) {
        Text text = new Text(numeral);  // Создаем текстовый элемент
        text.setFont(Font.font("Arial", 24));  // Шрифт
        text.setFill(Color.BLACK);              // Цвет текста

        // Переводим угол в радианы и вычисляем координаты
        double radians = Math.toRadians(angle);
        double x = radius * Math.sin(radians);   // Координата X
        double y = -radius * Math.cos(radians);  // Координата Y (ось Y вниз)

        // Устанавливаем позицию текста
        StackPane.setMargin(text, new Insets(y, x, 0, 0));
        root.getChildren().add(text);  // Добавляем текст на сцену
    }

    // Обновляет положение стрелок и дату
    private void updateClock(Group hourHand, Group minuteHand, Group secondHand, Text dateTimeText, Text yearText) {
        // Московский часовой пояс
        ZoneId moscowZone = ZoneId.of("Europe/Moscow");
        LocalDateTime now = LocalDateTime.now(moscowZone);  // Текущее время

        // Форматтеры для даты и года
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM", java.util.Locale.forLanguageTag("ru"));
        DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy", java.util.Locale.forLanguageTag("ru"));

        // Устанавливаем тексты
        dateTimeText.setText(now.format(dateFormatter));  // Дата
        yearText.setText(now.format(yearFormatter));      // Год

        // Получаем текущее время
        double hour = now.getHour() % 12;    // Часы (12-часовой формат)
        double minute = now.getMinute();     // Минуты
        double second = now.getSecond();     // Секунды

        // Вычисляем углы поворота стрелок
        double hourAngle = (hour * 30) + (minute * 0.5);    // 30° на час + 0.5° на минуту
        double minuteAngle = minute * 6 + (second * 0.1);   // 6° на минуту + 0.1° на секунду
        double secondAngle = second * 6;                     // 6° на секунду

        // Устанавливаем углы поворота
        hourHand.setRotate(hourAngle);      // Часовая стрелка
        minuteHand.setRotate(minuteAngle);  // Минутная стрелка
        secondHand.setRotate(secondAngle);  // Секундная стрелка
    }

    // Точка входа в приложение
    public static void main(String[] args) {
        launch(args);  // Запуск JavaFX приложения
    }
}