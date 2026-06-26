package com.studentwallet.controller;

import com.studentwallet.model.User;
import com.studentwallet.model.dto.GamificationDTO;
import com.studentwallet.repository.UserRepository;
import com.studentwallet.service.GamificationService;
import com.studentwallet.util.AuthUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gamification")
public class GamificationController {

    private final GamificationService gamificationService;
    private final UserRepository userRepository;

    public GamificationController(GamificationService gamificationService, UserRepository userRepository) {
        this.gamificationService = gamificationService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<GamificationDTO> getGamification() {
        String userId = AuthUtil.currentUserId();
        String displayName = userRepository.findById(userId).map(User::getDisplayName).orElse("Student");
        return ResponseEntity.ok(gamificationService.getGamification(userId, displayName));
    }
}
