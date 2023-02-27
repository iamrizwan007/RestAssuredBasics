import org.testng.Assert;

import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {
	public static void main(String[] args) {
		JsonPath js = new JsonPath(Requests.getCoursePrice());

//		1. Print No of courses returned by API
		System.out.println("No. of courses:" + js.getString("courses.size()"));

//		2.Print Purchase Amount
		System.out.println("Purchase amount:" + js.getString("dashboard.purchaseAmount"));

//		3. Print Title of the first course
		System.out.println("First Course Titile:" + js.getString("courses[0].title"));

//		4. Print All course titles and their respective Prices
		int totalCourses = js.getInt("courses.size()");
		for (int i = 0; i < totalCourses; i++) {
			System.out.println("Course Titile:" + js.getString("courses[" + i + "].title") + " and price is:"
					+ js.getString("courses[" + i + "].price"));
		}

//		5. Print no of copies sold by RPA Course
		System.out.println("Copies sold by RPA:" + js.getString("courses[2].copies"));
		
//		6. Verify if Sum of all Course prices matches with Purchase Amount
		int total = 0;
		for (int i = 0; i < totalCourses; i++) {
			total = total + js.getInt("courses["+i+"].price") * js.getInt("courses["+i+"].copies");
		}
		Assert.assertEquals(total, js.getInt("dashboard.purchaseAmount"));
	}
}
