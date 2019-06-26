package com.ts.springboot.security.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomExpiredSessionStrategy implements SessionInformationExpiredStrategy {
    //@Autowired
    private ObjectMapper objectMapper=new ObjectMapper();
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent) throws IOException, ServletException {
        Map<String, Object> map = new HashMap<String, Object>(16);
        map.put("code",0);
        map.put("msg","已经另一台机器登录，你被迫下线。"+sessionInformationExpiredEvent.getSessionInformation().getLastRequest());
        String json = objectMapper.writeValueAsString(map);
        sessionInformationExpiredEvent.getResponse().setContentType("application/json;charset=utf-8");
        sessionInformationExpiredEvent.getResponse().getWriter().write(json);


    }
}
