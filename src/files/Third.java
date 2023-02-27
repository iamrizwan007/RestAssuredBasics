package files;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

public class Third {

public static void main(String[] args) throws MessagingException {
	 Properties props = new Properties();        
	 props.setProperty("mail.store.protocol", "imap");
	  props.setProperty("mail.imap.ssl.enable", "true");
	  Session mailSession = Session.getInstance(props); 
	  mailSession.setDebug(true);
	  Store mailStore = mailSession.getStore("imap");
	  mailStore.connect("outlook.office365.com", "mrizwan0006@outlook.com", "iamriz007R.");
}
}
