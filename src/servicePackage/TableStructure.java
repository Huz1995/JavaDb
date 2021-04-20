package servicePackage;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.AbstractMap.SimpleImmutableEntry;

import exceptionPackage.UnableToExecuteCmd;
import tokenPackage.FormalGrammar.RegExp;

public class TableStructure {

    private ArrayList<ArrayList<String>> stringTable;
    private ArrayList<ArrayList<Object>> literalTable;
    private ArrayList<Boolean> doesRowMeetCondition;
    private ArrayList<SimpleImmutableEntry<Integer, String>> keyValPairs;
    private ArrayList<String> insertRow;
    private ArrayList<Integer> selectIndexs;
    private String tableName;

    public TableStructure(String tableName) {
        this.insertRow = new ArrayList<String>();
        this.literalTable = new ArrayList<ArrayList<Object>>();
        this.stringTable = new ArrayList<ArrayList<String>>();
        this.doesRowMeetCondition = new ArrayList<Boolean>();
        this.keyValPairs = new ArrayList<SimpleImmutableEntry<Integer, String>>();
        this.selectIndexs = new ArrayList<Integer>();
        this.tableName = tableName;

        this.insertRow.add("id");
    }

    public String getTableName() {
        return this.tableName;
    }

    /*insert new attribute or val into row for inserting*/
    public void setRowValue(String attribute) {
        this.insertRow.add(attribute);
    }

    public ArrayList<String> getInsertRow() {
        return this.insertRow;
    }

    /*removes default "id" if need be*/
    public void removeDefaultIdFromInsertRow() {
        if (insertRow.get(0).equals("id")) {
            this.getInsertRow().remove(0);
        }
    }

    /*removes value at specifed index in row*/
    public void removeColInRow(int rowNum, int colNum) {
        this.stringTable.get(rowNum).remove(colNum);
    }

    /*gets the string version of the table*/
    public ArrayList<ArrayList<String>> getStringTable() {
        return this.stringTable;
    }

    /*adds new attribute at end of row*/
    public void alterAddCol(String newAttribute, int rowNum) {
        this.stringTable.get(rowNum).add(newAttribute);
    }

    /*takes in row from service and puts in string table*/
    public void addRowToTable(String[] row) {

        ArrayList<String> arrayList = new ArrayList<String>();

        for (int i = 0; i < row.length; i++) {
            arrayList.add(row[i]);
        }

        this.stringTable.add(arrayList);
    }

    /*adds new inserted row to the table*/
    public void addInsertRowToTable() {
        this.stringTable.add(this.insertRow);
    }

    /*get the index of specified attribute/col in table*/
    public int getAttributePos(String attName, String allow_id) throws UnableToExecuteCmd {
        int pos;
        if (allow_id.equals("allow_id")) {
            for (int i = 0; i < this.stringTable.get(0).size(); i++) {

                if (attName.equals(stringTable.get(0).get(i))) {
                    pos = i;
                    return pos;
                }
            }
            throw new UnableToExecuteCmd("Attribute doesn't not exist in the table");
        } else {
            for (int i = 1; i < this.stringTable.get(0).size(); i++) {

                if (attName.equals(stringTable.get(0).get(i))) {
                    pos = i;
                    return pos;
                }
            }
            throw new UnableToExecuteCmd("Attribute doesn't not exist in the table");
        }

    }

    /*converts all the string values in db to real datatypes*/
    public void convertValToLiteral() {

        for (int j = 0; j < this.stringTable.size(); j++) {
            ArrayList<Object> row = new ArrayList<Object>();
            for (int i = 0; i < this.stringTable.get(0).size(); i++) {
                if (RegExp.FLOATLIT.doesTokenMatch(this.stringTable.get(j).get(i))) {
                    row.add(Double.parseDouble(this.stringTable.get(j).get(i)));
                } else if (RegExp.INTLIT.doesTokenMatch(this.stringTable.get(j).get(i))) {
                    row.add(Integer.parseInt(this.stringTable.get(j).get(i)));
                } else if (RegExp.BOOLLIT.doesTokenMatch(this.stringTable.get(j).get(i))) {
                    row.add(Boolean.valueOf(this.stringTable.get(j).get(i)));
                } else {
                    row.add(this.stringTable.get(j).get(i));
                }
            }
            this.literalTable.add(row);
        }
    }

    /*converts actual datatypes back to strings for storing in db*/
    public void convertLitToVal() {

        this.stringTable = new ArrayList<ArrayList<String>>();

        for (int j = 0; j < this.literalTable.size(); j++) {
            ArrayList<String> row = new ArrayList<String>();
            for (int i = 0; i < this.literalTable.get(0).size(); i++) {
                row.add(this.literalTable.get(j).get(i).toString());
            }
            this.stringTable.add(row);
        }
        for (int j = 0; j < this.literalTable.size(); j++) {
            this.literalTable.remove(j);
        }
    }

    /*returns the literal datatype full table*/
    public ArrayList<ArrayList<Object>> getLitValList() {
        return this.literalTable;
    }

    /*adds truth result of conditions applied to row*/
    public void addTruthyToRow(boolean doesRowMeetCond) {
        this.doesRowMeetCondition.add((doesRowMeetCond));
    }

    /*removes row if delete or select queries*/
    public void executeRemoveRow(String type) {
        if (type.equals("DELETE")) {
            this.doesRowMeetCondition.add(0, false);
        } else {
            this.doesRowMeetCondition.add(0, true);
        }
        ListIterator<ArrayList<Object>> litIter = this.literalTable.listIterator();
        ListIterator<Boolean> boolIter = this.doesRowMeetCondition.listIterator();
        while (litIter.hasNext()) {
            boolean toBeDel = boolIter.next();
            litIter.next();
            if (type.equals("DELETE")) {
                if (toBeDel == true) {
                    litIter.remove();
                    boolIter.remove();
                }
            } else {
                if (toBeDel == false) {
                    litIter.remove();
                    boolIter.remove();
                }
            }
        }
        this.convertLitToVal();
    }

    /*adds attribute index and value for updating values in table*/
    public void addKeyValPair(SimpleImmutableEntry<Integer, String> keyValPair) {
        this.keyValPairs.add(keyValPair);
    }

    /*uses keyvalue pairs to update values in table*/
    public void executeUpdate() {
        this.doesRowMeetCondition.add(0, false);
        for (int i = 1; i < this.literalTable.size(); i++) {
            if (this.doesRowMeetCondition.get(i) == true) {
                for (int j = 0; j < this.keyValPairs.size(); j++) {
                    this.literalTable.get(i).set(
                        this.keyValPairs.get(j).getKey(), 
                        this.keyValPairs.get(j).getValue());
                }
            }
        }
        this.convertLitToVal();
    }

    public void setSelectIndexs(int index) {
        this.selectIndexs.add(index);
    }

    public void removeUnselectedColumns() {
        for (int j = 0; j < this.stringTable.size(); j++) {
            for (int i = 0; i < this.stringTable.get(0).size(); i++) {
                if (this.selectIndexs.contains(i)) {
                    continue;
                } else {
                    this.stringTable.get(j).set(i, null);
                }
            }
        }
    }

    public void addLiteralRow(ArrayList<Object> litRow) {
        this.literalTable.add(litRow);
    }
}
