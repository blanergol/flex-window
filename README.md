# FlexWindow

**FlexWindow** - это Java-приложение, позволяющее управлять окнами в операционной системе Windows с использованием глобальных горячих клавиш. С помощью этого приложения вы можете быстро переключаться между полноэкранным режимом и центрированием активного окна на экране.

## Возможности
- Настройка горячих клавиш для полноэкранного режима и центрирования окна.
- Поддержка системного трея для быстрого доступа к приложению.

## Требования
- Java 8 или выше.
- Библиотека `jnativehook` для глобальных хуков клавиатуры.
- Библиотека `ini4j` для работы с ini-файлами.

## Установка
1. Склонируйте репозиторий:
    ```sh
    git clone https://github.com/blanergol/flex-window.git
    ```

2. Перейдите в директорию проекта:
    ```sh
    cd FlexWindow
    ```

3. Соберите проект с использованием Maven:
    ```sh
    mvn clean install
    ```

## Использование
1. Запустите приложение:
    ```sh
    java -jar target/FlexWindow-1.0.0.jar
    ```

2. Настройте горячие клавиши через графический интерфейс и сохраните настройки. Изменения будут сохранены в `config.ini`.

3. Используйте горячие клавиши для управления активными окнами.

## Вклад в проект
Если вы хотите внести свой вклад в проект, пожалуйста, создайте форк репозитория, сделайте свои изменения и отправьте pull request.

## Лицензия
Этот проект лицензируется по лицензии MIT. Подробности смотрите в файле LICENSE.

---

## FlexWindow

**FlexWindow** is a Java application that allows managing windows in the Windows operating system using global hotkeys. With this application, you can quickly switch between fullscreen mode and centering the active window on the screen.

## Features
- Configurable hotkeys for fullscreen mode and window centering.
- System tray support for quick access to the application.

## Requirements
- Java 8 or higher.
- `jnativehook` library for global keyboard hooks.
- `ini4j` library for working with ini files.

## Installation
1. Clone the repository:
    ```sh
    git clone https://github.com/blanergol/flex-window.git
    ```

2. Navigate to the project directory:
    ```sh
    cd FlexWindow
    ```

3. Build the project using Maven:
    ```sh
    mvn clean install
    ```

## Usage
1. Run the application:
    ```sh
    java -jar target/FlexWindow-1.0.0.jar
    ```

2. Configure the hotkeys through the graphical interface and save the settings. Changes will be saved to `config.ini`.

3. Use the hotkeys to manage active windows.

## Contributing
If you would like to contribute to the project, please fork the repository, make your changes, and submit a pull request.

## License
This project is licensed under the MIT License. See the LICENSE file for details.
