package android.taobao.windvane.security;

import android.taobao.windvane.util.TaoLog;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import mtopsdk.common.util.SymbolExpUtil;

public class SSLTunnelSocketFactory extends SSLSocketFactory {
    private SSLSocketFactory dfactory;
    private String tunnelHost;
    private int tunnelPort;
    private String useragent;

    public SSLTunnelSocketFactory(String proxyhost, int proxyport, SSLSocketFactory socketfactory, String user_agent) {
        this.tunnelHost = proxyhost;
        this.tunnelPort = proxyport;
        if (socketfactory != null) {
            this.dfactory = socketfactory;
        } else {
            this.dfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        }
        this.useragent = user_agent;
    }

    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        return createSocket((Socket) null, host, port, true);
    }

    public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) throws IOException, UnknownHostException {
        return createSocket((Socket) null, host, port, true);
    }

    public Socket createSocket(InetAddress host, int port) throws IOException {
        return createSocket((Socket) null, host.getHostName(), port, true);
    }

    public Socket createSocket(InetAddress address, int port, InetAddress clientAddress, int clientPort) throws IOException {
        return createSocket((Socket) null, address.getHostName(), port, true);
    }

    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        Socket tunnel = new Socket(this.tunnelHost, this.tunnelPort);
        doTunnelHandshake(tunnel, host, port);
        SSLSocket result = (SSLSocket) this.dfactory.createSocket(tunnel, host, port, autoClose);
        result.addHandshakeCompletedListener(new HandshakeCompletedListener() {
            public void handshakeCompleted(HandshakeCompletedEvent event) {
                TaoLog.d("tag", "Handshake finished!");
                TaoLog.d("tag", "\t CipherSuite:" + event.getCipherSuite());
                TaoLog.d("tag", "\t SessionId " + event.getSession());
                TaoLog.d("tag", "\t PeerHost " + event.getSession().getPeerHost());
            }
        });
        result.startHandshake();
        return result;
    }

    private void doTunnelHandshake(Socket tunnel, String host, int port) throws IOException {
        byte[] b;
        String replyStr;
        OutputStream out = tunnel.getOutputStream();
        String msg = "CONNECT " + host + SymbolExpUtil.SYMBOL_COLON + port + " HTTP/1.1\n" + "User-Agent: " + this.useragent + "\n" + "Host:" + host + "\r\n\r\n";
        try {
            b = msg.getBytes("ASCII7");
        } catch (UnsupportedEncodingException e) {
            b = msg.getBytes();
        }
        out.write(b);
        out.flush();
        byte[] reply = new byte[200];
        int replyLen = 0;
        int newlinesSeen = 0;
        boolean headerDone = false;
        InputStream in = tunnel.getInputStream();
        while (true) {
            int replyLen2 = replyLen;
            if (newlinesSeen < 2) {
                int i = in.read();
                if (i < 0) {
                    throw new IOException("Unexpected EOF from proxy");
                } else if (i == 10) {
                    headerDone = true;
                    newlinesSeen++;
                    replyLen = replyLen2;
                } else {
                    if (i != 13) {
                        newlinesSeen = 0;
                        if (!headerDone && replyLen2 < reply.length) {
                            replyLen = replyLen2 + 1;
                            reply[replyLen2] = (byte) i;
                        }
                    }
                    replyLen = replyLen2;
                }
            } else {
                try {
                    replyStr = new String(reply, 0, replyLen2, "ASCII7");
                } catch (UnsupportedEncodingException e2) {
                    replyStr = new String(reply, 0, replyLen2);
                }
                if (replyStr.toLowerCase().indexOf("200 connection established") == -1) {
                    throw new IOException("Unable to tunnel through " + this.tunnelHost + SymbolExpUtil.SYMBOL_COLON + this.tunnelPort + ".  Proxy returns \"" + replyStr + "\"");
                }
                return;
            }
        }
    }

    public String[] getDefaultCipherSuites() {
        return this.dfactory.getDefaultCipherSuites();
    }

    public String[] getSupportedCipherSuites() {
        return this.dfactory.getSupportedCipherSuites();
    }
}
