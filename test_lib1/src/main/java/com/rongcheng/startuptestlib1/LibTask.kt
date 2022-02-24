package com.rongcheng.startuptestlib1

import android.util.Log
import com.rongcheng.ftask_annotations.IFTask
import com.rongcheng.ftask_annotations.FTask

@FTask("lib1")
class LibTask : TempInterface {
    override fun execute() {
        Log.e("lib1", "Lib Task execute")
    }

    override fun getTag(): String {
        return "lib"
    }
}

@FTask("extend")
class LibExtends : TempClass() {
    override fun execute(): String {
        Log.e("extend", "extend Task execute")
        return "extend task"
    }

    override fun getTag(): String {
        return "extend"
    }

}

interface TempInterface : IFTask<Unit> {

}

abstract class TempClass : IFTask<String> {

}