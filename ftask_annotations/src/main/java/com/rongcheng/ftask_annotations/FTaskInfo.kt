package com.rongcheng.ftask_annotations

data class FTaskInfo(
    val name: String,
    val type: String,
    //被@FTask注解的类
    val clazzString: String,
    val task: IFTask<*>
)
