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
        var input = "<employee deparment = \"finance\">Garry Smith</employee>";
        var expected = "{\"employee\":\"#employee\":\"Garry Smith\",\"@deparment\":\"finance\"}";
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
}
