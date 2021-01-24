package io.realworld.client.response

import io.realworld.model.inout.Article

data class OutHistory(var articles: List<Article> = listOf() ,
                      var articlesCount : Int = 0)
