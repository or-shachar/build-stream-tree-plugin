package com.hp.mercury.ci.jenkins.plugins;

import com.hp.commons.core.string.StringUtils;
import com.hp.mercury.ci.jenkins.plugins.oo.core.OOArg;
import com.hp.mercury.ci.jenkins.plugins.oo.core.OOListResponse;
import com.hp.mercury.ci.jenkins.plugins.oo.core.OORunRequest;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 10/4/12
 * Time: 12:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestSerialization {

    @Test
    public void testListResponse() throws JAXBException {

        JAXBContext context = JAXBContext.newInstance(
                OOListResponse.class,
                OOListResponse.OOListItem.class);

        final Unmarshaller unmarshaller = context.createUnmarshaller();

        final InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("listResponse.xml");

        final OOListResponse unmarshal = (OOListResponse)unmarshaller.unmarshal(resourceAsStream);

        Assert.assertEquals("/Library/AGM/AGM - Java 6u25 Config", unmarshal.getItems().iterator().next().getValue());
    }

    @Test
    public void testRunRequest() throws JAXBException {

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        final OORunRequest ooRunRequest = new OORunRequest();
        ooRunRequest.setArgs(Arrays.asList(new OOArg("url","http://nathan")));
        JAXB.marshal(ooRunRequest, os);

        String serialization = new String(os.toByteArray());

        final String expectedRequest =
                "<run>\n" +
                "    <request>\n" +
                "        <arg name=\"url\">http://nathan</arg>\n" +
                "    </request>\n" +
                "</run>";

        Assert.assertTrue("request string is not as expected", StringUtils.removeWhitespaces(serialization).endsWith(
                StringUtils.removeWhitespaces(expectedRequest)));

    }
}
