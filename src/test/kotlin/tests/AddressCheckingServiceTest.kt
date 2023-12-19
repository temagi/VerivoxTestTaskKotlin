package tests

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.Response
import model.Postcode
import model.Streets
import utils.getResource
import utils.readProperty

class AddressCheckingServiceTest : DescribeSpec({

    describe("Address checking service") {

        context("Valid German postcodes") {

            val validPostcodes =
                listOf(
                    Postcode(10409, listOf("Berlin")),
                    Postcode(77716, listOf("Fischerbach", "Haslach", "Hofstetten")),
                )

            validPostcodes.forEach { code ->

                it("should return cities for postcode ${code.postcode}") {
                    val response = makeApiRequest("${code.postcode}")

                    response.apply {
                        then().apply {
                            statusCode(200)
                            then().assertThat().contentType(ContentType.JSON)
                        }
                        jsonPath().apply {
                            getList<String>("Cities") shouldNotBe emptyList<String>()
                            jsonPath().getList<String>("Cities") shouldBe code.cities
                        }
                    }
                }
            }
        }

        context("Invalid German postcode") {

            val invalidPostcode = 22333

            it("should return HTTP 404 for invalid postcode $invalidPostcode") {
                val response = makeApiRequest("$invalidPostcode")

                response.then().statusCode(404)
                response.body.asString().shouldBe("")
            }
        }

        context("Find the streets for a given postcode") {

            val streets =
                listOf(
                    Streets(10409, "Berlin", getResource("Berlin")),
                    Streets(77716, "Fischerbach", getResource("Fischerbach")),
                    Streets(77716, "Haslach", getResource("Haslach")),
                    Streets(77716, "Hofstetten", getResource("Hofstetten")),
                )

            streets.forEach { street ->
                it("should return streets for postcode ${street.postcode} and city ${street.city}") {
                    val response = makeApiRequest("${street.postcode}/${street.city}/streets")

                    response.apply {
                        then().apply {
                            statusCode(200)
                            assertThat().contentType(ContentType.JSON)
                        }
                        jsonPath().apply {
                            getList<String>("Streets") shouldNotBe emptyList<String>()
                            jsonPath().getList<String>("Streets") shouldBe street.streets
                        }
                    }
                }
            }
        }
    }
})

fun makeApiRequest(uri: String? = ""): Response {
    val baseUrl = readProperty("baseurl")
    return RestAssured.given()
        .contentType(ContentType.JSON)
        .`when`()
        .get("$baseUrl/$uri")
}
