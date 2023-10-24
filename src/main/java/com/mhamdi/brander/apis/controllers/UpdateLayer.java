package com.mhamdi.brander.apis.controllers;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mhamdi.brander.apis.resources.MyResponse;

@RestController
public class UpdateLayer {
    private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/test")
	public MyResponse greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new MyResponse(counter.incrementAndGet(), String.format(template, name));
	}
}
