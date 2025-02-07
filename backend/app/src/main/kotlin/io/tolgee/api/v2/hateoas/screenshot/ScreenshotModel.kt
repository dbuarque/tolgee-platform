package io.tolgee.api.v2.hateoas.screenshot

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import java.util.*

@Suppress("unused")
@Relation(collectionRelation = "screenshots", itemRelation = "screenshot")
open class ScreenshotModel(
  val id: Long,
  @Schema(
    description = """File name, which may be downloaded from the screenshot path.

When images are secured. Encrypted timestamp is appended to the filename.    
  """,
  )
  val filename: String,

  @Schema(
    description = """Thumbnail file name, which may be downloaded from the screenshot path.

When images are secured. Encrypted timestamp is appended to the filename.    
  """,
  )
  val thumbnail: String,
  val fileUrl: String,
  val thumbnailUrl: String,
  val createdAt: Date?,
) : RepresentationModel<ScreenshotModel>()
