package io.tolgee.dtos.response

import com.fasterxml.jackson.annotation.JsonIgnore
import io.tolgee.dtos.PathDTO
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class KeyDTO(
        @field:NotBlank
        @field:Size(min = 1, max = 300)
        var fullPathString: String? = null
) {
    @get:JsonIgnore
    val pathDto: PathDTO
        get() = PathDTO.fromFullPath(fullPathString)
}
