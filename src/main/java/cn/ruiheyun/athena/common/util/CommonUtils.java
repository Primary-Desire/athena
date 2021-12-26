package cn.ruiheyun.athena.common.util;

import java.util.Locale;
import java.util.UUID;

public class CommonUtils {

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase(Locale.ROOT);
    }

}
