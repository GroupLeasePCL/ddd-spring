package com.example.lunchtimeboot.cycle.repository;

import com.example.lunchtimeboot.cycle.entity.Cycle;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

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
}
