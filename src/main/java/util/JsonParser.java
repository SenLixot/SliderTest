package util;

import com.google.gson.Gson;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;

/**
 * Парсер Json-файлов
 */
public class JsonParser {

    private JsonParser() {}

    /**
     * Метод создания экземпляра класса на основе Json-файла
     * @param classOfT класс, экземпляр которого необходимо создать
     * @param fileName расположение Json-файла
     */
    public static <T> T fromJson(final Class<T> classOfT, final String fileName) {
        try (final Reader reader =
                new InputStreamReader(
                        new FileInputStream(fileName.concat(".json")), "UTF-8")) {
            final Gson gson = new Gson();
            return gson.fromJson(reader, (Type) classOfT);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка ввода/вывода", e);
        }
    }
}
