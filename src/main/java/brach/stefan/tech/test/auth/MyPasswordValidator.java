package brach.stefan.tech.test.auth;

import java.util.Arrays;
import java.util.List;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.WhitespaceRule;

public class MyPasswordValidator extends PasswordValidator {
    private final static List<Rule> list = Arrays.asList(
    // length between 2 and 100 characters
            new LengthRule(2, 100),
            // at least one character
            new CharacterRule(EnglishCharacterData.Alphabetical),
            // no whitespace
            new WhitespaceRule());

    public MyPasswordValidator() {
        super(list);
    }
}
