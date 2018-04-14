package framework.grizzly;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Logger;
import org.glassfish.grizzly.filterchain.FilterChainBuilder;
import org.glassfish.grizzly.filterchain.TransportFilter;
import org.glassfish.grizzly.nio.transport.TCPNIOTransport;
import org.glassfish.grizzly.nio.transport.TCPNIOTransportBuilder;
import org.glassfish.grizzly.utils.StringFilter;

/**
 * Class initializes and starts the echo server, based on Grizzly 2.0
 *
 * @author Alexey Stashok
 */
/**
 * @author mercy
 *通过换行去区分一个中断符
 */
public class GrizzlyEchoServer1 {
    private static final Logger logger = Logger.getLogger(GrizzlyEchoServer1.class.getName());
    public static final String ADDR="127.0.0.1";
    public static final int PORT = 10003;

    public static void main(String[] args) throws IOException {
        // Create a FilterChain using FilterChainBuilder
        FilterChainBuilder filterChainBuilder = FilterChainBuilder.stateless();

        // Add TransportFilter, which is responsible
        // for reading and writing data to the connection
        filterChainBuilder.add(new TransportFilter());

        // StringFilter is responsible for Buffer <-> String conversion
       // filterChainBuilder.add(new StringFilter(Charset.forName("GBK")));
        filterChainBuilder.add(new StringFilter(Charset.forName("GBK"), "\r\n"));
        // EchoFilter is responsible for echoing received messages
        filterChainBuilder.add(new GrizzlyEchoFilter());
        
        // Create TCP transport
        final TCPNIOTransport transport =
                TCPNIOTransportBuilder.newInstance().build();
        
        transport.setProcessor(filterChainBuilder.build());
        
        try {
            // binding transport to start listen on certain host and port
        	logger.info("binding transport to start listen on certain host and port");
            transport.bind(ADDR,PORT);

            // start the transport
            transport.start();

            //logger.info("Press any key to stop the server...");
            System.in.read();
       } finally {
            //logger.info("Stopping transport...");
            // stop the transport
            transport.shutdownNow();

            logger.info("Stopped transport...");
        }
    }
}