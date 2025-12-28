package ru.katie177.edu.service;

import ru.katie177.edu.dto.TagDTO;
import ru.katie177.edu.exception.ResourceNotFoundException;
import ru.katie177.edu.model.Tag;
import ru.katie177.edu.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<TagDTO> getAllTags() {
        return tagRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TagDTO getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Тег не найден с ID: " + id));
        return convertToDTO(tag);
    }

    public TagDTO getTagByName(String name) {
        Tag tag = tagRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Тег не найден с названием: " + name));
        return convertToDTO(tag);
    }

    public TagDTO createTag(TagDTO tagDTO) {
        Tag tag = new Tag();
        tag.setName(tagDTO.getName());

        Tag savedTag = tagRepository.save(tag);
        return convertToDTO(savedTag);
    }

    public TagDTO updateTag(Long id, TagDTO tagDTO) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Тег не найден с ID: " + id));

        tag.setName(tagDTO.getName());

        Tag updatedTag = tagRepository.save(tag);
        return convertToDTO(updatedTag);
    }

    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Тег не найден с ID: " + id));
        tagRepository.delete(tag);
    }

    private TagDTO convertToDTO(Tag tag) {
        TagDTO dto = new TagDTO();
        dto.setId(tag.getId());
        dto.setName(tag.getName());

        // Подсчет использования тега
        Long usageCount = (long) tag.getCourses().size();
        dto.setUsageCount(usageCount.intValue());

        return dto;
    }
}