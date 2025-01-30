//to use this test profile: mvn test -Dspring.profiles.active=test
package com.example.account_service;

import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.core.env.Environment;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
// @ActiveProfiles("test")
class AccountServiceApplicationTests {

	// @Autowired
	// private MockMvc mockMvc;

	// @Autowired
	// private Environment env;
	 
	@Test
	void contextLoads() {
		// assert mockMvc != null;
	}

	// @Test
	// void checkActiveProfile() {
	// 	System.out.println("Active profile: " + String.join(",", env.getActiveProfiles()));
	// }

}
