package servicePackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import exceptionPackage.UnableToExecuteCmd;
import parserPackage.DBLocation;
import parserPackage.DbState;

public class Service {
    
    public static void createDatabase(String dbName) throws UnableToExecuteCmd {

        String dbPath = DBLocation.PATH.get() + dbName;
        File newDB = new File(dbPath);
        if (!newDB.mkdirs()) {
            throw new UnableToExecuteCmd("Database: " + dbName + " is already built");
        }
    }

    public static void createTable(TableStructure table) throws UnableToExecuteCmd {
        String tablePath = DBLocation.PATH.get() + DbState.getDbInUse() + File.separator + table.getTableName()
                + ".txt";
        File newTableFile = new File(tablePath);
        try {
            if (!newTableFile.createNewFile()) {
                throw new UnableToExecuteCmd("This table already exists");
            }
        } catch (IOException e) {
            throw new UnableToExecuteCmd("This table already exists");
        }
        writeRow(table.getInsertRow(), newTableFile, true);
    }

    public static void setDBInUse(String dbName) throws UnableToExecuteCmd {
        File database = new File(DBLocation.PATH.get() + dbName);
        if (database.isDirectory()) {
            DbState.setDbInUse(dbName);
        } else {
            throw new UnableToExecuteCmd(" This database doesn't exist");
        }
    }

    public static void dropDatabase(String dbName) throws UnableToExecuteCmd {
        File dbPath = new File(DBLocation.PATH.get() + dbName);
        if (dbPath.isDirectory()) {
            String[] tableNames = dbPath.list();
            for (String table : tableNames) {
                File tablePath = new File(DBLocation.PATH.get() + dbName + File.separator + table);
                tablePath.delete();
            }
            dbPath.delete();
        } else {
            throw new UnableToExecuteCmd("This database doesn't exist");
        }
    }

    public static void dropTable(String tableName) throws UnableToExecuteCmd {
        File tablePath = new File(DBLocation.PATH.get() + DbState.getDbInUse() + File.separator + tableName + ".txt");
        if (tablePath.exists()) {
            tablePath.delete();
        } else {
            throw new UnableToExecuteCmd("This table doesn't exist");
        }
    }

    public static void loadInTable(TableStructure table) throws UnableToExecuteCmd {
        File tablePath = new File(
                DBLocation.PATH.get() + DbState.getDbInUse() + File.separator + table.getTableName() + ".txt");
        try {
            FileReader reader = new FileReader(tablePath);
            BufferedReader bReader = new BufferedReader(reader);
            String line = bReader.readLine();
            while (line != null) {
                String[] splitRow = line.split("\t");
                table.addRowToTable(splitRow);
                line = bReader.readLine();
            }
            reader.close();
            bReader.close();
        } catch (FileNotFoundException e) {
            throw new UnableToExecuteCmd("This table doesn't exist");
        } catch (IOException e) {
            throw new UnableToExecuteCmd("Unable to read table");
        }

    }

    public static void storeTableInDB(TableStructure table) throws UnableToExecuteCmd {
        File tablePath = new File(
                DBLocation.PATH.get() + DbState.getDbInUse() + File.separator + table.getTableName() + ".txt");

        for (int i = 0; i < table.getStringTable().size(); i++) {
            if (i == 0) {
                writeRow(table.getStringTable().get(i), tablePath, false);
            } else {
                writeRow(table.getStringTable().get(i), tablePath, true);
            }
        }

    }

    /*takes in a row and builds a string builder*/
    private static StringBuilder buildString(ArrayList<String> row) {
        StringBuilder builder = new StringBuilder();
        for (String el : row) {
            builder.append(el);
            builder.append('\t');
        }
        builder.append('\n');
        return builder;
    }

    private static void writeString(File path, StringBuilder builder, boolean append) throws IOException {
        FileWriter writer = new FileWriter(path, append);
        writer.write(builder.toString());
        writer.flush();
        writer.close();
    }

    /*writes table row to specifed path*/
    public static void writeRow(ArrayList<String> row, File file, boolean append) throws UnableToExecuteCmd {
        try {
            StringBuilder builder = buildString(row);
            writeString(file, builder, append);
        } catch (IOException e) {
            throw new UnableToExecuteCmd("Unable to write to the table");
        }
    }

}
