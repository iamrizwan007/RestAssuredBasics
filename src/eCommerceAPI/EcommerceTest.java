package eCommerceAPI;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.ArrayList;

import eCommerceAPI.createOrderPOJO.OrderDetails;
import eCommerceAPI.createOrderPOJO.Orders;
import eCommerceAPI.createProductPOJO.CreateProductResponsePOJO;
import eCommerceAPI.loginPOJO.LoginRequest;
import eCommerceAPI.loginPOJO.LoginResponse;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class EcommerceTest {
	public static void main(String[] args) {
		RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.setContentType(ContentType.JSON).build();

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUserEmail("riz@riz.com");
		loginRequest.setUserPassword("Abt@9j45ZDQVNM");
		LoginResponse loginResponse = given().spec(req).body(loginRequest).when().post("/api/ecom/auth/login").then()
				.extract().as(LoginResponse.class);
		String token = loginResponse.getToken();
		String userId = loginResponse.getUserId();
		System.out.println(token);
		System.out.println(userId);
		System.out.println(loginResponse.getMessage());

		// Create a new product
		RequestSpecification createProductSpec = new RequestSpecBuilder().setBaseUri("")
				.addHeader("Authorization", token).build();

		RequestSpecification addProductRequest = given().spec(createProductSpec).param("productName", "sansui qledtv")
				.param("productAddedBy", userId).param("productCategory", "fashion")
				.param("productSubCategory", "shirts").param("productPrice", "999")
				.param("productDescription", "65 inch led tv").param("productFor", "men")
				.multiPart("productImage", new File("CucumberFrameworkArchitecture.png"));

		CreateProductResponsePOJO addProductResponse = addProductRequest.when().log().all()
				.post("https://rahulshettyacademy.com/api/ecom/product/add-product").then().extract().response()
				.as(CreateProductResponsePOJO.class);

		String newlyCreatedProduct = addProductResponse.getProductId();

		// Create order

		RequestSpecification createOrderSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", token).setContentType(ContentType.JSON).build();

		OrderDetails orderDetails1 = new OrderDetails();
		OrderDetails orderDetails2 = new OrderDetails();
		orderDetails1.setCountry("India");
		orderDetails1.setProductOrderedId(newlyCreatedProduct);

		orderDetails2.setCountry("US");
		orderDetails2.setProductOrderedId(newlyCreatedProduct);

		ArrayList<OrderDetails> listOfOrderDetails = new ArrayList<OrderDetails>();
		listOfOrderDetails.add(orderDetails1);
		listOfOrderDetails.add(orderDetails2);

		Orders order = new Orders();
		order.setOrders(listOfOrderDetails);

		RequestSpecification addOrderReqSpec = given().spec(createOrderSpec).body(order);

		String orderDetails = addOrderReqSpec.when().post("/api/ecom/order/create-order").then().log().all().extract()
				.response().asString();
		System.out.println(orderDetails);

		// Delete product
		RequestSpecification deleteSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", token).setContentType(ContentType.JSON).build();

		RequestSpecification deleteRequest = given().spec(deleteSpec).pathParam("productId", newlyCreatedProduct);
		String deleteResponse = deleteRequest.when().delete("/api/ecom/product/delete-product/{productId}").then()
				.extract().asString();
		System.out.println(deleteResponse);
	}
}
