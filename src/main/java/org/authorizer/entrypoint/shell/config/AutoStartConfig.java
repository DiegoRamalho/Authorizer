package org.authorizer.entrypoint.shell.config;

import org.authorizer.entrypoint.shell.AuthorizeController;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
@Profile("shell")
public class AutoStartConfig {

    private final AuthorizeController authorizeController;

    @PostConstruct
    public void init() {
        authorizeController.processOperationsFromStdInToStdOut();
    }
}
