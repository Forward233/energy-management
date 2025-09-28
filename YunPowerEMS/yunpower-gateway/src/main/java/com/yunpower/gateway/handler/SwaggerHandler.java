package com.yunpower.gateway.handler;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import com.yunpower.gateway.config.SwaggerProvider;

@RestController
@RequestMapping("/swagger-resources")
public class SwaggerHandler
{
    private final SwaggerProvider swaggerProvider;

    public SwaggerHandler(SwaggerProvider swaggerProvider)
    {
        this.swaggerProvider = swaggerProvider;
    }

    @SuppressWarnings("rawtypes")
    @GetMapping("")
    public Mono<ResponseEntity> swaggerResources()
    {
        return Mono.just((new ResponseEntity<>(swaggerProvider.get(), HttpStatus.OK)));
    }
}
