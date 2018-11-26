package config;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;
import util.JsonParser;

/**
 * Основная конфигурация приложения
 */
public class MainConf {

    private static MainConf instance;

    /**
     * Адреса страниц
     */
    @SerializedName("Адреса")
    private Map<String, String> urls;

    /**
     * Тарифы
     */
    @SerializedName("Тарифы")
    private List<Tariff> tariffs;

    private static MainConf getInstance() {
        return instance;
    }

    /**
     * Инициализация класса из json-конфигурации
     * @param fileName расположение файла конфигурации
     */
    public static void init(String fileName) {
        instance = JsonParser.fromJson(MainConf.class, fileName);
    }

    public static String getUrl(String name) {
        MainConf mainConf = getInstance();
        return mainConf.urls.get(name);
    }

    public static List<Tariff> getTariffs() {
        MainConf mainConf = getInstance();
        return mainConf.tariffs;
    }
}
