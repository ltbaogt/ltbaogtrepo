package com.ryutb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ryutb.utils.Utils;

@Controller
public class RyuTBHelloWord {

	@RequestMapping("/welcome")
	public ModelAndView helloWord() {
		String message = Utils.WELCOME_MESSAGE;
		return new ModelAndView("welcome", "messageOne", message);
	}
}
