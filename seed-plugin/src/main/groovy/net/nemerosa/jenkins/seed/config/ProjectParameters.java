package net.nemerosa.jenkins.seed.config;

import lombok.Data;

/**
 * Actual parameters for a project.
 */
@Data
public class ProjectParameters {

    /**
     * Name of the project
     */
    private final String project;

    /**
     * SCM type
     */
    private final String scmType;

    /**
     * SCM base (without any branch)
     */
    private final String scmUrl;

    /**
     * SCM credentials (ref. to Jenkins credentials)
     */
    private final String scmCredentials;

    /**
     * Type of trigger end point being enabled
     */
    private final String triggerType;

    /**
     * Trigger secret token
     */
    private final String triggerSecret;

}