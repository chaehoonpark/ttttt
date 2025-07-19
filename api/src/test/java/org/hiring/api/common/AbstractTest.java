package org.hiring.api.common;

import com.navercorp.fixturemonkey.FixtureMonkey;
import org.hiring.api.common.testFixture.TestFixtureFactory;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class AbstractTest {

    protected final FixtureMonkey fixtureMonkey = TestFixtureFactory.getInstance();
}
