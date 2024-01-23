package org.rainbow.kinesheet.service.impl;

import org.rainbow.kinesheet.authentication.Session;
import org.rainbow.kinesheet.model.Achiever;
import org.rainbow.kinesheet.repository.AchieverRepository;
import org.rainbow.kinesheet.service.AchieverService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
class AchieverServiceImpl implements AchieverService {

    private static final String CACHE_NAME = "Achievers";
    private static final String CACHE_KEY = "T(org.rainbow.kinesheet.authentication.Session).getEmail()";

    private final AchieverRepository achieverRepository;

    AchieverServiceImpl(AchieverRepository achieverRepository) {
        this.achieverRepository = achieverRepository;
    }

    @Cacheable(value = CACHE_NAME, key = CACHE_KEY)
    @Override
    public Achiever getCurrent() {
        var tokenAttributes = Session.getAuthentication().getTokenAttributes();
        var email = (String) tokenAttributes.get("email");
        var achiever = achieverRepository.findByEmail(email);
        if (achiever != null) {
            return achiever;
        }
        var name = (String) tokenAttributes.get("name");
        achiever = new Achiever();
        achiever.setEmail(email);
        achiever.setName(name);
        return achieverRepository.save(achiever);
    }

}
