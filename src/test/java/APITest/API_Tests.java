package APITest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.ConfigurationReader;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;

public class API_Tests {

    Response response;

    @BeforeClass
    public void beforeClass(){
        RestAssured.baseURI = ConfigurationReader.getProperty("book_api_url");
    }

    /**
     1. Verify that the API starts with an empty store.
     • At the beginning of a test case, there should be no books stored on the server.
     */
    @Test
    public void BookStoreTest(){

        response = given().accept(ContentType.JSON)
                .when().get(baseURI + "/api/books/");

        // verify status code 200
        assertEquals(response.statusCode(),200);

        // Verify that the API starts with an empty store = has NO header "id"
        assertFalse(response.headers().hasHeaderWithName("id"));

    }

    /**
     2. Verify that title and author are required fields.
     • PUT on /api/books/ should return an error Field '<field_name>' is required.
     */

    @Test
    public void FieldNameRequiredTest(){

        given().accept(ContentType.JSON)
                .when().put(baseURI + "/api/books/");

        //Error message in response body -> Field '<field_name>' is required
        assertTrue(response.statusLine().contains("Field '<field_name>' is required"));


    }

    /**
     3. Verify that title and author cannot be empty.
     • PUT on /api/books/ should return an error Field '<field_name>' cannot be empty.
     */

    @Test
    public void FieldNameNoEmptyTest(){

        Map<String,Object> putMap = new HashMap<>();
        putMap.put("title","");
        putMap.put("author","");

        response = given().accept(ContentType.JSON)
                .and().body(putMap)
                .when().put(baseURI + "/api/books/");

        //Error message in response body -> Field '<field_name>' cannot be empty
        assertTrue(response.statusLine().contains("Field '<field_name>' cannot be empty"));


    }

    /**
     4. Verify that the id field is read−only.
     • You shouldn't be able to send it in the PUT request to /api/books/.
     */

    @Test
    public void ErrorWithIDTest(){
        Map<String,Object> putMap = new HashMap<>();
        putMap.put("id", "anyIntNumberNotString");
        putMap.put("title","*");
        putMap.put("author","*");

        // Bad request error code = 400
        given().accept(ContentType.JSON)
                .and().body(putMap)
                .when().put(baseURI + "/api/books/")
                .then().assertThat().statusCode(400);


    }
    /**
     5. Verify that you can create a new book via PUT.
     • The book should be returned in the response.
     • GET on /api/books/<book_id>/ should return the same book
     */

    @Test
    public void CreateNewBookTest(){
        Map<String,Object> putMap = new HashMap<>();
        putMap.put("title","title");
        putMap.put("author","author");

        response = given().accept(ContentType.JSON)
                .and().body(putMap)
                .when().put(baseURI + "/api/books/");

        // verify status code 200
        assertEquals(response.statusCode(),200);

        // verify the book is returned in the response body
        assertEquals(response.getHeader("title"), putMap.get("title"));

        // verify the book id with GET request
        // verify the title and the author with Hamcrest Matchers
        given().accept(ContentType.JSON)
                .when().get(baseURI + "/api/books/" + response.getHeader("id"))
                .then().assertThat().statusCode(200)
                .and().assertThat().body("id", equalTo(response.getHeader("id")),"title",equalTo("title"),
                "author", equalTo("author"));

    }

    /**
     6. Verify that you cannot create a duplicate book
     */

    @Test
    public void DuplicateBookTest(){

        

    }
}
