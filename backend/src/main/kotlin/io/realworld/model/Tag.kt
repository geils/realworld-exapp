package io.realworld.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.envers.Audited
import org.hibernate.envers.RelationTargetAuditMode
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
data class Tag(val name: String = "",
               @Id @GeneratedValue(strategy = GenerationType.AUTO)
               @JsonIgnore
               var id: Long = 0)