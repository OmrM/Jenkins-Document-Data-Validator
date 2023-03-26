package io.jenkins.plugins.docDataValidator;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;

public class DocDataValidatorBuilder extends Builder implements SimpleBuildStep {

    //Getters and setters for Parameters: name, userFrench
    private final String directory;
    private final String outputDirectory;


    @DataBoundConstructor
    public DocDataValidatorBuilder(String directory, String outputDirectory) {
        this.directory = directory;
        this.outputDirectory = outputDirectory;
    }

    public String getDirectory() {
        return directory;
    }
    public String getOutputDirectory() {
        return outputDirectory;
    }

    @Override
    public void perform(Run<?, ?> run, FilePath workspace, EnvVars env, Launcher launcher, TaskListener listener) throws InterruptedException, IOException {
        //calling the HelloWorldAction and passing in the name. from "Extend the Plugin" docs:
        run.addAction(new DocDataValidationAction(directory, outputDirectory));

        listener.getLogger().println("The directory selected is: " + directory);
        listener.getLogger().println("The output directory selected is: " + outputDirectory);

        //calling the main function in the FileTypeDetection.java file and passing the directory path from build step
        //FileTypeDetection.main(new String[]{directory});
        //new FileTypeDetection(directory);
        try {
            //try instantiating a new object and then call the main function?
            FileTypeDetection.main(new String[]{directory}, listener);
        } catch (Exception e) {
            listener.getLogger().println("Error running FileTypeDetection: " + e.getMessage());
        }

    }

    @Symbol("validateDocuments")
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {


        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }
        //display name for the Build step. will show up when adding plugin as a build step in the dashboard
        @Override
        public String getDisplayName() {
            return "Document Data Validator";
            //return Messages.HelloWorldBuilder_DescriptorImpl_DisplayName();
        }

    }

}
