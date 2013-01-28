package com.deepnighttwo.gen.task;

import com.deepnighttwo.gen.imagegen.JuliaSetImage;

public interface IGenPostTask {

    void postTask(JuliaSetImage image);

    IGenPostTask getNext();

    void setNext(IGenPostTask next);
}
