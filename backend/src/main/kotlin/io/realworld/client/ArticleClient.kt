package io.realworld.client

import feign.Headers
import feign.Param
import feign.RequestLine
import io.realworld.client.response.*

@Headers("Content-Type: application/json",
        "Authorization: Token {token}")
interface ArticleClient {
    @RequestLine("POST /api/articles")
    fun newArticle(@Param("token") token: String, article: InArticle ): OutArticle

    @RequestLine("PUT /api/articles/{slug}")
    fun updateArticle(@Param("token") token: String, @Param("slug") slug: String ,
                      article: InUpdateArticle): OutArticle

    @RequestLine("GET /api/articles/{slug}/history")
    fun history(@Param("token") token: String,
                @Param("slug") slug: String): OutHistory
}
