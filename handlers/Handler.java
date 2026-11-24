package handlers;

import facade.ShopFacade;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Handler {
    protected Handler next;
    protected final ShopFacade facade;

    protected Handler(ShopFacade facade) {
        this.facade = facade;
    }

    public Handler setNext(Handler next) {
        this.next = next;
        return next;
    }

    public boolean handle(String input) {
        if (input == null || input.isBlank()) {
            return false;
        }
        String trimmed = input.trim();
        if (canHandle(trimmed)) {
            process(trimmed);
            return true;
        }
        return next != null && next.handle(trimmed);
    }

    protected abstract boolean canHandle(String input);

    protected abstract void process(String input);

    protected List<String> tokenizeWithQuotes(String text) {
        List<String> tokens = new ArrayList<>();
        Matcher matcher = Pattern.compile("\"([^\"]*)\"|(\\S+)").matcher(text);
        while (matcher.find()) {
            tokens.add(matcher.group(1) != null ? matcher.group(1) : matcher.group(2));
        }
        return tokens;
    }
}