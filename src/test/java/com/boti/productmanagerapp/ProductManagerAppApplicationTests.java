package com.boti.productmanagerapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ProductManagerAppApplication.class)
@ActiveProfiles("test")
class ProductManagerAppApplicationTests {

	@Test
	void contextLoads() {
		System.out.println("Context Loads");
	}

}
