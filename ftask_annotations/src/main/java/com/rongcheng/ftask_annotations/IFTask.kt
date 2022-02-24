package com.rongcheng.ftask_annotations

/**
 * 实现接口的类会被收集(跨module)
 */
interface IFTask<T> {
    open fun beforeExecute() {

    }

    fun execute(): T

    open fun afterExecute(t: Any?) {

    }

    fun getTag(): String
}