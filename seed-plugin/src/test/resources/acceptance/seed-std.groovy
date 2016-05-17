package acceptance
/**
 * Sample pipeline generation, used for acceptance tests.
 *
 * The Seed plug-in will give the following parameters to this scripts, available directly as variables:
 *
 * - raw parameters (seed generator input + scm branch)
 *   - PROJECT - raw project name, like nemerosa/seed in GitHub
 *   - PROJECT_CLASS
 *   - PROJECT_SCM_TYPE
 *   - PROJECT_SCM_URL
 *   - BRANCH - basic branch name in the SCM, like branches/xxx in SVN
 *
 * - computed parameters:
 *   - SEED_PROJECT: project normalised name
 *   - SEED_BRANCH: branch normalised name
 *
 * The jobs are generated directly at the level of the branch seed job, so no folder needs to be created for the
 * branch itself.
 */

job("${SEED_PROJECT}-${SEED_BRANCH}-build") {
    parameters {
        stringParam('COMMIT', 'HEAD', 'Commit to build')
    }
    steps {
        shell '''\
echo "Commit: ${COMMIT}"
'''
    }
    publishers {
        downstreamParameterized {
            trigger("${SEED_PROJECT}-${SEED_BRANCH}-ci", 'SUCCESS', true) {
            }
        }
    }
}

job("${SEED_PROJECT}-${SEED_BRANCH}-ci") {
    publishers {
        downstreamParameterized {
            trigger("${SEED_PROJECT}-${SEED_BRANCH}-publish", 'SUCCESS', true) {
            }
        }
    }
}

job("${SEED_PROJECT}-${SEED_BRANCH}-publish") {

}

queue("${SEED_PROJECT}-${SEED_BRANCH}-build")
