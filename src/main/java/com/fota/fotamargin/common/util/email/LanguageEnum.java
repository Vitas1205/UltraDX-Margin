package com.fota.fotamargin.common.util.email;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author jiguang.qi
 * Created on 2018/8/24
 * Description
 */
public enum LanguageEnum {

	CHINESE(1, "zh"),
	ENGLISH(2, "en"),
	;

	private static Map<Integer, LanguageEnum> map;

	static {
		map = Arrays.stream(LanguageEnum.values())
				.collect(Collectors.toMap(LanguageEnum::getType, Function.identity()));
	}

	private Integer type;

	private String tag;

	LanguageEnum(Integer type, String tag) {
		this.type = type;
		this.tag = tag;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public static LanguageEnum getByType(Integer type) {
		return map.get(type);
	}
}
