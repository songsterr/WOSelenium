package com.woselenium;


import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class StringResponsePage extends Page {
	
	@FindBy(tagName = "body")
	private WebElement body;

	public StringResponsePage(WebDriver driver) {
		super(driver);
		
		wait.until(visibilityOf(body));
	}

	public String stringResponse() {
		return body.getText();
	}

}
