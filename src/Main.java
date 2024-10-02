import java.io.File;
import java.io.FileWriter;
import java.util.Vector;

public class Main{
	static Vector<Long>v=new Vector<>();
	static boolean b;
	static short z;
	static File file=new File("C:\\Users\\<USER>\\Desktop\\primes.txt");
	static FileWriter writer;
	public static void main(String[] args)throws Exception{
		file.createNewFile();
		writer=new FileWriter(file);
		v.add(2L);
		for(long i=3;i<Integer.MAX_VALUE;i+=2){
			b=true;
			for(int j=0;j<v.size()&&v.get(j)<Math.sqrt(i);j++)if(i%v.get(j)==0){
				b=false;
				break;
			}
			if(b){
				z++;
				if(z==10000){
					writer.flush();
					z=0;
				}
				writer.write(i+" ");
				v.add(i);
			}
		}
		writer.close();
	}
}