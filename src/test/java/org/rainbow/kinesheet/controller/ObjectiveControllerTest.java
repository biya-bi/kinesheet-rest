package org.rainbow.kinesheet.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.rainbow.kinesheet.config.JwtTestConfiguration;
import org.rainbow.kinesheet.jwt.JwtTokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = JwtTestConfiguration.class)
@AutoConfigureMockMvc
class ObjectiveControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private JwtTokenGenerator jwtTokenGenerator;

	@Test
	@WithMockUser(username = "user1")
	void findById_ObjectiveBelongsToRequester_ReturnObjective() throws Exception {
		String id = "350bebae-d54f-4e60-a2c8-77a0778e1c5b";
		String path = "/objectives/" + id;
		String token = jwtTokenGenerator.generate();

		mvc.perform(get(path).header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(id))
				.andExpect(jsonPath("$.title").value("Read Heavenly Mathematics"));
	}

	@Test
	@WithMockUser(username = "user2")
	void findById_ObjectiveDoesNotBelongToRequester_Return403() throws Exception {
		// The objective identified by this id belongs to user1 whose email is
		// user1@company.com. So we should get a 403 if user2 requests for it.
		String id = "350bebae-d54f-4e60-a2c8-77a0778e1c5b";
		String path = "/objectives/" + id;
		String token = jwtTokenGenerator
				.generate(customer -> customer.claim("name", "user2").claim("email", "user2@company.com"));

		mvc.perform(get(path).header("Authorization", "Bearer " + token)).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "user1")
	void findById_ObjectiveDoesNotExit_Return404() throws Exception {
		String id = "bdbd694c-d820-4f9f-a409-17210ce53038";
		String path = "/objectives/" + id;
		String token = jwtTokenGenerator.generate();

		mvc.perform(get(path).header("Authorization", "Bearer " + token)).andExpect(status().isNotFound());
	}
	
}
