package converter;

import org.junit.Assert;
import org.junit.Test;

public class Tests {
    @Test
    public void xml2json_simpleContent_noAttributes() {
        var input = "<employee>Garry Smith</employee>";
        var expected = "{\"employee\":\"Garry Smith\"}";
        var converter = Converter.Factory.createFor(input);
        var output = converter.convert(input);
        Assert.assertEquals(expected, output);
    }
    @Test
    public void xml2json_simpleContent_singleAttribute() {
        var input = "<employee department = \"finance\">Garry Smith</employee>";
        var expected = "{\"employee\":{\"#employee\":\"Garry Smith\",\"@department\":\"finance\"}}";
        var converter = Converter.Factory.createFor(input);
        var output = converter.convert(input);
        Assert.assertEquals(expected, output);
    }
    @Test
    public void xml2json_simpleObjectContent_noAttributes() {
        var input = "<employee><item>Garry Smith</item></employee>";
        var expected = "{\"employee\":{\"item\":\"Garry Smith\"}}";
        var converter = Converter.Factory.createFor(input);
        var output = converter.convert(input);
        Assert.assertEquals(expected, output);
    }
    @Test
    public void xml2json_simpleObjectContent_singleAttribute() {
        var input = "<employee department = \"finance\"><item>Garry Smith</item></employee>";
        var expected = "{\"employee\":{\"#employee\":{\"item\":\"Garry Smith\"},\"@department\":\"finance\"}}";
        var converter = Converter.Factory.createFor(input);
        var output = converter.convert(input);
        Assert.assertEquals(expected, output);
    }
    @Test
    public void xml2json_multipleSimpleObjectsContent_noAttributes() {
        var input = "<employee><item>Garry Smith</item><item>test</item></employee>";
        var expected = "{\"employee\":[{\"item\":\"Garry Smith\"},{\"item\":\"test\"}]}";
        var converter = Converter.Factory.createFor(input);
        var output = converter.convert(input);
        Assert.assertEquals(expected, output);
    }
    @Test
    public void xml2json_multipleSimpleObjectsContent_singleAttribute() {
        var input = "<employee department = \"finance\"><item>Garry Smith</item><item>test</item></employee>";
        var expected = "{\"employee\":{\"#employee\":[{\"item\":\"Garry Smith\"},{\"item\":\"test\"}],\"@department\":\"finance\"}}";
        var converter = Converter.Factory.createFor(input);
        var output = converter.convert(input);
        Assert.assertEquals(expected, output);
    }
    @Test
    public void xml2json_complexObject() {
        var input = "<node>\n" +
                "    <child name = \"child_name1\" type = \"child_type1\">\n" +
                "        <subchild id = \"1\" auth=\"auth1\">Value1</subchild>\n" +
                "    </child>\n" +
                "    <child name = \"child_name2\" type = \"child_type2\">\n" +
                "        <subchild id = \"2\" auth=\"auth1\">Value2</subchild>\n" +
                "        <subchild id = \"3\" auth=\"auth2\">Value3</subchild>\n" +
                "        <subchild id = \"4\" auth=\"auth3\"></subchild>\n" +
                "        <subchild id = \"5\" auth=\"auth3\"/>\n" +
                "    </child>\n" +
                "</node>";
        var expected = "{\"node\":[{\"child\":{\"#child\":{\"subchild\":{\"#subchild\":\"Value1\",\"@id\":\"1\",\"@auth\":\"auth1\"}},\"@name\":\"child_name1\",\"@type\":\"child_type1\"}},{\"child\":{\"#child\":[{\"subchild\":{\"#subchild\":\"Value2\",\"@id\":\"2\",\"@auth\":\"auth1\"}},{\"subchild\":{\"#subchild\":\"Value3\",\"@id\":\"3\",\"@auth\":\"auth2\"}},{\"subchild\":{\"#subchild\":\"\",\"@id\":\"4\",\"@auth\":\"auth3\"}},{\"subchild\":{\"#subchild\":null,\"@id\":\"5\",\"@auth\":\"auth3\"}}],\"@name\":\"child_name2\",\"@type\":\"child_type2\"}}]}";
        var converter = Converter.Factory.createFor(input);
        var output = converter.convert(input);
        Assert.assertEquals(expected, output);
    }
}
