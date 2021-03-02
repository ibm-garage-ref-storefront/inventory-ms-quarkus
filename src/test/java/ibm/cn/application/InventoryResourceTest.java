package ibm.cn.application;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@Tag("integration")
public class InventoryResourceTest {

    @Test
    public void testInventoryEndpoint() {
        given()
          .when().get("/micro/inventory/resource")
          .then()
             .statusCode(200)
             .body(is("InventoryResource response"));
    }

}