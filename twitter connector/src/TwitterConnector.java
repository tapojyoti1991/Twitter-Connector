import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;




public class TwitterConnector {
	public TwitterConnector() throws Exception{
		
		ConfigurationBuilder cb=new ConfigurationBuilder();
		cb.setOAuthConsumerKey("RTi4oxKa5qUPTUL0ivClQ"); //consumer key
		cb.setOAuthConsumerSecret("bS621jR2dcSUnSYkDaJmw9lzgZ8oyc5AFUUZOX6tTE");// consumer secret
		AccessToken accessToken =new AccessToken("107415206-4kqg5g5aASE0ifPczXCQk8Zaq5Dksjw1dMkuoecS","QWcDnkgrGYaObYoACNXAGbVgk4n4dbC5rPdf69Xg"); //access key,access secret
		TwitterFactory tf=new TwitterFactory(cb.build());//building the access configuration for the specified account that is registered for the application
		Twitter t= tf.getInstance(accessToken);// creating a twitter object for accessing with the above configuration
		System.out.println(t.getId());// t stores the information of the account used for access i.e home account
		System.out.println(t.getScreenName());// returns screen name of home account
		//Paging paging = new Paging(1, 100);
		File input=new File("C:\\Users\\Tapo\\workspace\\twitter connector\\src\\Handles.txt");//file containing the list of handles to access
		FileReader fr=new FileReader(input);
		BufferedReader br=new BufferedReader(fr);
		
		String handle=br.readLine();
		while(handle!=null)
		{
			ResponseList<User> users=t.searchUsers(handle, 1);//return list of users with the specified handle
			System.out.println(users.get(0).getName());
			User u=users.get(0);//storing the obtained user as an object
			List<Status> statuses = t.getUserTimeline(users.get(0).getId());// retrieves the recent tweets by the user(max 20-api maxinmum)
			System.out.println("Number of tweets = "+u.getStatusesCount());//returns total number if tweets made by user
			for(int i=0;i<statuses.size();i++)
			System.out.println(statuses.get(i).getText());// retrieving tweet
			System.out.println(u.getFollowersCount());
			handle=br.readLine();
		}
	}

	public static void main(String[] args) throws Exception {
		TwitterConnector tc=new TwitterConnector();

	}

}
