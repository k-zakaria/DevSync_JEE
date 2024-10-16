package org.capps.service.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Persistence;
import org.capps.entity.UserToken;
import org.capps.repository.UserTokenRepository;
import org.capps.repository.implementation.UserTokenRepositoryImpl;
import org.capps.service.UserTokenService;

import java.time.LocalDate;
import java.time.YearMonth;

public class UserTokenServiceImpl implements UserTokenService {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    private EntityManager em;
    private UserTokenRepository tokenRepository;

    public UserTokenServiceImpl() {
        em = emf.createEntityManager();
        tokenRepository = new UserTokenRepositoryImpl(em);
    }

    // Vérifie si l'utilisateur a encore un jeton journalier disponible
    @Override
    public boolean hasTokensLeft(int userId){
        UserToken token = tokenRepository.findByUserIdAndDate(userId, LocalDate.now());
        return token == null || token.getTokensUsed() < 2;
    }

    @Override
    public boolean checkAndRecordDeletion(int userId) {
        LocalDate today = LocalDate.now();

        YearMonth currentMonth = YearMonth.now();
        LocalDate firstDayOfMonth = currentMonth.atDay(1);

        UserToken token = tokenRepository.findByUserIdAndDate(userId, firstDayOfMonth);

        if (token == null) {
            token = new UserToken(userId, firstDayOfMonth, 0, true); // tokensUsed = 0 au début
            tokenRepository.save(token);
            return true;
        }else if (!token.isDeletedOnceInMonth()) {
            token.setDeletedOnceInMonth(true);
            tokenRepository.update(token);
            return true;
        }
        return false;
    }

    // Enregistre l'utilisation d'un jeton
    @Override
    public void useToken(int userId, boolean isMonthly){
        LocalDate date = isMonthly ? YearMonth.now().atDay(1) : LocalDate.now();
        UserToken token = tokenRepository.findByUserIdAndDate(userId, date);

        if (token == null){
            token = new UserToken(userId, date, 1, false);
        }else {
            token.setTokensUsed(token.getTokensUsed() + 1);
        }
        tokenRepository.save(token);
    }

    @Override
    public Integer getTokensUsed(int userId){
        return tokenRepository.getTokensUsed(userId);
    }

    @Override
    public void doubleTokensForUser(int userId) {
        // Chercher le jeton de l'utilisateur pour la date d'aujourd'hui
        UserToken token = tokenRepository.findByUserIdAndDate(userId, LocalDate.now());

        if (token != null) {
            token.setTokensUsed(token.getTokensUsed() * 2);
            tokenRepository.update(token);
        } else {
            throw new EntityNotFoundException("Aucun jeton trouvé pour l'utilisateur avec l'id : " + userId);
        }
    }
}
