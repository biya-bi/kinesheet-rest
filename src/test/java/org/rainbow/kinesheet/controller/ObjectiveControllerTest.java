package org.rainbow.kinesheet.controller;

import static org.rainbow.kinesheet.util.RequestUtil.setBearerHeader;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.rainbow.kinesheet.config.JwtTestConfiguration;
import org.rainbow.kinesheet.jwt.JwtGenerator;
import org.rainbow.kinesheet.request.ObjectiveWriteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = JwtTestConfiguration.class)
@AutoConfigureMockMvc
class ObjectiveControllerTest {

	private final static String WRITE_REQUEST_TEMPLATE = """
			{
				"title": "$title"
			}""";

	@Autowired
	private MockMvc mvc;

	@Autowired
	private JwtGenerator jwtGenerator;

	@Test
	@WithMockUser(username = "user1")
	void create_RequestIsValid_SaveObjective() throws Exception {
		String path = "/objectives";
		String token = jwtGenerator.generate();
		String title = "Upgrade to Java 21";
		String payload = getRequestPayload(title);

		String location = mvc.perform(setBearerHeader(post(path), token)
				.with(csrf())
				.contentType("application/json")
				.content(payload))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"))
				.andExpect(jsonPath("$..title").value(title))
				.andReturn().getResponse().getHeader("Location");

		mvc.perform(setBearerHeader(get(location), token)).andExpect(status().isOk())
				.andExpect(jsonPath("$..title").value(title));
	}

	@ParameterizedTest
	@ValueSource(strings = { "", "   " })
	@WithMockUser(username = "user1")
	void create_TitleIsBlank_ReturnBadRequest(String title) throws Exception {
		String path = "/objectives";
		String token = jwtGenerator.generate();

		String payload = getRequestPayload(title);

		mvc.perform(setBearerHeader(post(path), token)
				.with(csrf())
				.contentType("application/json")
				.content(payload))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$..title").value(ObjectiveWriteRequest.TITLE_REQUIRED_MESSAGE));
	}

	@Test
	@WithMockUser(username = "user1")
	void create_TitleIsNull_ReturnBadRequest() throws Exception {
		String path = "/objectives";
		String token = jwtGenerator.generate();

		String payload = getRequestPayload(null);

		mvc.perform(setBearerHeader(post(path), token)
				.with(csrf())
				.contentType("application/json")
				.content(payload))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$..title").value(ObjectiveWriteRequest.TITLE_REQUIRED_MESSAGE));
	}

	@Test
	@WithMockUser(username = "user1")
	void create_TitleIsMissing_ReturnBadRequest() throws Exception {
		String path = "/objectives";
		String token = jwtGenerator.generate();

		String payload = "{}";

		mvc.perform(setBearerHeader(post(path), token)
				.with(csrf())
				.contentType("application/json")
				.content(payload))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$..title").value(ObjectiveWriteRequest.TITLE_REQUIRED_MESSAGE));
	}

	@Test
	@WithMockUser(username = "user1")
	void findById_ObjectiveBelongsToRequester_ReturnObjective() throws Exception {
		String id = "350bebae-d54f-4e60-a2c8-77a0778e1c5b";
		String path = "/objectives/" + id;
		String token = jwtGenerator.generate();

		mvc.perform(setBearerHeader(get(path), token))
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
		String token = jwtGenerator
				.generate(customer -> customer.claim("name", "user2").claim("email", "user2@company.com"));

		mvc.perform(setBearerHeader(get(path), token)).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "user1")
	void findById_ObjectiveDoesNotExit_Return404() throws Exception {
		String id = "bdbd694c-d820-4f9f-a409-17210ce53038";
		String path = "/objectives/" + id;
		String token = jwtGenerator.generate();

		mvc.perform(setBearerHeader(get(path), token)).andExpect(status().isNotFound());
	}

	private String getRequestPayload(String title) {
		return replace(WRITE_REQUEST_TEMPLATE, "$title", title);
	}

	private String replace(String template, String placeHolder, String value) {
		String target;
		String replacement;
		if (value != null) {
			target = placeHolder;
			replacement = value;
		} else {
			target = String.format("\"%s\"", placeHolder);
			replacement = "null";
		}
		return template.replace(target, replacement);
	}
}
