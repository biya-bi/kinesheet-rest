package org.rainbow.kinesheet.authentication;

import org.apache.commons.lang3.StringUtils;
import org.rainbow.kinesheet.model.Achiever;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public final class Session {

    private Session() {
    }

    public static boolean isCurrent(Achiever achiever) {
        if (achiever != null) {
            var email = getClaims().getEmail();
            return StringUtils.trimToEmpty(email).equalsIgnoreCase(achiever.getEmail());
        }
        return false;
    }

    public static Claims getClaims() {
        var authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        var tokenAttributes = authentication.getTokenAttributes();
        var email = (String) tokenAttributes.get("email");
        var name = (String) tokenAttributes.get("name");
        return Claims.builder().email(email).name(name).build();
    }

}
