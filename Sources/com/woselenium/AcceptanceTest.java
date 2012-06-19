package com.woselenium;


import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.conn.HttpHostConnectException;
import org.jmock.api.Invocation;
import org.jmock.api.Invokable;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Ignore;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.server.DefaultDriverFactory;
import org.openqa.selenium.remote.server.DriverFactory;
import org.openqa.selenium.support.PageFactory;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WODirectAction;

@Ignore
public abstract class AcceptanceTest {
	public static final String BASE_URL = System.getProperty("selenium.baseUrl", "http://localhost/cgi-bin/WebObjects/Songsterr.woa/-41424");

	public static final String ACTION_COMMAND_SUCCEEDED_MESSAGE = "Action command succeeded.";

	protected WebDriver driver;

	public AcceptanceTest(DriverFactory driverFactory) {
		try {
			this.driver = new RemoteWebDriver(new URL("http://localhost:7055/hub"), DesiredCapabilities.firefox());
		} catch (WebDriverException e) {
			if (e.getCause() instanceof HttpHostConnectException) {
				this.driver = driverFactory.newInstance(DesiredCapabilities.firefox());
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public AcceptanceTest() {
		this(new DefaultDriverFactory());
	}

	@Before
	public void setUp() {
		driver.manage().deleteAllCookies();
	}
	
	/**
	 * Stubs call of action method with actual loading of direct action url by Selenium browser. E.g. action(Action.class).homeAction() called from test will result in Selenium browser navigating to "/wa/Action/home".
	 * 
	 * Intended to be used in Selenium tests to enable code completion and refactoring of test direct action calls.
	 */
	protected <T extends WODirectAction> T action(Class<T> directActionClass) {
		Invokable stub = new Invokable() {

			public Object invoke(Invocation invocation) throws Throwable {
				String methodName = invocation.getInvokedMethod().getName();

				if (!methodName.endsWith("Action")) {
					return null;
				}

				String actionName = methodName.replaceFirst("(.*)Action$", "$1");
				String enhancedClassName = invocation.getInvokedObject().getClass().getName();
				String actionClassName = enhancedClassName.replaceAll("(.*)\\$\\$EnhancerByCGLIB.*", "$1");
				String url = "/wa/" + actionClassName + "/" + actionName;
				goTo(url);
				return null;
			}
		};
		return ClassImposteriser.INSTANCE.imposterise(stub, directActionClass);
	}

	protected <T> T goTo(Class<T> pageClass, String url) {
		goTo(url);
		return PageFactory.initElements(driver, pageClass);
	}

	protected <T> T goTo(Class<T> pageClass, WOActionResults action) {
		return PageFactory.initElements(driver, pageClass);
	}

	private void goTo(String url) {
		driver.get(BASE_URL + url);
	}

	protected String stringResponseFrom(WOActionResults action) {
		return PageFactory.initElements(driver, StringResponsePage.class).stringResponse();
	}

	protected void perform(WOActionResults action) {
		assertThat(stringResponseFrom(action), is(ACTION_COMMAND_SUCCEEDED_MESSAGE));
	}

	protected <T> T currentPage(Class<T> pageClass) {
		return PageFactory.initElements(driver, pageClass);
	}

}
