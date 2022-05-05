package money.open.cards.visabase.listener;

import money.open.cards.visabase.config.VisaBaseConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

@WebListener
public class StartupListenerTransactionInterchange implements ServletContextListener {

    public static boolean masterLoopFlag = true;
    TransactionInterchangeListener visaBaseListener = null;

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        masterLoopFlag = false;
        for (Entry<String, Socket> entry : SocketMap.sckMap.entrySet()) {
            if (!entry.getValue().isClosed()) {
                try {
                    entry.getValue().close();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        for (Entry<String, ServerSocket> entry : SocketMap.serverScoketMap.entrySet()) {
            if (!entry.getValue().isClosed()) {
                try {
                    entry.getValue().close();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        //int socketCount = Integer.parseInt(Objects.requireNonNull(env.getProperty("socket.count")));
        int socketCount=1;
        System.out.println("Total Socket Connection->" + socketCount);
        for (int i = 0; i < socketCount; i++) {
            ApplicationContext context = new AnnotationConfigApplicationContext(VisaBaseConfig.class);
            visaBaseListener = context.getBean(TransactionInterchangeListener.class);

            visaBaseListener.setSocketIndex(i);
            visaBaseListener.setKeyxch(true);
            visaBaseListener.start();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }

    /*
     * private ScheduledExecutorService scheduler; private ExecutorService
     * executorService = Executors.newFixedThreadPool(10);
     */

    public void contextInitialized2(ServletContextEvent arg0) {
        // scheduler = Executors.newSingleThreadScheduledExecutor();
        // scheduler.scheduleAtFixedRate(new Task(), 0, 5, TimeUnit.SECONDS); //
        // Schedule to run every minute.

        // executorService.execute(new Task());
    }
}
