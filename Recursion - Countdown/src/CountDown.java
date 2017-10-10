public class CountDown {
	public static void countDown(int nCount)
	{
	System.out.println(nCount);
	if (nCount > 0)
	    countDown(nCount-1);
	}

	public static void countUp(int nCount)
	{
	if (nCount > 0)
	    countUp(nCount-1);
	System.out.println(nCount );
	}

	public static void main(String[ ] args)
	{
	System.out.println("CountDown\n");
	countDown(10);

	System.out.println("\nCountUp\n");
	countUp(10);
	}

}