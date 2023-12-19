package tests

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.Response
import java.io.BufferedReader
import java.io.File

class AddressCheckingServiceTest : DescribeSpec({

    data class Postcode(val postcode: Int, val cities: List<String>)

    data class Streets(val postcode: Int, val city: String, val streets: List<String>)

    describe("Address checking service") {

        context("Valid German postcodes") {

            val validPostcodes = listOf(
                Postcode(10409, listOf("Berlin")),
                Postcode(77716, listOf("Fischerbach", "Haslach", "Hofstetten"))
            )

            validPostcodes.forEach { code ->

                it("should return cities for postcode ${code.postcode}") {
                    val response = makeApiRequest(code.postcode)

                    response.then().statusCode(200)
                    response.then().assertThat().contentType(ContentType.JSON)
                    response.jsonPath().getList<String>("Cities") shouldNotBe emptyList<String>()
                    response.jsonPath().getList<String>("Cities") shouldBe code.cities
                }
            }
        }

        context("Invalid German postcode") {

            val invalidPostcode = 22333

            it("should return HTTP 404 for invalid postcode $invalidPostcode") {
                val response = makeApiRequest(invalidPostcode)

                response.then().statusCode(404)
                response.body.asString().shouldBe("")
            }
        }

        context("Find the streets for a given postcode") {

            fun getResource(name: String): List<String> {
                val bufferedReader: BufferedReader = File("src/test/resources/$name.txt").bufferedReader(Charsets.UTF_8)
                return bufferedReader.readLines()
            }

            val streets = listOf(
                Streets(10409, "Berlin", getResource("Berlin")),
                Streets(77716, "Fischerbach", getResource("Fischerbach")),
                Streets(77716, "Haslach", getResource("Haslach")),
                Streets(77716, "Hofstetten", getResource("Hofstetten"))
            )

            streets.forEach() { street ->
                it("should return streets for postcode ${street.postcode} and city ${street.city}") {
                    val response = RestAssured.given()
                        .contentType(ContentType.JSON)
                        .`when`()
                        .get("https://service.verivox.de/geo/latestv2/cities/${street.postcode}/${street.city}/streets")

                    response.then().statusCode(200)
                    response.then().assertThat().contentType(ContentType.JSON)
                    response.jsonPath().getList<String>("Streets") shouldNotBe emptyList<String>()
                    response.jsonPath().getList<String>("Streets") shouldBe street.streets
                }
            }
        }
    }
})

fun makeApiRequest(postcode: Int): Response {
    val baseUrl = "https://service.verivox.de/geo/latestv2/cities"
    return RestAssured.given()
        .contentType(ContentType.JSON)
        .`when`()
        .get("$baseUrl/$postcode")
}

