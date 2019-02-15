package com.example.lunchtimeboot.cycle.repository;

import com.example.lunchtimeboot.cycle.entity.readmodel.DailyPropose;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DailyProposeRepositoryImpl implements DailyProposeRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<DailyPropose> findCycleProposedRestaurantForDate(UUID cycleId, UUID restaurantId, LocalDate forDate) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<DailyPropose> cq = cb.createQuery(DailyPropose.class);

        Root<DailyPropose> dailyProposeRoot = cq.from(DailyPropose.class);
        cq.select(dailyProposeRoot);
        cq.where(
                cb.equal(dailyProposeRoot.get("cycle").get("cycleId"), cycleId),
                cb.equal(dailyProposeRoot.get("restaurant").get("restaurantId"), restaurantId),
                cb.equal(dailyProposeRoot.get("forDate"), forDate)
        );

        List<DailyPropose> found = em.createQuery(cq).getResultList();

        if (found.isEmpty()) {
            return Optional.empty();
        }

        Optional<DailyPropose> dailyPropose = Optional.of(found.get(0));

        return dailyPropose;
    }
}
