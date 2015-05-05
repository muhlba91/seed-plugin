/**
 * Script to generate a project seed.
 *
 * Parameters are:
 *
 * - PROJECT - Name (identifier) of the project
 * - PROJECT_CLASS - Class of project
 * - PROJECT_SCM_TYPE
 * - PROJECT_SCM_URL
 * - BRANCH
 *
 * Bound variables are:
 *
 * - branchSeedFolder
 * - branchSeedPath
 *
 * Extension points are:
 *
 * - branchSeedScm
 * - pipelineGeneration
 */

/**
 * Branch folder
 */

folder(branchSeedFolder) {}

/**
 * Branch seed
 */

freeStyleJob(branchSeedPath) {
    description "Branch seed for ${BRANCH} in ${PROJECT} - generates the pipeline for the ${BRANCH} branch."
    wrappers {
        environmentVariables {
            env('PROJECT', PROJECT)
            env('PROJECT_CLASS', PROJECT_CLASS)
            env('PROJECT_SCM_TYPE', PROJECT_SCM_TYPE)
            env('PROJECT_SCM_URL', PROJECT_SCM_URL)
            env('BRANCH', BRANCH)
        }
    }
    branchSeedScmExtensionPoint()
    pipelineGenerationExtensionPoint()
}