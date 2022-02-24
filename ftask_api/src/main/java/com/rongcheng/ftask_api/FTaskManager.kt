package com.rongcheng.ftask_api

import android.app.Application
import android.util.Log
import com.rongcheng.ftask_annotations.FTaskInfo

object FTaskManager {

    fun start(context: Application, func: (FTasks: List<FTaskInfo>) -> Unit) {
        val fTaskInfo: List<FTaskInfo> = getTasks()
        if (fTaskInfo.isEmpty()) {
            Log.w(
                "FT",
                "Task is null, Are you sure? Do you used @FTask and implement IFTask"
            )
            return
        }
        func(fTaskInfo)
    }


    private fun getTasks(): List<FTaskInfo> {

        return FTaskRegister().FTaskList
    }
}