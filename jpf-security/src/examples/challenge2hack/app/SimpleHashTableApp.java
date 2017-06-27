package challenge2hack.app;

import challenge2hack.util.*;
import java.io.*;
import java.util.*;

public class SimpleHashTableApp
{
    private static Command[] COMMANDS=null;;
    private static Map<String, Command> COMMANDS_BY_NAME=null;
    
    public static void main(final String[] array) {
        if (array.length < 1) {
            printHelp("missing argument");
        }
        else if (array.length > 1) {
            printHelp("too many arguments");
        }
        else if (array[0].equals("--help") || array[0].equals("-h")) {
            printHelp(null);
        }
        int int1 = 0;
        try {
            int1 = Integer.parseInt(array[0]);
        }
        catch (NumberFormatException ex) {
            printHelp("table-size must be an integer: " + array[0]);
        }
        if (int1 <= 0) {
            printHelp(new StringBuilder().append("table-size must be positive: ").append(int1).toString());
        }
        final HashTable hashTable = new HashTable(int1);
        System.err.format("Welcome to %s.%n", new Object[] { SimpleHashTableApp.class.getName() });
        System.err.println("Type 'help' at any time to view a list of commands.");
        while (doCommandLine(new BufferedReader((Reader)new InputStreamReader(System.in)), hashTable)) {}
        System.err.println("Goodbye!");
    }
    
    private static void printHelp(final String s) {
        final PrintStream printStream = (s == null) ? System.out : System.err;
        if (s != null) {
            printStream.format("Error: %s%n", new Object[] { s });
            printStream.println();
        }
        printStream.format("Usage: %s <table-size>%n", new Object[] { SimpleHashTableApp.class.getName() });
        printStream.println();
        printStream.println("Arguments:");
        printStream.println("    table-size");
        printStream.println("        The number of buckets in the hash");
        printStream.println("        table. Note that the table can still");
        printStream.println("        store more entries than this, with");
        printStream.println("        reduced efficiency.");
        System.exit((int)((s != null) ? 1 : 0));
    }
    
    private static boolean doCommandLine(final BufferedReader bufferedReader, final HashTable hashTable) {
        System.out.print("> ");
        String line;
        try {
            line = bufferedReader.readLine();
        }
        catch (IOException ex) {
            System.err.format("Error reading command: %s%n", new Object[] { ex });
            return false;
        }
        if (line == null) {
            return false;
        }
        final String[] split = line.split("\\s+");
        if (split.length == 0 || (split.length == 1 && split[0].equals(""))) {
            return true;
        }
        final String s = split[0];
        final String[] array = (String[])Arrays.copyOfRange((Object[])split, 1, split.length);
        final Command command = (Command)SimpleHashTableApp.COMMANDS_BY_NAME.get((Object)s);
        if (command == null) {
            System.err.format("Invalid command \"%s\", try \"help\" for a list.%n", new Object[] { s });
            return true;
        }
        if (array.length < command.minArgs || array.length > command.maxArgs) {
            System.err.println("Wrong number of arguments.");
            System.err.format("Usage: %s%n", new Object[] { command.getFullUsage() });
            return true;
        }
        return command.run(hashTable, array);
    }
    
