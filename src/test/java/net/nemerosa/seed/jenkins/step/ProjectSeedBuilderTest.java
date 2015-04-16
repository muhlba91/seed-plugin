package net.nemerosa.seed.jenkins.step;

import hudson.model.*;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.recipes.WithPlugin;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

// TODO Enable tests
@Ignore
public class ProjectSeedBuilderTest {
    /**
     * The Jenkins Rule.
     */
    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Test
    @WithPlugin("cloudbees-folder.jpi")
    public void seed_generation() throws Exception {
        FreeStyleProject project = j.getInstance().createProject(FreeStyleProject.class, "seed");
        project.getBuildersList().add(new ProjectSeedBuilder(
                "my_project",
                "test",
                "/seed/sample"
        ));
        Future<FreeStyleBuild> future = project.scheduleBuild2(0, new Cause.UserIdCause());
        FreeStyleBuild build = future.get(10, TimeUnit.SECONDS);
        j.assertBuildStatus(Result.SUCCESS, build);

        // Gets the created job
        TopLevelItem item = j.getInstance().getItem("my_project");
        assertNotNull(item);
        assertEquals("my_project", item.getName());
    }

}