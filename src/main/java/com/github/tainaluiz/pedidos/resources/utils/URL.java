package com.github.tainaluiz.pedidos.resources.utils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class URL {

	public static List<Long> decodeLongList(String s) throws NumberFormatException {
		return Arrays.asList(s.split(",")).stream().map(Long::parseLong).collect(Collectors.toList());
	}

	public static String decodeParam(String s) {
		return URLDecoder.decode(s, StandardCharsets.UTF_8);
	}
}
