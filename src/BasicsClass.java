import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import static org.hamcrest.Matchers.*;

import org.testng.Assert;


public class BasicsClass {

	public static void main(String[] args) {
		String key = "qaclick123";
		
		//ADD PLACE
		// Setup the baseURI
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		//setup in given, when is to send request, then is for verification.
		String response = given().queryParam("key", key).header("Content-Type","application/json").body(Requests.getAddPlaceBody()).when().post("/maps/api/place/add/json").then().assertThat().statusCode(200).body("scope", equalTo("APP")).header("server", "Apache/2.4.41 (Ubuntu)").extract().response().asString();
		
		System.out.println(response);
		
		// Parse the json using JsonPath class
		JsonPath js = new JsonPath(response);
		String placeId = js.get("place_id");
		System.out.println(placeId);
		
		//Update place API using place id in previous request
		String newAddress = "updated new address";
		given().log().all().queryParam("key", key).header("Content-Type","application/json").body("{\r\n"
				+ "\"place_id\":\""+placeId+"\",\r\n"
				+ "\"address\":\""+newAddress+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}\r\n"
				+ "").when().log().all().put("/maps/api/place/update/json").then().log().all().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));		
		
		//GET Place API to verify the address properly updated
		
		String getPlaceResponse = given().log().all().queryParams("key",key,"place_id",placeId).when().log().all().get("/maps/api/place/get/json").then().log().all().statusCode(200).extract().body().asString();
		
		
		Assert.assertEquals(ReusableMethods.rawToJson(getPlaceResponse).get("address"), newAddress, "Address not matched");
	}
}
