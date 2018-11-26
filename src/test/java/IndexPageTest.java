import config.MainConf;
import config.Tariff;
import java.util.ArrayList;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;
import page.IndexPage;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Title;
import ru.yandex.qatools.allure.model.DescriptionType;

/**
 * Основной тест заглавной страницы
 */
public class IndexPageTest extends BaseTest {

    @Title("Основной сценарий")
    @Description(value = "Основной сценарий", type = DescriptionType.MARKDOWN)
    @Test
    public void mainScenario() {
        IndexPage page = new IndexPage();
        page.init(System.getProperty("page-conf-location") + "indexpage", "index", driver.get());
        List<Tariff> tariffList = MainConf.getTariffs();
        page.reset();

        //Шаг 2
        Tariff currentTariff = page.getCurrentTariff();
        Tariff newTariff = page.getOfferedTariff();
        Assert.assertEquals(currentTariff, tariffList.get(0));
        Assert.assertEquals(newTariff, tariffList.get(0));
        boolean isPurchaseAvailable = page.isPurchaseAvailable();
        Assert.assertFalse(isPurchaseAvailable);
        Integer balance = page.getBalance();
        Assert.assertEquals((int) balance, 0);

        //Шаги 3-5
        page.clickIncrease();
        newTariff = page.getOfferedTariff();
        Assert.assertEquals(newTariff, tariffList.get(1));
        isPurchaseAvailable = page.isPurchaseAvailable();
        Assert.assertFalse(isPurchaseAvailable);
        page.addFundsByTariff(newTariff);
        balance = page.getBalance();
        Assert.assertEquals(balance, newTariff.getCost());
        isPurchaseAvailable = page.isPurchaseAvailable();
        Assert.assertTrue(isPurchaseAvailable);
        page.purchase();
        isPurchaseAvailable = page.isPurchaseAvailable();
        Assert.assertFalse(isPurchaseAvailable);
        currentTariff = page.getCurrentTariff();
        Assert.assertEquals(currentTariff, newTariff);

        //Шаг 6
        page.clickDecrease();
        newTariff = page.getOfferedTariff();
        Assert.assertEquals(newTariff, tariffList.get(0));
        isPurchaseAvailable = page.isPurchaseAvailable();
        Assert.assertTrue(isPurchaseAvailable);

        //Шаги 7-8
        page.clickIncrease();
        newTariff = page.getOfferedTariff();
        Assert.assertEquals(newTariff, tariffList.get(1));
        isPurchaseAvailable = page.isPurchaseAvailable();
        Assert.assertFalse(isPurchaseAvailable);

        //Шаг 9
        page.reset();
        currentTariff = page.getCurrentTariff();
        newTariff = page.getOfferedTariff();
        Assert.assertEquals(currentTariff, tariffList.get(0));
        Assert.assertEquals(newTariff, tariffList.get(0));
        balance = page.getBalance();
        Assert.assertEquals((int) balance, 0);
        isPurchaseAvailable = page.isPurchaseAvailable();
        Assert.assertFalse(isPurchaseAvailable);

        //Шаг 10
        tariffList.forEach(tariff -> {
            Tariff tempNewTariff = page.getOfferedTariff();
            Assert.assertEquals(tempNewTariff, tariff);
            boolean tempIsPurchaseAvailable;
            if (tariff.getCost() != 0) {
                page.addFundsByTariff(tariff);
                Integer tempBalance = page.getBalance();
                Assert.assertEquals(tempBalance, tariff.getCost());
                tempIsPurchaseAvailable = page.isPurchaseAvailable();
                Assert.assertTrue(tempIsPurchaseAvailable);
                page.purchase();
            }
            tempIsPurchaseAvailable = page.isPurchaseAvailable();
            Assert.assertFalse(tempIsPurchaseAvailable);
            Tariff tempCurrentTariff = page.getCurrentTariff();
            Assert.assertEquals(tempCurrentTariff, tariff);
            page.clickIncrease();
        });

        //Шаги 11-12
        Integer fairlyRandomNum = 100;
        page.addFunds(fairlyRandomNum);
        page.refresh();
        currentTariff = page.getCurrentTariff();
        newTariff = page.getOfferedTariff();
        Assert.assertEquals(currentTariff, tariffList.get(0));
        Assert.assertEquals(newTariff, tariffList.get(0));
        isPurchaseAvailable = page.isPurchaseAvailable();
        Assert.assertFalse(isPurchaseAvailable);
        balance = page.getBalance();
        Assert.assertEquals(balance, fairlyRandomNum);
    }

    @Title("Cценарий №3")
    @Description(value = "Проверка тарифов", type = DescriptionType.MARKDOWN)
    @Test
    public void tariffScenario() {
        IndexPage page = new IndexPage();
        page.init(System.getProperty("page-conf-location") + "indexpage", "index", driver.get());
        List<Tariff> tariffList = new ArrayList<>(MainConf.getTariffs());
        page.reset();
        Tariff displayedTariff;
        double sliderPosBefore;
        double sliderPosAfter;
        do {
            sliderPosBefore = page.getSliderPosition();
            displayedTariff = page.getOfferedTariff();
            Assert.assertTrue(tariffList.contains(displayedTariff));
            tariffList.remove(displayedTariff);
            page.clickIncrease();
            page.waitSlider();
            sliderPosAfter = page.getSliderPosition();
        } while (page.sliderMoved(sliderPosBefore, sliderPosAfter));
        Assert.assertTrue(tariffList.isEmpty());
    }
}
