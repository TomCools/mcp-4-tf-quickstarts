package be.tomcools.timefold.mcp.quickstarts;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

@QuarkusTest
@Disabled("Manual")
class TimefoldToolsTest {

    @Inject
    TimefoldTools timefoldTools;

    @Test
    void testStartStopCustom() {
        timefoldTools.setupIndustryDemoData(List.of("bla","da", "li"),List.of("bla","da", "li","sa"));
    }

    @Test
    void scoreAnalysis() {
        timefoldTools.scoreAnalysis("7638e507-4ebd-4f88-b336-4c1305478cd2");
    }

}