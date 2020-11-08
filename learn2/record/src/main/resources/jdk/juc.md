

# 1 locks
## 1.1 线程同步工具/方式
### 1.1.1 LockSupport
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
### 1.1.2 中断
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

### 1.1.3 wait/notify  
Object类的实例方法。  
- wait()/wait(long timeout)/wait(long timeout, int nanos)  
wait的三个重载方法。线程释放持有的锁，进入等待，wait过程中有中断抛异常  

- notify()/notifyAll()  
唤醒wait中的线程。不确定会唤醒哪一个。唤醒之后需要重新获取锁  

### 1.1.4 condition
- await()/awaitNanos(long nanosTimeout)/await(long time, TimeUnit unit)  
线程释放持有的锁，进入等待，wait过程中有中断抛异常。与wait/notify相同  
- awaitUntil(Date deadline)  
线程释放持有的锁，等待直到截止时间，wait过程中有中断抛异常  
- awaitUninterruptibly()  
线程释放持有的锁，一直等待，不响应中断  
- signal()/signalAll()  
唤醒在该condition上等待的线程，类似于notify，但可以自定义实现signal的逻辑  

## 1.2 Lock接口
lock接口定义了锁的获取、释放、实例化condition等方法。  
### 1.2.1 获取锁
- lock()  
阻塞直到获取锁，不响应中断  

- lockInterruptibly()  
阻塞获取锁，如果进入该方法前，中断标志已经被设置，或者线程休眠中被中断，则抛出异常  

- tryLock()  
获取锁，不管是否获取到锁都立即返回  

- tryLock(long time, TimeUnit timeUnit)  
带时间的获取锁  

### 1.2.2 解锁
- unlock()  
释放锁  

### 1.2.3 condition
- newCondition()  
实例化condition对象  

### 1.2.4 与synchronized的区别
- lock支持自定义获取锁的逻辑。存在竞争时，synchronized只能随机选择一个线程获取锁，lock可以自定义该逻辑（共享锁/独占锁，公平锁/非公平锁）  
- lock在获取锁时可以响应或不响应中断，synchronized不响应中断。  
- lock可以实例化多个condition对象，synchronized只能是锁本身一个。condition也支持更多的同步机制  
- lock支持更灵活的加锁/解锁方式，synchronized只能按加锁的逆序解锁  

## 1.3 AbstractQueuedSynchronizer
AQS支撑Lock实现lock/unlock方法，以及await/signal机制  
lock加锁的流程是先竞争获取锁，获取不到（当前有其他线程持有锁，竞争失败）进入AQS等待队列，在等待队列中的线程会休眠，
等待占有锁的线程释放后唤醒自己  
unlock解锁流程是先释放锁，如果锁完全释放了，会唤醒等待队列中的第一个线程，线程被唤醒后去获取锁  
await流程是先将自己加入该condition的等待队列，然后完全释放持有的锁，进入休眠，等待signal  
signal的流程是遍历该condition的等待队列，唤醒等待线程  
线程只有在等待锁的情况下才会进入在队列中，如果lock直接获取到锁了，并不会进入队列  

### 1.3.1 AQS主要方法
AQS实现了共享锁与独占锁，在锁的获取/释放上，有八个重要方法：其中两个释放锁的方法，分别用来释放共享锁和独占锁；
六个锁的获取方法，共享锁和独占锁的获取都包括不响应中断、响应中断、带时间限制三个版本
- acquire(int arg)  
    获取独占锁，获取不到锁线程休眠，获取成功方法返回，不响应中断。获取锁过程中如果有中断，获取锁成功后设置标志位  

- acquireInterruptibly(int arg)  
    获取独占锁，获取不到锁线程休眠，获取成功方法返回。获取过程中如果有中断抛异常  
    
- tryAcquireNanos(int arg, long nanosTimeout)
    获取独占锁，获取不到锁线程休眠，获取成功方法返回true。获取过程中如果有中断抛异常，时间限制到了还未获取到锁返回false  

- acquireShared(int arg)  
    获取共享锁，获取不到锁线程休眠，获取成功方法返回，不响应中断。获取锁过程中如果有中断，获取锁成功后设置标志位  

- acquireSharedInterruptibly(int arg)  
    获取共享锁，获取不到锁线程休眠，获取成功方法返回。获取锁过程中如果有中断抛异常  

- tryAcquireSharedNanos(int arg, long nanosTimeout)
    获取共享锁，获取不到锁线程休眠，获取成功方法返回true。获取过程中如果有中断抛异常，时间限制到了还未获取到锁返回false  
    
- release(int arg)  
    释放独占锁，锁完全释放后唤醒队列中第一个等待的线程  

- releaseShared(int arg)  
    释放共享锁，锁完全释放后唤醒队列中第一个等待线程  

ConditionObject中await/notify机制一共有七个重要方法，5个await方法和signal、signalAll方法
- awaitUninterruptibly()
    不响应中断一直等待，直到被其他线程signal。如果等待过程中被中断，则signal之后设置中断标志  

除了awaitUninterruptibly之外，其他的await遇到中断都会处理。处理分两种情况，如果中断是在被signal之前，则抛异常；如果中断是signal之后，则设置标志位  
- await()
    等待signal  
 
- await(long time, TimeUnit unit)
    相对时间的等待

- awaitNanos(long nanosTimeout)
    相对时间的等待

- awaitUntil(Date deadline)
    绝对时间等待

- signal()
    signal condition队列中第一个未被cancel的线程

- signalAll()
    signal所有未被cancel的线程
    
