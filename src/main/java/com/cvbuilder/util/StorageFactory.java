package com.cvbuilder.util;

import jakarta.servlet.ServletContext;

/**
 * UTILITY - A simple factory to get our FileStorage.
 *
 * We use ServletContext to get the data directory configured in web.xml.
 * This way all servlets share the same storage location.
 */
public class StorageFactory {

    private static FileStorage instance;

    /**
     * Returns the single FileStorage instance.
     * Creates it on first call using the data directory from web.xml context-param.
     */
    public static synchronized FileStorage getStorage(ServletContext context) {
        if (instance == null) {
            String dataDir = context.getInitParameter("dataDir");
            if (dataDir == null || dataDir.isEmpty()) {
                dataDir = System.getProperty("java.io.tmpdir") + "/cvbuilder-data";
            }
            instance = new FileStorage(dataDir);
            System.out.println("CV Builder storage initialized at: " + dataDir);
        }
        return instance;
    }
}
