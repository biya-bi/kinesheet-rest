package org.rainbow.kinesheet.repository;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rainbow.kinesheet.model.Objective;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@SpringBootTest
class ObjectiveRepositoryTest {

    @Autowired
    private ObjectiveRepository objectiveRepository;

    @MockBean
    private JwtAuthenticationToken authentication;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void findById_ObjectiveWasSetByCurrentAchiever_ReturnObjective() {
        when(authentication.getTokenAttributes()).thenReturn(Collections.singletonMap("email", "user1@company.com"));

        UUID id = UUID.fromString("350bebae-d54f-4e60-a2c8-77a0778e1c5b");

        Optional<Objective> objective = objectiveRepository.findById(id);

        assertTrue(objective.isPresent());
    }

    @Test
    void findById_ObjectiveWasSetByAnotherAchiever_ThrowAccessDeniedException() {
        when(authentication.getTokenAttributes()).thenReturn(Collections.singletonMap("email", "user1@company.com"));

        // The objective with this ID was set by user2. So, we expect an access denied
        // exception if user1 requests for it.
        UUID id = UUID.fromString("04397fe3-772d-4424-881d-0863f0a5bbbf");

        assertThrows(AccessDeniedException.class, () -> objectiveRepository.findById(id));
    }

    @Test
    void findAll_ManyAchieversHaveSetObjectives_ReturnCurrentAchieverObjectives() {
        String email = "user1@company.com";

        when(authentication.getTokenAttributes()).thenReturn(Collections.singletonMap("email", email));

        Iterable<Objective> objectives = objectiveRepository.findAll();

        List<String> emails = StreamSupport.stream(objectives.spliterator(), false)
                .map(objective -> objective.getAchiever().getEmail()).distinct().collect(Collectors.toList());

        // Assert that all the objectives returned were set by the same achiever
        assertIterableEquals(Arrays.asList(email), emails);
    }

}
