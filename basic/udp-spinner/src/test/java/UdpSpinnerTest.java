import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.SubscribableChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.integration.test.matcher.PayloadMatcher.hasPayload;

/**
 * This class is severely underdocumented.
 *
 * @author iwein
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:udpContext.xml")
public class UdpSpinnerTest {

    @Autowired MessageChannel toUdp;

    @Autowired
    QueueChannel fromUdp;

    @Test
    public void shouldSendOneMessage() throws InterruptedException {
        toUdp.send(MessageBuilder.withPayload("Hello".getBytes()).build());
        assertThat(fromUdp.receive(500), hasPayload("Hello".getBytes()));
    }

    @Test @Ignore // this takes a while
    public void floodLossless() {
        for (int i = 0; i<20000; i++){
           toUdp.send(MessageBuilder.withPayload((
                   "Long message with a lot of crap in it to see if we can fill up the buffers and make things go south"+i
           ).getBytes()).build());
        }
        for (int i = 0; i<20000; i++){
           assertThat(fromUdp.receive(100), is(notNullValue()));
        }
    }
}
