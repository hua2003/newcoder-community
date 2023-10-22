package com.nowcoder.community.service.impl;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.repository.DiscussPostRepository;
import com.nowcoder.community.service.ElasticsearchService;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {
    @Autowired
    private DiscussPostRepository discussPostRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public void saveDiscussPost(DiscussPost discussPost) {
        discussPostRepository.save(discussPost);
    }

    @Override
    public void delete(int id) {
        discussPostRepository.deleteById(String.valueOf(id));
    }

    @Override
    public Page<DiscussPost> searchDiscussPost(String keyword, int current, int limit) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(keyword, "title", "content"))
                .withSorts(SortBuilders.fieldSort("type").order(SortOrder.DESC),
                        SortBuilders.fieldSort("score").order(SortOrder.DESC),
                        SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(current, limit))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                )
                .build();

        SearchHits<DiscussPost> search = elasticsearchRestTemplate.search(searchQuery, DiscussPost.class);
        List<SearchHit<DiscussPost>> searchHits = search.getSearchHits();

        ArrayList<DiscussPost> discussPosts = new ArrayList<>();
        for (SearchHit<DiscussPost> hit : searchHits) {
            Map<String, List<String>> highlightFields = hit.getHighlightFields();
            hit.getContent().setTitle(highlightFields.get("title") == null ? hit.getContent().getTitle() : highlightFields.get("title").get(0));
            hit.getContent().setContent(highlightFields.get("content") == null ? hit.getContent().getContent() : highlightFields.get("content").get(0));
            discussPosts.add(hit.getContent());
        }

        for (DiscussPost discussPost : discussPosts) {
            System.out.println(discussPost);
        }

        return new PageImpl<>(discussPosts, searchQuery.getPageable(), search.getTotalHits());
    }
}
