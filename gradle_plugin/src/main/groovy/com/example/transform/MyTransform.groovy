package com.example.transform

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import org.gradle.api.logging.LoggingManager
import org.slf4j.LoggerFactory

import java.util.logging.Logger

public class MyTransform extends Transform {

    private Project project

    public MyTransform(Project project) {
        this.project = project
    }

    @Override

    String getName() {
        return "MyTransform"
    }

    // 指定输入的类型，通过这里的设定，可以指定我们要处理的文件类型
    //这样确保其他类型的文件不会传入
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    // 指定Transform的作用范围
    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {

        // Transform的inputs有两种类型，一种是目录，一种是jar包，要分开遍历
        transformInvocation.inputs.each {
            //对“文件夹”类型进行遍历
            it.directoryInputs.each {
                //文件夹里面包含的是我们手写的类以及R.class、BuildConfig.class以及R$XXX.class等
                println('directoryInputsName = '+it.name)
                MyInject.injectDir(it.file.absolutePath,'com\\example\\shuaige\\myplugin')
                //获取output目录
                def dest = transformInvocation.outputProvider.getContentLocation(it.name, it.contentTypes,
                        it.scopes, Format.DIRECTORY)

                FileUtils.copyDirectory(it.file, dest)

            }

            //对“jar”类型进行遍历
            it.jarInputs.each {
                //jar文件一般是第三方依赖库jar文件
                // 重命名输出文件（同目录copyFile会冲突）
                def jarName = it.name
                println('jarName = '+jarName)
                def md5name = DigestUtils.md5Hex(it.file.getAbsolutePath())
                if (jarName.endsWith('.jar')) {
                    jarName = jarName.substring(jarName.length() - 4)
                }

                //生成输出路径
                def dest = transformInvocation.outputProvider.getContentLocation(jarName + md5name,
                        it.contentTypes, it.scopes, Format.JAR)
                //将输入内容复制到输出
                FileUtils.copyFile(it.file,dest)


            }
        }
        super.transform(transformInvocation)

    }
}