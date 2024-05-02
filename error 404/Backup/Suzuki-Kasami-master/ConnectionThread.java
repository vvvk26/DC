import java.io.*;
import java.net.*;
import java.util.*;

public class ConnectionThread extends Thread{
	public Pdata pdata;
	public int node_connected_num;
	public ServerSocket serverSocket = null;
	public Socket socket = null;
	public DataOutputStream dout;
	public DataInputStream dis;
    		    
	public ConnectionThread(int i, Pdata pdata){
		// keeping pdata initially with node 1
		this.pdata=pdata;
		this.node_connected_num=i;
		if(pdata.pid==1)
		{
			pdata.has_token=true;
		}
		else
		{
			pdata.has_token=false;
		}
	}
	
	public void run() {
		try
		{
			// if server
			int port;
			if(node_connected_num>pdata.pid)
			{
                System.out.println("Server side "+ node_connected_num +">" + pdata.pid );
				// assigning port number for every connection
				port=pdata.pid*2000+node_connected_num;

        		try {
                    System.out.println("********************************************");
        			System.out.println("port number for connecting Server = "+port);
					serverSocket = new ServerSocket(port);
					socket = serverSocket.accept();
                    System.out.println("connection accepted = "+port);
					
					dout=new DataOutputStream(socket.getOutputStream());
					dis=new DataInputStream(socket.getInputStream());

					while(true)
						{
                            System.out.println("********************************************");
							String data=dis.readUTF();
							System.out.println("data received "+data);
							// data has request for token
					        System.out.println("********************************************");
							if(data.indexOf("+")!=-1)
								{
                                    System.out.println("Token Request received");					
									// return him token                                					
									String[] parts = data.split("\\+");
                                    System.out.println("data split at + to two parts ");
					      			System.out.println(" part 0 = pid = " + parts[0] + " part 1 = rn =  " + parts[1]);
									int part1 = Integer.parseInt(parts[0]); // pid
									int part2 = Integer.parseInt(parts[1]); // rn
                                
									pdata.rn[part1]=Math.max(pdata.rn[part1],part2);
                                System.out.println("process data rn " + pdata.rn[part1]);

									if(pdata.has_token==true && pdata.rn[part1]==pdata.ln[part1]+1)
										{
                                        System.out.println("process has token");
											sendpdata(part1);System.out.println("sending pdata of"+ part1);
										}
								}
							else
								{
                                    System.out.println("Request array Rn received ");
									String data2=dis.readUTF();
									System.out.println("received  "+ data2);
									pdata.has_token=true;
									String arr = data;
									String[] items = arr.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");
									System.out.println("replaced data");
                                    int[] results = new int[items.length];
                                    System.out.println("split into items");
                                    System.out.print("Ln is [");
									for (int i = 1; i < items.length; i++) {
										try {
											pdata.ln[i] = Integer.parseInt(items[i]);
                                            System.out.print( items[i] +" ");
											}
											catch (NumberFormatException e) {
												System.out.println("NumberFormatException");
                                                e.printStackTrace();
                                            };
									// take data of queue
										}
                                    System.out.println("]");
									pdata.queue.fromString(data2);
                                    System.out.println("take data from queue");
									if(pdata.want_to_use==true)
										{
                                         System.out.println("process want to use = true");
											request(pdata.rn[pdata.pid]);
                                         System.out.println("request complete");
											}
								}
                                System.out.println("********************************************");
						}
				}

				catch (Exception e) {
					System.out.println("exception 1 here in if loop");
					e.printStackTrace();
					}
			}
			
			else if (node_connected_num < pdata.pid && node_connected_num!=0)
			{
                System.out.println("********************************************");                
                System.out.println("Enters client "+ node_connected_num +" < " + pdata.pid +" && "+ node_connected_num + "!=0");
				port=pdata.pid+node_connected_num*2000;
				try{
					System.out.println("port number for connecting = "+port);
					socket=new Socket(InetAddress.getByName("127.0.0.1"),port);
                    System.out.println("socket initialized to local host");
					dout=new DataOutputStream(socket.getOutputStream());
					dis=new DataInputStream(socket.getInputStream());
					while(true)
						{
							String data=dis.readUTF();
							System.out.println("data received " + data);
							// data has request for token
                            System.out.println("data has request for token");
                        
							if(data.indexOf("+")!=-1)
								{
                                    System.out.println(" Token Request received"); 
									// return him token
									String[] parts = data.split("\\+");
                                    System.out.println("Splitiing into 2 parts");
									System.out.println(parts[0] + "   " + parts[1]);
									int part1 = Integer.parseInt(parts[0]); // pid
									int part2 = Integer.parseInt(parts[1]); // rn
									pdata.rn[part1]=Math.max(pdata.rn[part1],part2);
                                    System.out.println("Calculating max of rn[part1], part2");
									if(pdata.has_token==true && pdata.rn[part1]==pdata.ln[part1]+1)
										{
                                            System.out.println("process has token");
											sendpdata(part1);
                                            System.out.println("Sending "+ part1);
                                        }
                                 }
                             
                             else
                             {
                                 System.out.println("Request array Rn received");
                                 String data2=dis.readUTF();
                                 
								 System.out.println("received  "+data2);
								 this.pdata.has_token=true;
                                 System.out.println("prcess has token =true");
								 String arr = data;
								 String[] items = arr.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");
										int[] results = new int[items.length];
                                 System.out.println("ln array");
                                 System.out.print("Ln is [");
                                 for (int i = 1; i < items.length; i++) 
                                 {
                                     try {
                                         pdata.ln[i] = Integer.parseInt(items[i]);
                                         System.out.print( items[i] + ",");
									}
								    catch (NumberFormatException e) 
                                    {
                                        System.out.println(" Number Format Exception");
                                        e.printStackTrace();
                                        //NOTE: write something here if you need to recover from formatting errors
									};
									// take data of queue
								 }
                                 
								 System.out.println("]");
                                 pdata.queue.fromString(data2);
										if(pdata.want_to_use==true)
										{
                                            System.out.println("if proces want to use");
												request(pdata.rn[pdata.pid]);
                                            System.out.println("request complete");
                                        }
                             }
                        System.out.println("********************************************");
                    }
                }
                catch(Exception e){
                	System.out.println("exception2 here in else if");
					System.out.println(e);				
				}
            }
        }
        catch(Exception e){
                    System.out.println("Exception in total ");   
					System.out.println(e);				
				}
 }
    
    
    public void request(int num)
    {
        System.out.println("**************************************************");
        System.out.println("entered request with "+num);
    	if(pdata.has_token)
    	{
                System.out.println("process has token ");
    			System.out.println("Executing CS for "+ pdata.pid);
    			pdata.want_to_use=false;
                System.out.println("making want to use as false");
    			// updating ln of pdata after execution
    			pdata.ln[pdata.pid]=pdata.rn[pdata.pid];
                System.out.println("updating ln of pdata ");
    			// for every process k not in the pdata queue Q, it appends k to Q if RN_{i}[k]=LN[k]+1. 
    			// This indicates that process k has an outstanding request
    			for (int i=1;i<=pdata.total_num;i++)
    			{
                    System.out.println("for every process");
    				if(pdata.queue.checkinqueue(i)==false && pdata.rn[i]==pdata.ln[i]+1)
    				{
                        System.out.println("if not in queue and rn[i] = ln[i] +1");
    					System.out.println("Adding to queue "+ i);
    					pdata.queue.enqueue(i);
    				}
    			}
    			int number;
    			if(pdata.queue.isEmpty()==false)
    			{
                    System.out.println("if Queue is not empty");
    				number=pdata.queue.dequeue();
                    System.out.println("getting number ="+ number);
                    System.out.println("sending process data of number");
    				sendpdata(number);
    			}
    	}

        else
    	{
    		try{
                System.out.println("process has no token");
    			// request format is pid+rn[pid]
    			System.out.println("writing request for pdata from "+pdata.pid);
    			dout.writeUTF(pdata.pid+"+"+num);
                
    		} 
            catch (IOException e) {    			
					System.out.println("exception3 here in request if no token");
            	e.printStackTrace();
      		}
    	}
        System.out.println("*********************************************");
    }
    
    public void sendpdata(int num)
    {
        System.out.println("*********************************************");
        System.out.println("entered send pdata witn "+ num);
        
    		if(num==node_connected_num)
			{
                System.out.println("if " + num + " = node_connected_num");
				try{
				// send pdata to it
				System.out.println("Sending pdata to "+ num);
				dout.writeUTF(Arrays.toString(pdata.ln));
				dout.writeUTF(pdata.queue.toString());
				pdata.has_token=false;
                    System.out.println("making has token false");
				pdata.want_to_use=true;
                    System.out.println("want to use true");
                    
                } catch (IOException e) {
                    System.out.println("exception4 here in sendpdata");
                    e.printStackTrace();
                }
            }
        System.out.println("**********************************************");
    }
}