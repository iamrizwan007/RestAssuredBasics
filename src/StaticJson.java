import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.restassured.RestAssured;

public class StaticJson {
	
	public static void main(String[] args) throws IOException {
		String key = "qaclick123";
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		// pass the body from json file
		// read bytes from file and then byte to string
		
		String response = given().queryParam("key", key).header("Content-Type","application/json").body(new String(Files.readAllBytes(Paths.get("C:\\Users\\Rizwan\\Automation\\RestAssuredBasics\\src\\files\\addPlace.json")))).when().log().all().post("/maps/api/place/add/json").then().assertThat().statusCode(200).body("scope", equalTo("APP")).header("server", "Apache/2.4.41 (Ubuntu)").extract().response().asString();
		
		System.out.println(response);
	}
}
