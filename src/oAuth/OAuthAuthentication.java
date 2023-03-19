package oAuth;

import static io.restassured.RestAssured.given;

import io.restassured.path.json.JsonPath;

public class OAuthAuthentication {
	static String password = "0832882248";

	public static void main(String[] args) throws InterruptedException {

		String url = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AWtgzh7885ZzbIlP5Er0IqksUZxhYnLxDkUgVn8kSWD6KoTEvP5VefNqXjBLZFxrLyU6UQ&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none";
		String code = url.split("code=")[1].split("&scope")[0];
		
		System.out.println(code);
		// Generate access code
		String accessTokenResponse = given().urlEncodingEnabled(false)
				.queryParams("code", code)
				.queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
				.queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
				.queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
				.queryParams("grant_type", "authorization_code").when().log().all()
				.post("https://www.googleapis.com/oauth2/v4/token").asString();

		JsonPath js = new JsonPath(accessTokenResponse);
		String accessToken = js.getString("access_token");

		// hit the API with access code
		String response = given().queryParam("access_token", accessToken).when().log().all()
				.get("https://rahulshettyacademy.com/getCourse.php").asString();

		System.out.println(response);
	}

}
