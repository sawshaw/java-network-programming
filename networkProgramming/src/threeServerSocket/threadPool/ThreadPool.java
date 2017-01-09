package threeServerSocket.threadPool;

import java.util.LinkedList;

/**
 * @author mercy
 * 服务端线程池
 */
public class ThreadPool extends ThreadGroup{
	private boolean isClosed=false;//线程池是否关闭
	private LinkedList<Runnable> workQuene;//工作队列
	private static int threadPoolID;//线程池ID
	private int threadID;//工作线程ID

	public ThreadPool(int poolSize) {//指定线程池工作线程的数据
		super("ThreadPool-"+threadPoolID++);
		setDaemon(true);//创建守护线程(守护线程用于服务用户)
		workQuene=new LinkedList<Runnable>();//创建工作队列
		for(int i=0;i<poolSize;i++){
			new WorkThread().start();
		}
	}
	
	/**
	 * @author mercy
	 * 内部类工作线程
	 */
	private class WorkThread extends Thread{
		public WorkThread(){
			super(ThreadPool.this,"workThread-"+threadID++);
		}

		@Override
		public void run() {
			while(!isInterrupted()){//判断线程是否中断
				Runnable task=null;
				try{
					task=getTask();//取出任务	
				}catch(InterruptedException e){
				}
				//如果getTask返回null或线程执行getTask方法被中断，则结束此线程
				if(task==null){
					return;
				}
				try{
					task.run();
				}catch(Throwable t){
					t.printStackTrace();
				}
			}
		}

	}
	
	/**
	 * 
	 * @author mercy
	 * 关闭线程池
	 */
	protected synchronized void close(){
		if(!isClosed){
			isClosed=true;
			workQuene.clear();//清空队列
			interrupt();//中断线程
		}
	}
	
	/**
	 * 
	 * @author mercy
	 * 确保在关闭线程池之前，等待工作线程把所有任务执行完(作用也是关闭线程池)
	 */
	public void join(){
		synchronized (this) {
			isClosed=true;
			notifyAll();//唤醒还在getTask()方法中等待任务的工作线程
		}
		Thread[] threads=new Thread[activeCount()];//获取工作线程中活的线程
		int count=enumerate(threads);
		for(int i=0;i<count;i++){
			try {
				threads[i].join();//等待线程死去
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @param task
	 * @author mercy
	 * 往队列加任务
	 */
	public synchronized void execute(Runnable task){
		if(isClosed){
			throw new IllegalStateException();//线程池
		}
		if(task!=null){
			workQuene.add(task);
			notify();//唤醒等待任务的工作线程
		}
	}
	/**
	 * @return
	 * @throws InterruptedException
	 * @author mercy
	 * 从工作队列中取出任务
	 */
	protected synchronized Runnable  getTask() throws InterruptedException{
		while(workQuene.size()==0){
			if(isClosed){
				return null;
			}
			wait();
		}
		return workQuene.removeFirst();
	}
	

}
