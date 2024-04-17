import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class UserDataApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите данные (Фамилия Имя Отчество датарождения номертелефона пол):");
        String input = scanner.nextLine();

        try {
            processUserData(input);
            System.out.println("Данные успешно обработаны и записаны в файл.");
        } catch (UserDataFormatException e) {
            System.err.println("Ошибка в формате данных: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }

    private static void processUserData(String input) throws UserDataFormatException, IOException {
        String[] data = input.split("\\s+");

        // Проверка количества данных
        if (data.length != 6) {
            throw new UserDataFormatException("Неверное количество данных. Ожидается 6 параметров.");
        }

        // Распаковка данных
        String surname = data[0];
        String name = data[1];
        String patronymic = data[2];
        String birthDateString = data[3];
        String phoneNumber = data[4];
        String gender = data[5].toLowerCase(); // Приведение к нижнему регистру для унификации

        // Проверка формата даты рождения
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            dateFormat.setLenient(false);
            Date birthDate = dateFormat.parse(birthDateString);
        } catch (ParseException e) {
            throw new UserDataFormatException("Неверный формат даты рождения. Ожидается dd.MM.yyyy.");
        }

        // Проверка корректности значения пола
        if (!gender.equals("male") && !gender.equals("female")) {
            throw new UserDataFormatException("Некорректное значение пола. Допустимы только 'male' или 'female'.");
        }

        // Запись в CSV-файл
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("userdata.csv", true))) {
            writer.write(surname + "," + name + "," + patronymic + "," + birthDateString + "," + phoneNumber + "," + gender);
            writer.newLine();
        }
    }

    // Класс исключения для некорректного формата данных
    private static class UserDataFormatException extends Exception {
        public UserDataFormatException(String message) {
            super(message);
        }
    }
}
