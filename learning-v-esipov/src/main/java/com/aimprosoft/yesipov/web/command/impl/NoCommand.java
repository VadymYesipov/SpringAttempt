package com.aimprosoft.yesipov.web.command.impl;

import com.aimprosoft.yesipov.web.Path;
import com.aimprosoft.yesipov.web.command.Command;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NoCommand implements Command {

    private static final Logger log = Logger.getLogger(NoCommand.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) throws IOException, ServletException {
        log.debug("Command starts");

        String errorMessage = "No such command";
        request.setAttribute("errorMessage", errorMessage);
        log.error("Set the request attribute: errorMessage --> " + errorMessage);

        log.debug("Command finished");
        return Path.ERROR_PAGE;
    }
}
