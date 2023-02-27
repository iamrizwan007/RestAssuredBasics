package deserializationPOJO;

import static io.restassured.RestAssured.given;

import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;

public class OAuthAuthentication {
	public static void main(String[] args) throws InterruptedException {

		// Url to generate code =
		// https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php

		String url = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AWtgzh50TjrgTjuxr9uqm_MOVuUxA99WtLL4pPorRy2qG8Hrzx--AAwRBJ0m-1RoR5_tYw&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none";
		String code = url.split("code=")[1].split("&scope")[0];

		System.out.println(code);
		// Generate access code
		String accessTokenResponse = given().urlEncodingEnabled(false).queryParams("code", code)
				.queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
				.queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
				.queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
				.queryParams("grant_type", "authorization_code").when().log().all()
				.post("https://www.googleapis.com/oauth2/v4/token").asString();

		JsonPath js = new JsonPath(accessTokenResponse);
		String accessToken = js.getString("access_token");

		// hit the API with access code
		GetCourse gc = given().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON).when()
				.get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);

		System.out.println(gc.getLinkedIn());
		System.out.println(gc.getExpertise());
		System.out.println(gc.getInstructor());
		System.out.println(gc.getServices());
		System.out.println(gc.getCourses().getWebAutomation().get(0).getCourseTitle());
		for (WebAutomation webCourse : gc.getCourses().getWebAutomation()) {
			if (webCourse.getCourseTitle().equalsIgnoreCase("Protractor")) {
				System.out.println(webCourse.getPrice());
			}
		}
	}
}
