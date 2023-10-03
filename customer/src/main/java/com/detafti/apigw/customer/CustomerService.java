package com.detafti.apigw.customer;

import com.detafti.apigw.clients.fraud.FraudClient;
import com.detafti.apigw.clients.notification.NotificationClient;
import com.detafti.apigw.clients.notification.NotificationRequest;
import com.detafti.apigw.clients.fraud.FraudCheckResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final FraudClient fraudClient;
    private final NotificationClient notificationClient;

    public void registerCustomer(CustomerRegistrationRequest request) {

        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

//        todo: check if email valid
//        todo: check if email not taken
        customerRepository.saveAndFlush(customer); // in order to get id of the customer

        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());

        if (fraudCheckResponse.isFraudster()) {
            throw new IllegalStateException("fraudster");
        }

        // todo: make it async. i.e. add to queue
        notificationClient.sendNotification(
                new NotificationRequest(
                        customer.getId(),
                        customer.getEmail(),
                        String.format("Hi %s, welcome to Amigoscode...",
                                customer.getFirstName())
                )
        );
        // todo: send notification

    }
}
