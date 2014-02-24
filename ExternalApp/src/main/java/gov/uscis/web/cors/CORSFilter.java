package gov.uscis.web.cors;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CORSFilter extends OncePerRequestFilter {
    private Set<String> whitelist = new HashSet<>(Arrays.asList(new String[] {
            "http://useracctmgmt.acorn.ads.example.com:8080",
            "https://useracctmgmt.acorn.ads.example.com:8080"
    }));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (request.getMethod().equals("OPTIONS")) {
            response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
            response.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept");
            response.addHeader("Access-Control-Max-Age", "1800");
        }

        String origin = request.getHeader("Origin");

        if (origin != null && whitelist.contains(origin)) {
            response.addHeader("Access-Control-Allow-Origin", origin);
            response.addHeader("Access-Control-Allow-Credentials", "true");
        }

        filterChain.doFilter(request, response);
    }
}