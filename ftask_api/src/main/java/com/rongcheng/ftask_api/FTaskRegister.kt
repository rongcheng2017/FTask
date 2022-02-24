package com.rongcheng.ftask_api

import com.rongcheng.ftask_annotations.ModuleTaskRegister
import com.rongcheng.ftask_annotations.FTaskInfo

class FTaskRegister {
    // 通过FTaskAnnotationProcessor 反射调用改方法，将当前module生成的TaskInfo都注册到taskList中
    val FTaskList: MutableList<FTaskInfo> = mutableListOf()

    init {
        init()
    }

    private fun init() {
        //需要往这里注入代码，register(xxxModuleTaskRegister())
    }

    fun register(register: ModuleTaskRegister) {
        register.register(FTaskList)
    }
}