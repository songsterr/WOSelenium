package com.woselenium.example;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WORequest;
import com.woselenium.TestAction;
import com.woselenium.example.components.Main;

public class ExampleTestAction extends TestAction {

	public ExampleTestAction(WORequest request) {
		super(request);
	}

	public WOActionResults mainPageAction() {
		return pageWithName(Main.class.getSimpleName());
	}

}
