package parserPackage;

import java.io.File;

public enum DBLocation {
    PATH(".." + File.separator + "database" + File.separator);

    String path;

    private DBLocation(String path) {
        this.path = path;
    }

    public String get() {
        return path;
    }
}
