package com.bpa.qaproduct.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.hibernate.validator.util.privilegedactions.GetMethodFromPropertyName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.bpa.qaproduct.service.UserTokenService;

@Component
public class ServiceFilter implements Filter {
	
	//FilterConfig filterConfig;
	
	@Autowired
	private UserTokenService userTokenService;
    
	// Over-riding the doFilter method to process our logic for Session Filter. 
//	 Over-riding the doFilter method to process our logic for Session Filter. 
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
            FilterChain filterChain)
            throws IOException, ServletException {
        
        // Get an HttpServletRequest and Response.
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        
        // Set up the context relative request path.
        String contextRelativeUri = httpRequest.getRequestURI();
        contextRelativeUri = contextRelativeUri.replaceFirst(httpRequest.getContextPath(), "");
       // String userTokenValue = servletRequest.getParameter("userToken");
        String userTokenValue = httpRequest.getHeader("userToken");
        String loggedInUserId = httpRequest.getParameter("userId");
        
        boolean isContextUriMatches = true;
        if(contextRelativeUri.equalsIgnoreCase("/user/loginVerification.action") ){
        	isContextUriMatches = false;
        }else if (contextRelativeUri.equalsIgnoreCase("/customer/saveCustomer.action")) {
        	isContextUriMatches = false;
		}
        else if (contextRelativeUri.equalsIgnoreCase("/user/forgotPassword.action")) {
        	isContextUriMatches = false;
		}
		else if (contextRelativeUri.equalsIgnoreCase("/user/downloadUrlProperty.action")) {
        	isContextUriMatches = false;
		}
		else if (contextRelativeUri.equalsIgnoreCase("/executionResult/getDownloadReport.action")) {
        	isContextUriMatches = false;
		}
		else if (contextRelativeUri.equalsIgnoreCase("/executionResult/getExecutionDownloadReport.action")) {
        	isContextUriMatches = false;
		}
        
        //if (!(contextRelativeUri.equalsIgnoreCase("/user/loginVerification.action")) || (!(contextRelativeUri.equals("/customer/saveCustomer.action"))))
       // if ( !((contextRelativeUri.equalsIgnoreCase("/user/loginVerification.action"))) || !((contextRelativeUri.equalsIgnoreCase("/customer/saveCustomer.action"))) ||  !((contextRelativeUri.equalsIgnoreCase("/user/forgotPassword.action"))) )
       // ApplicationContext appCtx;
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        
		if (isContextUriMatches) {
			if (userTokenValue != null) {
				try {

					boolean status = userTokenService.validateToken(
							userTokenValue, loggedInUserId);
					if (status) {
						

					} else {
						
						System.out.println("Authorization denied");
						httpResponse.setStatus(403);
						return;
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}

			} else {
				System.out.println("Authorization denied");
				httpResponse.setStatus(403);
				return;
			}
		}
        // Continuing with the doFilter Method.
      filterChain.doFilter(servletRequest, servletResponse);
        
        
    }
	
	public void init(FilterConfig filterConfig) throws ServletException {
    	//this.filterConfig = filterConfig;
    }
    
    public void destroy() {
    }
}