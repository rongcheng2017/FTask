package com.rongcheng.fstartup

import android.util.Log
import com.rongcheng.ftask_annotations.IFTask
import com.rongcheng.ftask_annotations.FTask

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