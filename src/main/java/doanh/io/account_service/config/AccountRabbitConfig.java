package doanh.io.account_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountRabbitConfig {

    public static final String EXCHANGE_NAME = "account.rpc.exchange";

    // Queue names
    public static final String QUEUE_GET_ALL = "account.rpc.getAll";
    public static final String QUEUE_GET_ONE = "account.rpc.getOne";
    public static final String QUEUE_ADD = "account.rpc.add";
    public static final String QUEUE_UPDATE = "account.rpc.update";
    public static final String QUEUE_DELETE = "account.rpc.delete";
    public static final String QUEUE_CHANGE_PASSWORD = "account.rpc.changePassword";

    // Exchange
    @Bean
    public DirectExchange accountExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    // Queues
    @Bean public Queue getAllAccountsQueue()       { return new Queue(QUEUE_GET_ALL); }
    @Bean public Queue getOneAccountQueue()        { return new Queue(QUEUE_GET_ONE); }
    @Bean public Queue addAccountQueue()           { return new Queue(QUEUE_ADD); }
    @Bean public Queue updateAccountQueue()        { return new Queue(QUEUE_UPDATE); }
    @Bean public Queue deleteAccountQueue()        { return new Queue(QUEUE_DELETE); }
    @Bean public Queue changePasswordQueue()       { return new Queue(QUEUE_CHANGE_PASSWORD); }

    // Bindings
    @Bean public Binding bindingGetAll(Queue getAllAccountsQueue, DirectExchange accountExchange) {
        return BindingBuilder.bind(getAllAccountsQueue).to(accountExchange).with("account.getAll");
    }

    @Bean public Binding bindingGetOne(Queue getOneAccountQueue, DirectExchange accountExchange) {
        return BindingBuilder.bind(getOneAccountQueue).to(accountExchange).with("account.getOne");
    }

    @Bean public Binding bindingAdd(Queue addAccountQueue, DirectExchange accountExchange) {
        return BindingBuilder.bind(addAccountQueue).to(accountExchange).with("account.add");
    }

    @Bean public Binding bindingUpdate(Queue updateAccountQueue, DirectExchange accountExchange) {
        return BindingBuilder.bind(updateAccountQueue).to(accountExchange).with("account.update");
    }

    @Bean public Binding bindingDelete(Queue deleteAccountQueue, DirectExchange accountExchange) {
        return BindingBuilder.bind(deleteAccountQueue).to(accountExchange).with("account.delete");
    }

    @Bean public Binding bindingChangePassword(Queue changePasswordQueue, DirectExchange accountExchange) {
        return BindingBuilder.bind(changePasswordQueue).to(accountExchange).with("account.changePassword");
    }

    // Message Converter
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // RabbitTemplate sử dụng converter (dùng cho client khi gọi RPC)
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
