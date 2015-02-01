/*
 * Copyright (c) 2015, nwillc@gmail.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package almost.functional.utils;

import java.util.logging.Logger;

/**
 * Provide consistently named instances of Logger. Names are based on the callers class name.
 */
public final class LogFactory {

	private LogFactory() {}

	/**
	 * Returns a Logger named after the callers class.
	 * @return a Logger
	 */
	public static Logger getLogger(){
		return getLogger(Thread.currentThread().getStackTrace()[2].getClassName());
	}

	/**
	 * Return a instance of Logger with a given name.
	 * @param name of the resultant instance.
	 * @return a Logger
	 */
	public static Logger getLogger(final String name){
		return Logger.getLogger(name);
	}
}
