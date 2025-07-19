package org.hiring.api.common.testFixture;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.plugin.SimpleValueJqwikPlugin;

public final class TestFixture {

    public static final FixtureMonkey FM = FixtureMonkey.builder()
            .defaultNotNull(true)
            .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
            .plugin(new SimpleValueJqwikPlugin()
                    .minStringLength(1)
                    .maxStringLength(30)
                    .minNumberValue(1)
                    .maxNumberValue(2500)
            )
            .build();
}