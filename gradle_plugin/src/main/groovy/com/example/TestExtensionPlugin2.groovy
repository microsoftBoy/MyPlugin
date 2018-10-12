package com.example

import com.android.build.gradle.AppExtension
import com.example.transform.MyTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

public class TestExtensionPlugin2 implements Plugin<Project> {

    @Override
    void apply(Project project) {

        def android = project.extensions.getByType(AppExtension)
        android.registerTransform(new MyTransform(project))
        println "Hello World from the TestExtensionPlugin2222"


    }
}