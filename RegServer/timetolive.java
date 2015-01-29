public class timetolive implements Runnable {

	public timetolive()
	{
		
	}
	public void run()
	{	
		RegServer r = new RegServer();
		Long diff = null;
		Long ttl = null;
		while(true)
		{
			long current_time = System.currentTimeMillis( );
			for(int i=0; i<r.l8.size(); i++ )
			{
				diff = current_time-r.l8.get(i);
				r.l8.remove(i);
				r.l8.add(i, current_time);
				ttl = (Long)(r.l4.get(i));
				ttl=ttl*1000;
				ttl=(ttl-diff)/1000;
				if (ttl<=0)
				{
					ttl=(long) 0;
					r.l3.remove(i);
					r.l3.add(i, false);
				}
				r.l4.remove(i);
				r.l4.add(i, ttl);
				
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
