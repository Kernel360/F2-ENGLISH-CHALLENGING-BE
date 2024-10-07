package com.echall.platform.oauth2;

import com.echall.platform.message.error.code.UserErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
        throws IOException, ServletException {
        UserErrorCode userErrorCode = UserErrorCode.USER_PERMISSION_DENIED;

        log.error(userErrorCode.getCode() + " : " + userErrorCode.getMessage());

        setErrorResponse(response, userErrorCode);
    }

    private void setErrorResponse(HttpServletResponse response, UserErrorCode userErrorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(userErrorCode.getStatus().value());

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonResponse = objectMapper.writeValueAsString(
            Map.of("code", userErrorCode.getCode(),
                "message", userErrorCode.getMessage())
        );

        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}
