import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;




public class TwitterConnector {
	Calendar rightnow,createdAt;
	FileWriter fw;
	
	public TwitterConnector(Twitter t){
		//Paging paging = new Paging(1, 100);
		File input=new File("Handles.txt");//file containing the list of handles to access
		FileReader fr;
		try {
			fr = new FileReader(input);
		BufferedReader br=new BufferedReader(fr);
		
		
		String handle=br.readLine();
		rightnow=Calendar.getInstance();
		while(handle!=null)
		{
			
			ResponseList<User> users=t.searchUsers(handle, 1);//return list of users with the specified handle
			System.out.println(users.get(0).getName());
			User u=users.get(0);//storing the obtained user as an object
			
			File f=new File("C:\\Users\\Tapo\\"+rightnow.get(Calendar.YEAR)+"\\"+(rightnow.get(Calendar.MONTH)+1)+"\\"+rightnow.get(Calendar.DATE));
			f.mkdirs();// creating directories
			fw=new FileWriter(new File("C:\\Users\\Tapo\\"+rightnow.get(Calendar.YEAR)+"\\"+(rightnow.get(Calendar.MONTH)+1)+"\\"+rightnow.get(Calendar.DATE)+"\\"+u.getName()+".txt"));
			List<Status> statuses = t.getUserTimeline(users.get(0).getId(),new Paging(1, 200));// retrieves the recent tweets by the user 200 per page
			updateFile(statuses);
			
			handle=br.readLine();
			fw.close();
		}
		br.close();
		} catch (FileNotFoundException e) {
			System.out.println("file not found, please check location");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("error in reading from file.");
			e.printStackTrace();
		} catch (TwitterException e) {
			System.out.println("Error retrieving data");
			e.printStackTrace();
		}
	}
	public void updateFile(List<Status> statuses)
	{
		createdAt=Calendar.getInstance();
		for(int i=0;i<statuses.size();i++)
		{
			Status status=statuses.get(i);
			createdAt.setTime(status.getCreatedAt());
			
			if(createdAt.get(Calendar.DATE)==rightnow.get(Calendar.DATE) && (createdAt.get(Calendar.MONTH)==rightnow.get(Calendar.MONTH)) && (createdAt.get(Calendar.YEAR)==rightnow.get(Calendar.YEAR)))
			{
				System.out.println(status.getCreatedAt());
				try {
					fw.append(status.getId() +"\t"+ status.getText()+"\r\n");
				} catch (IOException e) {
					System.out.println("error in writing to file");
					e.printStackTrace();
				}
			}
		}
	}
	private static Twitter getAccess() throws Exception
	{
		ConfigurationBuilder cb=new ConfigurationBuilder();
		cb.setOAuthConsumerKey("RTi4oxKa5qUPTUL0ivClQ"); //consumer key
		cb.setOAuthConsumerSecret("bS621jR2dcSUnSYkDaJmw9lzgZ8oyc5AFUUZOX6tTE");// consumer secret
		AccessToken accessToken =new AccessToken("107415206-4kqg5g5aASE0ifPczXCQk8Zaq5Dksjw1dMkuoecS","QWcDnkgrGYaObYoACNXAGbVgk4n4dbC5rPdf69Xg"); //access key,access secret
		TwitterFactory tf=new TwitterFactory(cb.build());//building the access configuration for the specified account that is registered for the application
		Twitter t= tf.getInstance(accessToken);// creating a twitter object for accessing with the above configuration
		System.out.println(t.getId());// t stores the information of the account used for access i.e home account
		System.out.println(t.getScreenName());// returns screen name of home account
		return t;
	}

	public static void main(String[] args) throws Exception {
		TwitterConnector tc=new TwitterConnector(getAccess());

	}

}
