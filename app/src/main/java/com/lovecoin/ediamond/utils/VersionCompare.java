package com.lovecoin.ediamond.utils;

/**
 * Created on 2017/12/21.
 */

public class VersionCompare {
    /**
     * 版本号比较
     *
     * @param serverVersion
     * @param localVersion
     * @return
     */
    public static int compare(String serverVersion, String localVersion) {
        if (serverVersion.equals(localVersion)) {
            return 0;
        }
        String[] version1Array = serverVersion.split("\\.");
        String[] version2Array = localVersion.split("\\.");
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        for (int i = 0; i < minLen; i++) {
            diff = Integer.parseInt(version1Array[i]) - Integer.parseInt(version2Array[i]);
            if (diff != 0) {
                break;
            }
        }

        if (diff == 0) {
            // 如果位数不一致，比较多余位数
            for (int i = minLen; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = minLen; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }

    public static boolean needUpdate(String serverVersion, String localVersion) {
        return compare(serverVersion, localVersion) > 0;
    }
}
