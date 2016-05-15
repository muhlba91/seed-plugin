package net.nemerosa.jenkins.seed.generator;

import com.google.common.base.Function;
import hudson.EnvVars;
import net.nemerosa.jenkins.seed.config.ProjectParameters;
import net.nemerosa.jenkins.seed.config.ProjectPipelineConfig;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractSeedStep extends AbstractGenerationStep {

    protected abstract String getScriptPath();

    @Override
    protected GenerationContext configure(Function<String, String> expandFn, EnvVars env) {
        // Environment variables
        Map<String, String> config = new LinkedHashMap<>();
        // Project pipeline
        ProjectPipelineConfig projectConfig = getProjectConfig();
        // Project actual parameters
        ProjectParameters parameters = projectConfig.getProjectParameters(expandFn);
        // Environment variables
        configuration(projectConfig, parameters, config, env);
        // Script replacements
        Map<String, GenerationExtension> extensions = getExtensionPoints(env, projectConfig, parameters);
        // OK
        return new GenerationContext(
                config,
                extensions
        );
    }

    protected void configuration(ProjectPipelineConfig projectConfig, ProjectParameters parameters, Map<String, String> config, EnvVars env) {
        generalConfiguration(parameters, config);
        pipelineConfiguration(projectConfig, parameters, config);
        projectConfiguration(projectConfig, parameters, config);
        branchConfiguration(projectConfig, parameters, config, env);
        eventConfiguration(projectConfig, parameters, config);
    }

    protected void pipelineConfiguration(ProjectPipelineConfig projectConfig, @SuppressWarnings("UnusedParameters") ProjectParameters parameters, Map<String, String> config) {
        config.put("PIPELINE_DESTRUCTOR", String.valueOf(projectConfig.getPipelineConfig().isDestructor()));
        config.put("PIPELINE_COMMIT_PARARAMETER", projectConfig.getPipelineConfig().getCommitParameter());
        config.put("PIPELINE_BRANCH_SCM_PARAMETER", String.valueOf(projectConfig.getPipelineConfig().isBranchSCMParameter()));
        config.put("PIPELINE_BRANCH_PARAMETERS", projectConfig.getPipelineConfig().getBranchParameters());
        config.put("PIPELINE_GENERATION_EXTENSION", projectConfig.getPipelineConfig().getGenerationExtension());
    }

    protected void projectConfiguration(ProjectPipelineConfig projectConfig, ProjectParameters parameters, Map<String, String> config) {
        config.put("PROJECT_FOLDER_PATH", projectConfig.getPipelineConfig().getProjectFolder(parameters));
        config.put("SEED_PROJECT", projectConfig.getPipelineConfig().getProjectFolder(parameters));
        config.put("PROJECT_SEED_NAME", projectConfig.getPipelineConfig().getProjectSeedJob(parameters));
        config.put("PROJECT_DESTRUCTOR_NAME", String.valueOf(projectConfig.getPipelineConfig().getProjectDestructorJob(parameters)));
    }

    protected void branchConfiguration(ProjectPipelineConfig projectConfig, ProjectParameters parameters, Map<String, String> config, EnvVars env) {
        config.put("BRANCH_FOLDER_PATH", projectConfig.getPipelineConfig().getNamingStrategy().getBranchFolderPath());
        config.put("BRANCH_SEED_NAME", projectConfig.getPipelineConfig().getNamingStrategy().getBranchSeedName());
        config.put("BRANCH_START_NAME", String.valueOf(projectConfig.getPipelineConfig().getNamingStrategy().getBranchStartName()));
        config.put("BRANCH_NAME", String.valueOf(projectConfig.getPipelineConfig().getNamingStrategy().getBranchName()));
    }

    protected void generalConfiguration(ProjectParameters parameters, Map<String, String> config) {
        config.put("PROJECT", parameters.getProject());
        config.put("PROJECT_SCM_TYPE", parameters.getScmType());
        config.put("PROJECT_SCM_URL", parameters.getScmUrl());
        config.put("PROJECT_SCM_CREDENTIALS", parameters.getScmCredentials());
    }

    protected void eventConfiguration(ProjectPipelineConfig projectConfig, @SuppressWarnings("UnusedParameters") ProjectParameters parameters, Map<String, String> config) {
        config.put("EVENT_STRATEGY_DELETE", String.valueOf(projectConfig.getPipelineConfig().getEventStrategy().isDelete()));
        config.put("EVENT_STRATEGY_AUTO", String.valueOf(projectConfig.getPipelineConfig().getEventStrategy().isAuto()));
        config.put("EVENT_STRATEGY_TRIGGER", String.valueOf(projectConfig.getPipelineConfig().getEventStrategy().isTrigger()));
        config.put("EVENT_STRATEGY_START_AUTO", String.valueOf(projectConfig.getPipelineConfig().getEventStrategy().isStartAuto()));
        config.put("EVENT_STRATEGY_COMMIT", projectConfig.getPipelineConfig().getEventStrategy().getCommit());
    }

    protected abstract Map<String, GenerationExtension> getExtensionPoints(EnvVars env, ProjectPipelineConfig projectConfig, ProjectParameters parameters);

    protected abstract ProjectPipelineConfig getProjectConfig();
}
