package io.tolgee.controllers

import io.tolgee.assertions.Assertions.assertThat
import io.tolgee.assertions.UserApiAppAction
import io.tolgee.constants.ApiScope
import io.tolgee.fixtures.generateUniqueString
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.testng.annotations.Test

@SpringBootTest
@AutoConfigureMockMvc
class LanguageApiControllerTest : AbstractUserAppApiTest(), ITest {

    @Test
    fun findAllLanguages() {
        val repository = dbPopulator.createBase(generateUniqueString(), "ben")
        val apiKey = apiKeyService.createApiKey(repository.createdBy, setOf(*ApiScope.values()), repository)
        val contentAsString = performAction(UserApiAppAction(
                method = HttpMethod.GET,
                apiKey = apiKey.key,
                expectedStatus = HttpStatus.OK,
                url = "/api/languages"
        )).response.contentAsString
        assertThat(decodeJson<Set<*>>(contentAsString, Set::class.java)).hasSize(2)
    }
}
