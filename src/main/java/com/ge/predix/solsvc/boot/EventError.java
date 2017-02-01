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

/**
 * 
 * @author 212421693 -
 */
public class EventError
{
    /**
     * status
     */
    public Integer status;
    /**
     * 
     */
    /**
     * error
     */
    public String  error;
    /**
     * 
     */
    /**
     * message
     */
    public String  message;
    /**
     * timeStamp
     */
    public String  timeStamp;

    /**
     * @param status
     *            -
     * @param errorAttributes
     *            -
     */
    public EventError(int status, Map<String, Object> errorAttributes)
    {
        this.status = status;
        this.error = (String) errorAttributes.get("error"); //$NON-NLS-1$
        this.message = (String) errorAttributes.get("message"); //$NON-NLS-1$
        this.timeStamp = errorAttributes.get("timestamp").toString(); //$NON-NLS-1$

    }
}
