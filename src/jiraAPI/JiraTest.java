package jiraAPI;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.File;

import org.testng.Assert;

public class JiraTest {
	public static void main(String[] args) {
		RestAssured.baseURI = "http://localhost:8080";

		// Using session filer pass the cookie header
		SessionFilter session = new SessionFilter();

		// login
		@SuppressWarnings("unused")
		String loginResponse = given().header("Content-Type", "application/json").log().all()
				.body("{ \"username\":\"rizwan0006\",\"password\":\"1Qazxsw2\"}").filter(session).when().log().all()
				.post("/rest/auth/1/session").then().extract().response().asString();

		String expectedComment = "comment 1";
		// create comment
		String addedComment = given().pathParam("key", "10001").log().all().header("Content-Type", "application/json")
				.body("{\r\n" + "    \"body\": \"" + expectedComment + "\",\r\n" + "    \"visibility\": {\r\n"
						+ "        \"type\": \"role\",\r\n" + "        \"value\": \"Administrators\"\r\n" + "    }\r\n"
						+ "}")
				.filter(session).when().post("/rest/api/2/issue/{key}/comment").then().log().all().assertThat()
				.statusCode(201).extract().asString();

		JsonPath jsComment = new JsonPath(addedComment);
		String commentId = jsComment.get("id");

		/*
		 * Add Attachments curl -D- -u admin:admin -X POST -H
		 * "X-Atlassian-Token: no-check" -F "file=@myfile.txt"
		 * http://myhost/rest/api/2/issue/TEST-123/attachments
		 */

		// using multipart to upload the attachment
		given().header("X-Atlassian-Token", "no-check").filter(session).pathParam("key", "10001")
				.header("Content-Type", "multipart/form-data")
				.multiPart("file",
						new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "files"
								+ File.separator + "attachment.txt"))
				.when().log().all().post("/rest/api/2/issue/{key}/attachments").then().log().all().assertThat()
				.statusCode(200);

		// Get Issue
		System.out.println("***********************************************");
		String issueDetails = given().filter(session).pathParam("key", "10001").queryParam("fields", "comment").when()
				.get("/rest/api/2/issue/{key}").then().log().all().extract().response().asString();
		System.out.println(issueDetails);

		JsonPath js = new JsonPath(issueDetails);
		int commentSize = js.getInt("fields.comment.comments.size()");
		for (int i = 0; i < commentSize; i++) {
			if (js.get("fields.comment.comments[" + i + "].id").toString().equals(commentId)) {
				Assert.assertTrue(expectedComment.equals(js.get("fields.comment.comments[" + i + "].body").toString()));
			}

		}
	}
}
