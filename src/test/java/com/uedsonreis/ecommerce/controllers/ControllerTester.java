package com.uedsonreis.ecommerce.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class ControllerTester {

	protected MockMvc mockMvc;
	
	protected String treatToken(String token) {
		return "Bearer "+ token;
	}
	
	protected void setup(WebApplicationContext wac) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(sharedHttpSession()).build();
	}
	
	protected ResultActions test(RequestBuilder requestBuilder, ResultMatcher status) throws Exception {
		return this.mockMvc.perform(requestBuilder)
			.andDo(print()).andExpect(status);
	}

}