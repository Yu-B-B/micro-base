package com.demo.exception;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.demo.result.ER;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;

@Component
public class FlowException implements BlockExceptionHandler {
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       BlockException e) throws Exception {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        ER error = ER.error(500, "限流");
        String s = objectMapper.writeValueAsString(error);
        writer.write(s);

        writer.flush();
        writer.close();

    }
}
