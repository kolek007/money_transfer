import app.service.AccountsService;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

/**
 */
public class TransferTest {

    private static final int COUNT = 16;

    @Test(timeout = 1000)
    public void concurrentTransferring() {
        final AtomicLong transactions = new AtomicLong(0);
        ExecutorService service = Executors.newFixedThreadPool(COUNT);
        CountDownLatch latch = new CountDownLatch(COUNT);

        List<Future> futures = Lists.newArrayList();
        for(int i = 0; i < COUNT; i++) {
            Future<?> future = service.submit(() -> {
                AccountsService.INSTANCE.createAccount(1000d);
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException("Failed", e);
                }
                for(int j = 0; j < 1000; j++) {
                    long from = randomId(-1);
                    long to = randomId(from);
                    AccountsService.INSTANCE.transfer(from, to, 1);
                    transactions.incrementAndGet();
                }
            });
            futures.add(future);
        }

        futures.forEach(f -> {
            try {
                f.get();
            } catch (Exception e) {
                throw new RuntimeException("Failed", e);
            }
        });
        System.out.println("TRANSACTIONS PERFORMED: " + transactions.get());
    }

    private long randomId(long except) {
        Random random = new Random();
        long id = Math.abs(random.nextLong() % (COUNT + 2));
        while (id == except || id < 2 || id > (COUNT + 1)) {
            id = Math.abs(random.nextLong() % 16);
        }
        return id;
    }
}
