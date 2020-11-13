package com.wink.livemall.admin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController 
{

	@RequestMapping("/demo")
    public String index() 
	{
        return "demo, there";
    }
	
}