package com.ang.acb.nasaapp.util;

import com.ang.acb.nasaapp.utils.AppExecutors;

import java.util.concurrent.Executor;

public class InstantAppExecutors extends AppExecutors {

    private static Executor instant = Runnable::run;

    public InstantAppExecutors() {
        super(instant, instant, instant);
    }
}
