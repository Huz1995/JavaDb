package parserPackage;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.ListIterator;
import java.util.Stack;
import java.util.AbstractMap.SimpleImmutableEntry;

import exceptionPackage.ErrorException;
import exceptionPackage.UnableToExecuteCmd;
import interpreterPackage.*;
import tokenPackage.FormalGrammar.RegExp;
import tokenPackage.FormalGrammar.Literals;
import tokenPackage.Tokenizer;
import tokenPackage.FormalGrammar.Operators;

public class Parser {

    private Tokenizer tokens;
    private DbState response;

    public Parser(String incomingCommand, DbState response) throws ErrorException {

        this.tokens = new Tokenizer(incomingCommand);
        this.response = response;
        this.checkLastTknSemiColon();
        this.parseQuery();

    }

    private void parseQuery() throws UnableToExecuteCmd {

        String command = this.tokens.nextToken().getValue().toUpperCase();

        if (command.equals("CREATE")) {
            CreateCmd create = new CreateCmd(this.tokens);
        } else if (command.equals("USE")) {
            UseCmd use = new UseCmd(this.tokens);
        } else if (command.equals("DROP")) {
            DropCmd drop = new DropCmd(this.tokens);
        } else if (command.equals("INSERT")) {
            InsertCmd insert = new InsertCmd(this.tokens);
        } else if (command.equals("ALTER")) {
            AlterCmd insert = new AlterCmd(this.tokens);
        } else if (command.equals("DELETE")) {
            DeleteCmd delete = new DeleteCmd(this.tokens);
        } else if (command.equals("UPDATE")) {
            UpdateCmd update = new UpdateCmd(this.tokens);
        } else if (command.equals("SELECT")) {
            SelectCmd select = new SelectCmd(this.tokens, response);
        } else if (command.equals("JOIN")) {
            JoinCmd joinCmd = new JoinCmd(this.tokens, response);
        } else {
            throw new UnableToExecuteCmd("Need a command type");
        }
    }

    private void checkLastTknSemiColon() throws UnableToExecuteCmd {

        int lastIndex = this.tokens.getTokenArray().size() - 1;
        SimpleImmutableEntry<String, String> lastTkn = this.tokens.getTokenArray().get(lastIndex);
        if (lastTkn.getValue().equals(";")) {
            return;
        } else {
            throw new UnableToExecuteCmd("Unable to parse as missing semi colon");
        }
    }

    public static void checkIsAValidName(String name) throws UnableToExecuteCmd {

        if (RegExp.OBJECTNAME.doesTokenMatch(name)) {
            return;
        } else {
            throw new UnableToExecuteCmd("Have not entered a valid name, or is missing");
        }
    }

    /*checks if token is semi colon, carryOn sting is use so parse doesn't terminate if false*/
    public static boolean checkIfSemiColon(String carryOn, String token) throws UnableToExecuteCmd {

        if (carryOn.equals("CARRYON")) {
            if (token.equals(";")) {
                return true;
            } else {
                return false;
            }
        } else {
            if (token.equals(";")) {
                return true;
            } else {
                throw new UnableToExecuteCmd("Using multiple names");
            }
        }
    }

    public static void checkIfOpeningBrckt(String token, String peekToken) throws UnableToExecuteCmd {
        if (token.equals("(")) {
            if(peekToken.equals(",")) {
                throw new UnableToExecuteCmd("Comma used in wrong place"); 
            }
            return;
        } else {
            throw new UnableToExecuteCmd("You are missing an opening bracket");
        }
    }

    public static void checkForKW(String token, String keyWord) throws UnableToExecuteCmd {
        if (token.equals(keyWord)) {
            return;
        } else {
            throw new UnableToExecuteCmd(token + " is not a valid keyword ");
        }
    }

    /*check if valid alteration type*/
    public static void checkIsAValidAlt(String token) throws UnableToExecuteCmd {
        if (token.equals("ADD")) {
            return;
        } else if (token.equals("DROP")) {
            return;
        } else {
            throw new UnableToExecuteCmd(token + " is not a valid alteration type ");
        }
    }

