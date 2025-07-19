package org.hiring.api.common.testFixture;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BeanArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FailoverIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.plugin.SimpleValueJqwikPlugin;

import java.util.List;

public abstract class TestFixture {

    private TestFixture() {
        throw new IllegalArgumentException("It's a utility class");
    }

    public static final FixtureMonkey FM = FixtureMonkey.builder()
            .defaultNotNull(true)
            .objectIntrospector(
                    new FailoverIntrospector(
                            List.of(
                                    ConstructorPropertiesArbitraryIntrospector.INSTANCE,
                                    BeanArbitraryIntrospector.INSTANCE,
                                    FieldReflectionArbitraryIntrospector.INSTANCE
                            )
                    )
            )
            .plugin(new SimpleValueJqwikPlugin()
                    .minStringLength(1)
                    .maxStringLength(30)
                    .minNumberValue(1)
                    .maxNumberValue(2500)
            )
            .enableLoggingFail(false)
            .build();
}
