import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class crawl {

	private static Scanner in;
	static String magic="')\">";
	static String outputadd="C:\\Users\\liuhengyuan-jk\\Desktop\\Programs\\crawlers\\company link.txt";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String line;
		String path="C:\\Users\\liuhengyuan-jk\\Desktop\\Programs\\crawlers\\listor.txt";
		try {
			int count=0;
			FileWriter out=new FileWriter(outputadd);
			BufferedReader br=new BufferedReader(new FileReader(path));
		BufferedWriter output=new BufferedWriter(out);
			do {
		 line=br.readLine();
			if(count%6==2) {
				String info=line.substring(line.indexOf("','")+8,line.indexOf(magic))
						+"@"+line.substring(line.indexOf("onclick=\"info('")+15,line.indexOf("','"));
					output.write(info);
					output.newLine();
			}
			count++;
			output.flush();
		} while(line!=null);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
