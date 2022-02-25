# FTask
组件化中跨module获取呗@FTask注解的类

# 使用

配置maven:
```grovvy

  maven {
            allowInsecureProtocol true
            url "http://maven.geelib.360.cn/nexus/repository/mobiledj/" }
  
```
配置Auto-Register插件依赖
```grovvy
//project的build.gradle中
 dependencies {
        classpath "com.billy.android:autoregister:1.4.2"
    }

```


添加依赖

```grovvy
//项目的build.gradle
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'kotlin-kapt'
    //todo 1.
    id 'auto-register'
}
android {
    compileSdk 32
    defaultConfig {
        applicationId "com.rongcheng.ftask"
        minSdk 21
        targetSdk 32
        versionCode 1
        versionName "1.0"
        //todo 2 
        kapt {
            arguments {
                arg("moduleName", project.name)
            }
        }
        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

   ...
}
// todo 3.
autoregister {
    registerInfo = [
            [
                    "scanInterface"         : "com.rongcheng.ftask_annotations.ModuleTaskRegister",
                    "codeInsertToClassName" : "com.rongcheng.ftask_api.FTaskRegister",
                    "codeInsertToMethodName": "init",
                    "registerMethodName"    : "register",
                    "include"               : ["com/rongcheng/ftask/apt/taskregister/.*"]
            ]
    ]
}
dependencies {
    //todo 4.
    implementation "com.rongcheng:ftask-api:$VERSION_NAME"
    kapt "com.rongcheng:ftask-compiler:$VERSION_NAME"
}
```

# 使用方式

1. 使用@FTask注解,实现IFTask接口
```kotlin
@FTask("init")
class InitTask : IFTask<String> {
    override fun execute(): String {
        Log.e("Init","init task execute")
        return "this is init task"
    }

    override fun getTag(): String {
        return "Init"
    }

    override fun beforeExecute() {
        super.beforeExecute()
        Log.e("Init","before init execute")
    }

    override fun afterExecute(t: Any?) {
        super.afterExecute(t)
        Log.e("Init","after init execute")
    }
}
```

2. FTaskManager自己实现需要处理的功能
```grovvy
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        FTaskManager.start(this) {
            it.forEach { taskInfo ->
                taskInfo.task.beforeExecute()
                if (taskInfo.task.getTag() == "Init") {
                    val task: IFTask<String> = taskInfo.task as IFTask<String>
                    val t: String = task.execute()
                    task.afterExecute(t)
                } else {
                    val task: IFTask<Unit> = taskInfo.task as IFTask<Unit>
                    task.afterExecute(task.execute())
                }
            }
        }
    }
}

```

