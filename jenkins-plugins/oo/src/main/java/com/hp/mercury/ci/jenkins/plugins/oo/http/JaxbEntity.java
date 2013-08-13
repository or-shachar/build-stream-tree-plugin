package com.hp.mercury.ci.jenkins.plugins.oo.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.message.BasicHeader;

import javax.xml.bind.JAXB;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 10/4/12
 * Time: 11:09 AM
 * To change this template use File | Settings | File Templates.
 *
 * when using Apache Http Client, if you wish to attach data to a request, it must be
 * an HttpEntity.
 *
 * in this class we give an effective implementation of such an HttpEntity, for
 * Jaxb serialized entities, allowing HttpClient to integrate with Jaxb.
 *
 * TODO move to commons-http, if there are other http issues
 */
public class JaxbEntity extends AbstractHttpEntity {

    private Object jaxbObject;

    public JaxbEntity(Object obj) {
        this.jaxbObject = obj;
    }

    public boolean isRepeatable() {
        return true;
    }

    @Override
    public boolean isChunked() {
        return false;
    }

    public long getContentLength() {
        return -1;
    }

    public boolean isStreaming() {
        return false;
    }

    public InputStream getContent() throws IOException {
        throw new UnsupportedOperationException();
    }

    public void writeTo(final OutputStream outstream) throws IOException {
        JAXB.marshal(jaxbObject, outstream);
    }
}
