package ru.katie177.edu.service;

import ru.katie177.edu.dto.ProfileDTO;
import ru.katie177.edu.exception.ResourceNotFoundException;
import ru.katie177.edu.model.Profile;
import ru.katie177.edu.model.User;
import ru.katie177.edu.repository.ProfileRepository;
import ru.katie177.edu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    public ProfileDTO getProfileByUserId(Long userId) {
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Профиль не найден для пользователя с ID: " + userId));
        return convertToDTO(profile);
    }

    public ProfileDTO getProfileById(Long id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Профиль не найден с ID: " + id));
        return convertToDTO(profile);
    }

    public ProfileDTO createProfile(ProfileDTO profileDTO) {
        User user = userRepository.findById(profileDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден с ID: " + profileDTO.getUserId()));

        Profile profile = new Profile();
        profile.setUser(user);
        profile.setBio(profileDTO.getBio());
        profile.setAvatarUrl(profileDTO.getAvatarUrl());
        profile.setPhoneNumber(profileDTO.getPhoneNumber());
        profile.setDateOfBirth(profileDTO.getDateOfBirth());

        Profile savedProfile = profileRepository.save(profile);
        return convertToDTO(savedProfile);
    }

    public ProfileDTO updateProfile(Long id, ProfileDTO profileDTO) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Профиль не найден с ID: " + id));

        if (profileDTO.getBio() != null) {
            profile.setBio(profileDTO.getBio());
        }
        if (profileDTO.getAvatarUrl() != null) {
            profile.setAvatarUrl(profileDTO.getAvatarUrl());
        }
        if (profileDTO.getPhoneNumber() != null) {
            profile.setPhoneNumber(profileDTO.getPhoneNumber());
        }
        if (profileDTO.getDateOfBirth() != null) {
            profile.setDateOfBirth(profileDTO.getDateOfBirth());
        }

        Profile updatedProfile = profileRepository.save(profile);
        return convertToDTO(updatedProfile);
    }

    public void deleteProfile(Long id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Профиль не найден с ID: " + id));
        profileRepository.delete(profile);
    }

    private ProfileDTO convertToDTO(Profile profile) {
        ProfileDTO dto = new ProfileDTO();
        dto.setId(profile.getId());
        dto.setUserId(profile.getUser().getId());
        dto.setUserName(profile.getUser().getName());
        dto.setUserEmail(profile.getUser().getEmail());
        dto.setUserRole(profile.getUser().getRole().toString());
        dto.setBio(profile.getBio());
        dto.setAvatarUrl(profile.getAvatarUrl());
        dto.setPhoneNumber(profile.getPhoneNumber());
        dto.setDateOfBirth(profile.getDateOfBirth());
        return dto;
    }
}