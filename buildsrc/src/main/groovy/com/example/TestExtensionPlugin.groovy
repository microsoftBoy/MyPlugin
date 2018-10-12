package com.example

import com.android.build.gradle.AppExtension
import com.example.transform.MyTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

public class TestExtensionPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println "Hello World from the apply"
//        def android = project.extensions.getByType(AppExtension)
//        android.registerTransform(new MyTransform(project))
        project.task('readExtension') {
            println "Hello World from the TestExtensionPlugin"

        }


    }
}
