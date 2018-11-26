package config;

/**
 * Класс, репрезентирующий тариф
 */
public class Tariff {

    /**
     * Срок тарифа
     */
    private Integer time;

    /**
     * Скорость по тарифу
     */
    private String speed;

    /**
     * Цена тарифа
     */
    private Integer cost;

    public Tariff(Integer time, String speed, Integer cost) {
        this.time = time;
        this.speed = speed;
        this.cost = cost;
    }

    public Integer getCost() {
        return cost;
    }

    /**
     * Сравнение по значемиям
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Tariff)) {
            return false;
        }

        Tariff tariff = (Tariff) o;

        //остаток дней в текущем тарифе никогда не изменяется
        return tariff.speed.equals(speed) &&
                tariff.cost.equals(cost);
    }

    @Override
    public int hashCode() {
        int result = 17;
        //отключено, так как остаток дней в текущем тарифе никогда не изменяется
        //result = 31 * result + time.hashCode();
        result = 31 * result + speed.hashCode();
        result = 31 * result + cost.hashCode();
        return result;
    }
}
