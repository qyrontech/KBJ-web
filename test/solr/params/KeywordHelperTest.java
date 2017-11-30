package solr.params;

import org.junit.Test;
import play.Application;
import play.Logger;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;

import javax.inject.Inject;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class KeywordHelperTest extends WithApplication {

    public KeywordHelperTest() {}

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void testEscapeSpecialChars() {
        String actual, excepted;
        String target = "+-&&||!(){}[]^\"~*?:\\";
        for (char ch : target.toCharArray()) {
            actual = KeywordHelper.escapeSpecialChars(String.valueOf(ch));
            excepted = "\\" + ch;
            assertEquals(ch + " is escaped failed.", excepted, actual);
        }

        actual = KeywordHelper.escapeSpecialChars("+-&&||!(){}[]^\"~*?:\\");
        excepted = "\\+\\-\\&\\&\\|\\|\\!\\(\\)\\{\\}\\[\\]\\^\\\"\\~\\*\\?\\:\\\\";
        assertEquals(target + " is escaped failed", excepted, actual);

        actual = KeywordHelper.escapeSpecialChars(" ");
        excepted = "\\ ";
        assertEquals("half-width space" + " is escaped failed.", excepted, actual);

        actual = KeywordHelper.escapeSpecialChars("\\s");
        excepted = "\\\\s";
        assertEquals("half-width space" + " is escaped failed.", excepted, actual);

        // so, full-width space is also escaped.
        actual = KeywordHelper.escapeSpecialChars("　");
        excepted = "\\　";
        assertEquals("full-width space" + " is escaped failed.", excepted, actual);
    }

    @Test
    public void testSplitBySpaces() {

        String[] actual, excepted;
        // mixed with half-width and full-width spaces in the head and tail.
        String target = " A　B  C 　";
        excepted = new String[] {"A", "B", "C"};
        actual = KeywordHelper.splitBySpaces(target);
        for (String str : actual) {
            Logger.debug("[" + str + "]");
        }
        assertArrayEquals(excepted, actual);

        // mixed with half-width and full-width spaces in the head and tail.
        target = "　 A　B  C 　";
        excepted = new String[] {"A", "B", "C"};
        actual = KeywordHelper.splitBySpaces(target);
        for (String str : actual) {
            Logger.debug("[" + str + "]");
        }
        assertArrayEquals(excepted, actual);

        // mixed with half-width and full-width spaces.
        target = "　 　   　";
        excepted = new String[] {};
        actual = KeywordHelper.splitBySpaces(target);
        for (String str : actual) {
            Logger.debug("[" + str + "]");
        }
        assertArrayEquals(excepted, actual);


    }

}
