package parserPackage;

import java.util.ArrayList;

import exceptionPackage.UnableToExecuteCmd;

public class DbState {

    static private String dbInUse;
    private ArrayList<ArrayList<String>> stringTable;
    private StringBuilder clientResponse;

    public DbState() {
        this.clientResponse = new StringBuilder();
        clientResponse.append("[OK]");
    }

    static public String getDbInUse() {
        return dbInUse;
    }

    public static void setDbInUse(String newDbInUse) {
        dbInUse = newDbInUse;
    }

    public static void isDBInUse() throws UnableToExecuteCmd {
        if (dbInUse == null) {
            throw new UnableToExecuteCmd("You have not loaded database with USE command");
        }
    }

    /*takkes in table from table class*/
    public void setStringTable(ArrayList<ArrayList<String>> stringTable) {
        this.stringTable = stringTable;
    }

    /*builds response for the client to see out of table*/
    public void buildResponse() {
        for (int j = 0; j < stringTable.size(); j++) {
            if (j == 0) {
                clientResponse.append("\n");
            }
            for (int i = 0; i < stringTable.get(0).size(); i++) {
                if (stringTable.get(j).get(i) != null) {
                    clientResponse.append(stringTable.get(j).get(i));
                    clientResponse.append("\t");
                }
            }
            clientResponse.append("\n");
        }
    }

    public String getClientResponse() {
        return this.clientResponse.toString();
    }

}
