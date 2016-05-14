package net.nemerosa.jenkins.seed.config;

import lombok.Data;
import lombok.experimental.Wither;
import org.kohsuke.stapler.DataBoundConstructor;

import static net.nemerosa.jenkins.seed.support.Evaluator.evaluate;

@Data
public class NamingStrategyConfig {

    /**
     * Path to the project folder
     */
    @Wither
    private final String projectFolderPath;

    /**
     * Path to the branch folder
     */
    @Wither
    private final String branchFolderPath;

    /**
     * Name of the project seed
     */
    @Wither
    private final String projectSeedName;

    /**
     * Name of the project destructor
     */
    @Wither
    private final String projectDestructorName;

    /**
     * Name of the branch seed
     */
    @Wither
    private final String branchSeedName;

    /**
     * Start job name for the branch
     */
    @Wither
    private final String branchStartName;

    @DataBoundConstructor
    public NamingStrategyConfig(String projectFolderPath, String branchFolderPath, String projectSeedName, String branchSeedName, String branchStartName, String projectDestructorName) {
        this.projectFolderPath = projectFolderPath;
        this.branchFolderPath = branchFolderPath;
        this.projectSeedName = projectSeedName;
        this.projectDestructorName = projectDestructorName;
        this.branchSeedName = branchSeedName;
        this.branchStartName = branchStartName;
    }

    /**
     * Constructor with default values
     */
    public NamingStrategyConfig() {
        this(
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public String getProjectFolder(ProjectParameters parameters) {
        return evaluate(projectFolderPath, "${project}", "project", parameters.getProject());
    }

    public String getProjectSeedJob(ProjectParameters parameters) {
        return evaluate(projectSeedName, "${project}-seed", "project", parameters.getProject());
    }

    public String getProjectDestructorJob(ProjectParameters parameters) {
        return evaluate(projectDestructorName, "${project}-destructor", "project", parameters.getProject());
    }
}
