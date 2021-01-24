package io.realworld.repository

import io.realworld.model.Article
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.history.RevisionRepository
import org.springframework.stereotype.Repository

@Repository
interface ArticleRepository : PagingAndSortingRepository<Article, Long>, JpaSpecificationExecutor<Article> ,
        RevisionRepository<Article, Long, Long>{
    fun existsBySlug(slug: String): Boolean
    fun findBySlug(slug: String): Article?
    fun findByAuthorIdInOrderByCreatedAtDesc(ids: List<Long>, pageable: Pageable): List<Article>
}