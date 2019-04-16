package com.compomics.jtraml.thread;

/**
 * Implemented by asynchronous tasks that can return current status while they are running.
 */
public interface Job extends Runnable {

    /**
     * Get current status.
     */
    String getStatus();
}

