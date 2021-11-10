package com.example.cs.demo.processor;

public interface Processor<T,P> {
    T process(P p);
}
