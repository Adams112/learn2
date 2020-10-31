

# locks
## 线程同步工具/方式
### LockSupport
线程休眠/唤醒同步工具，底层通过Unsafe实现。使用permit进行同步，unpark生产许可，park消费许可。许可类似于信号量，多次生产许可不会叠加。  
- park  
当前线程进入休眠，如果满足条件之一，park方法将返回，但不会说明返回的原因。  
  - 存在许可（如果在park之前许可就存在，此时则直接返回）
  - 中断
  - 无原因的返回（spuriously (that is, for no reason) returns）  
    
- parkNanos/parkUntil  
与park返回条件类似，但是休眠时间达到也会返回。同样不会说明返回的原因。  
- unpark  
给对应线程生产许可。可以调用多次，但是许可不会叠加；可以在park之前生产许可，此时park方法立即返回。  

park/parkNanos/parkUntil都存在park(Object)重载方法，方便调试。  
### 中断
中断也可以用来在线程之间进行同步。一般情况，中断只是Thread的标志位，中断之后程序的行为完全取决于代码逻辑。主要是三个方法：  
- 实例方法 interrupt()  
  - 线程阻塞在wait/join/sleep方法时，标志位清除，抛出异常  
  - 线程阻塞在InterruptibleChannel类的I/O操作时，标志位设置，抛出异常  
  - 线程阻塞在Selector时，标志位设置，立即返回  
  - 其他情况，仅仅设置标志位，程序行为由被中断线程决定  
  
- 实例方法isInterrupted()  
返回线程的中断标志位，不清除标志位

- 静态方法interrupted()  
返回当前线程的标志位，清除中断  

### wait/notify  
Object类的实例方法。  
- wait()/wait(long timeout)/wait(long timeout, int nanos)  
wait的三个重载方法。线程释放持有的锁，进入等待，wait过程中有中断抛异常  

- notify()/notifyAll()  
唤醒wait中的线程。不确定会唤醒哪一个。唤醒之后需要重新获取锁  

### condition
- await()/awaitNanos(long nanosTimeout)/await(long time, TimeUnit unit)  
线程释放持有的锁，进入等待，wait过程中有中断抛异常。与wait/notify相同  
- awaitUntil(Date deadline)  
线程释放持有的锁，等待直到截止时间，wait过程中有中断抛异常  
- awaitUninterruptibly()  
线程释放持有的锁，一直等待，不响应中断  
- signal()/signalAll()  
唤醒在该condition上等待的线程，类似于notify，但可以自定义实现signal的逻辑  

## Lock接口
lock接口定义了锁的获取、释放、实例化condition。  
### 获取锁
- lock()  
阻塞直到获取锁，不响应中断  

- lockInterruptibly()  
阻塞获取锁，如果进入该方法前，中断标志已经被设置，或者线程休眠中被中断，则抛出异常  

- tryLock()  
获取锁，不管是否获取到锁都立即返回  

- tryLock(long time, TimeUnit timeUnit)  
带时间的获取锁  

### 解锁
- unlock()  
释放锁  

### condition
- newCondition()  
实例化condition对象  

### 与synchronized的区别
- lock支持自定义获取锁的逻辑。存在竞争时，synchronized只能随机选择一个线程获取锁，lock可以自定义该逻辑（共享锁/独占锁，公平锁/非公平锁）  
- lock在获取锁时可以响应或不响应中断，synchronized不响应中断。  
- lock可以实例化多个condition对象，synchronized只能是锁本身一个。condition也支持更多的同步机制  
- lock支持更灵活的加锁/解锁方式，synchronized只能按加锁的逆序解锁  

## Lock实现
### AbstractQueuedSynchronizer




数据结构，Node，队列

### ReentrantLock
### ReentrantReadWriteLock
### StampedLock

