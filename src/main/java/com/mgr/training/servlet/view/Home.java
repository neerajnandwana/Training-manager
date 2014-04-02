package com.mgr.training.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgr.training.data.User;
import com.mgr.training.view.View;

public class Home  extends View {
	final private static String TEMPLATE = "home.ftl.html";
	final private User user;
	final private ObjectMapper objectMapper;
	
    public Home(User user, ObjectMapper objectMapper) {
        super(TEMPLATE);
        this.user = user;
        this.objectMapper = objectMapper;
	}
    
    public User getUserProfile(){
    	return user;
    }
    
    public String getUserProfileAsJson() throws JsonProcessingException{
    	return objectMapper.writeValueAsString(user);
    }
}