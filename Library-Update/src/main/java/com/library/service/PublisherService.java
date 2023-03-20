package com.library.service;

import com.library.domain.Author;
import com.library.domain.Publisher;
import com.library.dto.AuthorDTO;
import com.library.dto.PublishersDTO;
import com.library.dto.request.PublisherRequest;
import com.library.dto.request.PublisherUpdateRequest;
import com.library.exception.BadRequestException;
import com.library.exception.ResourceNotFoundException;
import com.library.exception.message.ErrorMessage;
import com.library.mapper.PublisherMapper;
import com.library.repository.PublisherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PublisherService {

    private final PublisherMapper publisherMapper;
    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherMapper publisherMapper, PublisherRepository publisherRepository) {
        this.publisherMapper = publisherMapper;
        this.publisherRepository = publisherRepository;
    }


    public PublishersDTO savePublisher(PublisherRequest request) {
        Publisher publisher=publisherMapper.publisherRequestToPublisher(request);
        if (request.getBuiltIn() == null) {
            publisher.setBuiltIn(false);
        }
        publisherRepository.save(publisher);
        return publisherMapper.publisherToPublisherDTO(publisher);
    }


    public PublishersDTO findByPublisherId(Long id) {
        Publisher publisher=findById(id);
        return publisherMapper.publisherToPublisherDTO(publisher);
    }


    public Publisher findById(Long id) {
        return publisherRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException(String.format(ErrorMessage.PUBLISHER_NOT_FOUND_EXCEPTION,id)));
    }


    public Page<PublishersDTO> getAllPublishersByPage(Pageable pageable) {
        Page<Publisher> list=publisherRepository.findAll(pageable);
        return list.map(publisherMapper::publisherToPublisherDTO);
    }


    public PublishersDTO updateAuthor(PublisherUpdateRequest updateRequest, Long id) {
        Publisher publisher=findById(id);
        if (publisher.getBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITED_METOD_MESSAGE);
        }
        publisher.setBuiltIn(false);
        publisher.setName(updateRequest.getName());
        publisherRepository.save(publisher);
        return publisherMapper.publisherToPublisherDTO(publisher);
    }


    public PublishersDTO deletePublisher(Long id) {
        Publisher publisher=findById(id);
        if (publisher.getBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITED_METOD_MESSAGE);
        }
        if (!publisher.getBook().isEmpty()) {
            throw new BadRequestException(ErrorMessage.AUTHOR_HAS_BOOK_EXCEPTION);
        }
        PublishersDTO publishersDTO = publisherMapper.publisherToPublisherDTO(publisher);
        publisherRepository.deleteById(id);
        return publishersDTO;
    }


    public Long getUserCount() {
        return publisherRepository.count();
    }
}