package com.example.jobfinder.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale.Category;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.jobfinder.entities.Job;
// import com.example.jobfinder.serviceImpls.JobSearhCriteria;

public interface JobRepository extends JpaRepository <Job, Long> {
    // add custom queries here

    // derievedQuery -> findBy[fieldName:(e.g.category)] return all records where the [field:(e.g.category)] = value
    List<Job> findByCategory(String category);

    // derievedQuery -> findBy[fieldName:(e.g.amount)]Between return all records where the [field:(e.g.amount)] between both values
    List<Job> findBySalaryBetween(Double minSalary, Double maxSalary);

    // derievedQuery -> findBy[firstField]AND[secondField]
    List<Job> findByCategoryAndSalaryBetween(String category, Double minSalary, Double maxSalary);
}