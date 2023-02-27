package files;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.AuthenticationFailedException;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.mail.imap.IMAPFolder;

public class ReadInboundEmailService {
	private static final Logger logger = LoggerFactory.getLogger(ReadInboundEmailService.class);
	public String content = "";
	public void readInboundEmails(String hostname, int port, String username, String password) {
//create session object
		Session session = this.getImapSession(hostname, String.valueOf(port));
		try {
//connect to message store
			Store store = session.getStore("imap");
			store.connect(hostname, port, username, password);
			System.out.println("connected");
//open the inbox folder
			IMAPFolder inbox = (IMAPFolder) store.getFolder("INBOX");
			inbox.open(Folder.READ_WRITE);
//fetch messages
			Message[] messages = inbox.getMessages();
//read messages
			for (int i = 0; i < messages.length; i++) {
				System.out.println("record:"+i);
				Message msg = messages[i];
				if(msg.getSubject().contains("FlowCV")) {
					processMessageBody(msg);
				}
//				System.out.println(msg.getContent());
//				Address[] fromAddress = msg.getFrom();
//				String from = fromAddress[0].toString();
//				String subject = msg.getSubject();
//				Address[] toList = msg.getRecipients(RecipientType.TO);
//				Address[] ccList = msg.getRecipients(RecipientType.CC);
//				String contentType = msg.getContentType();
//				String content = msg.getContent().toString();
//				String description = msg.getDescription().toString();
//				System.out.println("**************");
//				System.out.println(from);
//				System.out.println(subject);
//				System.out.println(toList);
//				System.out.println(ccList);
//				System.out.println(contentType);
//				System.out.println(content);
//				System.out.println(description);
				System.out.println("**************");
			}
		} catch (AuthenticationFailedException e) {
			logger.error("Exception in reading EMails : " + e.getMessage());
		} catch (MessagingException e) {
			logger.error("Exception in reading EMails : " + e.getMessage());
		} catch (Exception e) {
			logger.error("Exception in reading EMails : " + e.getMessage());
		}
	}

	private Session getImapSession(String hostname, String port) {
		Properties props = new Properties();
		props.put("mail.store.protocol", "imap");
		props.put("mail.debug", "true");
		props.put("mail.imap.host", hostname);
		props.put("mail.imap.port", port);
		props.put("mail.imap.ssl.enable", "true");
	    props.put("mail.imap.ssl.protocols", "TLSv1.2");
		Session session = Session.getDefaultInstance(props, null);
		session.setDebug(false);
		System.out.println("returning session");
		return session;
	}
	
	public void processMessageBody(Message message) { 
		try { Object content = message.getContent(); 
		if (content instanceof String) { 
//			System.out.println(content); 
			this.content = content.toString();
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
//					System.out.println(o.toString()); 
					this.content = o.toString();
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
	
	public static void main(String[] args) {
		new ReadInboundEmailService().readInboundEmails("outlook.office365.com", 993, "mrizwan0006@outlook.com", "iamriz007R.");
	}
}