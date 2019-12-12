package com.eresh.spring.controller;

import com.eresh.spring.util.BaseRestOutboundProcessor;
import com.eresh.spring.ws.WSDiscount;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * Created By Gorantla, Eresh on 11/Dec/2019
 **/
@RestController
@RequestMapping("/api")
public class ProductController {

	@Autowired
	BaseRestOutboundProcessor baseRestOutboundProcessor;

	@GetMapping("/hi")
	public ResponseEntity<String> sayHi() {
		return ResponseEntity.ok("Hi");
	}

	@GetMapping("/discounts")
	@HystrixCommand(fallbackMethod = "getDefaultDiscount", commandKey = "getDefaultDiscount")
	public ResponseEntity<WSDiscount> getProductDiscount(@RequestParam("id") String id) {
		ResponseEntity<WSDiscount> responseEntity = baseRestOutboundProcessor.get("http://product-discount.com?id=" + id, null, WSDiscount.class, new HashMap<>());
		return ResponseEntity.ok(responseEntity.getBody());
	}

	public ResponseEntity<WSDiscount> getDefaultDiscount(String id) {
		WSDiscount discount = new WSDiscount();
		discount.setMaxDiscount("0");
		discount.setProductId(id);
		return ResponseEntity.ok(discount);
	}
}
