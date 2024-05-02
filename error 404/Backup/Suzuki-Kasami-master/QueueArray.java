public class QueueArray {

    private int[] arr;

    private int total, first, next;

    public QueueArray()
    {
        arr = new int[100];
        total=0;
        first=0;
        next=0;
    }
    
    public boolean isEmpty()
    {	
    	if (total==0)
    		return true;
    	else
    		return false;
    }


    public QueueArray enqueue(int ele)
    {
        arr[next++] = ele;
        if (next == arr.length) next = 0;
        total++;
        return this;
    }

    public int dequeue()
    {
        if (total == 0) throw new java.util.NoSuchElementException();
        int ele = arr[first];
        arr[first] = -1;
        if (++first == arr.length) first = 0;
        total--;
        return ele;
    }
    
    // for assuming we wont run out of queue
    public boolean checkinqueue(int num)
    {
    	for(int i=first;i<next;i++)
    	{
    			if(num==arr[i])
    			{
    				return true;
    			}
    	}
    	return false;
    }

	// Serialize to pass as string at the port
    @Override
    public String toString()
    {
    	String str;
    	str=Integer.toString(total);
    	for(int i=first;i<next;i++)
    	{
    		str=str + "|" + Integer.toString(arr[i]);
    	}
    	 return str;
    }
    
    // Deserialize the string 
    public void fromString(String str)
    {
    	String[] strr = str.split("|"); 
    	if(strr[0].equals(""))
    	{
    		strr[0]=str;
    	}
    	total=Integer.parseInt(strr[0].toString());
    	for(int i=1;i<strr.length;i++)
    	{
    		arr[i-1]=Integer.parseInt(strr[i]);
    	}
    	this.first=0;
    	this.next=total;
    }

}