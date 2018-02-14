package filters;

import com.github.youyinnn.youquickjetty.utils.BaseHttpFilter;
import utils.JWTHelper;
import utils.JsonHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author youyinnn
 */
public class AuthenticationFilter extends BaseHttpFilter {

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String token = request.getHeader("token");
        if (!requestURI.contains("login") && !requestURI.contains("signup") && !"/user".equals(requestURI)){
            if (token != null) {
                String remoteIP = request.getRemoteAddr();
                String tokenIP = JWTHelper.getClaimAsString(token, "ip");
                if (remoteIP.equals(tokenIP) && JWTHelper.verify(token)) {
                    chain.doFilter(request,response);
                } else {
                    response.setContentType("application/json; charset=utf-8");
                    response.getWriter().println(JsonHelper.getJSONObjectDeepInPool("auth","fail1"));
                }
            } else {
                response.setContentType("application/json; charset=utf-8");
                response.getWriter().println(JsonHelper.getJSONObjectDeepInPool("auth","fail2"));
            }
        } else {
            chain.doFilter(request,response);
        }
    }

}
