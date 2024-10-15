package org.capps.service.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Persistence;
import org.capps.entity.Task;
import org.capps.entity.TaskChangeRequest;
import org.capps.entity.User;
import org.capps.repository.TaskChangeRequestRepository;
import org.capps.repository.implementation.TaskChangeRequestRepositoryImp;
import org.capps.service.TaskChangeRequestService;
import org.capps.service.TaskService;
import org.capps.service.UserService;
import org.capps.service.UserTokenService;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Optional;

public class TaskChangeRequestServiceImpl implements TaskChangeRequestService {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
    private EntityManager em;
    private TaskChangeRequestRepository requestRepository;
    private UserTokenService tokenService;
    private UserService userService;
    private TaskService taskService;


    public TaskChangeRequestServiceImpl(){
        em = emf.createEntityManager();
        requestRepository = new TaskChangeRequestRepositoryImp(em);
        tokenService = new UserTokenServiceImpl();
        userService = new UserServiceImpl();
        taskService = new TaskServiceImpl();
    }

    @Override
    public void createRequest(int userId, int taskId){
        User user = userService.getUserById(userId);
        Task task = taskService.getTaskById(taskId);
        TaskChangeRequest request = new TaskChangeRequest(user, task, LocalDate.now(), "PENDING");
        requestRepository.save(request);

    }

    @Override
    public  boolean isRequestPendingOver12Hours(int userId){
        Optional<TaskChangeRequest> request = requestRepository.findPendingRequestByUserId(userId);
        if (request.isPresent()){
            LocalDate requestTime = request.get().getRequestTime();
            Duration duration = Duration.between(requestTime, LocalDate.now());
            return duration.toHours() >= 12;
        }
        return false;
    }

    @Override
    public void doubleUserTokens(int userId){
        User user = userService.getUserById(userId);

        if (user == null) {
            throw new EntityNotFoundException("Utilisateur avec l'id " + userId + " n'existe pas.");
        }
        tokenService.doubleTokensForUser(userId);
    }
}
