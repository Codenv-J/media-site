package com.coden.repository;

/**
 *
 * //public interface ESBookRepository extends ElasticsearchRepository<Book, String> {
 *
 * //    List<Book> findByTitleOrAuthor(String title, String author);
 * //
 * //    @Highlight(fields = {
 * //            @HighlightField(name = "title"),
 * //            @HighlightField(name = "author")
 * //    })
 * //    @Query("{\"match\":{\"title\":\"?0\"}}")
 * //    SearchHits<Book> find(String keyword);
 *
 * //}
 */
public interface EsBookRepository {
}