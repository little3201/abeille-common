/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.common.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;

/**
 * Controller Parent
 *
 * @author liwenqiang 2018/12/28 14:40
 **/
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public abstract class AbstractControllerMock<T> {

    /**
     * 开启日志
     */
    private static final Logger log = LoggerFactory.getLogger(AbstractControllerMock.class);

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String APPLICATION_JSON_UTF8 = "application/json;charset=UTF-8";
    private final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";
    private MockMvc mockMvc;

    /**
     * 设置要测试的controller
     *
     * @return T
     */
    protected abstract T getController();

    public MockMvc getMockMvc() {
        return mockMvc;
    }

    @Before
    public void setupMock() {
        /* initialize mock object */
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.getController()).build();
        MockitoAnnotations.initMocks(this);
    }

    /**
     * ====================  POST 重载 添加参数到request body ====================
     */
    public MvcResult postTest(String url, Object obj) throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(url).accept(APPLICATION_JSON_UTF8)
                .contentType(APPLICATION_JSON_UTF8).content(objectMapper.writeValueAsString(obj)));
        return resultActions.andReturn();
    }

    /**
     * ====================  POST 重载 添加Map类型参数到请求中 ====================
     */
    public MvcResult postTest(String url, MultiValueMap<String, String> params) throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(url).params(params).accept(APPLICATION_JSON_UTF8)
                .contentType(APPLICATION_JSON_UTF8));
        return resultActions.andReturn();
    }

    /**
     * ====================  GET 重载 添加参数到request body ====================
     */
    public MvcResult getTest(String url, Object obj) throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(url).accept(APPLICATION_FORM_URLENCODED)
                .contentType(APPLICATION_JSON_UTF8).content(objectMapper.writeValueAsString(obj)));
        return resultActions.andReturn();
    }

    /**
     * ====================  GET 重载 添加Map类型参数到请求中 ====================
     */
    public MvcResult getTest(String url, MultiValueMap<String, String> params) throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(url).params(params).accept(APPLICATION_FORM_URLENCODED)
                .contentType(APPLICATION_JSON_UTF8));
        return resultActions.andReturn();
    }

    /**
     * ====================  PUT  ====================
     */
    public MvcResult putTest(String url, Object obj) throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put(url).accept(APPLICATION_JSON_UTF8)
                .contentType(APPLICATION_JSON_UTF8).content(objectMapper.writeValueAsString(obj)));
        return resultActions.andReturn();
    }

    /**
     * ====================  DELETE  ====================
     */
    public MvcResult deleteTest(String url, Object obj) throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete(url).accept(APPLICATION_FORM_URLENCODED)
                .contentType(APPLICATION_JSON_UTF8).content(objectMapper.writeValueAsString(obj)));
        return resultActions.andReturn();
    }
}
