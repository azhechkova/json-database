package client.utils;

import com.beust.jcommander.Parameter;

public class CliArgs {

    @Parameter(names = "-t", description = "Type of the request")
    private String method;

    @Parameter(names = "-k", description = "Index of the cell")
    private String key;

    @Parameter(names = "-v", description = "The value to save in the database")
    private String value;

    @Parameter(names = "-in", description = "The value to save in the database")
    private String fileName;

    public CliArgs() {
    }

    public String getMethod() {
        return method;
    }


    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getFileName() {
        return fileName;
    }
}
