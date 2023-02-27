package files;

import java.util.Properties;
import javax.mail.*;

public class GPT1 {
	public static void main(String[] args) {
		String subjectLine = "FLowCV";
		String host = "outlook.office365.com";
		String mailStoreType = "imap";
		String username = "mrizwan0006@outlook.com";
		String password = "iamriz007R.";

		try {
			Properties properties = new Properties();
			properties.put("mail.imap.host", host);
			Session emailSession = Session.getDefaultInstance(properties);
			Store emailStore = emailSession.getStore(mailStoreType);
			emailStore.connect(host, username, password);

			Folder emailFolder = emailStore.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);

			Message[] messages = emailFolder.getMessages();
			for (Message message : messages) {
				if (message.getSubject().equals(subjectLine)) {
					System.out.println("Subject: " + message.getSubject());
					System.out.println("From: " + message.getFrom()[0]);
					System.out.println("Text: " + message.getContent().toString());
				}
			}

			emailFolder.close(false);
			emailStore.close();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