    static {
        COMMANDS = new Command[] { new Command("help", (String)null, "Prints this help message.", 0, 0) {
                @Override
                boolean run(final HashTable hashTable, final String... array) {
                    System.out.println("The following commands are available:");
                    for (final Command command : SimpleHashTableApp.COMMANDS_BY_NAME.values()) {
                        System.out.format("  %s\t\t%s%n", new Object[] { command.getFullUsage(), command.description });
                    }
                    return true;
                }
            }, new Command("exit", (String)null, "Quits the program.", 0, 0) {
                @Override
                boolean run(final HashTable hashTable, final String... array) {
                    return false;
                }
            }, new Command("print", (String)null, "Prints out the table.", 0, 0) {
                @Override
                boolean run(final HashTable hashTable, final String... array) {
                    System.out.println("Table contents:");
                    for (final Map.Entry entry : hashTable.entrySet()) {
                        System.out.format("  [\"%s\"] = \"%s\"%n", new Object[] { entry.getKey(), entry.getValue() });
                    }
                    System.out.format("%d entries%n", new Object[] { hashTable.size() });
                    return true;
                }
            }, new Command("clear", (String)null, "Clears the table.", 0, 0) {
                @Override
                boolean run(final HashTable hashTable, final String... array) {
                    hashTable.clear();
                    return true;
                }
            }, new Command("containsKey", "<key>", "Checks for the presence of a key.", 1, 1) {
                @Override
                boolean run(final HashTable hashTable, final String... array) {
                    if (hashTable.containsKey(array[0])) {
                        System.out.println("present");
                    }
                    else {
                        System.out.println("not present");
                    }
                    return true;
                }
            }, new Command("containsValue", "<value>", "Checks for the presence of a value.", 1, 1) {
                @Override
                boolean run(final HashTable hashTable, final String... array) {
                    if (hashTable.containsValue(array[0])) {
                        System.out.println("present");
                    }
                    else {
                        System.out.println("not present");
                    }
                    return true;
                }
            }, new Command("get", "<key>", "Get the value for the specified key.", 1, 1) {
                @Override
                boolean run(final HashTable hashTable, final String... array) {
                    System.out.println(hashTable.get(array[0]));
                    return true;
                }
            }, new Command("isEmpty", (String)null, "Test if the table is empty.", 0, 0) {
                @Override
                boolean run(final HashTable hashTable, final String... array) {
                    if (hashTable.isEmpty()) {
                        System.out.println("empty");
                    }
                    else {
                        System.out.println("not empty");
                    }
                    return true;
                }
            }, new Command("put", "<key> <value>", "Sets table[<key>] to <value>.", 2, 2) {
                @Override
                boolean run(final HashTable hashTable, final String... array) {
                    final String put = hashTable.put(array[0], array[1]);
                    if (put == null) {
                        System.out.format("Set \"%s\" to \"%s\".%n", new Object[] { array[0], array[1] });
                    }
                    else {
                        System.out.format("Set \"%s\" to \"%s\". Was: \"%s\".%n", new Object[] { array[0], array[1], put });
                    }
                    return true;
                }
            }, new Command("remove", "<key>", "Remove the specified key.", 1, 1) {
                @Override
                boolean run(final HashTable hashTable, final String... array) {
                    final String remove = hashTable.remove(array[0]);
                    if (remove == null) {
                        System.out.format("Key \"%s\" was not present.%n", new Object[] { array[0] });
                    }
                    else {
                        System.out.format("Removed \"%s\". Was: \"%s\".%n", new Object[] { array[0], remove });
                    }
                    return true;
                }
            }, new Command("size", (String)null, "Prints out the number of entries in the table.", 0, 0) {
                @Override
                boolean run(final HashTable hashTable, final String... array) {
                    System.out.format("%d entries%n", new Object[] { hashTable.size() });
                    return true;
                }
            } };
        COMMANDS_BY_NAME = (Map<String,Command>) new TreeMap<String,Command>();
        for (final Command command : SimpleHashTableApp.COMMANDS) {
//            SimpleHashTableApp.COMMANDS_BY_NAME.put((Object)command.name, (Object)command);
            SimpleHashTableApp.COMMANDS_BY_NAME.put(command.name, command);
        }
    }
    
    private abstract static class Command
    {
        String name;
        String usage;
        String description;
        int minArgs;
        int maxArgs;
        
        Command(final String name, final String usage, final String description, final int minArgs, final int maxArgs) {
            this.name = name;
            this.usage = usage;
            this.description = description;
            this.minArgs = minArgs;
            this.maxArgs = maxArgs;
        }
        
        abstract boolean run(final HashTable p0, final String... p1);
        
        String getFullUsage() {
            if (this.usage == null) {
                return this.name;
            }
            return this.name + " " + this.usage;
        }
    }
}
