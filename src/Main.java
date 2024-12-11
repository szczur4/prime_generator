import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main{
	private record SieveTask(boolean[]isPrime,long start,long end,long prime)implements Runnable{
		@Override public void run(){
			long firstMultiple=Math.max(prime*prime,start+(prime-start%prime)%prime);
			for(long i=firstMultiple;i<end;i+=prime)isPrime[(int)(i-start)]=false;
		}
	}
	public static void sieve(long n,int numThreads) {
		long limit=(long)Math.sqrt(n)+1;
		boolean[]isPrimeSmall=new boolean[(int)limit+1];
		Arrays.fill(isPrimeSmall,true);
		isPrimeSmall[0]=isPrimeSmall[1]=false;
		// Find all primes up to sqrt(n)
		for(long i=2;i*i<=limit;i++)if(isPrimeSmall[(int)i])for(long j=i*i;j<=limit;j+=i)isPrimeSmall[(int)j]=false;
		List<Long> smallPrimes=new ArrayList<>();
		for(long i=2;i<=limit;i++)if(isPrimeSmall[(int)i])smallPrimes.add(i);
		// Segment the range and process each segment
		long segmentSize=1_000_0000; // Size of each segment
		List<Long>primeBuffer=new ArrayList<>(1_000_0000); // Buffer for primes
		try(BufferedWriter writer=new BufferedWriter(new FileWriter("primes.txt"))){
			for(long low=2;low<=n;low+=segmentSize){
				long high=Math.min(low+segmentSize-1,n);
				boolean[]isPrimeSegment=new boolean[(int)(high-low+1)];
				Arrays.fill(isPrimeSegment,true);
				// Use small primes to mark non-primes in the current segment
				ExecutorService executor=Executors.newFixedThreadPool(numThreads);
				for(long prime:smallPrimes) {
					if(prime*prime>high)break;
					executor.submit(new SieveTask(isPrimeSegment,low,high+1,prime));
				}
				executor.shutdown();
				//wait for executors
				while(!executor.isTerminated()){}
				// Collect prime numbers from the segment
				for(int i=0;i<isPrimeSegment.length;i++) {
					if (isPrimeSegment[i]){
						long primeNumber=low+i;
						primeBuffer.add(primeNumber);
						// Write to file every million primes
						if(primeBuffer.size()==1_000_0000) {
							for(Long prime:primeBuffer)writer.write(prime + " ");
							System.out.println(primeBuffer.get(9999999));
							writer.flush(); // Ensure the output is written to the file
							primeBuffer.clear(); // Clear the buffer
						}
					}
				}
			}
			// Write any remaining primes in the buffer
			if(!primeBuffer.isEmpty()){
				for (Long prime:primeBuffer)writer.write(prime+" ");
				writer.flush(); // Ensure the output is written to the file
			}
			System.out.println("Primes saved to primes.txt in batches of one million.");
		}catch(IOException e){e.printStackTrace();}
	}
	public static void main(String[]args){
		long n=100_000_000_000L; // Find primes up to 100 million
		int numThreads=32; // Number of threads to use
		sieve(n,numThreads);
	}
}
