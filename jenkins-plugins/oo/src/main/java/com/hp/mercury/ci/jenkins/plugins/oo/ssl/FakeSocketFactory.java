package com.hp.mercury.ci.jenkins.plugins.oo.ssl;



import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.LayeredSchemeSocketFactory;
import org.apache.http.conn.scheme.SchemeSocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * we use this class to override SSL on the clientside, effectively using http
 * instead of https
 */
public class FakeSocketFactory implements SchemeSocketFactory, LayeredSchemeSocketFactory
{
     private SSLContext sslcontext = null;

     private static SSLContext createEasySSLContext() throws IOException {
             try {
                     SSLContext context = SSLContext.getInstance("TLS");
                     context.init(null, new TrustManager[] { new EasyX509TrustManager(null) }, null);
                     return context;
             } catch (Exception e) {
                     throw new IOException(e.getMessage());
             }
     }


    private SSLContext getSSLContext() throws IOException {
             if (this.sslcontext == null) {
                     this.sslcontext = createEasySSLContext();
             }
             return this.sslcontext;
     }

     /**
      * @see org.apache.http.conn.scheme.SchemeSocketFactory#isSecure(java.net.Socket)
      */
     public boolean isSecure(Socket socket) throws IllegalArgumentException {
             return true;
     }

     // -------------------------------------------------------------------
     // javadoc in org.apache.http.conn.scheme.SocketFactory says :
     // Both Object.equals() and Object.hashCode() must be overridden
     // for the correct operation of some connection managers
     // -------------------------------------------------------------------

     public boolean equals(Object obj) {
             return ((obj != null) && obj.getClass().equals(
                             FakeSocketFactory.class));
     }

     public int hashCode() {
             return FakeSocketFactory.class.hashCode();
     }

    //this method is modified
    @Override
    public Socket connectSocket(Socket sock, InetSocketAddress remoteAddress,
            InetSocketAddress localAddress, HttpParams params) throws IOException,
            UnknownHostException, ConnectTimeoutException {

        int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
        int soTimeout = HttpConnectionParams.getSoTimeout(params);
        SSLSocket sslsock = (SSLSocket) ((sock != null) ? sock : createSocket(params));
        if (localAddress != null) {
            // we need to bind explicitly
            sslsock.bind(localAddress);
    }

    sslsock.connect(remoteAddress, connTimeout);
    sslsock.setSoTimeout(soTimeout);
    return sslsock;
    }

    //this method is modified
    @Override
    public Socket createSocket(HttpParams arg0) throws IOException {
         return getSSLContext().getSocketFactory().createSocket();
    }


    @Override
    public Socket createLayeredSocket(Socket socket, String s, int i, boolean b) throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(socket,s,i,b);
    }
}