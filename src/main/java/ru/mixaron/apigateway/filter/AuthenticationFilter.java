package ru.mixaron.apigateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//@Component
//public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
//
//    @Autowired
//    private RouterValidator validator;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    // Инициализация логгера
//    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
//
//    public AuthenticationFilter() {
//        super(Config.class);
//    }
//
//    @Override
//    public GatewayFilter apply(Config config) {
//        return ((exchange, chain) -> {
//            if (validator.isSecured.test(exchange.getRequest())) {
//                logger.info("Request to secured route: {}", exchange.getRequest().getURI());
//
//                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
//                    logger.warn("Missing Authorization Header");
//                    return onError(exchange, "Missing Authorization Header", HttpStatus.UNAUTHORIZED);
//                }
//
//                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
//                if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                    authHeader = authHeader.substring(7);
//                } else {
//                    logger.warn("Invalid Authorization Header format");
//                    return onError(exchange, "Invalid Authorization Header", HttpStatus.UNAUTHORIZED);
//                }
//
//                logger.info("Token received: {}", authHeader);  // Логирование токена
//
//                try {
//                    jwtUtil.validateToken(authHeader);  // Валидация JWT
//                    logger.info("JWT token validated successfully");
//                } catch (Exception e) {
//                    logger.error("JWT validation failed: {}", e.getMessage());
//                    return onError(exchange, "Unauthorized Access", HttpStatus.UNAUTHORIZED);
//                }
//            }
//            return chain.filter(exchange);
//        });
//    }
//
//
//    // Метод для возврата ошибок
//    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
//        ServerHttpResponse response = exchange.getResponse();
//        response.setStatusCode(httpStatus);
//        logger.error("Error occurred: {}, status code: {}", err, httpStatus);
//        return response.setComplete();
//    }
//
//    public static class Config {
//        // Конфигурация фильтра, если потребуется
//    }
//}

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Value("${jwt.secret}")
    private String secret;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // Логирование для отладки
            System.out.println("Request headers: " + request.getHeaders());

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                System.out.println("No Authorization header");
                return onError(exchange, "No Authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String token = authHeader.replace("Bearer ", "");
            System.out.println("Token: " + token);

            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                System.out.println("Claims: " + claims);
                String id = claims.get("id", String.class);
                request = exchange.getRequest().mutate()
                        .header("id", id)
                        .build();
                return chain.filter(exchange.mutate().request(request).build());
            } catch (Exception e) {
                System.out.println("Invalid token: " + e.getMessage());
                return onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
            }
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    public static class Config {
    }
}