package com.woselenium;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;

public class RunningDriverManager {

	public WebDriver connect() {
		// FireFox sometimes starts up on ports after 7055 (which is WebDriver's default port).
		// Looks like this happens if driver is quit and started again quickly. Checking 10 ports
		// to make sure that we find the running browser if any.
		for (int port = 7055; port < 7065; port++) {
			try {
				return new RemoteWebDriver(new URL(String.format("http://localhost:%d/hub", port)), DesiredCapabilities.firefox());
			} catch (UnreachableBrowserException e) {
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	public void quit() {
		WebDriver runningDriver = connect();
		if (runningDriver != null) {
			runningDriver.quit();
			try {
				// Looks like browser is quit asynchronously which can lead to error upon access
				// to running driver that has just been quit. 3 second pause helps to avoid this.
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}
		}
	}

}