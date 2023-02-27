package files;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Certificate;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Calendar;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureOptions;

public class PDFSignature {
    public static void main(String[] args) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        String pdfFilePath = "D:\\C9646878_Signed_OfferLetter.pdf";
//        String keyStorePath = "D:\\myKeystore.p12";
//        char[] password = "123456".toCharArray();
//        String alias = "myAlias";
        
//        KeyStore ks = KeyStore.getInstance("PKCS12");
//        ks.load(new FileInputStream(keyStorePath), password);
//        PrivateKey privateKey = (PrivateKey) ks.getKey(alias, password);
//        java.security.cert.Certificate[] chain = ks.getCertificateChain(alias);
        
        PDDocument pdDocument = PDDocument.load(new File(pdfFilePath));
//        PDSignature signature = new PDSignature();
//        signature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);
//        signature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED);
//        signature.setName("Mohammad Rizwan");
//        signature.setLocation("Delhi");
//        signature.setReason("");
//        SignatureOptions option = new SignatureOptions();
//        option.setPreferredSignatureSize(600);
//        document.addSignature(signature, option);
//        document.saveIncremental(new FileOutputStream(pdfFilePath));
//        document.close();
        try {
        	 
			PDSignature pdSignature = new PDSignature();
			pdSignature.setFilter(PDSignature.FILTER_VERISIGN_PPKVS);
			pdSignature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_SHA1);
			pdSignature.setName("MohammadRizwan");
			pdSignature.setLocation("WFH");
			pdSignature.setReason("Sample Signature test");
			pdSignature.setSignDate(Calendar.getInstance());
			pdDocument.addSignature(pdSignature);
 
			pdDocument.save(pdfFilePath);
			pdDocument.close();
			System.out.println("PDF saved to the location !!!");
 
		} catch (IOException ioe) {
			System.out.println("Error while saving pdf" + ioe.getMessage());
		}
    }
}