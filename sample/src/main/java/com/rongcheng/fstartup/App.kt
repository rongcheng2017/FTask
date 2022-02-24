package com.rongcheng.fstartup

import android.app.Application
import com.rongcheng.ftask_annotations.IFTask
import com.rongcheng.ftask_api.FTaskManager

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