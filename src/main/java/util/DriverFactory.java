package util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class DriverFactory {

    public synchronized static ThreadLocal<WebDriver> getDriver () {
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);

        ChromeOptions opts = new ChromeOptions();
        opts.addArguments("start-maximized");
        capabilities.setCapability(ChromeOptions.CAPABILITY, opts);
        return ThreadLocal.withInitial(() -> new ChromeDriver(capabilities));
    }
}
