package org.rainbow.kinesheet.service.impl;

import org.rainbow.kinesheet.model.Achiever;
import org.rainbow.kinesheet.repository.AchieverRepository;
import org.rainbow.kinesheet.service.AchieverService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
class AchieverServiceImpl implements AchieverService {

    private final AchieverRepository achieverRepository;

    AchieverServiceImpl(AchieverRepository achieverRepository) {
        this.achieverRepository = achieverRepository;
    }

    @Override
    public Achiever getCurrent() {
        var context = SecurityContextHolder.getContext();
        var token = (JwtAuthenticationToken) context.getAuthentication();
        var tokenAttributes = token.getTokenAttributes();
        var email = (String) tokenAttributes.get("email");
        var achiever = achieverRepository.findByEmail(email);
        if (achiever == null) {
            var name = (String) tokenAttributes.get("name");
            var newAchiever = new Achiever();
            newAchiever.setEmail(email);
            newAchiever.setName(name);
            return achieverRepository.save(newAchiever);
        }
        return achiever;
    }

}
