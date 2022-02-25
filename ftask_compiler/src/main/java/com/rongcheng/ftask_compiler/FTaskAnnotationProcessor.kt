package com.rongcheng.ftask_compiler

import com.rongcheng.ftask_annotations.ModuleTaskRegister
import com.rongcheng.ftask_annotations.FTask
import com.rongcheng.ftask_annotations.FTaskInfo
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

/**
 * @StartUpTask 的注解处理器
 */
class FTaskAnnotationProcessor : AbstractProcessor() {
    private lateinit var elementUtil: Elements
    private lateinit var filer: Filer
    private lateinit var typeUtil: Types
    private var moduleName: String? = null
    private val needImplInfName = "com.rongcheng.ftask_annotations.IFTask"
    private val generatedClassPath = "com.rongcheng.ftask.apt.taskregister"
    private val generatedClassNameEnd= "ModuleFTaskRegister"
    private val TAG="FT"

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        Log.setLogger(processingEnv.messager)
        elementUtil = processingEnv.elementUtils
        filer = processingEnv.filer
        typeUtil = processingEnv.typeUtils
        moduleName = processingEnv.options["moduleName"]
        if (moduleName == null || moduleName?.isEmpty() == true) {
            throw IllegalArgumentException(
                "$TAG , Can not find apt argument 'moduleName', check if has add the code like this in module's build.gradle:\n" +
                        "    In Kotlin:\n" +
                        "    \n" +
                        "    kapt {\n" +
                        "        arguments {\n" +
                        "          arg(\"moduleName\", project.name)\n" +
                        "        }\n" +
                        "    }\n"
            )
        }
        Log.i("$TAG , start deal [$moduleName] module")

    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        val supportAnnotationTypes = mutableSetOf<String>()
        supportAnnotationTypes.add(FTask::class.java.canonicalName)
        return supportAnnotationTypes
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun process(p0: MutableSet<out TypeElement>?, p1: RoundEnvironment?): Boolean {
        val taskElements = p1?.getElementsAnnotatedWith(FTask::class.java)

        if (taskElements?.isEmpty() == true) {
            return false
        }
        Log.i("$TAG , FTask Annotation Processor find tasks, size is ${taskElements?.size}")

        /**
         * generate class
        //import com.rongcheng.ftask_annotations.ModuleTaskRegister
        //import com.rongcheng.ftask_annotations.TaskInfo
        //
        //class xxModuleTaskRegister : ModuleTaskRegister {
        //    override fun register(fTaskInfoList: MutableList<FTaskInfo>) {
        //        fTaskInfoList.add(FTaskInfo(""))
        //    }
        //}
         */

        /**
         *  1. param  type : MutableList<FTaskInfo>
         */
        val inputMapTypeName = ClassName(
            "kotlin.collections",
            "MutableList"
        ).parameterizedBy(FTaskInfo::class.asTypeName())

        /**
         *  2. param name: fTaskInfoList : MutableList<FTaskInfo>
         */
        val groupParameterSpec = ParameterSpec.builder("fTaskInfoList", inputMapTypeName).build()

        /**
         *  3. method :  override fun register(tasks: MutableList<FTaskInfo>)
         */
        val loadTaskMethodBuilder = FunSpec.builder("register").addModifiers(KModifier.OVERRIDE)
            .addParameter(groupParameterSpec)


        /**
         *  4. taskList.add(FTaskInfo(task.name,task)
         */
        taskElements?.forEach { element ->
            val task = element.getAnnotation(FTask::class.java)
            val result = intArrayOf(0)
            Log.i("$TAG  element is  : ${element.asType()}")
            checkoutInterfaces(element as TypeElement, result)
            if (result[0] > 0) {
                val taskCn = element.asClassName()
                loadTaskMethodBuilder.addStatement(
                    "%N.add(%T(%S,%S,%S,%T()))",
                    "fTaskInfoList",
                    FTaskInfo::class.java,
                    task.name,
                    task.type,
                    taskCn,
                    taskCn
                )
            }
        }

        /**
         * 5. create class and write to file
         */
        FileSpec.builder(
            generatedClassPath,
            "$moduleName\$$generatedClassNameEnd"
        )
            .addType(
                TypeSpec.classBuilder("$moduleName\$$generatedClassNameEnd")
                    .addSuperinterface(ModuleTaskRegister::class.java)
                    .addFunction(loadTaskMethodBuilder.build())
                    .build()
            ).build()
            .writeTo(filer)

        return true
    }

    /**
     * 判断当前element是否实现了IStartUpTask接口
     *
     * 添加泛型后 下面方法不灵
     * val taskType = elementUtil.getTypeElement([needImplInfName]})
     * typeUtil.isSubtype(element.asType(),taskType.asType())
     */
    private fun checkoutInterfaces(element: TypeElement, result: IntArray) {
        val interfaces = element.interfaces
        if (interfaces.isNotEmpty()) {
            for (i in interfaces) {
                val bool = i.asTypeName().toString()
                    .contains(needImplInfName)
                if (bool) {
                    result[0]++
                    return
                } else {
                    checkoutInterfaces(elementUtil.getTypeElement(i.toString()), result)
                    return
                }
            }
        }
        val superClazzs = element.superclass ?: return
        checkoutInterfaces(elementUtil.getTypeElement(superClazzs.toString()), result)

    }

}