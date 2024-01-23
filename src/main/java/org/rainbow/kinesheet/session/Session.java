package org.rainbow.kinesheet.session;

import org.apache.commons.lang3.StringUtils;
import org.rainbow.kinesheet.model.Achiever;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public final class Session {

    private Session() {
    }

    public static boolean isCurrent(Achiever achiever) {
        if (achiever == null) {
            return false;
        }

        var authentication = getAuthentication();

        var email = StringUtils.trimToEmpty((String) authentication.getTokenAttributes().get("email"));

        return email.equalsIgnoreCase(achiever.getEmail());
    }

    public static JwtAuthenticationToken getAuthentication() {
        return (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    }

    public static String getId() {
        return (String) getAuthentication().getTokenAttributes().get("email");
    }

}
