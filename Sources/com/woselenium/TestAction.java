package com.woselenium;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WODirectAction;
import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOResponse;

import er.extensions.foundation.ERXProperties;

public class TestAction extends WODirectAction {

	private static final String ACTIONS_ENABLED_PROPERTY = "WOSelenium.actionsEnabled";

	public TestAction(WORequest request) {
		super(request);
	}
	
	public WOActionResults performActionNamed(String anActionName) {
		// Security protection from calling setup actions in production
	    boolean actionsEnabled = ERXProperties.booleanForKeyWithDefault(ACTIONS_ENABLED_PROPERTY, false);
	    if (!actionsEnabled) {
	    	String message = String.format("WOSelenium actions are disabled. You can enabled them via %s property.", ACTIONS_ENABLED_PROPERTY);
			return stringResponse(message);
		}

	    WOResponse response = super.performActionNamed(anActionName).generateResponse();

	    // It's convenient to assume in test setup actions that session is created and wosid cookie is appended
	    if(!session().isTerminating()) {
	        session()._appendCookieToResponse(response);
	    }
	    
	    return response;
	}

	public WOActionResults terminateSessionAction() {
		session().terminate();
		return success();
	}

    public WOActionResults success() {
		WOResponse response = new WOResponse();
		response.setContent(AcceptanceTest.ACTION_COMMAND_SUCCEEDED_MESSAGE);
		return response;
	}

	protected WOResponse stringResponse(String s) {
	    WOResponse response = new WOResponse();
	    response.appendContentString(s);
	    return response;  	
	}

}