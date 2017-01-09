package threeServerSocket.threadPool;

public class ThreadPoolTest {
	private static Runnable createTask(final int taskID){
		return new Runnable(){
			@Override
			public void run() {
				System.out.println("Task"+taskID+"start");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Task"+taskID+"end");
			}
		};
		
	}
	public static void main(String[] args) {
		int tasks=50;
		ThreadPool threadPool=new ThreadPool(30);
		//运行任务
		for(int i=0;i<tasks;i++){
			threadPool.execute(createTask(i));
		}
		threadPool.join();//等待工作线程完成所有任务关闭线程池
	}

}
