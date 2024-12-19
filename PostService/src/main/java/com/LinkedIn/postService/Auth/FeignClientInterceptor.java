package com.LinkedIn.postService.Auth;
import org.springframework.stereotype.Component;
import feign.RequestInterceptor;
import feign.RequestTemplate;


@Component
public class FeignClientInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        Long userId = UserContextHolder.getCurrentUserId();
        if(userId!=null){
            requestTemplate.header("UserId", userId.toString());
        }
    }
}
