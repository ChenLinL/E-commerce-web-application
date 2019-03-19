import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class parse {
	public static void main(String[] args)
	{
				
		String fileName = "C:\\Users\\Jing\\Logfile_scaledcase4.txt";
		
		FileReader fileReader = null;
		BufferedReader br = null;
		
		long ts = 0;
		long tj = 0;
		long count = 0;
		double average_ts = 0;
		double average_tj = 0;
		
		try
		{
			fileReader = new FileReader(fileName);
			br = new BufferedReader(fileReader);
			
			String currentline;
			while ((currentline = br.readLine()) != null)
			{
				count += 1;
				
				String[] tokens = currentline.split("TS: ");
				
				String[] secondtokens = tokens[1].split("TJ: ");				
				secondtokens[0] = secondtokens[0].replaceAll("\\s+","");
								
				ts += Long.parseLong(secondtokens[0]);
				tj += Long.parseLong(secondtokens[1]);			
			}
						
			average_ts = (double)ts / count;
			average_tj = (double)tj / count;
			
			System.out.println("Average TS Time: "  + average_ts / 1000000);
			System.out.println("Average TJ Time: " + average_tj / 1000000);
			
		} catch(IOException e)
		{
			e.printStackTrace();
		}
		finally {
			try {
				if (br != null) br.close();
				if (fileReader != null) fileReader.close();
				
			}catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}	
}
