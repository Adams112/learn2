package com.jzq.jdk;

import java.util.concurrent.locks.LockSupport;

public class LockTest {
    public static void main(String[] args) {
        park();
    }

    public static void func() {
        final Object lock = new Object();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    System.out.println(Thread.currentThread().getName() + "locked");
                    System.out.println(Thread.interrupted());
                }
            }
        };
        Thread t1 = new Thread(r, "t1");
        synchronized (lock) {
            System.out.println(Thread.currentThread().getName() + "locked");
            t1.start();
            t1.interrupt();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "exit");
        }
    }


    public static void park() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().interrupt();
                System.out.println(Thread.currentThread().getName() + "interrupted");
                System.out.println(Thread.currentThread().getName() + Thread.currentThread().isInterrupted());
                LockSupport.park();
                System.out.println(Thread.currentThread().getName() + "interrupted");
            }
        });


        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
