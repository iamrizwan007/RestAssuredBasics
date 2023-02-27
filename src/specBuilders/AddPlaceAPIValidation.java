package specBuilders;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;

import deserializationPOJO.AddedPlace;

public class AddPlaceAPIValidation {
	public static void main(String[] args) {

		RestAssured.baseURI = "https://rahulshettyacademy.com";
		List<String> myType = new ArrayList<String>();
		myType.add("type1");
		myType.add("type2");

		AddPlace place = new AddPlace();
		place.setAccuracy(500);
		place.setAddress("shaheen bagh3");
		place.setLanguage("urdu");
		place.setName("Rizwan");
		place.setWebsite("wantify.in");
		place.setTypes(myType);

		Location location = new Location();
		location.setLat(-70.1234567);
		location.setLng(7.649372694);
		place.setLocation(location);

		RequestSpecification reqSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addQueryParam("key", " qaclick123").setContentType(ContentType.JSON).build();
		ResponseSpecification resSpec = new ResponseSpecBuilder().expectContentType(ContentType.JSON)
				.expectStatusCode(200).build();

		RequestSpecification request = given().spec(reqSpec).body(place);

		AddedPlace deserializedObject = request.when().post("/maps/api/place/add/json").then().log().all().spec(resSpec)
				.extract().response().as(AddedPlace.class);

		System.out.println(deserializedObject.getStatus());
		System.out.println(deserializedObject.getPlace_id());
		System.out.println(deserializedObject.getScope());
		System.out.println(deserializedObject.getReference());
		System.out.println(deserializedObject.getId());
	}
}
