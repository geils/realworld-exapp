package io.realworld.model

import org.hibernate.envers.Audited
import org.hibernate.envers.RelationTargetAuditMode
import java.time.OffsetDateTime
import javax.persistence.*

@Entity
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
data class Comment(var createdAt: OffsetDateTime = OffsetDateTime.now(),
                   var updatedAt: OffsetDateTime = OffsetDateTime.now(),
                   var body: String = "",
                   @ManyToOne
                   var article: Article = Article(),
                   @ManyToOne
                   var author: User = User(),
                   @Id @GeneratedValue(strategy = GenerationType.AUTO)
                   var id: Long = 0)