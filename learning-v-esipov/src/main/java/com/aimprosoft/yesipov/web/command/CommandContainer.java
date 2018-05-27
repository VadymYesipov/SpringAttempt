package com.aimprosoft.yesipov.web.command;

import com.aimprosoft.yesipov.web.command.impl.*;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.TreeMap;

public class CommandContainer {

    private static final Logger log = Logger.getLogger(CommandContainer.class);

    private static Map<String, Command> commands = new TreeMap<String, Command>();

    static {
        commands.put("list", new ListCommand());
        commands.put("filteredList", new FilteredListCommand());
        commands.put("addEdit", new AddEditCommand());
        commands.put("addDepartment", new AddDepartmentCommand());
        commands.put("editDepartment", new EditDepartmentCommand());
        commands.put("removeDepartment", new RemoveDepartmentCommand());
        commands.put("addEmployee", new AddEmployeeCommand());
        commands.put("editEmployee", new EditEmployeeCommand());
        commands.put("removeEmployee", new RemoveEmployeeCommand());

        log.debug("Command container was successfully initialized");
        log.trace("Number of commands --> " + commands.size());
    }

    /**
     * Returns command object with the given name.
     *
     * @param commandName Name of the command.
     * @return Command object.
     */
    public static Command get(String commandName) {
        if (commandName == null || !commands.containsKey(commandName)) {
            log.trace("Command not found, name --> " + commandName);
            return commands.get("noCommand");
        }

        return commands.get(commandName);
    }
}
