/*******************************************************************************
 * Copyright (c) 2015 jftreading.wordpress.com.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     jftreading.wordpress.com - email: machinehead449@gmail.com
 ******************************************************************************/
package com.wordpress.jftreading;

public interface WebViewInterface {
    public boolean networkIsUp();
    public String getErrorPage();
    public String linkSelector();
}