### 1.3.2 数据结构
AQS中有两个重要队列：1 sync队列，2 condition队列。sync队列是一个双链表，保存等待锁的线程；condition队列是单链表，保存等待condition的线程。
队列的节点Node表示等待锁或Condition的线程。Node节点具有独占和共享两种模式，表示该线程获取的是独占锁还是共享锁；以及五种状态，表示等待状态。  
- AQS  
    head/tail：由双向链表实现的队列  
    state：记录锁的同步状态，state具体含义由实现的锁决定  
    Node：表示等待锁或Condition的线程  
    ConditonObject：condition  
- Node  
    Node实例用来表示一个等待锁或Condition的线程。有prev/next、nextWaiter、waitStatus、thread几个属性  
    - prev/next  
        双向链表  
    - thread  
        该node节点代表的线程  
    - waitStatus  
        该节点的等待状态。共5个状态：CANCELLED=1、SIGNAL=-1、CONDITION=-2，PROPAGATE=-3，其他=0。sync队列中不可能存在CONDITION节点，
        condition队列只存在CANCELLED、CONDITION两种节点    
        0。初始状态  
        CANCELLED。该线程已经取消等待的锁或者Condition，cancel的原因可能是超时或者中断  
        SIGNAL。表示该线程释放锁或者取消时，需要唤醒队列里的线程。当一个线程进入sync队列后，只有确保会有其他线程唤醒自己，该线程才会block    
        Condition。该线程在condition队列中，只有被signal之后，才会回到sync队列  
        PROPAGATE。队列中线程获取到共享锁时，用来同步线程唤醒后面获取共享锁的线程  
    - nextWaiter
        两个作用。一个特定值SHARED = new Node()表示线程为共享模式；其他值表示等待同一个condition链表的下一个元素  
    
    Node有三个构造方法  
    ```
        Node() {    // Used to establish initial head or SHARED marker
        }

        Node(Thread thread, Node mode) {     // Used by addWaiter
            this.nextWaiter = mode;
            this.thread = thread;
        }

        Node(Thread thread, int waitStatus) { // Used by Condition
            this.waitStatus = waitStatus;
            this.thread = thread;
        }
    ```
    Node()用来创建head节点  
    Node(Thread thread, Node mode)只被addWaiter调用。addWaiter会将独占或者共享模式的node添加到sync队列  
    Node(Thread thread, int waitStatus)只在添加到condition队列时调用，waitStatus只会是Node.CONDITION  
    
- sync队列入队  
    线程获取独占锁或共享锁tryAcquire不成功时都会通过addWaiter进入sync队列，因此sync队列中node的模式包括EXLUSIVE和SHARED两种。
    addWaiter假定队列已经初始化了进行快速入队，快速入队失败再使用enq方法入队。快速入队与enq的差别在于，enq需要初始化head和tail   
    ```
        private Node addWaiter(Node mode) {
            Node node = new Node(Thread.currentThread(), mode);
            // Try the fast path of enq; backup to full enq on failure
            Node pred = tail;
            if (pred != null) {
                node.prev = pred;
                if (compareAndSetTail(pred, node)) {
                    pred.next = node;
                    return node;
                }
            }
            enq(node);
            return node;
        }
    ```
    enq  
    - 若未初始化head/tail，初始化head/tail  
    - 1 先将node.prev = tail， tail/t <= node  
    - 2 然后cas替换tail，t <= tail/node  
    - 3 最后t指向tail，t <=> tail/node  
    ```
        private Node enq(final Node node) {
            for (;;) {
                Node t = tail;
                if (t == null) { // Must initialize
                    if (compareAndSetHead(new Node()))
                        tail = head;
                } else {
                    node.prev = t;
                    if (compareAndSetTail(t, node)) {
                        t.next = node;
                        return t;
                    }
                }
            }
        }
    ```
    由于这样的入队方式，当发现t.next = null时，不代表后面没有node了，其实只要enq的步骤2执行完，即代表node已经入队了。所以AQS对sync队列
    进行遍历时，当发现t.next = null时，会从tail反向遍历，例如unparkSuccessor方法  
    ```
        Node s = node.next;
        if (s == null || s.waitStatus > 0) {
            s = null;
            for (Node t = tail; t != null && t != node; t = t.prev)
                if (t.waitStatus <= 0)
                    s = t;
        }
    ```

- ConditionObject  
    firstWaiter和lastWaiter保存condition队列的头和尾  
    THROW_IE/REINTERRUPT表示如何处理异常

- condition队列入队  
    condition队列是一个单链表，头尾分别是firstWaiter、lastWaiter。入队首先清除队列尾部cancel了的节点，然后新建node加入到链表。
    这里没有使用cas，因为只有独占锁才有condition，在进入condition队列时还持有锁，保证了此时只有一个线程能进入到该方法，不需要cas
    ```
        private Node addConditionWaiter() {
            Node t = lastWaiter;
            // If lastWaiter is cancelled, clean out.
            if (t != null && t.waitStatus != Node.CONDITION) {
                unlinkCancelledWaiters();
                t = lastWaiter;
            }
            Node node = new Node(Thread.currentThread(), Node.CONDITION);
            if (t == null)
                firstWaiter = node;
            else
                t.nextWaiter = node;
            lastWaiter = node;
            return node;
        }
    ```

### 1.3.4 独占锁的获取和释放
独占锁的获取流程，首先tryAcquire，tryAcquire失败向队列中添加一个独占模式的Node，然后acquireQueued在适当的时候休眠，被唤醒后继续获取锁，
获取到锁后判断获取锁期间是否有中断
```
    public final void acquire(int arg) {
        if (!tryAcquire(arg) &&
            acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
            selfInterrupt();
    }
```


### 1.3.5 共享锁的获取和释放  

### 1.3.6 await/signal机制  


## 1.4 ReentrantLock
## 1.5 ReentrantReadWriteLock
