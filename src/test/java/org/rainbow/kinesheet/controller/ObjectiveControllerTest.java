package org.rainbow.kinesheet.controller;

import static org.rainbow.kinesheet.util.RequestUtil.setBearerHeader;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.rainbow.kinesheet.config.JwtTestConfiguration;
import org.rainbow.kinesheet.dto.ObjectiveDto;
import org.rainbow.kinesheet.jwt.JwtGenerator;
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
				.andExpect(jsonPath("$..title").value(ObjectiveDto.TITLE_REQUIRED_MESSAGE));
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
				.andExpect(jsonPath("$..title").value(ObjectiveDto.TITLE_REQUIRED_MESSAGE));
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
				.andExpect(jsonPath("$..title").value(ObjectiveDto.TITLE_REQUIRED_MESSAGE));
	}

	@Test
	@WithMockUser(username = "user2")
	void update_RequestIsValid_ReturnOk() throws Exception {
		String id = "04397fe3-772d-4424-881d-0863f0a5bbbf";
		String path = "/objectives/" + id;
		String token = jwtGenerator
				.generate(customer -> customer.claim("name", "user2").claim("email", "user2@company.com"));
		String title = "Do researches on General Relativity";
		String payload = getRequestPayload(title);

		mvc.perform(setBearerHeader(put(path), token)
				.with(csrf())
				.contentType("application/json")
				.content(payload))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$..title").value(title));
	}

	@ParameterizedTest
	@ValueSource(strings = { "", " " })
	@WithMockUser(username = "user2")
	void update_TitleIsBlank_ReturnBadRequest(String title) throws Exception {
		String id = "04397fe3-772d-4424-881d-0863f0a5bbbf";
		String path = "/objectives/" + id;
		String token = jwtGenerator
				.generate(customer -> customer.claim("name", "user2").claim("email", "user2@company.com"));

		String payload = getRequestPayload(title);

		mvc.perform(setBearerHeader(put(path), token)
				.with(csrf())
				.contentType("application/json")
				.content(payload))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$..title").value(ObjectiveDto.TITLE_REQUIRED_MESSAGE));
	}

	@Test
	@WithMockUser(username = "user2")
	void update_TitleIsNull_ReturnBadRequest() throws Exception {
		String id = "04397fe3-772d-4424-881d-0863f0a5bbbf";
		String path = "/objectives/" + id;
		String token = jwtGenerator
				.generate(customer -> customer.claim("name", "user2").claim("email", "user2@company.com"));

		String payload = getRequestPayload(null);

		mvc.perform(setBearerHeader(put(path), token)
				.with(csrf())
				.contentType("application/json")
				.content(payload))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$..title").value(ObjectiveDto.TITLE_REQUIRED_MESSAGE));
	}

	@Test
	@WithMockUser(username = "user2")
	void update_TitleIsMissing_ReturnBadRequest() throws Exception {
		String id = "04397fe3-772d-4424-881d-0863f0a5bbbf";
		String path = "/objectives/" + id;
		String token = jwtGenerator
				.generate(customer -> customer.claim("name", "user2").claim("email", "user2@company.com"));

		String payload = "{}";

		mvc.perform(setBearerHeader(put(path), token)
				.with(csrf())
				.contentType("application/json")
				.content(payload))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$..title").value(ObjectiveDto.TITLE_REQUIRED_MESSAGE));
	}

	@Test
	@WithMockUser(username = "user2")
	void update_ObjectiveIsMissing_ReturnNotFound() throws Exception {
		// There is no objective with this id. So we expect a 404.
		String id = "b2e246e0-6d6e-46c5-811c-1005d27e9ea9";
		String path = "/objectives/" + id;
		String token = jwtGenerator
				.generate(customer -> customer.claim("name", "user2").claim("email", "user2@company.com"));

		String title = "Do researches on General Relativity";
		String payload = getRequestPayload(title);

		mvc.perform(setBearerHeader(put(path), token)
				.with(csrf())
				.contentType("application/json")
				.content(payload))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(username = "user3")
	void delete_ObjectiveExists_ReturnOk() throws Exception {
		String id = "ae9fdde2-6b6d-4bb9-b7c2-2ff27308472a";
		String path = "/objectives/" + id;
		String token = jwtGenerator
				.generate(customer -> customer.claim("name", "user3").claim("email", "user3@company.com"));

		mvc.perform(setBearerHeader(delete(path), token)
				.with(csrf())
				.contentType("application/json"))
				.andExpect(status().isOk());

		mvc.perform(setBearerHeader(get(path), token)
				.with(csrf())
				.contentType("application/json"))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(username = "user3")
	void delete_ObjectiveDoesNotExist_ReturnNotFound() throws Exception {
		String id = "bdc91996-6245-4483-9955-d7133bcf23db";
		String path = "/objectives/" + id;
		String token = jwtGenerator
				.generate(customer -> customer.claim("name", "user3").claim("email", "user3@company.com"));

		mvc.perform(setBearerHeader(delete(path), token)
				.with(csrf())
				.contentType("application/json"))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(username = "user1")
	void findById_ObjectiveWasSetByCurrentAchiever_ReturnObjective() throws Exception {
		String id = "350bebae-d54f-4e60-a2c8-77a0778e1c5b";
		String path = "/objectives/" + id;
		String token = jwtGenerator.generate();

		mvc.perform(setBearerHeader(get(path), token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(id))
				.andExpect(jsonPath("$.title").value("Read Heavenly Mathematics"));
	}

	@Test
	@WithMockUser(username = "user1")
	void findById_ObjectiveDoesNotExit_ReturnNotFound() throws Exception {
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
