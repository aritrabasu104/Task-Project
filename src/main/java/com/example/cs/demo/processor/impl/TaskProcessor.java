package com.example.cs.demo.processor.impl;

import com.example.cs.demo.model.Task;
import com.example.cs.demo.processor.Processor;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskProcessor implements Processor<Task,String> {

    @Autowired
    private Gson gson;

    @Override
    public Task process(String s){
        return gson.fromJson(s, Task.class);
    }
}
