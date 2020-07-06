package converter;

import org.junit.Assert;
import org.junit.Test;

public class JSON2XMLTests {
    @Test
    public void json2xml_simpleContent_noAttributes() {
        var input = "{\"employee\":\"Garry Smith\"}";
        var expected = "<employee>Garry Smith</employee>";
        var converter = Converter.Factory.createFor(input);
        var output = converter.convert(input);
        Assert.assertEquals(expected, output);
    }

    @Test
    public void json2xml_simpleContent_singleAttribute() {
        var input = "{\"employee\":{\"#employee\":\"Garry Smith\",\"@department\":\"finance\"}}";
        var expected = "<employee department = \"finance\">Garry Smith</employee>";
        var converter = Converter.Factory.createFor(input);
        var output = converter.convert(input);
        Assert.assertEquals(expected, output);
    }

    @Test
    public void json2xml_simpleObjectContent_noAttributes() {
        var input = "{\"employee\":{\"item\":\"Garry Smith\"}}";
        var expected = "<employee><item>Garry Smith</item></employee>";
        var converter = Converter.Factory.createFor(input);
        var output = converter.convert(input);
        Assert.assertEquals(expected, output);
    }

    @Test
    public void json2xml_simpleObjectContent_singleAttribute() {
        var input = "{\"employee\":{\"#employee\":{\"item\":\"Garry Smith\"},\"@department\":\"finance\"}}";
        var expected = "<employee department = \"finance\"><item>Garry Smith</item></employee>";
        var converter = Converter.Factory.createFor(input);
        var output = converter.convert(input);
        Assert.assertEquals(expected, output);
    }

    @Test
    public void json2xml_multipleSimpleObjectsContent_noAttributes() {
        var input = "{\"employee\":[{\"item\":\"Garry Smith\"},{\"item\":\"test\"}]}";
        var expected = "<employee><item>Garry Smith</item><item>test</item></employee>";
        var converter = Converter.Factory.createFor(input);
        var output = converter.convert(input);
        Assert.assertEquals(expected, output);
    }

    @Test
    public void json2xml_multipleSimpleObjectsContent_singleAttribute() {
        var input = "{\"employee\":{\"#employee\":[{\"item\":\"Garry Smith\"},{\"item\":\"test\"}],\"@department\":\"finance\"}}";
        var expected = "<employee department = \"finance\"><item>Garry Smith</item><item>test</item></employee>";
        var converter = Converter.Factory.createFor(input);
        var output = converter.convert(input);
        Assert.assertEquals(expected, output);
    }

    @Test
    public void json2xml_complexObject() {
        var input = "{\"node\":[{\"child\":{\"#child\":{\"subchild\":{\"#subchild\":\"Value1\",\"@id\":\"1\",\"@auth\":\"auth1\"}},\"@name\":\"child_name1\",\"@type\":\"child_type1\"}},{\"child\":{\"#child\":[{\"subchild\":{\"#subchild\":\"Value2\",\"@id\":\"2\",\"@auth\":\"auth1\"}},{\"subchild\":{\"#subchild\":\"Value3\",\"@id\":\"3\",\"@auth\":\"auth2\"}},{\"subchild\":{\"#subchild\":\"\",\"@id\":\"4\",\"@auth\":\"auth3\"}},{\"subchild\":{\"#subchild\":null,\"@id\":\"5\",\"@auth\":\"auth3\"}}],\"@name\":\"child_name2\",\"@type\":\"child_type2\"}}]}";
        var expected = "<node><child name=\"child_name1\" type=\"child_type1\"><subchild id=\"1\" auth=\"auth1\">Value1</subchild></child><child name=\"child_name2\" type=\"child_type2\"><subchild id=\"2\" auth=\"auth1\">Value2</subchild><subchild id=\"3\" auth=\"auth2\">Value3</subchild><subchild id=\"4\" auth=\"auth3\"></subchild><subchild id=\"5\" auth=\"auth3\"/></child></node>";
        var converter = Converter.Factory.createFor(input);
        var output = converter.convert(input);
        Assert.assertEquals(expected, output);
    }
}
