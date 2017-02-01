/*
 * Copyright (c) 2016 General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.ge.predix.solsvc.boot;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 
 * @author 212421693 -
 */
@RestController
public class ErrorResponseController
        implements ErrorController
{
    private static final String PATH = "/error"; //$NON-NLS-1$

    @Value("${debug:false}")
    private boolean             debug;

    @Autowired
    private ErrorAttributes     errorAttributes;

    /**
     * @param request -
     * @param response -
     * @return -
     */
    @RequestMapping(value = PATH)
    EventError error(HttpServletRequest request, HttpServletResponse response)
    {
        // Appropriate HTTP response code (e.g. 404 or 500) is automatically set by Spring.
        // Here we just define response body.
        return new EventError(response.getStatus(), getErrorAttributes(request, this.debug));
    }

    @Override
    public String getErrorPath()
    {
        return PATH;
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace)
    {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return this.errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
    }
}
