package com.library.mapper;

import com.library.domain.*;
import com.library.dto.BookDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;


@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "authorName",source = "author",qualifiedByName = "convertAuthor")
    @Mapping(target = "publisherName",source = "publisher",qualifiedByName = "convertPublisher")
    @Mapping(target = "categoryName",source = "categorie",qualifiedByName = "convertCategories")
    BookDTO bookToBookDto(Book book);

    List<BookDTO> map(List<Book> books);

    @Named("convertAuthor")
    public static String convertAuthor(Author author){
        return author.getName();
    }

    @Named("convertPublisher")
    public static String convertPublisher(Publisher publisher){
        return publisher.getName();
    }

    @Named("convertCategories")
    public static String convertCategories(Categories categories){
        return categories.getName();
    }
}