

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
锁的获取和释放大部分都是相同的，主要区别在独占锁/共享锁、对异常的处理以及其他控制行为，例如确保tryAcquire抛异常的情况下删除锁的获取
#### 1.3.4.1 acquire
独占锁的获取流程，首先tryAcquire，tryAcquire是获取锁的逻辑，由子类实现。tryAcquire失败向队列中添加一个独占模式的Node；
然后acquireQueued在适当的时候休眠，被唤醒后继续获取锁，获取成功后acquireQueued返回，如果期间有中断，则设置中断位
```
    public final void acquire(int arg) {
        if (!tryAcquire(arg) &&
            acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
            selfInterrupt();
    }
```

独占锁共享锁的addWaiter的逻辑和共享锁的是一样的，都是添加一个node到sync队列里，acquireQueued在for循环里面不断尝试获取锁，流程如下
- 1、如果该节点的前面就是head，则尝试获取锁，获取成功了将自己设置为head，返回  
- 2、如果前面不是head，或者获取锁不成功，则判断当前是否可以进入休眠（休眠前需要确保有线程唤醒自己）  
- 3、如果可以休眠，则进入休眠，被唤醒后继续重复流程  
- 4、如果不能休眠，则重复流程  
- 5、如果任何形况下抛异常了，则取消获取锁  

```
    final boolean acquireQueued(final Node node, int arg) {
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return interrupted;
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                    parkAndCheckInterrupt())
                    interrupted = true;
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }
```

流程1里面，会判断前面是不是head，是为了保证队列是先入先出FIFO的，即使是非公平锁，进入队列了也会保证公平性。head可以理解为当前持有锁的线程，
但其实没有特别含义，第一个入sync队列的线程会初始化head，其他线程拿到锁会把自己设置为head，如果一个线程发现自己前面就是head，可能当前有别的线程持有锁，
也可能没有线程持有锁（前一个线程释放锁了，但自己还没入队）  
setHead方法就是普通方法，对队列的修改不涉及cas，因为能进入setHead方法的线程已经获取锁了，不需要cas  
```
    private void setHead(Node node) {
        head = node;
        node.thread = null;
        node.prev = null;
    }
```

线程进入休眠之前必须要确保有线程唤醒自己，流程2里面，shouldParkAfterFailedAcquire里判断前一个节点的waitStatus，有几种情况：
- 1、如果是SIGNAL，已经确定可以休眠  
- 2、如果大于0，CANCELLED，while循环清除掉所有CANCELLED节点（cancel时候一般会在队列中清除掉自己，特别情况才不会清除掉）  
- 3、如果是其他的，只可能是0或者PROPAGATE了，这个时候cas修改为SIGNAL  

2和3都会再次进入acquireQueued的，2循环2次，3循环一次。为什么要重试tryAcquire而不是cas为SIGNAL就可以休眠了？因为如果持有锁的线程在tryAcquire
之后、cas设置waitStatus之前释放了锁，释放锁之后并不会唤醒该线程，必须确保tryAcquire时waitStatus是SIGNAL，才可以休眠  
为什么2清除掉CANCELLED节点之后不直接cas设置为SIGNAL而直接返回false导致循环两遍？我也想不通  
```
    private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
        int ws = pred.waitStatus;
        if (ws == Node.SIGNAL)
            /*
             * This node has already set status asking a release
             * to signal it, so it can safely park.
             */
            return true;
        if (ws > 0) {
            /*
             * Predecessor was cancelled. Skip over predecessors and
             * indicate retry.
             */
            do {
                node.prev = pred = pred.prev;
            } while (pred.waitStatus > 0);
            pred.next = node;
        } else {
            /*
             * waitStatus must be 0 or PROPAGATE.  Indicate that we
             * need a signal, but don't park yet.  Caller will need to
             * retry to make sure it cannot acquire before parking.
             */
            compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
        }
        return false;
    }
```

parkAndCheckInterrupt进入休眠，休眠结束返回是否中断  

5如果获取锁失败了会cancelAcquire取消获取锁，除了清理sync队列之外，还要保证后面的节点一定能够被唤醒，如果不能够保证的话则唤醒后面的线程  
```
    // 用到了很多cas，失败了会怎么样？
    private void cancelAcquire(Node node) {
        // Ignore if node doesn't exist
        if (node == null)
            return;

        node.thread = null;

        // Skip cancelled predecessors
        // 首先while循环删除前面的CANCELLED节点
        Node pred = node.prev;
        while (pred.waitStatus > 0)
            node.prev = pred = pred.prev;

        // predNext is the apparent node to unsplice. CASes below will
        // fail if not, in which case, we lost race vs another cancel
        // or signal, so no further action is necessary.
        Node predNext = pred.next;

        // Can use unconditional write instead of CAS here.
        // After this atomic step, other Nodes can skip past us.
        // Before, we are free of interference from other threads.
        // 为什么不用cas？因为不管waitStatus被设置为什么，后面会确保后继节点一定能够被唤醒
        node.waitStatus = Node.CANCELLED;

        // If we are the tail, remove ourselves.
        // 如果自己已经是tail了，也把自己清理掉
        if (node == tail && compareAndSetTail(node, pred)) {
            compareAndSetNext(pred, predNext, null);
        } else {
            // If successor needs signal, try to set pred's next-link
            // so it will get one. Otherwise wake it up to propagate.
            // 这里的判断比较复杂，这里的条件是确保node.next节点一定能够被唤醒，首先pred不能是head，如果是head无法确定当前head是否持有锁
            // 其次pred的waitStatus要么本来就是SIGNAL、要么cas设置为SIGNAL
            // 最后node.thead不能是null，确保这个线程是存在的
            // 满足这些条件，cas把自己删除掉就能确保next能够被唤醒
            // 为什么pred.thread还有可能是null？
            int ws;
            if (pred != head &&
                ((ws = pred.waitStatus) == Node.SIGNAL ||
                 (ws <= 0 && compareAndSetWaitStatus(pred, ws, Node.SIGNAL))) &&
                pred.thread != null) {
                Node next = node.next;
                if (next != null && next.waitStatus <= 0)
                    compareAndSetNext(pred, predNext, next);
            } else {
                // 如果不能确保next能够被唤醒的话，就唤醒后续节点，让他自己来确保自己能够被唤醒
                unparkSuccessor(node);
            }

            node.next = node; // help GC
        }
    }
```

