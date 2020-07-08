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
    public void json2xml_invalidAttribute() {
        var input = "\"inner4\": {\n" +
                "            \"@\": 123,\n" +
                "            \"#inner4\": \"value3\"\n" +
                "        },";
        var expected = "<inner4><inner4>value3</inner4></inner4>";
        var converter = Converter.Factory.createFor(input);
        var output = converter.convert(input);
        Assert.assertEquals(expected, output);
    }

    @Test
    public void json2xml_invalidContent() {
        var input = "\"inner5\": {\n" +
                "            \"@attr1\": 123.456,\n" +
                "            \"#inner4\": \"value4\"\n" +
                "        }";
        var expected = "<inner5><attr1>123.456</attr1><inner4>value4</inner4></inner5>";
        var converter = Converter.Factory.createFor(input);
        var output = converter.convert(input);
        Assert.assertEquals(expected, output);
    }

    @Test
    public void json2xml_invalidElement() {
        var input = "\"inner4.2\": {\n" +
                "            \"\": 123,\n" +
                "            \"#inner4.2\": \"value3\"\n" +
                "        }";
        var expected = "<inner4.2><inner4.2>value3</inner4.2></inner4.2>";
        var converter = Converter.Factory.createFor(input);
        var output = converter.convert(input);
        Assert.assertEquals(expected, output);
    }

    @Test
    public void json2xml_simpleContent_singleAttribute() {
        var input = "{\"employee\":{\"#employee\":\"Garry Smith\",\"@department\":\"finance\"}}";
        var expected = "<employee department=\"finance\">Garry Smith</employee>";
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
        var expected = "<employee department=\"finance\"><item>Garry Smith</item></employee>";
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
        var expected = "<employee department=\"finance\"><item>Garry Smith</item><item>test</item></employee>";
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
