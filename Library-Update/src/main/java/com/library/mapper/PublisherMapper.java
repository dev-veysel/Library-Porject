package com.library.mapper;

import com.library.domain.Publisher;
import com.library.dto.PublishersDTO;
import com.library.dto.request.PublisherRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PublisherMapper {

    Publisher publisherRequestToPublisher(PublisherRequest request);

    PublishersDTO publisherToPublisherDTO(Publisher publisher);
}