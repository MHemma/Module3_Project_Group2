package com.example.jobfinder.serviceImpls;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.jobfinder.entities.Job;
import com.example.jobfinder.entities.User;
import com.example.jobfinder.exceptions.JobAlreadySavedException;
import com.example.jobfinder.exceptions.JobNotFoundException;
import com.example.jobfinder.exceptions.UserNotFoundException;
import com.example.jobfinder.repositories.JobRepository;
import com.example.jobfinder.repositories.UserRepository;
import com.example.jobfinder.services.UserService;


@Service
@Transactional   
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;
    private JobRepository jobRepository;

    public UserServiceImpl (UserRepository userRepository, JobRepository jobRepository) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    @Override
    public User createUser(User user) {
        User newUser = userRepository.save(user);
        return newUser;
    }

    @Override
    public ArrayList<User> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        return (ArrayList<User>) allUsers;
    }


    @Override
    public User getUser(Long id) {
        User foundUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return foundUser;
    }

    @Override
    public User updateUser(Long id, User user) {
        User userToUpdate = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setLastName(user.getLastName());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setCountry(user.getCountry());
        userRepository.save(userToUpdate);
        return userToUpdate;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


    @Override 
    public Job addJobToUser(Long id, Long job_id) {
        logger.info("addJobToUser method being called");
        // find the user by Id
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        Set <Job> userJobs = user.getJobs();

        if (userJobs == null) {
            userJobs = new HashSet<>();
        }

        Job job = jobRepository.findById(job_id).orElseThrow(() -> new JobNotFoundException(job_id));

        Set <User> jobUsers = job.getUsers();

        if (jobUsers == null) {
            jobUsers = new HashSet<>();
        }

        boolean jobAlreadyInUserList = userJobs.stream().anyMatch((existingJob -> existingJob.getId().equals(job_id)));
            if (jobAlreadyInUserList){
                throw new JobAlreadySavedException(job_id);           
            } else {
                jobUsers.add(user);
                job.setUsers(jobUsers);
                jobRepository.save(job);
            }
            
        logger.info("Job: " + job);
    
    return job;

}
 
}