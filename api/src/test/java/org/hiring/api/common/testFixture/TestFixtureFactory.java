package org.hiring.api.common.testFixture;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BeanArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FailoverIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.plugin.SimpleValueJqwikPlugin;

import java.util.List;

public abstract class TestFixtureFactory {

    private TestFixtureFactory() {
        throw new IllegalArgumentException("It's a utility class");
    }

    private static final FixtureMonkey FIXTURE_MONKEY = FixtureMonkey.builder()
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

    public static FixtureMonkey getInstance(){
        return FIXTURE_MONKEY;
    }
}
