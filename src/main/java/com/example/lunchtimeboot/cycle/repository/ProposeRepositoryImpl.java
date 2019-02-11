package com.example.lunchtimeboot.cycle.repository;

import com.example.lunchtimeboot.cycle.entity.Propose;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProposeRepositoryImpl implements ProposeRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Propose> findUserProposeForDate(UUID userId, LocalDate forDate) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Propose> cq = cb.createQuery(Propose.class);

        Root<Propose> root = cq.from(Propose.class);
        cq.select(root);
        cq.where(
                cb.equal(root.get("userId"), userId),
                cb.equal(root.get("forDate"), forDate)
        );

        List<Propose> result = em.createQuery(cq).getResultList();
        if (result.isEmpty()) {
            return Optional.empty();
        }

        Optional<Propose> propose = Optional.of(result.get(0));

        return propose;
    }
}
