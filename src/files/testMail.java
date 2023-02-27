package files;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.AuthenticationFailedException;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.AndTerm;
import javax.mail.search.BodyTerm;
import javax.mail.search.RecipientStringTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;
import javax.net.ssl.SSLHandshakeException;

public class testMail {
	
	
	public static Store getNewConnection(String serverName, String username, String password) throws InterruptedException, SSLHandshakeException, NoSuchProviderException {
		final Properties properties = new Properties();
	    properties.put("mail.imap.ssl.enable", "true");
	    properties.put("mail.imap.ssl.protocols", "TLSv1.2");
	    Session imapSession = Session.getInstance(properties, null);
	    imapSession.setDebug(false);
	    Store imapStore = imapSession.getStore("imap");
	    for(int i=0; i<30; i++) {
	        try{
	          imapStore.connect(serverName, username, password);
	          break;
	        } catch (NoSuchProviderException e) {
	          e.printStackTrace();
	          throw e;
	        } catch (MessagingException e) {
	          e.printStackTrace();
	          //throw e;
	        }
	        Thread.sleep(5000);
	      }
	    return imapStore;
	}
	
	public SearchTerm createFilter(String emailSubject, String bodyContent, int filterTimeInMilliSecs, String emailTo){
		try{
			SearchTerm searchTerm;
			List<Object> searchFilterList = new ArrayList<Object>();

			if (emailSubject != null) {
				searchFilterList.add(new SubjectTerm(emailSubject));
			}

			if (bodyContent != null) {
				searchFilterList.add(new BodyTerm(bodyContent));
			}

			if (emailTo != null) {
				searchFilterList.add(new RecipientStringTerm(RecipientType.TO, emailTo));
			}

			searchTerm = new AndTerm(searchFilterList.toArray(new SearchTerm[searchFilterList.size() - 1]));

			return searchTerm;
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
public void getMessageContent(String username, String password, String serverName, String emailSubject, String bodyContent, int filterTimeInMilliSecs, String emailTo) throws MessagingException, SSLHandshakeException, InterruptedException {
	String mailMessageContent = "";
	Folder emailFolder = null;
	for(int i=0; i<25; i++) {
		try {
			Store connection = getNewConnection(serverName, username, password);
			System.out.println(connection);
			emailFolder = connection.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);
			break;
		} catch(MessagingException e) {
			System.out.println("exception");
			getNewConnection(serverName, username, password).close();
		}
	}
	Message[] messages = emailFolder.search(createFilter(emailSubject, bodyContent, filterTimeInMilliSecs, emailTo));
//	Date receivedDate = messages[messages.length-1].getReceivedDate();
	if(messages.length != 0){
		Message message = messages[messages.length-1];
//		System.out.println();
		processMessageBody(message);
		String content = null;
		try {
			content = message.getContent().toString();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		mailMessageContent = content;
	}
	System.out.println("printing");
	System.out.println(mailMessageContent);
	emailFolder.close(false);
}

public void processMessageBody(Message message) { 
	try { Object content = message.getContent(); 
	if (content instanceof String) { System.out.println(content); 
	} else if (content instanceof Multipart) { 
		Multipart multiPart = (Multipart) content; 
		procesMultiPart(multiPart); 
		} else if (content instanceof InputStream) { 
			InputStream inStream = (InputStream) content; 
			int ch; 
			while ((ch = inStream.read()) != -1) { 
				System.out.write(ch); 
				} 
			} 
	} catch (IOException e) { 
		e.printStackTrace(); 
		} catch (MessagingException e) { 
			e.printStackTrace(); 
			} 
	} 

public void procesMultiPart(Multipart content) { 
	try { 
		int multiPartCount = content.getCount(); 
		for (int i = 0; i < multiPartCount; i++) { 
			BodyPart bodyPart = content.getBodyPart(i); 
			Object o; 
			o = bodyPart.getContent(); 
			if (o instanceof String) { 
				System.out.println(o); 
				} else if (o instanceof Multipart) { 
					procesMultiPart((Multipart) o); 
					} 
			} 
		} catch (IOException e) { 
			e.printStackTrace(); 
			} catch (MessagingException e) { 
				e.printStackTrace(); 
				} 
	} 
public static void main(String[] args) throws SSLHandshakeException, MessagingException, InterruptedException {
	new testMail().getMessageContent("mrizwan0006@outlook.com", "iamriz007R.", "outlook.office365.com", "test", "test", 1000, "mrizwan0006@outlook.com");
}
}
