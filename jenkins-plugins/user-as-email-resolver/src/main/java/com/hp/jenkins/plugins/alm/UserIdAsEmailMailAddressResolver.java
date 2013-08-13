package com.hp.jenkins.plugins.alm;

import hudson.Extension;
import hudson.model.User;
import hudson.tasks.MailAddressResolver;


//generally should be first, but leave room for unexpected...
@Extension(ordinal = (Integer.MAX_VALUE - 100))
public class UserIdAsEmailMailAddressResolver extends MailAddressResolver {


    @Override
    public String findMailAddressFor(User u) {

        final String potentialEmail = u.getId();

        if (potentialEmail != null && potentialEmail.contains("@")) {
            return potentialEmail;
        }

        return null;
    }
}

