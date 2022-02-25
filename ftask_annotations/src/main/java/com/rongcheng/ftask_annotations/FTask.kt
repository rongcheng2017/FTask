package com.rongcheng.ftask_annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class FTask(
    val name: String,
    val type: String = "default"
)
