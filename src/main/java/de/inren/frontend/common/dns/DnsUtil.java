/**
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.inren.frontend.common.dns;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Resolve hostname for an ip address.
 * 
 * @author Ingo Renner
 *
 */
public class DnsUtil {
    
    //** no instance */
    private DnsUtil() {
        super();
    }
    
    private static boolean isHostname(String s) {
        char[] ca = s.toCharArray();
        // if we see a character that is neither a digit nor a period
        // then s is probably a hostname
        for (int i = 0; i < ca.length; i++) {
            if (!Character.isDigit(ca[i]) && ca[i] != '.') {
                return true;
            }
        }
        // Everything was either a digit or a period
        // so s looks like an IP address in dotted quad format
        return false;
    }

    public static String lookup(String s) {
        if (isHostname(s)) {
            return s;
        } else {
            try {
                return InetAddress.getByName(s).getHostName();
            } catch (UnknownHostException e) {
                return "";
            }
        }
    }
}
