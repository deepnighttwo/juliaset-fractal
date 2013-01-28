package com.deepnighttwo.gen.task;

import com.deepnighttwo.gen.imagegen.JuliaSetArgs;

public interface ITaskArg {
    JuliaSetArgs getArgsForGen();

    IGenPostTask getGenPostTask();

    void setGenPostTask(IGenPostTask genPostTask);

    void taskFinished();
}
