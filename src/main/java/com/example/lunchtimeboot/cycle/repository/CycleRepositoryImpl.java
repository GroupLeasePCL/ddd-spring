package com.example.lunchtimeboot.cycle.repository;

import com.example.lunchtimeboot.cycle.entity.Cycle;
import com.example.lunchtimeboot.cycle.entity.CycleMember;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.UUID;

public class CycleRepositoryImpl implements CycleRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Cycle> searchByName(String search) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Cycle> cq = cb.createQuery(Cycle.class);

        Root<Cycle> root = cq.from(Cycle.class);
        cq.select(root);
        cq.where(
                cb.like(root.get("name"), "%" + search + "%")
        );

        List<Cycle> cycles = em.createQuery(cq).setMaxResults(Constants.MAX_PAGE_SIZE).getResultList();

        return cycles;
    }

    @Override
    public List<Cycle> findByMemberUserId(UUID userId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Cycle> cq = cb.createQuery(Cycle.class);
        Root<Cycle> cycle = cq.from(Cycle.class);
        Join<Cycle, CycleMember> member = cycle.join("members", JoinType.INNER);
        member.on(cb.equal(member.get("userId"), userId));

        List<Cycle> cycles = em.createQuery(cq).getResultList();

        return cycles;
    }
}
