package cn.ucas.irsys.utils;

import java.util.UUID;

public class IdGenerator {
	public static String idGenerator() {
		String idStr = UUID.randomUUID().toString();
		return idStr;
	}
}