    public static void checkIsAValidStructure(String token) throws UnableToExecuteCmd {
        if (token.equals("DATABASE")) {
            return;
        } else if (token.equals("TABLE")) {
            return;
        } else {
            throw new UnableToExecuteCmd(token + " is not a valid structure");
        }
    }

    /*when used gets the condition segement of the tokens for analysis*/
    public static ArrayList<String> getConditionSegment(Tokenizer tokens) throws UnableToExecuteCmd {
        ArrayList<String> conditionStatement = new ArrayList<String>();
        String nextToken = tokens.getTokenArray().get(tokens.getTokenIndex()).getValue();

        if (nextToken.equals("(") || RegExp.OBJECTNAME.doesTokenMatch(nextToken)) {
            for (int i = tokens.getTokenIndex(); i < tokens.getTokenArray().size(); i++) {
                /*get the value at position i*/
                conditionStatement.add(tokens.getTokenArray().get(i).getValue());
            }
            return conditionStatement;
        }
        throw new UnableToExecuteCmd("Invalid command statement");
    }

    /*check to see if bracked at balances in a token segment*/
    public static void areBracketsBalanced(ArrayList<String> tokenSegment) throws UnableToExecuteCmd {
        Stack<String> stack = new Stack<String>();
        for (int i = 0; i < tokenSegment.size(); i++) {
            if (tokenSegment.get(i).equals("(")) {
                stack.push(tokenSegment.get(i));
            }
            if (tokenSegment.get(i).equals(")")) {
                try {
                    stack.pop();
                } catch (EmptyStackException e) {
                    throw new UnableToExecuteCmd("Brackets are not balances");
                }
            }
        }
        if (!stack.empty()) {
            throw new UnableToExecuteCmd("Brackets not balanced");
        }
    }

    /*parses the condition statement to ensure it is valid when sent to interpreter*/
    public static void isValidConditionStatement(ListIterator<String> iter) throws UnableToExecuteCmd {

        String token = iter.next().toUpperCase();
        if (token.equals(";")) {
            return;
        } else if (token.equals("(")) {
            isValidConditionStatement(iter);
        } else if (RegExp.OBJECTNAME.doesTokenMatch(token)) {
            checkIndividualCompStatement(iter);
        } else if (token.equals(")")) {
            checkIfValidClosingBrckt(iter);
        } else if (token.equals("AND") || token.equals("OR")) {
            checkIfValidAndOr(iter);
        } else {
            throw new UnableToExecuteCmd("Invalid comparison statement");
        }
    }

    /*when operater need to enure next 2 tokens are operator and literal*/
    private static void checkIndividualCompStatement(ListIterator<String> iter) throws UnableToExecuteCmd {
        if (new Operators().isTokenInGroup(iter.next())) {
            if (new Literals().isTokenInGroup(iter.next())) {
                isValidConditionStatement(iter);
            } else {
                throw new UnableToExecuteCmd("Invalid comparison statement");
            }
        } else {
            throw new UnableToExecuteCmd("Invalid comparison statement");
        }
    }

    /*check if and or preceded by coorect token*/
    private static void checkIfValidAndOr(ListIterator<String> iter) throws UnableToExecuteCmd {
        String next = iter.next();
        if (next.equals("(")) {
            iter.previous();
            isValidConditionStatement(iter);
        } else {
            throw new UnableToExecuteCmd("Invalid comparison statement");
        }
    }

    /*check if closing bracket is preceded by correct token*/
    private static void checkIfValidClosingBrckt(ListIterator<String> iter) throws UnableToExecuteCmd {
        String next = iter.next().toUpperCase();
        if (next.equals("AND") || next.equals("OR") || next.equals(";") || next.equals(")")) {
            iter.previous();
            isValidConditionStatement(iter);
        } else {
            throw new UnableToExecuteCmd("Invalid comparison statement");
        }
    }

}