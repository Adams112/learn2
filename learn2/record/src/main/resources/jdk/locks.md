# LockSupport
提供线程休眠/唤醒同步的基础工具，使用permit进行同步，unpark生产许可，park消费许可。许可类似于信号量，多次生产许可不会叠加。底层调用Usafe实现。  
1. park  
    当前线程进入休眠，如果  
    - 存在许可（如果在park之前许可就存在，此时则直接返回）
    - 中断
    - 无原因的返回（spuriously (that is, for no reason) returns）  
      
    park方法将返回，但不会说明返回的原因。  
2. parkNanos/parkUntil  
      与park返回条件类似，但是休眠时间达到也会返回。同样不会说明返回的原因。  
3. unpark  
   给对应线程生产许可。可以调用多次，但是许可不会叠加；可以在park之前生产许可，此时park方法立即返回。  
   
park/parkNanos/parkUntil都存在park(Object)重载方法，方便调试。  














-------------------------------------
lock规定
condition规定
AQS实现
  数据结构
  acquire
  acquireShared
  acquireInterruptibly
  acquireSharedInterruptibly
  condition
  
  