package com.kthksgy.bloginnersystem.constants;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class CommonSort {
	public static final Sort CREATED_TIME_DESC = Sort.by(Direction.DESC, "createdTime");
}
