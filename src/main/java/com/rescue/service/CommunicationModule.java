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
import com.rescue.service.observer.CoordinatorAlertObserver;
import com.rescue.service.observer.DonationLogObserver;
import com.rescue.service.observer.EventUpdateObserver;
import com.rescue.service.observer.EventUpdated;
import com.rescue.service.observer.ThankYouMailerObserver;
import com.rescue.service.strategy.EmailStrategy;
import com.rescue.service.strategy.Notifier;
import com.rescue.service.strategy.SmsStrategy;
import com.rescue.service.strategy.SocialPostStrategy;
import com.rescue.view.CommunicationView;

// Builds the whole Communication part in one place:
// the Notifier with its 3 strategies, the activity feed,
// and the 3 subjects (donations, animals, events) with their observers attached.
//
// The whole app shares ONE module (getInstance), so a donation saved in the
// Donations screen shows up in the Communication screen.
public class CommunicationModule {

    private static CommunicationModule instance;

    private final Notifier notifier;
    private final ActivityFeed feed = new ActivityFeed();

    // The "channels" other modules publish to.
    private final Subject<Donation> donationSubject = new Subject<>();
    private final Subject<AnimalStatusChanged> animalSubject = new Subject<>();
    private final Subject<EventUpdated> eventSubject = new Subject<>();

    // The one shared module for the whole app.
    public static CommunicationModule getInstance() {
        if (instance == null) {
            instance = new CommunicationModule();
        }
        return instance;
    }

    // Default setup: settings from AppConfig, messages stored in the DataStore.
    public CommunicationModule() {
        this(new SentMessageRepository(),
                AppConfig.getInstance().getOrgName(),
                new Contact("Coordinator", AppConfig.getInstance().getContactEmail(), "+20100000000"));
    }

    // Custom setup (used by the tests and the demo).
    public CommunicationModule(Repository<SentMessage> messageLog, String organisationName, Recipient coordinator) {
        // Strategy: the Notifier and its 3 ways of sending.
        notifier = new Notifier(messageLog);
        notifier.addStrategy(new EmailStrategy(organisationName));
        notifier.addStrategy(new SmsStrategy(organisationName));
        notifier.addStrategy(new SocialPostStrategy(organisationName));

        // Observer: subscribe the observers to each subject.
        donationSubject.attach(new ThankYouMailerObserver(notifier));
        donationSubject.attach(new CoordinatorAlertObserver(notifier, coordinator));
        donationSubject.attach(new DonationLogObserver(feed));

        animalSubject.attach(new AnimalStatusObserver(notifier, feed));

        eventSubject.attach(new EventUpdateObserver(notifier, feed));
    }

    public Notifier getNotifier() {
        return notifier;
    }

    public ActivityFeed getFeed() {
        return feed;
    }

    public Subject<Donation> getDonationSubject() {
        return donationSubject;
    }

    public Subject<AnimalStatusChanged> getAnimalSubject() {
        return animalSubject;
    }

    public Subject<EventUpdated> getEventSubject() {
        return eventSubject;
    }

    // Builds the Communication screen (view + controller).
    public CommunicationController createController() {
        return new CommunicationController(new CommunicationView(), notifier, feed);
    }
}
