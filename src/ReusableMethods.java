import io.restassured.path.json.JsonPath;

public class ReusableMethods {
	public static JsonPath js;
	
	public static JsonPath rawToJson(String response) {
		js = new JsonPath(response);
		return js;
	}
}