#### 1.3.4.2 acquireInterruptibly
逻辑和acquire几乎一样，区别在于对中断的处理。acquire方法在获取锁期间不响应中断，获取锁成功后设置中断标志位。acquireInterruptibly响应中断，
遇到中断抛出异常。  
```
    public final void acquireInterruptibly(int arg)
            throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        if (!tryAcquire(arg))
            doAcquireInterruptibly(arg);
    }

    private void doAcquireInterruptibly(int arg)
        throws InterruptedException {
        final Node node = addWaiter(Node.EXCLUSIVE);
        boolean failed = true;
        try {
            for (;;) {
                final Node p = node.predecessor();
                 if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return;
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                    parkAndCheckInterrupt())
                    throw new InterruptedException();
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }
```

#### 1.3.4.3 tryAcquireNanos
逻辑和acquire也是一样，区别在于休眠时设置时长，并且也会响应中断。其实就是使用LockSupport休眠时设置了休眠时长。这个方法用来实现了Lock里面的
tryLock(long, TimeUnit)方法。  
```
    public final boolean tryAcquireNanos(int arg, long nanosTimeout)
            throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        return tryAcquire(arg) ||
            doAcquireNanos(arg, nanosTimeout);
    }

    private boolean doAcquireNanos(int arg, long nanosTimeout)
            throws InterruptedException {
        if (nanosTimeout <= 0L)
            return false;
        final long deadline = System.nanoTime() + nanosTimeout;
        final Node node = addWaiter(Node.EXCLUSIVE);
        boolean failed = true;
        try {
            for (;;) {
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return true;
                }
                nanosTimeout = deadline - System.nanoTime();
                if (nanosTimeout <= 0L)
                    return false;
                if (shouldParkAfterFailedAcquire(p, node) &&
                    nanosTimeout > spinForTimeoutThreshold)
                    LockSupport.parkNanos(this, nanosTimeout);
                if (Thread.interrupted())
                    throw new InterruptedException();
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }
```

#### 1.3.4.4 release
首先释放锁tryRelease，然后判断锁是否完全释放了，如果完全释放了则唤醒后继线程  
```
    public final boolean release(int arg) {
        if (tryRelease(arg)) {
            Node h = head;
            if (h != null && h.waitStatus != 0)
                unparkSuccessor(h);
            return true;
        }
        return false;
    }
```

### 1.3.5 共享锁的获取和释放 
#### 1.3.5.1 tryAcquireShared
这是子类需要实现的方法，该方法返回有三种情况，涉及到共享锁获取成功后是否需要传播  
- 返回为负，获取锁失败  
- 返回为0， 获取锁成功，但下一次获取锁一定不成功  
- 返回为正，获取锁成功，下一次获取锁也会成功  

#### 1.3.5.2 acquireShared
acquireShared获取共享锁，不响应中断，如果有中断则获取结束之后设置标志位。首先尝试获取锁，获取不成功进入队列，在队列中获取锁成功后，根据
tryAcquireShared的返回值确定共享锁的获取是否需要传播，如果需要传播的话，则唤醒后继线程  
doAcquireShared首先添加一个SHARED模式的节点到队列里面，然后for循环开始获取锁。与独享锁获取类似，当前任节点是head时才会进行获取锁流程，
获取成功后，setHeadAndPropagate方法将会唤醒后面的获取共享锁的线程
```
    public final void acquireShared(int arg) {
        if (tryAcquireShared(arg) < 0)
            doAcquireShared(arg);
    }

    private void doAcquireShared(int arg) {
        final Node node = addWaiter(Node.SHARED);
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
                final Node p = node.predecessor();
                if (p == head) {
                    int r = tryAcquireShared(arg);
                    if (r >= 0) {
                        setHeadAndPropagate(node, r);
                        p.next = null; // help GC
                        if (interrupted)
                            selfInterrupt();
                        failed = false;
                        return;
                    }
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                    parkAndCheckInterrupt())
                    interrupted = true;
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }
```

#### 1.3.5.3 acquireSharedInterruptibly

#### 1.3.5.4 tryAcquireSharedNanos

#### 1.3.5.5 releaseShared


### 1.3.6 await/signal机制  
#### 1.3.6.1 await()

#### 1.3.6.2 await(long time, TimeUnit unit)

#### 1.3.6.3 awaitNanos(long nanosTimeout)

#### 1.3.6.4 awaitUntil(Date deadline)

#### 1.3.6.5 awaitUninterruptibly()

#### 1.3.6.6 signal()

#### 1.3.6.7 signalAll()

## 1.4 ReentrantLock
## 1.5 ReentrantReadWriteLock
