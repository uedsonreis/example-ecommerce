package com.uedsonreis.ecommerce.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class ControllerTester {

	public static final String RETURNED = "data";
	public static final String SUCCESS = "success";
	
	protected MockMvc mockMvc;
	
	protected void setup(WebApplicationContext wac) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(sharedHttpSession()).build();
	}
	
	protected ResultActions test(RequestBuilder requestBuilder, Object success, ResultMatcher matcher) throws Exception {
		return this.mockMvc.perform(requestBuilder)
			.andDo(print()).andExpect(status().isOk())
			.andExpect(jsonPath(SUCCESS).value(success))
			.andExpect(matcher);
	}

}