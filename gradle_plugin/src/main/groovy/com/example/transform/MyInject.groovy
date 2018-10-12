package com.example.transform

import javassist.ClassPool
import javassist.CtClass
import javassist.CtConstructor
import javassist.CtMethod

import java.lang.reflect.Modifier

public class MyInject {
    private static ClassPool pool = ClassPool.getDefault()
    private static String injectStr = "System.out.println(\"this is transform generation\" ); ";

    public static void injectDir(String path, String packageName) {
        pool.appendClassPath(path)
        println('path = '+path)
        File dir = new File(path)
        println('dir = '+dir.isDirectory())
        if (dir.isDirectory()) {
            dir.eachFileRecurse { File file ->
                String filePath = file.absolutePath
                println('filePath = '+filePath)
                //确保当前文件是class文件，并且不是系统自动生成的class文件
                if (filePath.endsWith('.class')
                        && !filePath.contains('R$')
                        && !filePath.contains('R.class')
                        && !filePath.contains('BuildConfig.class')){
                    // 判断当前目录是否是在我们的应用包里面
                    int index = filePath.indexOf(packageName)
                    boolean isMyPackage = index != -1
                    println('isMyPackage = '+isMyPackage)
                    if (isMyPackage){
                        int end = filePath.length() - 6 //.class = 6
                        String classNameTemp = filePath.substring(index,end)
                        println('classNameTemp = '+classNameTemp)
                        String className = classNameTemp.replace('\\','.').replace('/','.')
                        println('className = '+className)

                        //开始修改class文件
                        CtClass clazz = pool.getCtClass(className)

                        if (clazz.isFrozen()){
                            clazz.defrost()
                        }

                        CtConstructor[] constructors = clazz.getDeclaredConstructors()

                        CtMethod method = new CtMethod(CtClass.voidType,'add',new CtClass[0],clazz)

                        method.setModifiers(Modifier.PUBLIC)

                        method.setBody(new StringBuilder("{\n System.out.println(\"haha\"); \n}").toString());

                        clazz.addMethod(method)

                        if (constructors == null || constructors.length == 0){
                            //手动创建一个构造函数
                            CtConstructor constructor = new CtConstructor(new CtClass[0],clazz)
                            //插入代码
                            constructor.insertBeforeBody(injectStr)

//                            clazz.addConstructor(constructor)
                        } else {
                            constructors[0].insertBeforeBody(injectStr)
                        }

                        //clazz.writeFile(filePath) //1.0.4
                        clazz.writeFile(path)
                        clazz.detach()

                    }



                }
            }
        }
    }
}
