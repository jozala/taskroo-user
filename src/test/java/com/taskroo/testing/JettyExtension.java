package com.taskroo.testing;

import com.taskroo.user.server.JettyRunner;
import org.spockframework.runtime.AbstractRunListener;
import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension;
import org.spockframework.runtime.model.FeatureInfo;
import org.spockframework.runtime.model.SpecInfo;

public class JettyExtension extends AbstractAnnotationDrivenExtension<RunJetty> {
    @Override
    public void visitSpecAnnotation(RunJetty runJetty, SpecInfo specInfo) {
        isSpecAnnotated = true;
        specInfo.addListener(new AbstractRunListener() {
            @Override
            public void beforeSpec(SpecInfo specInfo) {
                JettyRunner.startIfNotRunning(runJetty.host(), runJetty.port());
            }

            @Override
            public void afterSpec(SpecInfo specInfo) {
                JettyRunner.stop();
            }
        });

    }

    @Override
    public void visitFeatureAnnotation(RunJetty runJetty, FeatureInfo featureInfo) {
        if (isSpecAnnotated) {
            throw new RuntimeException(String.format("A single specification cannot have both Specification and Feature annotated " + "by %s", RunJetty.class.getSimpleName()));
        }


        featureInfo.getParent().addListener(new AbstractRunListener() {
            @Override
            public void beforeFeature(FeatureInfo featureInfo) {
                JettyRunner.startIfNotRunning(runJetty.host(), runJetty.port());
            }

            @Override
            public void afterFeature(FeatureInfo featureInfo) {
                JettyRunner.stop();
            }

        });
    }

    private boolean isSpecAnnotated;
}
