import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

public class DynamicJson {
	
	@Test(dataProvider = "bookData")
	public void addBook(String name, String aisle, String isbn, String author) {
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		given().header("Content-Type","application/json").body(Requests.addBook(name, aisle, isbn, author))
		.when().log().all().post("/Library/Addbook.php").then().log().all().assertThat().statusCode(200);
	}
	
	@Test(dataProvider = "bookData",dependsOnMethods = "addBook")
	public void deleteBook(String name, String aisle, String isbn, String author) {
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		given().header("Content-Type","application/json").body(Requests.getBookId(aisle, isbn))
		.when().log().all().delete("/Library/DeleteBook.php").then().log().all().assertThat().statusCode(200);
	}
	
	@DataProvider(name="bookData")
	public Object[][] bookData(){
		return new Object[][] {{"bigboss1","bigboss1","0001","iamriz007"},{"bigboss2","bigboss2","0002","iamriz007"},{"bigboss3","bigboss3","0003","iamriz007"}};
	}
}
