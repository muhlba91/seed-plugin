/**
 * Script to generate a branch folder and seed.
 *
 * @see net.nemerosa.jenkins.seed.generator.BranchGenerationStep
 */

/**
 * Branch folder
 */

folder(BRANCH_FOLDER_PATH) {}

/**
 * Branch seed
 */

job("${BRANCH_FOLDER_PATH}/${BRANCH_SEED_NAME}") {
    description "Branch seed for ${BRANCH} in ${PROJECT} - generates the pipeline for the ${BRANCH} branch."

    // SCM configuration
    branchSeedScmExtensionPoint()

    // Branch parameters injection
    branchSeedParametersExtensionPoint()

    /**
     * Pipeline generation
     *
     * Reads information from the property file and generates a Gradle build file
     * for the purpose of download the DSL libraries and extracting the bootstrap
     * script file.
     */
    configure { node ->
        node / 'builders' / 'net.nemerosa.jenkins.seed.generator.PipelineGenerationStep' {
            // The whole pipeline configuration is no longer needed, only project parameters
            // and branch parameters
            // Project
            project PROJECT
            scmType PROJECT_SCM_TYPE
            scmUrl PROJECT_SCM_URL
            scmCredentials PROJECT_SCM_CREDENTIALS
            // Branch
            branch BRANCH
            // Jenkins-safe names
            seedProject SEED_PROJECT
            seedBranch SEED_BRANCH
        }
    }

    /**
     * Defines a Gradle step for the build file generated by the previous step:
     * - downloads the dependencies
     * - extract the DSL bootstrap script from the indicated JAR
     */
    wrappers {
        injectPasswords()
    }
    steps {
        shell '''\\
#!/bin/bash
if [ "\${SEED_GRADLE}" == "yes" ]
then
    cd seed
    chmod u+x gradlew
    ./gradlew prepare --refresh-dependencies
fi
'''
    }

    /**
     * Runs the script DSL
     */
    steps {
        dsl {
            external 'seed/seed.groovy'
            removeAction 'DELETE'
            lookupStrategy 'SEED_JOB'
            ignoreExisting false
            additionalClasspath 'seed/lib/*.jar'
        }
    }

    // TODO Branch seed extensions
}

/**
 * Firing the branch seed
 */

if (EVENT_STRATEGY_AUTO == "yes") {
    queue("${BRANCH_FOLDER_PATH}/${BRANCH_SEED_NAME}")
}
