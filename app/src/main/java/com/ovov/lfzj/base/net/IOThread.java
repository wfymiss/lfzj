package com.ovov.lfzj.base.net;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kaite on 2018/9/29.
 */

public class IOThread {
    private static ExecutorService singleThread;

    public static Executor getSingleThread() {
        if (singleThread == null) {
            singleThread = Executors.newSingleThreadExecutor();
        }
        return singleThread;
    }
}
