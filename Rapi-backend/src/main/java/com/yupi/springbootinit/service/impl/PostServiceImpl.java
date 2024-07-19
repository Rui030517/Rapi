//package com.yupi.springbootinit.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.yupi.springbootinit.common.ErrorCode;
//import com.yupi.springbootinit.constant.CommonConstant;
//import com.yupi.springbootinit.exception.BusinessException;
//import com.yupi.springbootinit.exception.ThrowUtils;
//import com.yupi.springbootinit.mapper.PostMapper;
//import com.yupi.springbootinit.model.dto.post.PostQueryRequest;
//import com.yupi.springbootinit.model.entity.Post;
//import com.yupi.springbootinit.model.entity.User;
//import com.yupi.springbootinit.model.vo.PostVO;
//import com.yupi.springbootinit.model.vo.UserVO;
//import com.yupi.springbootinit.service.PostService;
//import com.yupi.springbootinit.service.UserService;
//import com.yupi.springbootinit.utils.SqlUtils;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.stream.Collectors;
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import lombok.extern.slf4j.Slf4j;
//import cn.hutool.core.collection.CollUtil;
//import org.apache.commons.lang3.ObjectUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.sort.SortBuilder;
//import org.elasticsearch.search.sort.SortBuilders;
//import org.elasticsearch.search.sort.SortOrder;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
//import org.springframework.data.elasticsearch.core.SearchHit;
//import org.springframework.data.elasticsearch.core.SearchHits;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
//import org.springframework.stereotype.Service;
//
///**
// * 帖子服务实现
// *
// */
//@Service
//@Slf4j
//public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {
//
//    @Resource
//    private UserService userService;
//
//    @Resource
//    private ElasticsearchRestTemplate elasticsearchRestTemplate;
//
//    @Override
//    public void validPost(Post post, boolean add) {
//        if (post == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        String title = post.getTitle();
//        String content = post.getContent();
//        String tags = post.getTags();
//        // 创建时，参数不能为空
//        if (add) {
//            ThrowUtils.throwIf(StringUtils.isAnyBlank(title, content, tags), ErrorCode.PARAMS_ERROR);
//        }
//        // 有参数则校验
//        if (StringUtils.isNotBlank(title) && title.length() > 80) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
//        }
//        if (StringUtils.isNotBlank(content) && content.length() > 8192) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
//        }
//    }
//
//    /**
//     * 获取查询包装类
//     *
//     * @param postQueryRequest
//     * @return
//     */
//    @Override
//    public QueryWrapper<Post> getQueryWrapper(PostQueryRequest postQueryRequest) {
//        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
//        if (postQueryRequest == null) {
//            return queryWrapper;
//        }
//        String searchText = postQueryRequest.getSearchText();
//        String sortField = postQueryRequest.getSortField();
//        String sortOrder = postQueryRequest.getSortOrder();
//        Long id = postQueryRequest.getId();
//        String title = postQueryRequest.getTitle();
//        String content = postQueryRequest.getContent();
//        List<String> tagList = postQueryRequest.getTags();
//        Long userId = postQueryRequest.getUserId();
//        Long notId = postQueryRequest.getNotId();
//        // 拼接查询条件
//        if (StringUtils.isNotBlank(searchText)) {
//            queryWrapper.and(qw -> qw.like("title", searchText).or().like("content", searchText));
//        }
//        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
//        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
//        if (CollUtil.isNotEmpty(tagList)) {
//            for (String tag : tagList) {
//                queryWrapper.like("tags", "\"" + tag + "\"");
//            }
//        }
//        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
//        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
//        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
//        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
//                sortField);
//        return queryWrapper;
//    }
//
//
//
//}
//
//
//
//
