package org.cqq.openlibrary.common.enums;

import com.google.common.base.CaseFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cqq.openlibrary.common.util.Assert;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Naming style enum
 *
 * @author Qingquan
 */
@Getter
@AllArgsConstructor
public enum NamingStyleEnum {
    
    LOWER_CAMEL("camelCase", CaseFormat.LOWER_CAMEL, Pattern.compile("^[a-z][a-zA-Z0-9]*$")),
    UPPER_CAMEL("PascalCase", CaseFormat.UPPER_CAMEL, Pattern.compile("^[A-Z][a-zA-Z0-9]*$")),
    LOWER_UNDERSCORE("snake_case", CaseFormat.LOWER_UNDERSCORE, Pattern.compile("^[a-z]+(_[a-z0-9]+)*$")),
    UPPER_UNDERSCORE("UPPER_SNAKE_CASE", CaseFormat.UPPER_UNDERSCORE, Pattern.compile("^[A-Z]+(_[A-Z0-9]+)*$")),
    LOWER_HYPHEN("kebab-case", CaseFormat.LOWER_HYPHEN, Pattern.compile("^[a-z]+(-[a-z0-9]+)*$")),
    ;
    
    private final String alias;
    
    private final CaseFormat caseFormat;
    
    private final Pattern pattern;
    
    public boolean match(String input) {
        return pattern.matcher(input).matches();
    }
    
    public static Optional<NamingStyleEnum> detectStyle(String name) {
        for (NamingStyleEnum value : values()) {
            if (value.pattern.matcher(name).matches()) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }
    
    public static String change2(NamingStyleEnum targetNamingStyleEnum, String name) {
        Assert.notBlank(name, "name cannot be blank");
        return detectStyle(name)
                .map(namingStyleEnum -> namingStyleEnum.getCaseFormat().to(targetNamingStyleEnum.getCaseFormat(), name))
                .orElseThrow(() -> new IllegalArgumentException("Unresolvable naming style: " + name));
    }
}
