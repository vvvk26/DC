import java.io.*;
import java.net.*;
import java.util.*;  

public class Process
{
	public static Pdata pdata;
	
	public static void main(String args[]){

		pdata = new Pdata();

		System.out.println("*************************************************************************************");
		System.out.println("Process  " + args[0] + "  of  " +args[1] + " processess ");
		System.out.println("*************************************************************************************");

		String node_str = new String(args[0]);

		//System.out.println("enteresd process");

		pdata.pid = Integer.parseInt(args[0]);
		String total_str = new String(args[1]);
		pdata.total_num = Integer.parseInt(args[1]);

		ArrayList<ConnectionThread> threads = new ArrayList<ConnectionThread>(pdata.total_num+1);					

		for(int i=0;i<=pdata.total_num;i++)
		{
			threads.add(new ConnectionThread(i,pdata));
			threads.get(i).start();	
		}

		pdata.rn = new int[pdata.total_num+1];

		pdata.ln = new int[pdata.total_num+1];

		pdata.want_to_use=false;

		for(int i=0;i<=pdata.total_num;i++)
			{
				pdata.rn[i]=0;
				pdata.ln[i]=0;
			}

		pdata.queue = new QueueArray();

		Scanner sc = new Scanner(System.in);
			
		// Asking the user if it wants to enter critical section
		while(true)
		{		
			//System.out.println("polling and enter 1 if you want to enter critical section");
			int input; //= sc.nextInt();
			// Enter 1 if you want to enter critical section
			
            try
			{
				Thread.sleep(5000);
				System.out.println("Testing at ..." + new Date());
                
			} catch (InterruptedException e) {
			e.printStackTrace();
			}
			input = 1;
			if(input==1){
				pdata.rn[pdata.pid]++;
				// Send request to all nodes to get pdata

				for(int i=1;i<=pdata.total_num;i++)
				{
					if(i!=pdata.pid)
					{
						threads.get(i).request(pdata.rn[pdata.pid]);								
					}					
				}
			}
		}				
	}
}