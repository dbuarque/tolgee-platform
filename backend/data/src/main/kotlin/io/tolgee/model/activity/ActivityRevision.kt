package io.tolgee.model.activity

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import io.tolgee.activity.data.ActivityType
import io.tolgee.component.CurrentDateProvider
import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import org.springframework.beans.factory.ObjectFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Configurable
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.OneToMany
import javax.persistence.PrePersist
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType

@Entity
@Table(
  indexes = [
    Index(columnList = "projectId"),
    Index(columnList = "authorId"),
    Index(columnList = "type")
  ]
)
@EntityListeners(ActivityRevision.Companion.ActivityRevisionListener::class)
@TypeDefs(
  value = [TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)]
)
class ActivityRevision : java.io.Serializable {

  @Id
  @SequenceGenerator(
    name = "activitySequenceGenerator",
    sequenceName = "activity_sequence",
    initialValue = 0,
    allocationSize = 10
  )
  @GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "activitySequenceGenerator"
  )
  val id: Long = 0

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "timestamp", nullable = false, updatable = false)
  lateinit var timestamp: Date

  /**
   * We don't want a foreign key, since user could have been deleted
   */
  var authorId: Long? = null

  @Type(type = "jsonb")
  var meta: MutableMap<String, Any?>? = null

  @Enumerated(EnumType.STRING)
  var type: ActivityType? = null

  /**
   * Project of the change
   */
  var projectId: Long? = null

  @OneToMany(mappedBy = "activityRevision")
  @NotFound(action = NotFoundAction.IGNORE)
  var describingRelations: MutableList<ActivityDescribingEntity> = mutableListOf()

  @OneToMany(mappedBy = "activityRevision")
  var modifiedEntities: MutableList<ActivityModifiedEntity> = mutableListOf()

  companion object {
    @Configurable
    class ActivityRevisionListener {
      @Autowired
      lateinit var provider: ObjectFactory<CurrentDateProvider>

      @PrePersist
      fun preRemove(activityRevision: ActivityRevision) {
        activityRevision.timestamp = provider.`object`.date
      }
    }
  }
}
