package com.woselenium;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;

/**
 * Braintree tests that require TLS version v1.1 and greater are broken locally in old FF. 
 * Use Chrome for local tests:
 * brew tap homebrew/cask
 * brew cask install chromedriver
 * /usr/local/bin/chromedriver --port=9515
 * Start test as a JUnit
 */
public class RunningDriverManager {

    private static final String CHROMEDRIVER_PATH = "/usr/local/bin/chromedriver";
    private static final boolean IS_CHROME_DRIVER = new File(CHROMEDRIVER_PATH).exists();
    private static WebDriver driver;

    private String userAgent;

    public RunningDriverManager() {
        super();
    }

    public RunningDriverManager(String userAgentString) {
        this();
        userAgent = userAgentString;
    }

    public WebDriver connect() {
        if (driver == null) {
            if (IS_CHROME_DRIVER) {
                driver = connectChrome();
            } else {
                driver = connectFirefox();
            }
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        }
        return driver;
	}

    public WebDriver connectChrome() {
        try {
            final ChromeOptions chromeOptions = new ChromeOptions();
//            chromeOptions.addArguments("--headless");
            if (userAgent != null) {
                chromeOptions.addArguments("--user-agent=" + userAgent);
            }
            DesiredCapabilities dc = DesiredCapabilities.chrome();
            dc.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
            
            URL serviceUrl = new URL("http://localhost:9515");
            return new RemoteWebDriver(serviceUrl, dc);
        } catch (Exception e) {
            System.out.print("Error creating chromedriver");
        }
        return null;
    }

    public WebDriver connectFirefox() {
        // FireFox sometimes starts up on ports after 7055 (which is WebDriver's default port).
        // Looks like this happens if driver is quit and started again quickly. Checking 10 ports
        // to make sure that we find the running browser if any.
        for (int port = 7055; port < 7065; port++) {
            try {
                return new RemoteWebDriver(new URL(String.format("http://localhost:%d/hub", port)), DesiredCapabilities.firefox());
            } catch (UnreachableBrowserException e) {
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
            }
        }
        
        // Create new driver if no browser is running
        FirefoxProfile profile = new FirefoxProfile();
        if (userAgent != null) {
            profile.setPreference("general.useragent.override", userAgent);
        }
        return new FirefoxDriver(profile);
    }
    
	public void quit() {
		WebDriver runningDriver = connect();
		if (runningDriver != null) {
			runningDriver.quit();
			driver = null;
			try {
				// Looks like browser is quit asynchronously which can lead to error upon access
				// to running driver that has just been quit. 3 second pause helps to avoid this.
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}
		}
	}

}