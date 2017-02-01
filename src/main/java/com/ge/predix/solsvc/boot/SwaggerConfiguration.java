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

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 
 * @author 212421693 -
 */
@Configuration
public class SwaggerConfiguration extends WebMvcConfigurerAdapter
{
    @Override
    public void addViewControllers(ViewControllerRegistry registry)
    {
        registry.addRedirectViewController("/api/v2/api-docs", "/v2/api-docs"); //$NON-NLS-1$ //$NON-NLS-2$
        registry.addRedirectViewController("/api/configuration/ui", "/configuration/ui"); //$NON-NLS-1$ //$NON-NLS-2$
        registry.addRedirectViewController("/api/configuration/security", "/configuration/security"); //$NON-NLS-1$ //$NON-NLS-2$
        registry.addRedirectViewController("/api/swagger-resources", "/swagger-resources"); //$NON-NLS-1$ //$NON-NLS-2$
 
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        registry.addResourceHandler("/api/**").addResourceLocations("classpath:/META-INF/resources/"); //$NON-NLS-1$ //$NON-NLS-2$
    }
}
