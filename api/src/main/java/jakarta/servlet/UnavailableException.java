/*
 * Copyright (c) 1997, 2023 Oracle and/or its affiliates and others.
 * All rights reserved.
 * Copyright 2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jakarta.servlet;

/**
 * Defines an exception that a servlet or filter throws to indicate that it is permanently or temporarily unavailable.
 *
 * <p>
 * When a servlet or filter is permanently unavailable, something is wrong with it, and it cannot handle requests until
 * some action is taken. For example, a servlet might be configured incorrectly, or a filter's state may be corrupted.
 * The component should log both the error and the corrective action that is needed.
 *
 * <p>
 * A servlet or filter is temporarily unavailable if it cannot handle requests momentarily due to some system-wide
 * problem. For example, a third-tier server might not be accessible, or there may be insufficient memory or disk
 * storage to handle requests. A system administrator may need to take corrective action.
 *
 * <p>
 * Servlet containers can safely treat both types of unavailable exceptions in the same way. However, treating temporary
 * unavailability effectively makes the servlet container more robust. Specifically, the servlet container might block
 * requests to the servlet or filter for a period of time suggested by the exception, rather than rejecting them until
 * the servlet container restarts.
 *
 *
 * @author Various
 *
 */
public class UnavailableException extends ServletException {

    private static final long serialVersionUID = 5622686609215003468L;

    private boolean permanent; // needs admin action?
    private int seconds; // unavailability estimate

    /**
     *
     * Constructs a new exception with a descriptive message indicating that the servlet is permanently unavailable.
     *
     * @param msg a <code>String</code> specifying the descriptive message
     *
     */
    public UnavailableException(String msg) {
        super(msg);

        permanent = true;
    }

    /**
     * Constructs a new exception with a descriptive message indicating that the servlet is temporarily unavailable and
     * giving an estimate of how long it will be unavailable.
     *
     * <p>
     * In some cases, the servlet cannot make an estimate. For example, the servlet might know that a server it needs is not
     * running, but not be able to report how long it will take to be restored to functionality. This can be indicated with
     * a negative or zero value for the <code>seconds</code> argument.
     *
     * @param msg a <code>String</code> specifying the descriptive message, which can be written to a log file or displayed
     * for the user.
     *
     * @param seconds an integer specifying the number of seconds the servlet expects to be unavailable; if zero or
     * negative, indicates that the servlet can't make an estimate
     *
     */
    public UnavailableException(String msg, int seconds) {
        super(msg);

        if (seconds <= 0)
            this.seconds = -1;
        else
            this.seconds = seconds;

        permanent = false;
    }

    /**
     *
     * Returns a <code>boolean</code> indicating whether the servlet is permanently unavailable. If so, something is wrong
     * with the servlet, and the system administrator must take some corrective action.
     *
     * @return <code>true</code> if the servlet is permanently unavailable; <code>false</code> if the servlet is available
     * or temporarily unavailable
     *
     */
    public boolean isPermanent() {
        return permanent;
    }

    /**
     * Returns the number of seconds the servlet expects to be temporarily unavailable.
     *
     * <p>
     * If this method returns a negative number, the servlet is permanently unavailable or cannot provide an estimate of how
     * long it will be unavailable. No effort is made to correct for the time elapsed since the exception was first
     * reported.
     *
     * @return an integer specifying the number of seconds the servlet will be temporarily unavailable, or a negative number
     * if the servlet is permanently unavailable or cannot make an estimate
     *
     */
    public int getUnavailableSeconds() {
        return permanent ? -1 : seconds;
    }
}
