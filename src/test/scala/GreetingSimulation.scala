import io.gatling.core.Predef._
import io.gatling.http.Predef._

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.DurationInt

class GreetingSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("http://localhost:8088/") // Here is the root for all relative URLs
    .acceptHeader("application/json") // Here are the common headers
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Gatling Runner")

  val scn = scenario("Greeting API tests") // A scenario is a chain of requests and pauses
    .exec(http("hello-get")
      .get("/hello"))
    .pause("5", "500", TimeUnit.MILLISECONDS)
    .exec(http("hello-post") // Here's an example of a POST request
      .post("/hello")
      .body(StringBody("""{ "message": "Darek" }""")).asJson)
    .pause("5", "500", TimeUnit.MILLISECONDS)
    .exec(http("hello-post-reversed") // Here's an example of a POST request
      .post("/helloReversed")
      .body(StringBody("""{ "message": "Dariusz" }""")).asJson)

  setUp(scn.inject(rampUsers(10000).during(1.minutes)).protocols(httpProtocol))
}