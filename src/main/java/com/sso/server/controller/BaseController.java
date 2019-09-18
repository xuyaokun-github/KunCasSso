/**
 *
 */
package com.sso.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @project web-sso
 * @description
 * @version 1.0
 * @history 修订历史（历次修订内容、修订人、修订时间等）
 *
 */
@Controller
@RequestMapping(value="/base")
public class BaseController {
	
	private static final Logger log = LoggerFactory.getLogger(BaseController.class);

	public static String trim(Object o) {
		if (o == null) return "";
		return String.valueOf(o).trim();
	}

}
