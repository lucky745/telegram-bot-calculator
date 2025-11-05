package com.lucky.bot.telegram.response.handler.command;

import com.lucky.bot.telegram.response.Response;
import com.lucky.bot.util.CalculatorUsageTracker;
import org.springframework.stereotype.Component;

@Component
public class UsageCommand extends BaseAdminCommandHandler {
    private final CalculatorUsageTracker usageTracker;

    protected UsageCommand(CalculatorUsageTracker usageTracker) {
        super(Command.USAGE);
        this.usageTracker = usageTracker;
    }

    @Override
    public Response handle(CommandData commandData) {
        return new Response(usageTracker.getComprehensiveStats());
    }
}
