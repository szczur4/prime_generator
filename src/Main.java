import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class Main{
	static ArrayList<Integer>v=new ArrayList<>();
	static boolean b;
	static int a=2,z;
	static File file=new File("C:\\Users\\<USER>\\Desktop\\primes.txt");
	static BufferedWriter writer;
	public static void main(String[] args)throws Exception{
		file.createNewFile();
		writer=new BufferedWriter(new FileWriter(file));
		v.add(2);
		writer.write(2+" ");
		for(int i=3;i<Integer.MAX_VALUE;i+=2){
			b=true;
			for(int j=0;j<v.size()&&v.get(j)<=Math.sqrt(i);j++)if(i%v.get(j)==0){
				b=false;
				break;
			}
			if(b){
				z++;
				if(z==1000000){
					System.out.println(i);
					writer.flush();
					z=0;
				}
				writer.write(i-a+" ");
				a=i;
				v.add(i);
			}
		}
		writer.close();
	}
}
