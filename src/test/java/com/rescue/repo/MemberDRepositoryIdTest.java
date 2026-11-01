package com.rescue.repo;

import com.rescue.model.RescueEvent;
import com.rescue.model.Volunteer;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MemberDRepositoryIdTest {

    @Test
    void volunteerIdsAreAutomaticallyAssignedAndNotReusedAfterDelete() {
        VolunteerRepository repository = new VolunteerRepository();
        Volunteer first = volunteer();
        repository.save(first);
        int firstId = first.getId();

        repository.delete(firstId);
        Volunteer second = volunteer();
        new VolunteerRepository().save(second);

        assertTrue(firstId > 0);
        assertTrue(second.getId() > firstId);
        assertNotEquals(firstId, second.getId());
    }

    @Test
    void eventIdsAreAutomaticallyAssignedAndNotReusedAfterDelete() {
        RescueEventRepository repository = new RescueEventRepository();
        RescueEvent first = event();
        repository.save(first);
        int firstId = first.getId();

        repository.delete(firstId);
        RescueEvent second = event();
        new RescueEventRepository().save(second);

        assertTrue(firstId > 0);
        assertTrue(second.getId() > firstId);
        assertNotEquals(firstId, second.getId());
    }

    private static Volunteer volunteer() {
        return new Volunteer(0, "Test Volunteer", "test@example.com", "01000000000",
                List.of("First Aid"), "Weekends", "Helper", 0, List.of());
    }

    private static RescueEvent event() {
        return new RescueEvent(0, "Test Event", "Adoption Day", LocalDate.now(),
                "Shelter", List.of("First Aid"), "Repository ID test");
    }
}
