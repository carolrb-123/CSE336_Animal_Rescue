package com.rescue.service;

import com.rescue.controller.CommunicationController;
import com.rescue.model.ActivityFeed;
import com.rescue.model.AppConfig;
import com.rescue.model.Contact;
import com.rescue.model.Donation;
import com.rescue.model.Recipient;
import com.rescue.model.SentMessage;
import com.rescue.repo.Repository;
import com.rescue.repo.SentMessageRepository;
import com.rescue.service.observer.AnimalStatusChanged;
import com.rescue.service.observer.AnimalStatusObserver;
import com.rescue.service.observer.CoordinatorObserver;
import com.rescue.service.observer.DashboardObserver;
import com.rescue.service.observer.EventUpdateObserver;
import com.rescue.service.observer.EventUpdated;
import com.rescue.service.observer.ThankYouMailerObserver;
import com.rescue.service.strategy.ConsoleGateway;
import com.rescue.service.strategy.EmailStrategy;
import com.rescue.service.strategy.MessageGateway;
import com.rescue.service.strategy.Notifier;
import com.rescue.service.strategy.SmsStrategy;
import com.rescue.service.strategy.SocialPostStrategy;
import com.rescue.view.CommunicationView;

import java.util.Locale;
import java.util.function.Function;

/**
 * Builds the whole Communication part.
 * Integrated with Control Panel using Singleton configuration.
 */
public final class CommunicationModule {

    private final Notifier notifier;
    private final ActivityFeed feed = new ActivityFeed();
    private final Recipient coordinator;
    private final String currency;

    /**
     * Default constructor for Control Panel integration.
     * Pulls default settings from the AppConfig singleton and utilizes the shared DataStore repository.
     */
    public CommunicationModule() {
        // Fetch shared settings from the Singleton
        String orgName = AppConfig.getInstance().getOrgName();
        String contactEmail = AppConfig.getInstance().getContactEmail();

        this.currency = "EGP";
        this.coordinator = new Contact("Coordinator", contactEmail, "+20100000000");

        Repository<SentMessage> messageLog = new SentMessageRepository();
        MessageGateway gateway = new ConsoleGateway();

        this.notifier = new Notifier(messageLog)
                .register(new EmailStrategy(gateway, orgName))
                .register(new SmsStrategy(gateway, orgName))
                .register(new SocialPostStrategy(gateway, orgName));
    }

    /**
     * Original Parameterized Constructor
     */
    public CommunicationModule(Repository<SentMessage> messageLog, MessageGateway gateway,
                               String organisationName, String currency, Recipient coordinator) {
        this.notifier = new Notifier(messageLog)
                .register(new EmailStrategy(gateway, organisationName))
                .register(new SmsStrategy(gateway, organisationName))
                .register(new SocialPostStrategy(gateway, organisationName));
        this.coordinator = coordinator;
        this.currency = currency;
    }

    /** B: new donation -> thank-you to the donor, alert to the coordinator, line on the dashboard. */
    public void connectDonations(Subject<Donation> donations) {
        Function<Donation, String> summary = d -> String.format(Locale.US, "Donation #%d of %s %,.2f from %s",
                d.getId(), currency, d.getAmount(), d.getDonor().getName());
        donations.attach(new DashboardObserver<>(feed, summary));
        donations.attach(new ThankYouMailerObserver(notifier, currency));
        donations.attach(new CoordinatorObserver<>(notifier, coordinator, summary));
    }

    /** E: status change -> followers told, public post when available/adopted, coordinator alert, dashboard. */
    public void connectAnimals(Subject<AnimalStatusChanged> animals) {
        animals.attach(new DashboardObserver<>(feed, AnimalStatusChanged::summary));
        animals.attach(new AnimalStatusObserver(notifier));
        animals.attach(new CoordinatorObserver<>(notifier, coordinator, AnimalStatusChanged::summary));
    }

    /** D: event change -> registered volunteers told, coordinator alert, dashboard. */
    public void connectEvents(Subject<EventUpdated> events) {
        events.attach(new DashboardObserver<>(feed, EventUpdated::summary));
        events.attach(new EventUpdateObserver(notifier));
        events.attach(new CoordinatorObserver<>(notifier, coordinator, EventUpdated::summary));
    }

    public Notifier notifier() {
        return notifier;
    }

    public ActivityFeed feed() {
        return feed;
    }

    /** Builds the Communication screen (view + controller). Call on the Swing thread. */
    public CommunicationController createController() {
        return new CommunicationController(new CommunicationView(), notifier, feed);
    }
}