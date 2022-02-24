package com.rongcheng.startuptestlib1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rongcheng.ftask_annotations.IFTask;
import com.rongcheng.ftask_annotations.FTask;

@FTask(name = "java")
public class JavaTask implements IFTask<String> {
    @Override
    public String execute() {
        return " java task";
    }

    @NonNull
    @Override
    public String getTag() {
        return "java";
    }

    @Override
    public void beforeExecute() {
    }

    @Override
    public void afterExecute(@Nullable Object t) {
    }
}
