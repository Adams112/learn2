package com.jzq.jdk.juc;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class lockTest {
    public static void main(String[] args) {
        func1();
    }

    public static void func1() {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock(false);
        new Thread(new Worker(1000, lock.readLock())).start();
        new Thread(new Worker(2000, lock.readLock())).start();
        new Thread(new Worker(3000, lock.writeLock())).start();
        new Thread(new Worker(4000, lock.readLock())).start();

        System.out.println(1);
    }

    static class Worker implements Runnable {
        int time;
        Lock lock;

        Worker(int time, Lock lock) {
            this.time = time;
            this.lock = lock;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread() + " started");
            lock.lock();
            System.out.println(Thread.currentThread() + " stopped");
            lock.unlock();
        }
    }
}
