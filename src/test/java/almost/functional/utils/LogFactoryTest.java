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

import com.github.nwillc.contracts.PrivateConstructorContract;
import org.junit.Test;

import java.util.logging.Logger;

import static almost.functional.utils.LogFactory.getLogger;
import static org.assertj.core.api.Assertions.assertThat;

public class LogFactoryTest extends PrivateConstructorContract {

    @Override
    public Class<?> getClassToTest() {
        return LogFactory.class;
    }

    @Test
	public void shouldGetClassName() throws Exception {
	   Logger logger = getLogger();

		assertThat(logger).isNotNull();
		assertThat(logger.getName()).isEqualTo(getClass().getName());
	}

	@Test
	public void shouldGetLogger() throws Exception {
		Logger logger1 = getLogger(getClass().getName());
		Logger logger2 = getLogger();

		assertThat(logger2).isEqualTo(logger1);
	}
}
