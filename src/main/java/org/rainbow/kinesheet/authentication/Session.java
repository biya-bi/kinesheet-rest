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
            return StringUtils.trimToEmpty(getEmail()).equalsIgnoreCase(achiever.getEmail());
        }
        return false;
    }

    public static JwtAuthenticationToken getAuthentication() {
        return (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    }

    public static String getEmail() {
        return (String) getAuthentication().getTokenAttributes().get("email");
    }

}
